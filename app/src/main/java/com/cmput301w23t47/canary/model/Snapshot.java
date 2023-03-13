package com.cmput301w23t47.canary.model;

import android.graphics.Bitmap;

/**
 * Class for Snapshot
 */
public class Snapshot {
    private Bitmap bitmap;

    /**
     * Instantiates a new Snapshot.
     *
     * @param bitmap the bitmap
     */
    public Snapshot(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Gets bitmap.
     *
     * @return the bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Sets bitmap.
     *
     * @param bitmap the bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
