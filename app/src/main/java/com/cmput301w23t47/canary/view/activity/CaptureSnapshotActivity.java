package com.cmput301w23t47.canary.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.view.contract.SnapshotContract;

/**
 * Activity to capture the snapshot for a QR
 */
public class CaptureSnapshotActivity extends CameraBaseActivity {
    public static final String TAG = "CaptureSnapshotActivity";
    ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     *  Handles the layout of the activity, and called on activity creation.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_snapshot);
        init();
    }

    /**
     * Launches an activity and gets the bitmap image from the camera
     */
    protected void init() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        Bitmap img = (Bitmap)data.getExtras().get("data");
                        Intent intent = new Intent();
                        intent.putExtra(SnapshotContract.RESPONSE_TAG, img);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
        checkPermissions();
    }

    /**
     * Opens the camera app
     */
    @Override
    protected void openCamera() {
        Log.d(TAG, "openCamera: ");
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(camera);
    }
}
