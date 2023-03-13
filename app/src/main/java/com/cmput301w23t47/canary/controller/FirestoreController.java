package com.cmput301w23t47.canary.controller;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmput301w23t47.canary.callback.UpdateLeaderboardCallback;
import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.repository.PlayerQrCodeRepository;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.cmput301w23t47.canary.repository.QrCodeRepository;
import com.cmput301w23t47.canary.repository.SnapshotRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    public static boolean testMode = false;
    private static String testPlayer = "fzDzmMoLTOCxpMD8fGjVms";
    public static boolean firstTimeTester = false;
    private static String firstTimeTestPlayer = "testPlayerFirst";
    protected static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static final CollectionReference players = db.collection("Player");
    protected final CollectionReference qrCodes = db.collection("QRCode");
    protected final CollectionReference leaderboard = db.collection("Leaderboard");
    protected final CollectionReference snapshot = db.collection("Snapshot");
    protected final String globalLeaderboardDocument = "Global";

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

    protected  <TResult> TResult waitForTask(Task<DocumentSnapshot> snapshotTask, @NonNull Class<TResult> classType) {
        try {
            DocumentSnapshot doc = Tasks.await(snapshotTask);
            return doc.toObject(classType);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Waits for the query snapshot task to complete
     * @param querySnapshotTask the query snapshot object
     */
    protected void waitForQuery(Task<QuerySnapshot> querySnapshotTask) {
        try {
            Tasks.await(querySnapshotTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the document snapshot task to complete
     * @param snapshotTask the document snapshot object
     */
    protected void waitForTask(Task<DocumentSnapshot> snapshotTask) {
        try {
            Tasks.await(snapshotTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Waits for the document snapshot task to complete
     * @param snapshotTask the document snapshot object
     */
    protected void waitForUpdateTask(Task<Void> snapshotTask) {
        try {
            Tasks.await(snapshotTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the General task to complete
     * @param task the task to wait for
     */
    protected <K extends Object> void waitForGeneralTask(Task<K> task) {
        try {
            Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for the document snapshot task to complete
     * @param referenceTask the document reference object
     */
    protected void waitForReference(Task<DocumentReference> referenceTask) {
        try {
            Tasks.await(referenceTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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
                                player.setUniquePlayerId(installationId);
                                Map<String, Object> playerData = new HashMap<>();
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

    public void getActivePlayerUserName(OnSuccessListener<String> successListener, OnFailureListener failureListener){
        FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
        firebaseInstallations.getId().addOnSuccessListener(installationId -> {
            players.document(installationId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        if(data != null){
                            successListener.onSuccess((String) data.get("username"));
                        }
                    } else {
                        failureListener.onFailure(new Exception("Player ID not found"));
                    }
                } else {
                    failureListener.onFailure(task.getException());
                }
            });
        }).addOnFailureListener(failureListener);
    }

    /**
     * Gets the unique id for the player
     */
    public static void identifyPlayer(OnSuccessListener<String> successListener, OnFailureListener failureListener) {
        if(testMode) successListener.onSuccess(testPlayer);
        else {
            FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
            firebaseInstallations.getId().addOnSuccessListener(installationId -> {
                players.document(installationId).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String playerId = document.getId();
                            successListener.onSuccess(playerId);
                        } else {
                            failureListener.onFailure(new Exception("Player ID not found"));
                        }
                    } else {
                        failureListener.onFailure(task.getException());
                    }
                });
            }).addOnFailureListener(failureListener);
        }
    }

    /**
     * Gets the doc id for the current player
     * @return the doc if for player
     */
    protected String identifyPlayer() {
        if(firstTimeTester) return firstTimeTestPlayer;
        if(testMode) return testPlayer;
        FirebaseInstallations firebaseInstallations = FirebaseInstallations.getInstance();
        Task<String> idTask = firebaseInstallations.getId();
        waitForGeneralTask(idTask);
        return idTask.getResult();
    }

    /**
     * Gets the Player from db
     * @param playerId the id of the player
     */
    public void getPlayer(String playerId, GetPlayerCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            // get Player Repo
            Task<DocumentSnapshot> playerTask = players.document(playerId).get();
            PlayerRepository playerRepository = waitForTask(playerTask, PlayerRepository.class);
            // Get associated QR Repos
            for (PlayerQrCodeRepository playerQrCodesRepo : playerRepository.getQrCodes()) {
                QrCodeRepository qrCodeRepo = waitForTask(playerQrCodesRepo.getQrCode().get(), QrCodeRepository.class);
                // TODO: Fix the snapshot here
                playerQrCodesRepo.setParsedQrCode(qrCodeRepo.retrieveParsedQrCode(), null);
            }
            Player player = playerRepository.retrieveParsedPlayer();
            handler.post(() -> {
                callback.getPlayer(player);
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


    protected ArrayList<String> getUsernamesOfPlayersWhoScanned(String qrHash){
        String qrDocID = findQrDocId(qrHash);
        ArrayList<String> usernameList = new ArrayList<>();
        Query query = players.whereArrayContains("qrCodes.qrCode", "/QRCode/" + qrDocID);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot playersQuery = task.getResult();
                    for (QueryDocumentSnapshot playerDoc : playersQuery) {
                        usernameList.add(playerDoc.getId());
                    }
                } else {
                    // Handle any errors that occurred while retrieving the players collection
                    Exception e = task.getException();
                    System.err.println("Error retrieving data: " + e.getMessage());
                }
            }
        });
        return usernameList;
    }

    protected String findQrDocId(String qrHash){
        Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", qrHash).get();
        try {
            QuerySnapshot qrCodeSnapshot = Tasks.await(qrCodeQuery);
            if (!qrCodeSnapshot.isEmpty()) {
                DocumentSnapshot qrCodeDoc = qrCodeSnapshot.getDocuments().get(0);
                return qrCodeDoc.getId();
            }
        } catch (Exception e) {
            System.err.println("Error retrieving QR code document: " + e.getMessage());
        }
        return null;
    }

    /**
     * Determines if qr with the given hash exists
     * @param qrHash the hash of the qr to find
     * @return true if the given qr Exists
     * @apiNote do not call from the UI thread
     */
    protected boolean doesQrWithHashExist(String qrHash) {
        // determine if qr with the given hash exists
        Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", qrHash).get();
        waitForQuery(qrCodeQuery);
        return !qrCodeQuery.getResult().isEmpty();
    }

    /**
     * Gets the player Repo
     * @param playerDocId
     * @return
     */
    protected PlayerRepository getPlayerRepo(String playerDocId) {
        Task<DocumentSnapshot> playerTask = players.document(playerDocId).get();
        return waitForTask(playerTask, PlayerRepository.class);
    }

    /**
     * Saves the snapshot to firestore
     * @param image the image to save
     * @param owner the owner of resource
     * @return the reference to the stored image
     */
    protected DocumentReference persistSnapshot(Bitmap image, String owner) {
        // compress and store the image
        String encodedSnap = SnapshotController.getBase64Image(image);
        SnapshotRepository snapshotRepository = new SnapshotRepository(encodedSnap,
                getReferenceToPlayer(owner));
        Task<DocumentReference> referenceTask = snapshot.add(snapshotRepository);
        waitForReference(referenceTask);
        return referenceTask.getResult();
    }

    /**
     * Gets the reference for the player document
     * @param username the username of the player
     * @return the document reference to the player's doc
     */
    protected DocumentReference getReferenceToPlayer(String username) {
        Task<DocumentSnapshot> playerTask = players.document(username).get();
        waitForTask(playerTask);
        return playerTask.getResult().getReference();
    }

    /**
     * Gets the reference to the given qr; Creates it if it does not exist
     * @param qrCodeRepository the qr's reference to return
     * @return the reference to the given qr
     */
    protected DocumentReference getReferenceToQrOrCreate(QrCodeRepository qrCodeRepository) {
        Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", qrCodeRepository.getHash()).get();
        waitForQuery(qrCodeQuery);
        if (qrCodeQuery.getResult().getDocuments().size() > 0) {
            // qr resource exist; return reference to it
            return qrCodeQuery.getResult().getDocuments().get(0).getReference();
        }
        // create the QR Code repo
        return persistQrCode(qrCodeRepository);
    }

    protected DocumentReference persistQrCode(QrCodeRepository qrCodeRepository) {
        Task<DocumentReference> referenceTask = qrCodes.add(qrCodeRepository);
        waitForReference(referenceTask);
        return referenceTask.getResult();
    }
}
