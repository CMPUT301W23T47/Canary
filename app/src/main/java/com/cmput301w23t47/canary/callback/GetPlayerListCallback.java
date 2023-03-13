package com.cmput301w23t47.canary.callback;

import com.cmput301w23t47.canary.model.Player;

import java.util.ArrayList;

/**
 * Interface for retrieving the list of players
 */
public interface GetPlayerListCallback {
    /**
     * Method to get the list of players
     * @param players the list of players
     */
    public void getPlayerList(ArrayList<Player> players);
}
