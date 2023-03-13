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

    public QrCodeRepository(String hash, long score, GeoPoint location, String name, String qrImage) {
        this.hash = hash;
        this.score = score;
        this.location = location;
        this.name = name;
        this.qrImage = qrImage;
    }

    @Exclude
    public QrCode retrieveParsedQrCode() {
        Location loc = new Location("");
        loc.setLatitude(location.getLatitude());
        loc.setLongitude(location.getLongitude());
        Bitmap bitmap = null;
        if (qrImage != "") {
            bitmap = SnapshotController.getImage(qrImage);
        }
        return new QrCode(hash, score, loc , name, bitmap);
    }

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
        return new QrCode(hash, score, null, name, SnapshotController.getImage(qrImage));
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

    public String getQrImage() {
        return qrImage;
    }

    public void setQrImage(String qrImage) {
        this.qrImage = qrImage;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "%s", name);
    }
}
