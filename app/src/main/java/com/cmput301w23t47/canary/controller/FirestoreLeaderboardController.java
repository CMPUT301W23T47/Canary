package com.cmput301w23t47.canary.controller;

import android.os.Handler;

import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Controller for operating on the leaderboard collection
 */
public class FirestoreLeaderboardController extends FirestoreController{
    /**
     * Checks if the leaderboard needs to be updated using the new player model
     * @param playerRepo the player that changed
     */
    public void updateLeaderboardIfRequired(PlayerRepository playerRepo) {
        new Thread(() -> {
            Task<DocumentSnapshot> lbTask = leaderboard.document(globalLeaderboardDocument).get();
            Leaderboard leaderboard = waitForTask(lbTask, Leaderboard.class);
            boolean updateLeaderboard = false;
            if (leaderboard.getMaxQrPlayer().equals(playerRepo.getUsername())) {
                updateLeaderboard = updatePlayerWithMaxQr(leaderboard);
            }
            if (leaderboard.getMaxScorePlayer().equals(playerRepo.getUsername())) {
                updateLeaderboard |= updatePlayerWithMaxScore(leaderboard);
            }

            // update the leaderboard if it was indeed updated
            if (updateLeaderboard) {
                Task<Void> updateTask = getGlobalLeaderboardDoc().set(leaderboard);
                waitForUpdateTask(updateTask);
            }
        }).start();
    }

    /**
     * Updates the player with the maximum number of qrcodes
     * @param leaderboard The leaderboard to update
     * @return boolean True if updated, False if not
     */
    protected boolean updatePlayerWithMaxQr(Leaderboard leaderboard) {
        Task<QuerySnapshot> queryTask = players.orderBy("qrCodesSize", Query.Direction.DESCENDING).limit(1).get();
        waitForQuery(queryTask);
        if (!queryTask.getResult().isEmpty()) {
            DocumentSnapshot maxQrPlayerDoc = queryTask.getResult().getDocuments().get(0);
            PlayerRepository maxQrPlayer = maxQrPlayerDoc.toObject(PlayerRepository.class);
            leaderboard.setMaxQrPlayer(maxQrPlayer.getUsername());
            leaderboard.setMaxQr(maxQrPlayer.getQrCodesSize());
            return true;
        }
        return false;
    }

    /**
     * Updates the player with the maximum qr code score
     * @param leaderboard The leaderboard to update
     * @return boolean True if updated, False if not
     */
    protected boolean updatePlayerWithMaxScore(Leaderboard leaderboard) {
        Task<QuerySnapshot> queryTask = players.orderBy("score", Query.Direction.DESCENDING).limit(1).get();
        waitForQuery(queryTask);
        if (!queryTask.getResult().isEmpty()) {
            DocumentSnapshot maxScorePlayerDoc = queryTask.getResult().getDocuments().get(0);
            PlayerRepository maxQrPlayer = maxScorePlayerDoc.toObject(PlayerRepository.class);
            leaderboard.setMaxScorePlayer(maxQrPlayer.getUsername());
            leaderboard.setMaxScore(maxQrPlayer.getQrCodesSize());
            return true;
        }
        return false;
    }

    /**
     * Gets the global leaderboard document
     * @return global leaderboard document
     */
    protected DocumentReference getGlobalLeaderboardDoc() {
        return leaderboard.document(globalLeaderboardDocument);
    }
}
