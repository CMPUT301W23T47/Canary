package com.cmput301w23t47.canary.model;

import android.graphics.Bitmap;
import android.location.Location;

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

    /**
     * Instantiates a new Qr code.
     *
     * @param hash     the hash
     * @param score    the score
     * @param location the location
     * @param name     the name
     */
    public QrCode(String hash, long score, Location location, String name, Bitmap qrImage, ArrayList<Comment> comments) {
        this.hash = hash;
        this.score = score;
        this.location = location;
        this.name = name;
        this.qrImage = qrImage;
        this.comments = comments;
    }

    /**
     * Instantiates a new Qr code.
     */
    public QrCode() {}

    /**
     * Gets hash.
     *
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets hash.
     *
     * @param hash the hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public long getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
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
     * Getter for comments
     * @return the list of comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * Setter for comments
     * @param comments the list of comments
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Determines whether location for the qr is available
     *
     * @return true if location is available
     */
    public boolean hasLocation() {
        return location != null;
    }
}
