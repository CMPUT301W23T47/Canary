package com.cmput301w23t47.canary.repository;

import android.location.Location;

import androidx.annotation.NonNull;

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

    public QrCodeRepository() {}

    public QrCodeRepository(String hash, long score, GeoPoint location, String name) {
        this.hash = hash;
        this.score = score;
        this.location = location;
        this.name = name;
    }

    @Exclude
    public QrCode retrieveParsedQrCode() {
        Location loc = new Location("");
        loc.setLatitude(location.getLatitude());
        loc.setLongitude(location.getLongitude());
        return new QrCode(hash, score, loc , name);
    }

    public static QrCodeRepository retrieveQrCodeRepo(QrCode qrCode) {
        GeoPoint point = null;
        if (qrCode.hasLocation()) {
            point = new GeoPoint(qrCode.getLocation().getLatitude(), qrCode.getLocation().getLongitude());
        }
        return new QrCodeRepository(qrCode.getHash(), qrCode.getScore(), point, qrCode.getName());
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public QrCode getQrCode() {
        return new QrCode(hash, score, null, name);
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "%s", name);
    }
}
