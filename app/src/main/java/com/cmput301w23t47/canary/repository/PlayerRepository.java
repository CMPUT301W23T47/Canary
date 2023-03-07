package com.cmput301w23t47.canary.repository;

import androidx.annotation.NonNull;

import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Models the storage for models on Firestore
 */
public class PlayerRepository {
    // Document Id
    @DocumentId
    private String docId;
    // username of the player
    private String username;
    // firstname of the player
    private String firstName;
    // lastname of the player
    private String lastName;
    // score of player
    private long score;
    // QR with max score
    private long maxScoreQr;
    private ArrayList<PlayerQrCodeRepository> qrCodes;


    // Default Constructor
    public PlayerRepository() {
        qrCodes = new ArrayList<>();
    }

    public Player getParsedPlayer() {
        Player player = new Player(username, firstName, lastName);
        ArrayList<PlayerQrCode> playerQrCodes = new ArrayList<>();
        for (PlayerQrCodeRepository qrRepo : qrCodes) {
            playerQrCodes.add(qrRepo.getParsedPlayerQrCode());
        }
        player.setQrCodes(playerQrCodes);
        return player;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<PlayerQrCodeRepository> getQrCodes() {
        return qrCodes;
    }

    public void setQrCodes(ArrayList<PlayerQrCodeRepository> qrCodes) {
        this.qrCodes = qrCodes;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getMaxScoreQr() {
        return maxScoreQr;
    }

    public void setMaxScoreQr(long maxScoreQr) {
        this.maxScoreQr = maxScoreQr;
    }

    public LeaderboardPlayer getLeaderboardPlayer() {
        return new LeaderboardPlayer(username, score, maxScoreQr);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "{%s, %s, %s, %d}", username, firstName, lastName, qrCodes.size());
    }

    public void setFieldsInPlayer(Player player) {
        player.setUsername(username);
        player.setFirstName(firstName);
        player.setLastName(lastName);
    }

    public Player getPlayer() {
Player player = new Player(username, firstName, lastName);
        player.setScore(score);
        ArrayList<PlayerQrCode> playerQrCodes = new ArrayList<>();
        for (PlayerQrCodeRepository qrRepo : qrCodes) {
            playerQrCodes.add(qrRepo.getParsedPlayerQrCode());
        }
        player.setQrCodes(playerQrCodes);
        return player;
    }
}
