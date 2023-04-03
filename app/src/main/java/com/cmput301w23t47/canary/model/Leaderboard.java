package com.cmput301w23t47.canary.model;

import com.cmput301w23t47.canary.controller.LeaderboardController;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Class for Leaderboard
 */
public class Leaderboard {
    /*Attributes*/
    // the username for player with max QRs
    private String maxQrPlayer;
    // the # of QRs for the winner
    private long maxQr;
    // the username for player max score
    private String maxScorePlayer;
    // the score of winner
    private long maxScore;

    @Exclude
    // list of all players to determine ranking
    private ArrayList<LeaderboardPlayer> players; // absent from Firestore
    @Exclude
    // player with max score
    private ArrayList<LeaderboardPlayer> byScore; // absent from Firestore
    @Exclude
    // player with max qr
    private ArrayList<LeaderboardPlayer> byHighestScoringQr; // absent from Firestore
    @Exclude
    // the current player
    private LeaderboardPlayer currentPlayer; // absent from Firestore

    /**
     * Instantiates a new Leaderboard.
     */
    public Leaderboard() {}

    /**
     * Constructor for testing purpose
     */
    public Leaderboard(String maxQrPlayer, long maxQr, String maxScorePlayer, long maxScore, ArrayList<LeaderboardPlayer> players, LeaderboardPlayer currentPlayer) {
        this.maxQrPlayer = maxQrPlayer;
        this.maxQr = maxQr;
        this.maxScorePlayer = maxScorePlayer;
        this.maxScore = maxScore;
        this.players = players;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets max qr player.
     *
     * @return the max qr player
     */
    /*Getters and Setters*/
    public String getMaxQrPlayer() {
        return maxQrPlayer;
    }

    /**
     * Sets max qr player.
     *
     * @param maxQrPlayer the max qr player
     */
    public void setMaxQrPlayer(String maxQrPlayer) {
        this.maxQrPlayer = maxQrPlayer;
    }

    /**
     * Gets max qr.
     *
     * @return the max qr
     */
    public long getMaxQr() {
        return maxQr;
    }

    /**
     * Sets max qr.
     *
     * @param maxQr the max qr
     */
    public void setMaxQr(long maxQr) {
        this.maxQr = maxQr;
    }

    /**
     * Gets max score player.
     *
     * @return the max score player
     */
    public String getMaxScorePlayer() {
        return maxScorePlayer;
    }

    /**
     * Sets max score player.
     *
     * @param maxScorePlayer the max score player
     */
    public void setMaxScorePlayer(String maxScorePlayer) {
        this.maxScorePlayer = maxScorePlayer;
    }

    /**
     * Gets max score.
     *
     * @return the max score
     */
    public long getMaxScore() {
        return maxScore;
    }

    /**
     * Sets max score.
     *
     * @param maxScore the max score
     */
    public void setMaxScore(long maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    @Exclude
    public ArrayList<LeaderboardPlayer> getPlayers() {
        return players;
    }



    /**
     * Sets the player list; Also sets the player with max score and QR
     * @param players the list of players
     */
    public void setPlayers(ArrayList<LeaderboardPlayer> players) {
        this.players = players;
        this.byScore = LeaderboardController.getPlayersSortedByScore(players);
        this.byHighestScoringQr = LeaderboardController.getPlayersSortedByMaxQr(players);
    }

    /**
     * Gives the list of the scores
     * @return  byScore Sorted list of scores
     */
    @Exclude
    public ArrayList<LeaderboardPlayer> getByScore() {
        return byScore;
    }

    /**
     * Gives the list of qrs sorted by the highes score code
     * @return  byHighestScoringQr Sorted list
     */
    @Exclude
    public ArrayList<LeaderboardPlayer> getByHighestScoringQr() {
        return byHighestScoringQr;
    }

    /**
     * Gives the current player
     * @return the current player
     */
    @Exclude
    public LeaderboardPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(LeaderboardPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Converts the string to a specific format
     * @return The converted String
     */
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "%s %d %s %d", maxScorePlayer, maxScore,
                maxQrPlayer, maxQr);
    }
}
