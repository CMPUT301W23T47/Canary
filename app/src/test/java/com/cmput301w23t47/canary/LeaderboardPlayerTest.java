package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import com.cmput301w23t47.canary.model.LeaderboardPlayer;

import org.junit.Test;

public class LeaderboardPlayerTest {
    LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
    LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
    LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
    @Test
    public void getUsernameTest() {
        assertEquals("player1", player1.getUsername());
        assertEquals("player2", player2.getUsername());
        assertEquals("player3", player3.getUsername());
    }
    @Test
    public void getScoreTest() {
        assertEquals(10, player1.getScore());
        assertEquals(20, player2.getScore());
        assertEquals(30, player3.getScore());
    }
    @Test
    public void getMaxScoreQrTest() {
        assertEquals(10, player1.getMaxScoreQr());
        assertEquals(20, player2.getMaxScoreQr());
        assertEquals(30, player3.getMaxScoreQr());
    }
}
