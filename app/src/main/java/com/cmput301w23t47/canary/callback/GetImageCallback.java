package com.cmput301w23t47.canary.callback;

import android.graphics.Bitmap;

/**
 * Callback for getting the image
 */
public interface GetImageCallback {
    /**
     * Called when image available
     * @param bitmap the bitmap image available
     */
    void getImage(Bitmap bitmap);
}
