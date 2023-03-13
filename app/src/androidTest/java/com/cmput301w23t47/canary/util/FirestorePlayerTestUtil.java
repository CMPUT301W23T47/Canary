package com.cmput301w23t47.canary.util;

import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirestorePlayerTestUtil extends FirestorePlayerController {
    /**
     * Gets the test player
     * @return the test player
     */
    public Player getTestPlayer() {
        Task<QuerySnapshot> playersTask = players.get();
        waitForQuery(playersTask);
        // map all players to object
        if (playersTask.getResult().getDocuments().size() == 0) {
            return null;
        }
        return playersTask.getResult().getDocuments().get(0).toObject(PlayerRepository.class)
                .retrieveParsedPlayer();
    }
}
