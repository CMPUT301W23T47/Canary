package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.google.common.collect.Comparators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * The tests Leaderboard Model.
 */
public class LeaderboardTest {
    // The leaderboard object
    Leaderboard leaderboard = null;

    /**
     * Prepares the mock object for leaderboard
     */
    public void setMockLeaderboard() {
        ArrayList<LeaderboardPlayer> players = new ArrayList<>();
        LeaderboardPlayer p = new LeaderboardPlayer("testPlayer1", 100, 10);
        players.add(p);
        players.add(new LeaderboardPlayer("testPlayer2", 222, 100));
        players.add(new LeaderboardPlayer("testPlayer3", 500, 13));
        players.add(new LeaderboardPlayer("testPlayer4", 450, 43));
        players.add(new LeaderboardPlayer("testPlayer5", 230, 88));
        leaderboard = new Leaderboard("testPlayer4", 50, "testPlayer2", 100, null, p);
        leaderboard.setPlayers(players);
    }

    /**
     * Tests the call for getting player with maximum QR
     */
    @Test
    public void testGetMaxQr() {
        setMockLeaderboard();
        assertEquals(leaderboard.getMaxQrPlayer(), "testPlayer4");
        assertEquals(leaderboard.getMaxQr(), 50);
    }

    /**
     * Tests the call for getting player with maximum score
     */
    @Test
    public void testGetMaxScore() {
        setMockLeaderboard();
        assertEquals(leaderboard.getMaxScorePlayer(), "testPlayer2");
        assertEquals(leaderboard.getMaxScore(), 100);
    }

    /**
     * Tests the call for getting the current player
     */
    @Test
    public void testGetCurrentPlayer() {
        setMockLeaderboard();
        assertEquals(leaderboard.getCurrentPlayer().getUsername(), "testPlayer1");
    }

    /**
     * Tests the call for getting the players sorted by score
     */
    @Test
    public void testGetByScore() {
        setMockLeaderboard();
        // assert that the players are sorted in descending order by score
        assertTrue(Comparators.isInOrder(leaderboard.getByScore(), (Object o1, Object o2) ->
                (int)(((LeaderboardPlayer)o2).getScore() - ((LeaderboardPlayer)o1).getScore()))
        );
    }


    /**
     * Tests the call for getting the players sorted by Highest Scoring QR
     */
    @Test
    public void testGetByHighestScoringQr() {
        setMockLeaderboard();
        // assert that the players are sorted in descending order by maxScoreQr
        assertTrue(Comparators.isInOrder(leaderboard.getByHighestScoringQr(), (Object o1, Object o2) ->
                (int)(((LeaderboardPlayer)o2).getMaxScoreQr() - ((LeaderboardPlayer)o1).getMaxScoreQr()))
        );
    }

    /**
     * Gets leaderboard test.
     */
    @Test
    public void testSetPlayers() {
        setMockLeaderboard();
        // add more players to leaderboard
        ArrayList<LeaderboardPlayer> players = new ArrayList<>();
        players.addAll(leaderboard.getPlayers());
        players.add(new LeaderboardPlayer("testPlayer11", 234, 23));
        players.add(new LeaderboardPlayer("testPlayer12", 123, 12));
        players.add(new LeaderboardPlayer("testPlayer13", 123, 53));
        players.add(new LeaderboardPlayer("testPlayer14", 324, 54));
        leaderboard.setPlayers(players);
        // verify the size of players
        assertEquals(leaderboard.getPlayers().size(), players.size());
        // verify the ordering of new players
        assertTrue(Comparators.isInOrder(leaderboard.getByHighestScoringQr(), (Object o1, Object o2) ->
                (int)(((LeaderboardPlayer)o2).getMaxScoreQr() - ((LeaderboardPlayer)o1).getMaxScoreQr()))
        );
        assertTrue(Comparators.isInOrder(leaderboard.getByScore(), (Object o1, Object o2) ->
                (int)(((LeaderboardPlayer)o2).getScore() - ((LeaderboardPlayer)o1).getScore()))
        );
    }

    /**
     * Tests the call for getting the players sorted by Highest Scoring QR
     */
    @Test
    public void testLeaderboardPlayer() {
        LeaderboardPlayer player = new LeaderboardPlayer("testPlayer11", 234, 23);
        assertEquals(player.getUsername(), "testPlayer11");
        assertEquals(player.getScore(), 234);
        assertEquals(player.getMaxScoreQr(), 23);
    }
}
