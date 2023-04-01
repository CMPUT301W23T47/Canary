package com.cmput301w23t47.canary.view.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w23t47.canary.view.activity.CaptureSnapshotActivity;
import com.cmput301w23t47.canary.view.activity.ScanQRCodeActivity;

/**
 * Contract for launching the Snapshot capture activity
 */
public class SnapshotContract extends ActivityResultContract<Object, Bitmap> {
    static public final String RESPONSE_TAG = "SnapshotContractResp";

    /**
     * Creates the intent for launching the application
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Object o) {
        return new Intent(context, CaptureSnapshotActivity.class);
    }

    /**
     * Parses the result and returns it
     * @return the result string
     */
    @Override
    public Bitmap parseResult(int i, @Nullable Intent intent) {
        if (i == Activity.RESULT_OK) {
            return (Bitmap) intent.getExtras().get(RESPONSE_TAG);
        }
        return null;
    }
}
