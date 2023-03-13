package com.cmput301w23t47.canary.model;

import android.location.Location;

import com.cmput301w23t47.canary.controller.QrCodeController;

import java.util.Date;


/**
 * Class for the QR Code scanned by the Player
 */
public class PlayerQrCode {
    // QR Code scanned
    private QrCode qrCode;
    // Snapshot of the QR
    private Snapshot snapshot;
    // the date when qr was scanned by player
    private Date scanDate;
    // whether the location of qr is shared
    private boolean locationShared;

    public PlayerQrCode() {}

    public PlayerQrCode(QrCode qrCode, Date date) {
        this.qrCode = qrCode;
        this.scanDate = date;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    public String getName() {return qrCode.getName();}

    public Location getLocation() {return qrCode.getLocation();}

    public Date getScanDate() {
        return scanDate;
    }

    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    public boolean isLocationShared() {
        return locationShared;
    }

    public void setLocationShared(boolean locationShared) {
        this.locationShared = locationShared;
    }

    /**
     * Gets the score for the qr
     * @return the score of the qr
     */
    public long retrieveScore() {
        return qrCode.getScore();
    }

    /**
     * Gets the formatted scan date
     * @return the formatted string for scan date
     */
    public String retrieveDateString() {
        if (scanDate == null) { return ""; }
        return QrCodeController.getFormattedDate(scanDate);
    }

    /**
     * Retrieves the hash of the qr
     * @return the hash of the qr
     */
    public String retrieveHash() {
        return qrCode.getHash();
    }

    /**
     * Setter for the location in the qr code
     * @param newLocation the new location to set
     */
    public void putLocation(Location newLocation) {
        qrCode.setLocation(newLocation);
    }
}
