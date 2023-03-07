package com.cmput301w23t47.canary.model;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.repository.PlayerRepository;

import java.util.ArrayList;

public class PlayerList{
   //store playername and their score
    private ArrayList<Player> players;

    private FirestoreController firestoreController;

    /**
     * Constructor for PlayerList
     */
    public void createPlayerList()
    {
        players = new ArrayList<>();
    }
    /**
     * Gets the player list from the repo stored in firestore
     * @param playerRepoList ArrayList(LeaderboardPlayer): the list of players in firestore model
     * @return mapped list of Playlist models
     */
    public ArrayList<Player> getPlayerList(ArrayList<PlayerRepository> playerRepoList){
        ArrayList<Player> playerList = new ArrayList<>();
        for (PlayerRepository playerRepo : playerRepoList) {
            playerList.add(playerRepo.getPlayer());
        }
        return playerList;
    }
    public Player get(int position) {
        return players.get(position);
    }
}
