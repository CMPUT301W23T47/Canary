package com.cmput301w23t47.canary.model;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Class for the QR Code
 */
public class QrCode {
    // Hash of QR
    private String hash;
    // score of the QR
    private long score;
    // Location of the QR; Stores the last known location
    private Location location;
    // name of the QR
    private String name;
    // image of qr
    private Bitmap qrImage;
    ArrayList<Comment> comments = new ArrayList<>();

    public QrCode(String hash, long score, Location location, String name, Bitmap qrImage) {
        this.hash = hash;
        this.score = score;
        this.location = location;
        this.name = name;
        this.qrImage = qrImage;
    }

    public QrCode() {}

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getQrImage() {
        return qrImage;
    }

    public void setQrImage(Bitmap qrImage) {
        this.qrImage = qrImage;
    }

    /**
     * Determines whether location for the qr is available
     * @return true if location is available
     */
    public boolean hasLocation() {
        return location != null;
    }
}
