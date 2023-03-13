package com.cmput301w23t47.canary.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.MainActivity;
import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetPlayerQrCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.controller.LocationController;
import com.cmput301w23t47.canary.databinding.FragmentQrCodeViewBinding;
import com.cmput301w23t47.canary.model.PlayerQrCode;

import java.util.Locale;

/**
 * Fragment to view the QR Code page
 */
public class QRCodeViewFragment extends Fragment implements GetPlayerQrCallback, OperationStatusCallback {
    private static final String TAG = "QRCodeViewFragment";

    private PlayerQrCode playerQrCode;
    private boolean owner = false;

    private FragmentQrCodeViewBinding binding;
    AlertDialog.Builder builder;

    private final FirestorePlayerController firestorePlayerController = new FirestorePlayerController();

    /**
     * Required empty public constructor.
     */
    public QRCodeViewFragment() {
    }

    /**
     *  Handles the layout of the activity, and called on activity creation.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Updates the location if available
     */
    private void updateLocation() {
        // set location
        if (playerQrCode.isLocationShared()) {
            String cityName = LocationController.retrieveCityName(playerQrCode.getLocation());
            if (cityName.equals("")) {
                // no city name given
                binding.qrScanLocation.setText("_ _ _");
            } else {
                binding.qrScanLocation.setText(cityName);
            }
        }
    }

    /**
     * Updates the snapshot if available
     */
    private void updateSnapshot() {
        if (playerQrCode.getSnapshot() != null) {
            binding.qrSnapshot.setImageBitmap(playerQrCode.getSnapshot().getBitmap());
            binding.noSnapshotBox.setVisibility(View.GONE);
            binding.qrSnapshot.setVisibility(View.VISIBLE);
        } else {

        }
    }

    /**
     * Updates all the data in the fragment
     */
    public void updateFragmentData(){
        Log.d(TAG, "updateFragmentData: called");
        if (playerQrCode == null) {
            return;
        }
        // set qr info
        binding.qrTitle.setText(getNameString());
        binding.qrScoreVal.setText(String.format(Locale.CANADA, "Score: %d Pts", playerQrCode.getQrCode().getScore()));
        if (playerQrCode.getScanDate() != null) {
            binding.qrScanDate.setText(playerQrCode.getScanDate().toString());
        }
        updateLocation();
        updateSnapshot();

        hideLoadingBar();
    }

    /**
     * Shows the loading bar
     */
    private void showLoadingBar() {
        binding.progressBarBox.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the loading bar
     */
    private void hideLoadingBar() {
        binding.progressBarBox.setVisibility(View.GONE);
    }

    /**
     * Gets the formatted name of the qr
     * @return the formatted name of the qr, truncates it if it's too long
     */
    private String getNameString() {
        String name = playerQrCode.getName();
        if (name.length() < 15) {
            return name;
        }
        return String.format(Locale.CANADA, "%s...", name.substring(0, 11));
    }

    /**
     * Initializes the view for this page
     */
    private void init() {
        showLoadingBar();
        String qrHash = QRCodeViewFragmentArgs.fromBundle(getArguments()).getQrHash();
        owner = QRCodeViewFragmentArgs.fromBundle(getArguments()).getOwner();
        if (!owner) {
            binding.qrDeleteIcon.setVisibility(View.GONE);
        }
        firestorePlayerController.getPlayerQr(qrHash, this);
    }

    /**
     * Initializes the view and creates a bundle object for this view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the binding object on which we can work
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQrCodeViewBinding.inflate(inflater, container, false);
        builder = new AlertDialog.Builder(getContext());
        initUi();
        return binding.getRoot();
    }

    /**
     * Initializes the UI
     */
    private void initUi() {
        binding.qrDeleteIcon.setOnClickListener(view -> {
            builder.setMessage("Are you sure you want to delete this QR. This will update your score")
                    .setTitle("Delete QR")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                        deleteQr();
                    }).setNegativeButton("Cancel", (DialogInterface dialog, int id) -> {
                        dialog.dismiss();
                    }).create().show();
        });
    }

    /**
     * Deletes a Qr from a player's profile by calling the firestore controller.
     */
    private void deleteQr() {
        if (playerQrCode == null) {
            return;
        }
        showLoadingBar();
        firestorePlayerController.deleteQrFromPlayer(playerQrCode, this);
    }

    /**
     * Defines what to do on resuming the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    /**
     * Sets the playerqrcode to the given code
     * @param playerQrCode To be set
     */
    @Override
    public void getPlayerQr(PlayerQrCode playerQrCode) {
        this.playerQrCode = playerQrCode;
        updateFragmentData();
    }

    /**
     * Depending upon status, determines whether the player has been created or not.
     * @param status boolean value giving if a state is valid or not
     */
    @Override
    public void operationStatus(boolean status) {
        hideLoadingBar();
        if (status) {
            new AlertDialog.Builder(getContext())
                    .setMessage("QR Deleted")
                    .setTitle("QR Deleted successfully")
                    .setCancelable(false)
                    .setPositiveButton("Continue", (DialogInterface dialog, int id) -> {
                        returnToHome();
                    }).create().show();
        } else {
            new AlertDialog.Builder(getContext())
                    .setMessage("Request failed")
                    .setTitle("QR couldn't be Deleted. Try again later")
                    .setCancelable(false)
                    .setPositiveButton("Continue", (DialogInterface dialog, int id) -> {
                        returnToHome();
                    }).create().show();
        }
    }

    /**
     * Returns to the home page after deleting a qr
     */
    protected void returnToHome() {
        Navigation.findNavController(getView()).navigate(R.id.action_goToHomeFromQRCodeView);
    }
}