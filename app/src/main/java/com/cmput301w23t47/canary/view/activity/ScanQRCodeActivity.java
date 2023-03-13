/*
 * Copyright 2022 The Android Open Source Project, Meharpreet Singh Nanda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.cmput301w23t47.canary.view.activity;

import static androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.camera.view.LifecycleCameraController;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.cmput301w23t47.canary.databinding.ActivityScanQrcodeBinding;
import com.cmput301w23t47.canary.view.contract.ScanQrContract;
import com.cmput301w23t47.canary.view.model.QrCodeVm;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Activity for Scanning QR Code
 * Reference: https://github.com/android/camera-samples/tree/main/CameraX-MLKit
 * @author Meharpreet Singh Nanda
 */
public class ScanQRCodeActivity extends CameraBaseActivity {
    protected ActivityScanQrcodeBinding binding;

    protected BarcodeScanner qrScanner;
    protected ExecutorService cameraExecutor;

    /**
     *  Handles the layout of the activity, and called on activity creation.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    /**
     * Initializes the UI and starts the scanning of the qr
     */
    @Override
    protected void init() {
        checkPermissions();
        cameraExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     * Free resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        qrScanner.close();
    }

    /**
     * Turns on the camera
     */
    @Override
    protected void openCamera() {
        LifecycleCameraController camController = new LifecycleCameraController(getBaseContext());
        PreviewView preview = binding.cameraView;
        // Add the option to detect all types of QR Codes
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        qrScanner = BarcodeScanning.getClient(options);
        camController.setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(this),
                getQrCodeAnalyzer(preview));

        camController.bindToLifecycle(this);
        preview.setController(camController);
    }

    /**
     * Gets the analyzer for the images
     * @return MlKitAnalyzer the analyzer object
     */
    protected MlKitAnalyzer getQrCodeAnalyzer(PreviewView preview) {
        return new MlKitAnalyzer(
                Arrays.asList(qrScanner), COORDINATE_SYSTEM_VIEW_REFERENCED, ContextCompat.getMainExecutor(this),
                result -> {
                    List<Barcode> qrResList = result.getValue(qrScanner);
                    if (qrResList == null || qrResList.size() == 0 || qrResList.get(0) == null) {
                        // if there is no result
//                                preview.getOverlay().clear();
//                                // TODO: Understand
//                                preview.setOnTouchListener((view, motionEvent) -> {
//                                    return false;
//                                });
                        return;
                    }
                    Barcode qr = qrResList.get(0);
                    QrCodeVm qrVm = new QrCodeVm(qr);
                    Log.d(TAG, "getQrCodeAnalyzer: raw " + qrResList.get(0).getRawValue());
                    Log.d(TAG, "getQrCodeAnalyzer:  bytes" + qrResList.get(0).getRawBytes().toString());
//                            QrCodeDrawable qrCodeDrawable = new QrCodeDrawable();
                    preview.setOnTouchListener(qrVm::qrCodePressCallback);
                    preview.getOverlay().clear();
                    Intent intent = new Intent();
                    intent.putExtra(ScanQrContract.RESPONSE_TAG, qr.getRawValue());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
        );
    }
}