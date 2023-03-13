package com.cmput301w23t47.canary.callback;

import com.cmput301w23t47.canary.model.Leaderboard;

/**
 * The interface Update leaderboard callback.
 */
public interface UpdateLeaderboardCallback {
    /**
     * Update leaderboard.
     *
     * @param leaderboard the leaderboard
     */
    void updateLeaderboard(Leaderboard leaderboard);
}
