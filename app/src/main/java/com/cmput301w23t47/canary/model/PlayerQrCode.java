package com.cmput301w23t47.canary.model;

import android.graphics.Bitmap;
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

    /**
     * Instantiates a new Player qr code.
     */
    public PlayerQrCode() {}

    /**
     * Instantiates a new Player qr code.
     *
     * @param qrCode         the qr code
     * @param date           the date
     * @param locationShared the location shared
     */
    public PlayerQrCode(QrCode qrCode, Date date, boolean locationShared) {
        this.qrCode = qrCode;
        this.scanDate = date;
        this.locationShared = locationShared;
    }

    /**
     * Gets qr code.
     *
     * @return the qr code
     */
    public QrCode getQrCode() {
        return qrCode;
    }

    /**
     * Sets qr code.
     *
     * @param qrCode the qr code
     */
    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Gets snapshot.
     *
     * @return the snapshot
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Sets snapshot.
     *
     * @param snapshot the snapshot
     */
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {return qrCode.getName();}

    /**
     * Gets location.
     *
     * @return the location
     */
    public Location getLocation() {return qrCode.getLocation();}

    /**
     * Gets scan date.
     *
     * @return the scan date
     */
    public Date getScanDate() {
        return scanDate;
    }

    /**
     * Sets scan date.
     *
     * @param scanDate the scan date
     */
    public void setScanDate(Date scanDate) {
        this.scanDate = scanDate;
    }

    /**
     * Is location shared boolean.
     *
     * @return the boolean
     */
    public boolean isLocationShared() {
        return locationShared;
    }

    /**
     * Sets location shared.
     *
     * @param locationShared the location shared
     */
    public void setLocationShared(boolean locationShared) {
        this.locationShared = locationShared;
    }

    /**
     * Gets the score for the qr
     *
     * @return the score of the qr
     */
    public long retrieveScore() {
        return qrCode.getScore();
    }

    /**
     * Gets the formatted scan date
     *
     * @return the formatted string for scan date
     */
    public String retrieveDateString() {
        if (scanDate == null) { return ""; }
        return QrCodeController.getFormattedDate(scanDate);
    }

    /**
     * Retrieves the hash of the qr
     *
     * @return the hash of the qr
     */
    public String retrieveHash() {
        return qrCode.getHash();
    }

    /**
     * Setter for the location in the qr code
     *
     * @param newLocation the new location to set
     */
    public void putLocation(Location newLocation) {
        qrCode.setLocation(newLocation);
    }

}