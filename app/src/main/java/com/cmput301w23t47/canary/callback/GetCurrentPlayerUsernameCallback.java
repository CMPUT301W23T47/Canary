package com.cmput301w23t47.canary.callback;

/**
 * Callback interface for getting the username of the current player
 * on the device
 */
public interface GetCurrentPlayerUsernameCallback {
    /**
     * The callback function that gets called with the username
     * of the current player
     * @param playerUsername the username of the current player
     */
    void getCurrentPlayerUsername(String playerUsername);
}
