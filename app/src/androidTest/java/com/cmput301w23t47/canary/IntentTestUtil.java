package com.cmput301w23t47.canary;

import android.app.Activity;
import android.content.Intent;


import android.app.Instrumentation.ActivityResult;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cmput301w23t47.canary.view.contract.ScanQrContract;
import com.robotium.solo.Condition;
import com.robotium.solo.Solo;


/**
 * Util Class for Intent testing
 */
public class IntentTestUtil {
    // The result of the QR Code scanned
    public static final String scanQrCodeResult = "Qr Code Found: Canary T47";

    /**
     * Gets the mock result for the ScanQRCodeActivity
     * @return the intent that mimics the response of activity
     */
    public static ActivityResult getMockResultForScanQrCodeActivity() {
        Intent result = new Intent();
        result.putExtra(ScanQrContract.RESPONSE_TAG, scanQrCodeResult);
        return new ActivityResult(Activity.RESULT_OK, result);
    }

    public static void waitForProgressBarToHide(ConstraintLayout progressBarBox, Solo solo) {
        solo.waitForCondition(new Condition() {
            @Override
            public boolean isSatisfied() {
                return progressBarBox.getVisibility() == View.GONE;
            }
        }, 2000);
    }
}
