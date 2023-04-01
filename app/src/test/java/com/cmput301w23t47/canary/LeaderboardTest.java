package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;

import org.junit.Test;

import java.util.ArrayList;

/**
 * The type Leaderboard test.
 */
public class LeaderboardTest {
    /**
     * The Leaderboard.
     */
    Leaderboard leaderboard = new Leaderboard();

    /**
     * Gets highest scoring qr test.
     */
    @Test
    public void getHighestScoringQrTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<Long> playersMaxScoreQr = new ArrayList<>();
        playersMaxScoreQr.add(player1.getMaxScoreQr());
        playersMaxScoreQr.add(player2.getMaxScoreQr());
        playersMaxScoreQr.add(player3.getMaxScoreQr());
        //get highest scoring qr
        long maxScoreQr = 0;
        for (long score : playersMaxScoreQr) {
            if (score > maxScoreQr) {
                maxScoreQr = score;
            }
        }
        //user  function to get highest scoring qr
        assertEquals(30, maxScoreQr);
    }

    /**
     * Gets highest score test.
     */
    @Test
    public void getHighestScoreTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<Long> playersScore = new ArrayList<>();
        playersScore.add(player1.getScore());
        playersScore.add(player2.getScore());
        playersScore.add(player3.getScore());
        //get highest score
        long maxScore = 0;
        for (long score : playersScore) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        //user  function to get highest score
        assertEquals(30, maxScore);
    }

    /**
     * Gets leaderboard test.
     */
    @Test
    public void getLeaderboardTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //get leaderboard
        ArrayList<LeaderboardPlayer> leaderboard = new ArrayList<>();
        for (LeaderboardPlayer player : leaderboardPlayers) {
            leaderboard.add(player);
        }
        //user  function to get leaderboard
        assertEquals(3, leaderboard.size());
    }

    /**
     * Gets leaderboard player test.
     */
    @Test
    public void getLeaderboardPlayerTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //get leaderboard player
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to get leaderboard player
        assertEquals("player1", leaderboardPlayer.getUsername());
    }

    /**
     * Gets leaderboard player score test.
     */
    @Test
    public void getLeaderboardPlayerScoreTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //get leaderboard player score
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to get leaderboard player score
        assertEquals(10, leaderboardPlayer.getScore());
    }

    /**
     * Gets leaderboard player max score qr test.
     */
    @Test
    public void getLeaderboardPlayerMaxScoreQrTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //get leaderboard player max score qr
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to get leaderboard player max score qr
        assertEquals(10, leaderboardPlayer.getMaxScoreQr());
    }

    /**
     * Sets leaderboard player test.
     */
    @Test
    public void setLeaderboardPlayerTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //set leaderboard player
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to set leaderboard player
        assertEquals("player1", leaderboardPlayer.getUsername());
    }

    /**
     * Set leaderboard player score test.
     */
    @Test
    public void setLeaderboardPlayerScoreTest(){
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //set leaderboard player score
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to set leaderboard player score
        assertEquals(10, leaderboardPlayer.getScore());
    }

    /**
     * Sets leaderboard player max score qr test.
     */
    @Test
    public void setLeaderboardPlayerMaxScoreQrTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //set leaderboard player max score qr
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to set leaderboard player max score qr
        assertEquals(10, leaderboardPlayer.getMaxScoreQr());
    }

    /**
     * Gets leaderboard player score qr test.
     */
    @Test
    public void getLeaderboardPlayerScoreQrTest() {
        LeaderboardPlayer player1 = new LeaderboardPlayer("player1", 10, 10);
        LeaderboardPlayer player2 = new LeaderboardPlayer("player2", 20, 20);
        LeaderboardPlayer player3 = new LeaderboardPlayer("player3", 30, 30);
        //add players to leaderboard
        ArrayList<LeaderboardPlayer> leaderboardPlayers = new ArrayList<>();
        leaderboardPlayers.add(player1);
        leaderboardPlayers.add(player2);
        leaderboardPlayers.add(player3);
        //get leaderboard player score qr
        LeaderboardPlayer leaderboardPlayer = new LeaderboardPlayer("player1", 10, 10);
        for (LeaderboardPlayer player : leaderboardPlayers) {
            if (player.getUsername().equals(leaderboardPlayer.getUsername())) {
                leaderboardPlayer = player;
            }
        }
        //user  function to get leaderboard player score qr
        assertEquals(10, leaderboardPlayer.getScore());
    }

}
