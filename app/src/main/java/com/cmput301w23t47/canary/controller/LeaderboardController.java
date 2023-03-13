package com.cmput301w23t47.canary.controller;

import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Controller class for Leaderboard
 */
public class LeaderboardController {
    /**
     * Gets the leaderboard player list from the repo stored in firestore
     * @param playerRepoList ArrayList(LeaderboardPlayer): the list of players in firestore model
     * @return mapped list of LeaderboardPlayer models
     */
    public static ArrayList<LeaderboardPlayer> getLeaderboardPlayerList(
            ArrayList<PlayerRepository> playerRepoList
    ) {
        ArrayList<LeaderboardPlayer> playerList = new ArrayList<>();
        for (PlayerRepository playerRepo : playerRepoList) {
            playerList.add(playerRepo.retrieveLeaderboardPlayer());
        }
        return playerList;
    }

    /**
     * Gets the player with maximum score
     * @param players list of LeaderboardPlayer
     * @return the player with maximum score
     */
    public static LeaderboardPlayer getPlayerWithMaxScore(ArrayList<LeaderboardPlayer> players) {
        if (players.size() == 0) {
            return new LeaderboardPlayer("", 0, 0);
        }
        LeaderboardPlayer maxPlayer = players.get(0);
        for (LeaderboardPlayer p : players) {
            if (p.getScore() > maxPlayer.getScore()) {
                maxPlayer = p;
            }
        }
        return maxPlayer;
    }

    /**
     * Gets the player list sorted by score
     * @param playersOrig the original list of players
     * @return sorted list of players
     */
    public static ArrayList<LeaderboardPlayer> getPlayersSortedByScore(ArrayList<LeaderboardPlayer> playersOrig) {
        ArrayList<LeaderboardPlayer> players = new ArrayList<>(playersOrig);
        playersOrig.sort(Comparator.comparing(LeaderboardPlayer::getScore).reversed());
        return players;
    }

    /**
     * Gets the player list sorted by max scoring qr
     * @param playersOrig the original list of players
     * @return sorted list of players
     */
    public static ArrayList<LeaderboardPlayer> getPlayersSortedByMaxQr(ArrayList<LeaderboardPlayer> playersOrig) {
        ArrayList<LeaderboardPlayer> players = new ArrayList<>(playersOrig);
        playersOrig.sort(Comparator.comparing(LeaderboardPlayer::getMaxScoreQr).reversed());
        return players;
    }

    /**
     * Gets the overall rank of the player among all the players in the leaderboard
     * @param username The username of the player to get
     * @param sortedPlayers The list of players sorted with highest qr code
     * @return The rank of the player
     */
    public static int getRankForPlayer(String username, ArrayList<LeaderboardPlayer> sortedPlayers) {
        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getUsername().equals(username))
                return i+1;
        }
        return 0;
    }
}
