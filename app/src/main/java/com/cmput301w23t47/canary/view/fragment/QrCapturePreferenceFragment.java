package com.cmput301w23t47.canary.view.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.MainActivity;
import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.DoesResourceExistCallback;
import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.controller.ImageGenerator;
import com.cmput301w23t47.canary.controller.RandomNameGenerator;
import com.cmput301w23t47.canary.controller.ScoreCalculator;
import com.cmput301w23t47.canary.databinding.FragmentQrCapturePreferenceBinding;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.model.Snapshot;
import com.cmput301w23t47.canary.view.contract.AddNewQrContract;
import com.cmput301w23t47.canary.view.contract.SnapshotContract;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.Locale;

/**
 * A fragment for the setting the preference of capturing the Qr.
 * Continues without saving location if permission denied
 */
public class QrCapturePreferenceFragment extends LocationBaseFragment implements
        DoesResourceExistCallback, OperationStatusCallback, GetImageCallback {
    public static final String TAG = "QrCapturePreferenceFragment";

    private FragmentQrCapturePreferenceBinding binding;
    AlertDialog.Builder builder;
    private ActivityResultLauncher<Object> snapshotActivityLauncher;

    // controller for making qr related queries
    private final FirestorePlayerController firestorePlayerController = new FirestorePlayerController();

    private boolean saveLocation = true;
    private final QrCode qrCode = new QrCode();
    private Bitmap qrImage = null;
    private boolean imageCallbackReturned = false;
    private boolean qrCheckReturned = false;

    /**
     * Required empty public constructor.
     */
    public QrCapturePreferenceFragment() {}

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
        binding = FragmentQrCapturePreferenceBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Sets the view of the page and calls the function to show the UI of the page.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrCode.setHash(QrCapturePreferenceFragmentArgs.fromBundle(getArguments()).getQrHash());
        firestorePlayerController.doesCurrentPlayerHaveQr(qrCode.getHash(), this);
        ImageGenerator.getImage(getString(R.string.random_image_generator), this);
        binding.saveLocationCheckbox.setChecked(saveLocation);
    }

    /**
     * Initializes the UI for this page.
     */
    void init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        binding.saveLocationCheckbox.setOnClickListener(view -> {
            saveLocation = binding.saveLocationCheckbox.isChecked();
        });
        snapshotActivityLauncher = registerForActivityResult(new SnapshotContract(), this::receiveSnapshot);

        binding.takeSnap.setOnClickListener(view -> {
            captureSnapshot();
        });
        binding.noSnap.setOnClickListener(view -> {
            persistQr(null);
        });

        builder = new AlertDialog.Builder(getContext());
        showLoadingBar();
        askPermissions();
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
        if (qrCheckReturned && imageCallbackReturned) {
            binding.progressBarBox.setVisibility(View.GONE);
        }
    }

    /**
     * Checks whther the qr with given hash exists or not
     * assigns the name and scores and updates the UI if does not exist
     * @param exists
     */
    @Override
    public void doesResourceExists(boolean exists) {
        qrCheckReturned = true;
        hideLoadingBar();
        if (exists) {
            // if qr with the given hash exist, show an alert
            builder.setMessage("The selected QR is deleted")
                    .setTitle("QR Code Deleted")
                    .setCancelable(false)
                    .setPositiveButton("Continue", (DialogInterface dialog, int id) -> {
                        // TODO: handle already scanned QR
                        returnToQrCodePage();
                    }).create().show();
        } else {
            RandomNameGenerator nameGenerator = new RandomNameGenerator();
            qrCode.setName(nameGenerator.getWord());
            qrCode.setScore(ScoreCalculator.calculateScore(qrCode.getHash()));
            updateUi();
        }
    }

    /**
     * Returns the screen to the Qr Code page
     */
    public void returnToQrCodePage() {
        Intent intent = new Intent();
        intent.putExtra(AddNewQrContract.RESPONSE_TAG, qrCode.getHash());
        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Updates the UI
     */
    private void updateUi() {
        binding.qrName.setText(qrCode.getName());
        binding.qrScore.setText(String.format(Locale.CANADA, "%d", qrCode.getScore()));
    }

    /**
     * Receives the snapshot as a bitmap image.
     * @param image the bitmap image which has to be persisted
     */
    private void receiveSnapshot(Bitmap image) {
        showLoadingBar();
        persistQr(image);
    }

    /**
     * Persists the scanned QR to db
     * @param snapshot The snapshot of qr
     */
    private void persistQr(Bitmap snapshot) {
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCode, new Date(), false);
        if (saveLocation && playerLocation != null) {
            // only if location is available and the user shared the location
            playerQrCode.setLocationShared(true);
            playerQrCode.putLocation(playerLocation);
        }
        if (snapshot != null) {
            playerQrCode.setSnapshot(new Snapshot(snapshot));
        }
        firestorePlayerController.addQrToCurrentPlayer(playerQrCode, this);
    }

    /**
     * Calls the activity which captures a snapshot
     */
    private void captureSnapshot() {
        snapshotActivityLauncher.launch(null);
    }

    /**
     * Depending upon status, determines whether the player has been created or not.
     * @param status boolean value giving if a state is valid or not
     */
    @Override
    public void operationStatus(boolean status) {
        // QR Persisted, go back
        returnToQrCodePage();
    }

    @Override
    protected void updateLocation() {
    }

    @Override
    protected void locationPermissionGranted() {
        getLocationRequest();
    }

    @Override
    protected void locationPermissionNotGranted() {
        playerLocation = null;
    }

    /**
     * Callback for when the image is loaded for the qr
     * @param bitmap the bitmap image available
     */
    @Override
    public void getImage(Bitmap bitmap) {
        imageCallbackReturned = true;
        qrCode.setQrImage(bitmap);
        hideLoadingBar();
    }
}