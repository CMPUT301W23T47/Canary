package com.cmput301w23t47.canary.controller;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w23t47.canary.callback.UpdateLeaderboardCallback;
import com.cmput301w23t47.canary.callback.UpdatePlayerCallback;
import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.repository.PlayerQrCodeRepository;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.cmput301w23t47.canary.repository.QrCodeRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import com.google.firebase.installations.FirebaseInstallations;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Handles the interaction with the database
 * Objects are singleton
 */
public class FirestoreController {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference players = db.collection("Player");
    private final CollectionReference qrCodes = db.collection("QRCode");
    private final CollectionReference leaderboard = db.collection("Leaderboard");
    private final String globalLeaderboardDocument = "Global";

    private static final String TAG = "Firestore Controller";

    // singleton instance of object
    private static FirestoreController instance;

    /**
     * Gets the singleton instance of object
     * @return the singleton instance of object
     */
    public static FirestoreController getInstance() {
        if (instance == null) {
            instance = new FirestoreController();
        }
        return instance;
    }

    private <TResult> TResult waitForTask(Task<DocumentSnapshot> snapshotTask, @NonNull Class<TResult> classType) {
        try {
            DocumentSnapshot doc = Tasks.await(snapshotTask);
            return doc.toObject(classType);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setPlayer(Player player){
        FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
        firebaseInstallations.getId().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String installationId) {
                players.document(installationId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Player document already exists");
                            } else {
                                Map<String, Object> playerData = new HashMap<>();
                                playerData.put("UniquePlayerID", installationId);
                                playerData.put("username", player.getUsername());
                                playerData.put("firstName" , player.getFirstName());
                                playerData.put("lastName", player.getLastName());
                                players.document(installationId).set(playerData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "Player document created successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error creating player document", e);
                                            }
                                        });
                            }
                        } else {
                            // Handle errors here
                            Log.w(TAG, "Error getting document", task.getException());
                        }
                    }
                });
            }
        });

    }

    /**
     * Gets the Player from db
     * @param playerId the id of the player
     */
    public void getPlayer(String playerId, UpdatePlayerCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            // get Player Repo
            Task<DocumentSnapshot> playerTask = players.document(playerId).get();
            PlayerRepository playerRepository = waitForTask(playerTask, PlayerRepository.class);
            Log.d(TAG, "getPlayer: " + playerRepository.toString());
            // Get associated QR Repos
            for (PlayerQrCodeRepository playerQrCodesRepo : playerRepository.getQrCodes()) {
                QrCodeRepository qrCodeRepo = waitForTask(playerQrCodesRepo.getQrCode().get(), QrCodeRepository.class);
                playerQrCodesRepo.setParsedQrCode(qrCodeRepo.getParsedQrCode());
            }
            Player player = playerRepository.getParsedPlayer();
            handler.post(() -> {
                callback.updatePlayer(player);
            });
        }).start();
    }

    /**
     * Gets Leaderboard from db
     * Calls the callback function when result  is ready
     */
    public void getLeaderboard(UpdateLeaderboardCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            // get Player Repo
            Task<DocumentSnapshot> lbTask = leaderboard.document(globalLeaderboardDocument).get();
            Leaderboard leaderboard = waitForTask(lbTask, Leaderboard.class);
            // Get Player ranks
            ArrayList<PlayerRepository> playersRepoList = getAllPlayers();
            // map the players into leaderboardPlayer
            ArrayList<LeaderboardPlayer> playerList =
                    LeaderboardController.getLeaderboardPlayerList(playersRepoList);
            leaderboard.setPlayers(playerList);

            handler.post(() -> {
                callback.updateLeaderboard(leaderboard);
            });
        }).start();
    }

    /**
     * Gets the repo model of all players
     * @return ArrayList of all Player Repo models
     */
    private ArrayList<PlayerRepository> getAllPlayers() {
        ArrayList<PlayerRepository> playersList = new ArrayList<>();
        Task<QuerySnapshot> allPlayersTask = players.get();
        try {
            QuerySnapshot playersQuery = Tasks.await(allPlayersTask);
            for (QueryDocumentSnapshot playerDoc : playersQuery) {
                playersList.add(playerDoc.toObject(PlayerRepository.class));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return playersList;
    }

}