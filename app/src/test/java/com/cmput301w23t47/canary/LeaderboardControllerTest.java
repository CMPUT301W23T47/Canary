package com.cmput301w23t47.canary;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cmput301w23t47.canary.controller.LeaderboardController;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.google.common.collect.Comparators;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests for the leaderboard Controller
 */
public class LeaderboardControllerTest {
    private ArrayList<LeaderboardPlayer> getMockLeaderboardPlayers() {
        ArrayList<LeaderboardPlayer> players = new ArrayList<>();
        players.add(new LeaderboardPlayer("testPlayer2", 222, 100));
        players.add(new LeaderboardPlayer("testPlayer3", 500, 13));
        players.add(new LeaderboardPlayer("testPlayer4", 450, 43));
        players.add(new LeaderboardPlayer("testPlayer5", 230, 88));
        return players;
    }

    /**
     * Tests the getLeaderboardPlayerList function
     */
    @Test
    public void testGetLeaderboardPlayerList() {
        ArrayList<PlayerRepository> playerRepoList = new ArrayList<>();
        playerRepoList.add(new PlayerRepository("player1", "p1", "l1", 100, 50, new ArrayList<>(), 0,
                new ArrayList<>()));
        playerRepoList.add(new PlayerRepository("player2", "p2", "l2", 101, 53, new ArrayList<>(), 0,
                new ArrayList<>()));
        playerRepoList.add(new PlayerRepository("player3", "p3", "l3", 110, 55, new ArrayList<>(), 0,
                new ArrayList<>()));
        playerRepoList.add(new PlayerRepository("player4", "p4", "l4", 121, 58, new ArrayList<>(), 0,
                new ArrayList<>()));

        ArrayList<LeaderboardPlayer> leaderboardPlayers = LeaderboardController.getLeaderboardPlayerList(playerRepoList);
        // verify the size and attributes
        assertEquals(leaderboardPlayers.size(), playerRepoList.size());
        for (int i = 0; i < leaderboardPlayers.size(); i++) {
            LeaderboardPlayer p1 = leaderboardPlayers.get(i);
            PlayerRepository p2 = playerRepoList.get(i);
            assertEquals(p1.getMaxScoreQr(), p2.getMaxScoreQr());
            assertEquals(p1.getScore(), p2.getScore());
            assertEquals(p1.getUsername(), p2.getUsername());
        }
    }

    @Test
    public void testGetPlayerWithMaxScore() {
        ArrayList<LeaderboardPlayer> players = getMockLeaderboardPlayers();
        LeaderboardPlayer maxScorePlayer = LeaderboardController.getPlayerWithMaxScore(players);
        // verify the returned player
        assertEquals(maxScorePlayer.getUsername(), "testPlayer3");
        assertEquals(maxScorePlayer.getScore(), 500);
    }

    @Test
    public void testGetRankForPlayer() {
        ArrayList<LeaderboardPlayer> players = getMockLeaderboardPlayers();
        players = LeaderboardController.getPlayersSortedByMaxQr(players);
        assertEquals(1, LeaderboardController.getRankForPlayer("testPlayer2", players));
        assertEquals(4, LeaderboardController.getRankForPlayer("testPlayer3", players));
    }

    @Test
    public void testGetPlayersSortedByMaxQr() {
        ArrayList<LeaderboardPlayer> players = getMockLeaderboardPlayers();
        players = LeaderboardController.getPlayersSortedByMaxQr(players);
        // verify the order of the elements
        assertTrue(Comparators.isInOrder(players, (Object o1, Object o2) ->
                (int)(((LeaderboardPlayer)o2).getMaxScoreQr() - ((LeaderboardPlayer)o1).getMaxScoreQr()))
        );
    }
}
