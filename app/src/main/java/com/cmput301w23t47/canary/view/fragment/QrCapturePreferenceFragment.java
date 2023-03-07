package com.cmput301w23t47.canary.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.controller.FirestoreQrController;
import com.cmput301w23t47.canary.controller.RandomNameGenerator;
import com.cmput301w23t47.canary.controller.ScoreCalculator;
import com.cmput301w23t47.canary.databinding.FragmentQrCapturePreferenceBinding;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.model.Snapshot;
import com.cmput301w23t47.canary.view.contract.QrCodeContract;
import com.cmput301w23t47.canary.view.contract.SnapshotContract;

import java.util.Locale;

/**
 * Continues without saving location if permission denied
 */
public class QrCapturePreferenceFragment extends Fragment implements DoesResourceExistCallback {
    public static final String TAG = "QrCapturePreferenceFragment";

    private FragmentQrCapturePreferenceBinding binding;
    AlertDialog.Builder builder;
    private ActivityResultLauncher<Object> snapshotActivityLauncher;

    // controller for making qr related queries
    private final FirestorePlayerController firestorePlayerController = new FirestorePlayerController();

    private boolean saveLocation = true;
    private final QrCode qrCode = new QrCode();

    public QrCapturePreferenceFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQrCapturePreferenceBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrCode.setHash(QrCapturePreferenceFragmentArgs.fromBundle(getArguments()).getQrHash());
        firestorePlayerController.doesPlayerHaveQr(qrCode.getHash(), MainActivity.playerUsername, this);
        binding.saveLocationCheckbox.setChecked(saveLocation);
    }

    void init() {
        binding.saveLocationCheckbox.setOnClickListener(view -> {
            saveLocation = binding.saveLocationCheckbox.isChecked();
        });
        snapshotActivityLauncher = registerForActivityResult(new SnapshotContract(), this::receiveSnapshot);

        binding.takeSnap.setOnClickListener(view -> {
            captureSnapshot();
        });

        builder = new AlertDialog.Builder(getContext());
    }

    @Override
    public void doesResourceExists(boolean exists) {
        if (exists) {
            // if qr with the given hash exist, show an alert
            builder.setMessage(R.string.qr_exists_message)
                    .setTitle(R.string.qr_exists_title)
                    .setCancelable(false)
                    .setPositiveButton("Continue", (DialogInterface dialog, int id) -> {
                        // TODO: handle already scanned QR
                        Log.d("", "doesResourceExists: qr exists");
                    }).create().show();
        } else {
            RandomNameGenerator nameGenerator = new RandomNameGenerator();
            qrCode.setName(nameGenerator.getWord());
            qrCode.setScore(ScoreCalculator.calculateScore(qrCode.getHash()));
            updateUi();
        }
    }

    /**
     * Updates the UI
     */
    private void updateUi() {
        binding.qrName.setText(qrCode.getName());
        binding.qrScore.setText(String.format(Locale.CANADA, "%d", qrCode.getScore()));
    }

    private void receiveSnapshot(Bitmap image) {

        returnFromActivity(image);
    }

    /**
     * Persists the scanned QR to db
     * @param snapshot The snapshot of qr
     */
    private void persistQr(Bitmap snapshot) {
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCode);
        // TODO: Get location
        playerQrCode.setSnapshot(new Snapshot(snapshot));
    }

    private void returnFromActivity(Bitmap image) {
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCode);
        playerQrCode.setSnapshot(new Snapshot(image));
        Intent intent = new Intent();
        intent.putExtra(QrCodeContract.RESPONSE_TAG, playerQrCode);
    }

    private void captureSnapshot() {
        snapshotActivityLauncher.launch(null);
    }
}