package com.cmput301w23t47.canary.repository;

import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.model.Snapshot;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import java.util.Date;

/**
 * The type Player qr code repository.
 */
public class PlayerQrCodeRepository {
    // TODO: Add support for snapshot
    // Store the reference to the QRCode object
    private DocumentReference qrCode;
    // store the reference to the snapshot
    private DocumentReference snapshot;
    // store the date the qr was scanned
    private Timestamp scanDate;
    // store the score of qr; required to speed up qr deletion
    private long qrScore;

    @Exclude
    // stores the parsed playerQrCode
    private PlayerQrCode parsedPlayerQrCode;

    /**
     * A default constructor
     */
    public PlayerQrCodeRepository() {
        parsedPlayerQrCode = new PlayerQrCode();
    }

    /**
     * Another constructor which initializes the PLayerQrCodeRepo
     */
    public PlayerQrCodeRepository(DocumentReference qrCode, DocumentReference snapshot, Timestamp scanDate, long qrScore) {
        this.qrCode = qrCode;
        this.snapshot = snapshot;
        this.scanDate = scanDate;
        this.qrScore = qrScore;
    }

    /**
     * Assigns QRCode, Snapshot and Date to the ParsedQr
     * @param qrCode QrCode object to set
     * @param locSnap Snapshot of the Qr to be set
     */
    public void setParsedQrCode(QrCode qrCode, Snapshot locSnap) {
        this.parsedPlayerQrCode.setQrCode(qrCode);
        this.parsedPlayerQrCode.setSnapshot(locSnap);
        this.parsedPlayerQrCode.setScanDate(scanDate.toDate());
    }

    /**
     * Gives the Parsed Qr code for the player
     * @return parsedPlayerQrCode the qr code of the player
     */
    @Exclude
    public PlayerQrCode retrieveParsedPlayerQrCode() {
        return parsedPlayerQrCode;
    }

    /**
     * Gives the QrCode of the player
     * @return  qrCode A DocumentReference object referring to player qr code
     */
    public DocumentReference getQrCode() {
        return qrCode;
    }

    /**
     * Sets the Qrcode from the DocumentRefernce object
     * @param qrCode The Qr to be set
     */
    public void setQrCode(DocumentReference qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * Gives the Snapshot of the QR
     * @return Snapshot A Snapshot object referring to qr code
     */
    public DocumentReference getSnapshot() {
        return snapshot;
    }

    /**
     * Sets the Snapshot of the Qr from the DocumentReference object
     * @param snapshot The snapshot to be set
     */
    public void setSnapshot(DocumentReference snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Gives the date when the qr was scanned
     * @return scanDate The date when the qr was scanned
     */
    public Timestamp getScanDate() {
        return scanDate;
    }

    /**
     * Sets the date scanned of the qr to the given date
     * @param scanDate The given date to put in scanDate of the qr
     */
    public void setScanDate(Timestamp scanDate) {
        this.scanDate = scanDate;
    }

    /**
     * Gets the score of the qrcode
     * @return the score of the qrcode
     */
    public long getQrScore() {
        return qrScore;
    }

    /**
     * Sets the score of the qr
     * @param qrScore
     */
    public void setQrScore(long qrScore) {
        this.qrScore = qrScore;
    }

    /**
     * Retrieves the player qr code
     * @param qrCodeRepository the QrCodeRepo
     * @param snapRepo the snapshot repo
     * @return the PlayerQrCode object
     */
    public static PlayerQrCode retrievePlayerQrCode(QrCodeRepository qrCodeRepository, SnapshotRepository snapRepo, Timestamp date) {
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCodeRepository.retrieveParsedQrCode(), null);
        if (date != null) {
            playerQrCode.setScanDate(date.toDate());
        }
        if (snapRepo != null) {
            playerQrCode.setSnapshot(snapRepo.retrieveSnapshot());
        }
        return playerQrCode;
    }
}
