package com.cmput301w23t47.canary.view.fragment;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetCurrentPlayerUsernameCallback;
import com.cmput301w23t47.canary.callback.GetPlayerQrCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.controller.LocationController;
import com.cmput301w23t47.canary.databinding.FragmentQrCodeViewBinding;
import com.cmput301w23t47.canary.model.Comment;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.view.adapter.CommentListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment to view the QR Code page
 */
public class QRCodeViewFragment extends Fragment implements GetPlayerQrCallback, OperationStatusCallback, GetCurrentPlayerUsernameCallback {
    private static final String TAG = "QRCodeViewFragment";
    private String currentPlayerUsername;
    private PlayerQrCode playerQrCode;
    private boolean owner = false;

    private FragmentQrCodeViewBinding binding;
    AlertDialog.Builder builder;
    private CommentListAdapter commentListAdapter;

    private final FirestorePlayerController firestorePlayerController = new FirestorePlayerController();

    private String qrHash;
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
        Log.d(TAG, "updateLocation: " + playerQrCode.isLocationShared());
        if (playerQrCode.isLocationShared()) {
            String cityName = LocationController.retrieveCityName(playerQrCode.getLocation(), getContext());
            if (cityName == null || cityName.equals("")) {
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
     * Updates the qr image if available
     */
    private void updateQrImage() {
        if (playerQrCode.retrieveQrImage() != null) {
            binding.qrImage.setImageBitmap(playerQrCode.retrieveQrImage());
        }
    }

    /**
     * Updates the comments in the view
     */
    private void updateComments() {
        commentListAdapter.updateList(playerQrCode.getQrCode().getComments());
    }

    public void updateFragmentData(){
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
        updateQrImage();
        updateComments();
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

        // comment list init
        commentListAdapter = new CommentListAdapter(new ArrayList<>());
        binding.qrCommentsList.setAdapter(commentListAdapter);

        // qr init
        qrHash = QRCodeViewFragmentArgs.fromBundle(getArguments()).getQrHash();
        owner = QRCodeViewFragmentArgs.fromBundle(getArguments()).getOwner();

        if (!owner) {
            binding.qrDeleteIcon.setVisibility(View.GONE);
        }
        firestorePlayerController.getPlayerQr(qrHash, this);
        firestorePlayerController.getCurrentPlayerUsername(this);
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

    public boolean isEmptyEditText(EditText e){
        return e.getText().toString().trim().length() == 0;
    }

    private String getMessage(){
        return binding.addCommentText.getText().toString();
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

        binding.viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation();

                Location qrLocation = playerQrCode.getLocation();

                if (!playerQrCode.isLocationShared()){
//                    return;
                    Toast.makeText(getContext(), "No Location found!", Toast.LENGTH_LONG).show();
                }
                else{
                    returnToMapScreen(qrLocation);
                }
            }
        });
        binding.postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = getMessage();
                if(!isEmptyEditText(binding.addCommentText) ) {
                    Comment comment = new Comment(currentPlayerUsername, message, new Date() );
                    firestorePlayerController.addCommentToExistingQr(playerQrCode.retrieveHash(), comment);
                    commentListAdapter.addComment(comment);
                    binding.addCommentText.setText("");
                }
                else{
                    CharSequence fillComment = "Please enter comment!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getContext(), fillComment,duration);
                    toast.show();
                }
            }
        });

        binding.viewOtherPlayers.setOnClickListener(view -> {
            returnToViewOtherPlayers();
            //builder.setMessage("HELLO").setTitle("Thrtr").create().show();
        });
    }

    private void returnToViewOtherPlayers() {
        QRCodeViewFragmentDirections.ActionQRCodeViewToViewOtherPlayersFragment action =
                QRCodeViewFragmentDirections.actionQRCodeViewToViewOtherPlayersFragment(qrHash);
        Navigation.findNavController(getView()).navigate(action);
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

    protected void returnToMapScreen(Location qrLocation){
        QRCodeViewFragmentDirections.ActionQRCodeViewToSearchNearbyQrMapFragment action =
                QRCodeViewFragmentDirections.actionQRCodeViewToSearchNearbyQrMapFragment().setQrLocation(qrLocation);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void getCurrentPlayerUsername(String playerUsername) {
        this.currentPlayerUsername = playerUsername;
        binding.currentPlayerUsername.setText(currentPlayerUsername);
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