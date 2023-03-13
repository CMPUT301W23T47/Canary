package com.cmput301w23t47.canary.repository;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.annotation.NonNull;

import com.cmput301w23t47.canary.controller.SnapshotController;
import com.cmput301w23t47.canary.model.QrCode;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.util.Locale;

public class QrCodeRepository {
    // Hash of QR
    private String hash;
    // score of the QR
    private long score;
    // Location of the QR; Stores the last known location
    private GeoPoint location;
    // name of the QR
    private String name;
    // date when qr was created
    private Timestamp createdOn;
    // the photo of the qr
    private String qrImage;

    public QrCodeRepository() {}

    /**
     * Instantiates a new Qr code repository.
     *
     * @param hash     the hash
     * @param score    the score
     * @param location the location
     * @param name     the name
     */
    public QrCodeRepository(String hash, long score, GeoPoint location, String name, String qrImage) {
        this.hash = hash;
        this.score = score;
        this.location = location;
        this.name = name;
        this.qrImage = qrImage;
    }

    /**
     * Retrieve parsed qr code qr code.
     *
     * @return the qr code
     */
    @Exclude
    public QrCode retrieveParsedQrCode() {
        Location loc = new Location("");
        if (location != null) {
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
        }
        Bitmap bitmap = null;
        if (qrImage != "") {
            bitmap = SnapshotController.getImage(qrImage);
        }
        return new QrCode(hash, score, loc , name, bitmap);
    }

    /**
     * Retrieve qr code repo qr code repository.
     *
     * @param qrCode the qr code
     * @return the qr code repository
     */
    public static QrCodeRepository retrieveQrCodeRepo(QrCode qrCode) {
        GeoPoint point = null;
        if (qrCode.hasLocation()) {
            point = new GeoPoint(qrCode.getLocation().getLatitude(), qrCode.getLocation().getLongitude());
        }
        String imageBase64 = "";
        if (qrCode.getQrImage() != null) {
            imageBase64 = SnapshotController.getBase64Image(qrCode.getQrImage());
        }
        return new QrCodeRepository(qrCode.getHash(), qrCode.getScore(), point, qrCode.getName(), imageBase64);
    }

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

    /**
     * Gets qr code.
     *
     * @return the qr code
     */
    @Exclude
    public QrCode getQrCode() {
        return new QrCode(hash, score, null, name, SnapshotController.getImage(qrImage));
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public GeoPoint getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    /**
     * Gets created on.
     *
     * @return the created on
     */
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets created on.
     *
     * @param createdOn the created on
     */
    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    /**
     * Converts a String to a specific date format
     * @return The converted String
     */
    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "%s", name);
    }
}
