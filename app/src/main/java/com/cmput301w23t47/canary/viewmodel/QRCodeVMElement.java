package com.cmput301w23t47.canary.viewmodel;

import android.location.Location;

import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.QRCode;
import com.cmput301w23t47.canary.model.Snapshot;

public class QRCodeVMElement {

    private String QRname;
    private Location playerPictureLocation;

    private String QRdate;

    private long QrPoints;
    private Snapshot playerPicture;

    // this is more an extension of the QRCode class specifically for displaying the qrcodes both on a map and in lists
    // therefore it cannot be made empty constructor


    public QRCodeVMElement() {
        this.QRname = "TEMPORARY DATA";
        this.playerPictureLocation = new Location("QRCodeVMElement");
        QrPoints = -1;
        this.playerPicture = new Snapshot();
    }

    public QRCodeVMElement(String QRname, Location playerPictureLocation, int qrPoints,String date ,Snapshot playerPicture) {
        this.QRname = QRname;
        this.playerPictureLocation = playerPictureLocation;
        this.QRdate = date;
        QrPoints = qrPoints;
        this.playerPicture = playerPicture;
    }

    public QRCodeVMElement(Player Player) {
        QRCode temp = Player.getQrCode();

        this.QRname = Player.getName();
        this.playerPictureLocation = Player.getLocation();
        QrPoints = temp.getScore();
        this.playerPicture = Player.getSnapshot();
    }

    public QRCodeVMElement changeCurrentData(Player Player) {
        QRCode temp = Player.getQrCode();

        this.QRname = Player.getName();
        this.playerPictureLocation = Player.getLocation();
        QrPoints = temp.getScore();
        this.playerPicture = Player.getSnapshot();
        return this;
    }

    public QRCodeVMElement setdata(String QRname, Location playerPictureLocation, int qrPoints, Snapshot playerPicture) {
        this.QRname = QRname;
        this.playerPictureLocation = playerPictureLocation;
        QrPoints = qrPoints;
        this.playerPicture = playerPicture;
        return this;
    }

    public String getQRdate() {
        return QRdate;
    }

    public void setQRdate(String QRdate) {
        this.QRdate = QRdate;
    }

    public String getQRname() {
        return QRname;
    }

    public void setQRname(String QRname) {
        this.QRname = QRname;
    }

    public Location getPlayerPictureLocation() {
        return playerPictureLocation;
    }

    public void setPlayerPictureLocation(Location playerPictureLocation) {
        this.playerPictureLocation = playerPictureLocation;
    }

    public long getQrPoints() {
        return QrPoints;
    }

    public void setQrPoints(int qrPoints) {
        QrPoints = qrPoints;
    }

    public Snapshot getPlayerPicture() {
        return playerPicture;
    }

    public void setPlayerPicture(Snapshot playerPicture) {
        this.playerPicture = playerPicture;
    }
}
