package com.cmput301w23t47.canary.model;

import java.util.Date;

/**
 * Class for comment
 */
public class Comment {
    // Player who added the comment
    private String playerUsername;
    // The message in the comment
    private String message;
    // The date when QR was added
    private Date date;

    /**
     * Constructor for QR
     * @param playerUsername the player username
     * @param message the message of comment
     * @param date the date when comment was posted
     */
    public Comment(String playerUsername, String message, Date date) {
        this.playerUsername = playerUsername;
        this.message = message;
        this.date = date;
    }

    public Comment(){};

    /**
     * Getter for username of player
     * @return
     */
    public String getPlayerUsername() {
        return playerUsername;
    }

    /**
     * Getter for body of comment
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * The date when qr was created
     * @return
     */
    public Date getDate() {
        return date;
    }
}
