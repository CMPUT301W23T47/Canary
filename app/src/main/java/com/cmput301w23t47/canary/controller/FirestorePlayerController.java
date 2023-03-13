package com.cmput301w23t47.canary.controller;

import android.os.Handler;
import android.util.Log;

import com.cmput301w23t47.canary.callback.DoesResourceExistCallback;
import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.callback.GetPlayerListCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.callback.GetPlayerQrCallback;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.Snapshot;
import com.cmput301w23t47.canary.repository.GetIndexArg;
import com.cmput301w23t47.canary.repository.PlayerQrCodeRepository;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.cmput301w23t47.canary.repository.QrCodeRepository;
import com.cmput301w23t47.canary.repository.SnapshotRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Firestore controller for interacting with Player model
 */
public class FirestorePlayerController extends FirestoreController{
    public static final String TAG = "FirestorePlayerController";
    FirestoreLeaderboardController firestoreLeaderboardController = new FirestoreLeaderboardController();

    /**
     * Determines whether the player has the given qr
     * @param qrHash the hash of the qr
     * @param resExistCallback the callback for returning the response
     */
    public void doesCurrentPlayerHaveQr(String qrHash, DoesResourceExistCallback resExistCallback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            boolean playerHasQr = doesPlayerHaveQr(qrHash, playerDocId);
            // call the callback when the result is available
            handler.post(() -> {
                resExistCallback.doesResourceExists(playerHasQr);
            });
        }).start();
    }

    /**
     * Determines whether the player has the given qr
     * @param qrHash the hash of the qr
     * @return true if player has the qr
     */
    protected boolean doesPlayerHaveQr(String qrHash, String playerDocId) {
        // determine if the qr resource exist with the given hash
        Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", qrHash).get();
        waitForQuery(qrCodeQuery);
        if (qrCodeQuery.getResult().size() == 0) {
            // qr resource does not exist; player cannot have it
            return false;
        }
        DocumentReference qrDocRef = qrCodeQuery.getResult().getDocuments().get(0).getReference();

        // get the player model
        PlayerRepository playerRepo = getPlayerRepo(playerDocId);
        return playerRepo.containsQrRef(qrDocRef);
    }

    /**
     * Adds the given qr to the player's model
     * @param playerQrCode the qr to add
     * @param callback the callback to inform when operation completes
     */
    public void addQrToCurrentPlayer(PlayerQrCode playerQrCode, OperationStatusCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            PlayerRepository playerRepo = getPlayerRepo(playerDocId);
            playerRepo.addQrToPlayerStats(playerQrCode);
            QrCodeRepository qrCodeRepository = QrCodeRepository.retrieveQrCodeRepo(playerQrCode.getQrCode());
            DocumentReference qrReference = getReferenceToQrOrCreate(qrCodeRepository);
            DocumentReference snapshotReference = null;
            if (playerQrCode.getSnapshot() != null) {
                snapshotReference = persistSnapshot(playerQrCode.getSnapshot().getBitmap(), playerDocId);
            }
            playerRepo.getQrCodes().add(new PlayerQrCodeRepository(qrReference, snapshotReference,
                    new Timestamp(playerQrCode.getScanDate()), playerQrCode.retrieveScore()));
            updatePlayer(playerRepo);
            handler.post(() -> {
               callback.operationStatus(true);
            });
        }).start();
    }

    protected void updatePlayer(PlayerRepository playerRepo) {
        Task<Void> updatePlayerTask = players.document(playerRepo.getDocId()).set(playerRepo);
        waitForUpdateTask(updatePlayerTask);
    }

    /**
     * Get the PlayerQr object
     * @param qrHash the hash of the qr
     * @param callback the callback to return the result
     */
    public void getPlayerQr(String qrHash, GetPlayerQrCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            PlayerRepository playerRepo = getPlayerRepo(playerDocId);
            Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", qrHash).get();
            waitForQuery(qrCodeQuery);
            if (qrCodeQuery.getResult().isEmpty()) {
                handler.post(() -> {
                    callback.getPlayerQr(null);
                });
                // the given qr does not exist
                return;
            }
            QrCodeRepository qrRepo = qrCodeQuery.getResult().getDocuments().get(0).toObject(QrCodeRepository.class);
            DocumentReference qrRef = qrCodeQuery.getResult().getDocuments().get(0).getReference();
            GetIndexArg indexArg = new GetIndexArg();
            SnapshotRepository snapRepo = retrieveQrSnapshotFromPlayer(playerRepo, qrRef, indexArg);
            Integer a = new Integer(1);
            handler.post(() -> {
                // return the playerQr Model
                Timestamp scanDate = playerRepo.getQrCodes().get(indexArg.i).getScanDate();
                callback.getPlayerQr(PlayerQrCodeRepository.retrievePlayerQrCode(qrRepo, snapRepo, scanDate));
            });
        }).start();
    }

    /**
     * Retrieves the snapshot repo if it exists
     * @param playerRepo the player repo that contains the qr ref and snapshot ref
     * @param qrRef the qr ref to search
     * @param indexArg gets the index of the found element
     * @return the snapshot repo if it exists
     */
    public SnapshotRepository retrieveQrSnapshotFromPlayer(PlayerRepository playerRepo, DocumentReference qrRef, GetIndexArg indexArg) {
        DocumentReference snapRef = null;
        for (int i = 0; i < playerRepo.getQrCodes().size(); i++) {
            PlayerQrCodeRepository playerQrCodeRepository = playerRepo.getQrCodes().get(i);
            if (qrRef.equals(playerQrCodeRepository.getQrCode())) {
                snapRef = playerQrCodeRepository.getSnapshot();
                indexArg.i = i;
                break;
            }
        }
        if (snapRef == null) {
            return null;
        }
        Task<DocumentSnapshot> snapTask = snapRef.get();
        return waitForTask(snapTask, SnapshotRepository.class);
    }

    /**
     * Deletes the Qr from the player
     * @param playerQrCode the qr to delete
     * @param callback the callback to inform
     */
    public void deleteQrFromPlayer(PlayerQrCode playerQrCode, OperationStatusCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            // get the player and qr
            String playerDocId = identifyPlayer();
            PlayerRepository playerRepo = getPlayerRepo(playerDocId);
            Task<QuerySnapshot> qrCodeQuery = qrCodes.whereEqualTo("hash", playerQrCode.retrieveHash()).get();
            waitForQuery(qrCodeQuery);
            if (qrCodeQuery.getResult().isEmpty()) {
                // the given qr does not exist
                handler.post(() -> {
                    callback.operationStatus(false);
                });
                return;
            }

            // delete the qr, snapshot from player
            DocumentReference qrRef = qrCodeQuery.getResult().getDocuments().get(0).getReference();
            int qrIndex = getIndexOfQrInPlayer(playerRepo, qrRef);
            if (qrIndex < 0) {
                // qr not found in player
                handler.post(() -> {
                    callback.operationStatus(false);
                });
                return;
            }

            deleteSnapshotForQr(playerRepo, qrIndex);
            playerRepo.removeQrAt(qrIndex, playerQrCode);
            Task<Void> playerUpdateTask = players.document(playerDocId).set(playerRepo);
            waitForUpdateTask(playerUpdateTask);
            firestoreLeaderboardController.updateLeaderboardIfRequired(playerRepo);
            handler.post(() -> {
                callback.operationStatus(true);
            });
        }).start();
    }

    /**
     * Gets the complete model for the current player
     * @param callback the callback to return the response to
     */
    public void getCompleteCurrentPlayer(GetPlayerCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            Player player = retrieveCompletePlayer(playerDocId);
            // return result
            handler.post(() -> {
                callback.getPlayer(player);
            });
        }).start();
    }

    /**
     * Gets the complete model for the foreign player
     * @param callback the callback to return the response to
     */
    public void getCompleteForeignPlayer(String playerDocId, GetPlayerCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            Player player = retrieveCompletePlayer(playerDocId);
            // return result
            handler.post(() -> {
                callback.getPlayer(player);
            });
        }).start();
    }

    protected Player retrieveCompletePlayer(String playerDocId) {
        Task<DocumentSnapshot> playerTask = players.document(playerDocId).get();
        PlayerRepository playerRepository = waitForTask(playerTask, PlayerRepository.class);
        for (PlayerQrCodeRepository playerQrCodesRepo : playerRepository.getQrCodes()) {
            QrCodeRepository qrCodeRepo = waitForTask(playerQrCodesRepo.getQrCode().get(), QrCodeRepository.class);
            Snapshot snap = null;
            if (playerQrCodesRepo.getSnapshot() != null) {
                SnapshotRepository snapRepo = waitForTask(playerQrCodesRepo.getSnapshot().get(), SnapshotRepository.class);
                snap = snapRepo.retrieveSnapshot();
            }
            playerQrCodesRepo.setParsedQrCode(qrCodeRepo.retrieveParsedQrCode(), snap);
        }

        // get Complete Player model
        return playerRepository.retrieveParsedPlayer();
    }

    /**
     * Creates the player
     * @param player the player to save
     */
    public void createPlayer(Player player, OperationStatusCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            PlayerRepository playerRepo = PlayerRepository.retrievePlayerRepo(player);
            // save the player
            Task<Void> saveTask = players.document(playerDocId).set(playerRepo);
            waitForUpdateTask(saveTask);
            handler.post(() -> {
                callback.operationStatus(true);
            });
        }).start();
    }

    /**
     * Gets the list of players
     * @param callback the callback to call when list available
     */
    public void getListOfPlayers(GetPlayerListCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            Task<QuerySnapshot> playersTask = players.get();
            waitForQuery(playersTask);
            ArrayList<Player> playerList = new ArrayList<>();
            // map all players to object
            for (DocumentSnapshot playerDoc : playersTask.getResult().getDocuments()) {
                playerList.add(playerDoc.toObject(PlayerRepository.class)
                        .retrieveParsedPlayer());
            }
            handler.post(() -> {
               callback.getPlayerList(playerList);
            });
        }).start();
    }

    /**
     * Determines whether the current player exists in db
     * @param callback the callback to return the result
     *                 called with true if player exists; false otherwise
     */
    public void doesCurrentPlayerExist(OperationStatusCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            String playerDocId = identifyPlayer();
            Log.d(TAG, "doesCurrentPlayerExist: " + playerDocId);
            Task<DocumentSnapshot> playerTask = players.document(playerDocId).get();
            waitForTask(playerTask);
            handler.post(() -> {
                callback.operationStatus(playerTask.getResult().exists());
            });
        }).start();
    }

    /**
     * Gets the index of the qr in the player
     * @param playerRepo the player to search
     * @param qrRef the reference to the qr
     * @return the index of the qr in player
     */
    protected int getIndexOfQrInPlayer(PlayerRepository playerRepo, DocumentReference qrRef) {
        for (int i = 0; i < playerRepo.getQrCodes().size(); i++) {
            PlayerQrCodeRepository playerQrCodeRepository = playerRepo.getQrCodes().get(i);
            if (qrRef.equals(playerQrCodeRepository.getQrCode())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Deletes the snapshot for the qr from the player
     * @param playerRepo the player repo
     * @param qrIndex the index of the qr in the player repo
     */
    protected void deleteSnapshotForQr(PlayerRepository playerRepo, int qrIndex) {
        DocumentReference snapRef = playerRepo.getQrCodes().get(qrIndex).getSnapshot();
        if (snapRef == null) {
            return;
        }
        Task<Void> deleteTask = snapRef.delete();
        waitForUpdateTask(deleteTask);
    }
}
