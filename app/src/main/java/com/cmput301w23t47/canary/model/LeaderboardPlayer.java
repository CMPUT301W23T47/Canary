package com.cmput301w23t47.canary.model;


/**
 * Class for recording the players in the leaderboard
 */
public class LeaderboardPlayer {
    // the username of player
    private String username;
    // the score of player
    private long score;
    // the score of maximum scoring QR for player
    private long maxScoreQr;

    /**
     * Constructor for LeaderboardPlayer
     *
     * @param username   the username
     * @param score      the score
     * @param maxScoreQr the max score qr
     */
    public LeaderboardPlayer(String username, long score, long maxScoreQr) {
        this.username = username;
        this.score = score;
        this.maxScoreQr = maxScoreQr;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
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
     * Gets max score qr.
     *
     * @return the max score qr
     */
    public long getMaxScoreQr() {
        return maxScoreQr;
    }
}

