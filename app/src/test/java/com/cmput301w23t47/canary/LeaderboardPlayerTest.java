package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import com.cmput301w23t47.canary.model.LeaderboardPlayer;

import org.junit.Test;

/**
 * The type Leaderboard player test.
 */
public class LeaderboardPlayerTest {
    /**
     * The Player 1.
     */
    LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
    /**
     * The Player 2.
     */
    LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
    /**
     * The Player 3.
     */
    LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);

    /**
     * Gets username test.
     */
    @Test
    public void getUsernameTest() {
        assertEquals("player1", player1.getUsername());
        assertEquals("player2", player2.getUsername());
        assertEquals("player3", player3.getUsername());
    }

    /**
     * Gets score test.
     */
    @Test
    public void getScoreTest() {
        assertEquals(10, player1.getScore());
        assertEquals(20, player2.getScore());
        assertEquals(30, player3.getScore());
    }

    /**
     * Gets max score qr test.
     */
    @Test
    public void getMaxScoreQrTest() {
        assertEquals(10, player1.getMaxScoreQr());
        assertEquals(20, player2.getMaxScoreQr());
        assertEquals(30, player3.getMaxScoreQr());
    }
}
