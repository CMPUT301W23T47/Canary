package com.cmput301w23t47.canary.view.fragment;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import java.io.IOException;
import java.util.List;
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
    // Default constructor
    public QRCodeViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Updates the location if available
     */
    private void updateLocation() {
        // set location
        Log.d(TAG, "updateLocation: " + playerQrCode.isLocationShared());
        if (playerQrCode.isLocationShared()) {
            String cityName = LocationController.retrieveCityName(playerQrCode.getLocation(), getContext());
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
     * Initializes the view
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

    private void deleteQr() {
        if (playerQrCode == null) {
            return;
        }
        showLoadingBar();
        firestorePlayerController.deleteQrFromPlayer(playerQrCode, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void getPlayerQr(PlayerQrCode playerQrCode) {
        this.playerQrCode = playerQrCode;
        updateFragmentData();
    }

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


//    /**
//     * Retrieves the city name
//     * @param location the location to parse
//     * @return the city name if applicable, empty string "" otherwise
//     */
//    public String retrieveCityName(Location location) {
//        try {
//            Geocoder geocoder = new Geocoder(getContext());
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            if (addresses.isEmpty()) {
//                return "";
//            }
//            return addresses.get(0).getLocality();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}