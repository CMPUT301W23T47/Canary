package com.cmput301w23t47.canary.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Class for Player
 * @author Meharpreet Singh Nanda
 */
public class Player {
    // username of the player
    private String username;
    // firstname of the player
    private String firstName;
    // lastname of the player
    private String lastName;
    // list of QR codes scanned by user
    private ArrayList<PlayerQrCode> qrCodes;
    // score of the player
    private long score = 0;
    // unique ID of the player, stored as the document name on firestore
    private String uniquePlayerId;
    private Bitmap playerImage;


    /**
     * Instantiates a new Player.
     *
     * @param username  the username
     * @param firstName the first name
     * @param lastName  the last name
     */
    public Player(String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.qrCodes = new ArrayList<>();
    }

    /**
     * Add qr code.
     *
     * @param playerQrCode the player qr code
     */
    public void addQrCode(PlayerQrCode playerQrCode) {
        qrCodes.add(playerQrCode);
    }

    /**
     * Instantiates a new Player.
     */
    public Player() {}

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets qr codes.
     *
     * @return the qr codes
     */
    public ArrayList<PlayerQrCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Sets qr codes.
     *
     * @param qrCodes the qr codes
     */
    public void setQrCodes(ArrayList<PlayerQrCode> qrCodes) {
        this.qrCodes = qrCodes;
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
     * Gets unique player id.
     *
     * @return the unique player id
     */
    public String getUniquePlayerId() {
        return uniquePlayerId;
    }

    /**
     * Sets unique player id.
     *
     * @param uniquePlayerId the unique player id
     */
    public void setUniquePlayerId(String uniquePlayerId) {
        this.uniquePlayerId = uniquePlayerId;
    }

    /**
     * Gets player image.
     *
     * @return the player image
     */
    public Bitmap getPlayerImage() {
        return playerImage;
    }

    /**
     * Sets player image.
     *
     * @param playerImage the player image
     */
    public void setPlayerImage(Bitmap playerImage) {
        this.playerImage = playerImage;
    }

    /**
     * Gets the highest qr score
     *
     * @return the highest qr score
     */
    public long getHighestQr(){
        PlayerQrCode highest = retrieveQrWithHighestScore();
        if (highest == null) {
            return 0;
        }
        return highest.retrieveScore();
    }

    /**
     * Gets the lowest qr
     *
     * @return the lowest qr
     */
    public long getLowestQr(){
        PlayerQrCode lowest = retrieveQrWithLowestScore();
        if (lowest == null) {
            return 0;
        }
        return lowest.retrieveScore();
    }

    /**
     * Gets the string to draw for the player
     *
     * @return the first char of the username
     */
    public String retrieveStringToDraw() {
        return String.valueOf(username.charAt(0));
    }

    /**
     * Retrieves the qr with highest score
     * @return the QR with highest score
     */
    public PlayerQrCode retrieveQrWithHighestScore() {
        if (qrCodes.isEmpty()) {
            return null;
        }
        PlayerQrCode highest = qrCodes.get(0);
        for (PlayerQrCode qr: qrCodes){
            if(highest.retrieveScore() < qr.retrieveScore())
                highest = qr;
        }
        return highest;
    }

    /**
     * Retrieves the qr with lowest score
     * @return the QR with lowest score
     */
    public PlayerQrCode retrieveQrWithLowestScore() {
        if (qrCodes.isEmpty()) {
            return null;
        }
        PlayerQrCode lowest = qrCodes.get(0);
        for (PlayerQrCode qr: qrCodes){
            if(lowest.retrieveScore() > qr.retrieveScore())
                lowest = qr;
        }
        return lowest;
    }
}
