package com.cmput301w23t47.canary.repository;

import androidx.annotation.NonNull;

import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Models the storage for Player on Firestore
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
    // qr codes that player has
    private ArrayList<PlayerQrCodeRepository> qrCodes;
    // the number of qr codes that player has
    private long qrCodesSize;
    // the references to the scanned QR; for querying the scanned QRs among all players
    private ArrayList<DocumentReference> qrCodeReferences;
    // contact info for user
    private String contactInfo = "";


    // Default Constructor
    public PlayerRepository() {
        qrCodes = new ArrayList<>();
    }

    /**
     * Constructor to set all attributes
     */
    public PlayerRepository(String username, String firstName, String lastName, long score, long maxScoreQr, ArrayList<PlayerQrCodeRepository> qrCodes, long qrCodesSize,
                            ArrayList<DocumentReference> qrCodeReferences) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.score = score;
        this.maxScoreQr = maxScoreQr;
        this.qrCodes = qrCodes;
        this.qrCodesSize = qrCodesSize;
        this.qrCodeReferences = qrCodeReferences;
    }

    /**
     * Retrieve parsed player player.
     *
     * @return the player
     */
    public Player retrieveParsedPlayer() {
        Player player = new Player(username, firstName, lastName, contactInfo);
        ArrayList<PlayerQrCode> playerQrCodes = new ArrayList<>();
        for (PlayerQrCodeRepository qrRepo : qrCodes) {
            playerQrCodes.add(qrRepo.retrieveParsedPlayerQrCode());
        }
        player.setQrCodes(playerQrCodes);
        player.setScore(this.score);
        player.setUniquePlayerId(docId);
        return player;
    }

    /**
     * Gets doc id.
     *
     * @return the doc id
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Sets doc id.
     *
     * @param docId the doc id
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets qr codes.
     *
     * @return the qr codes
     */
    public ArrayList<PlayerQrCodeRepository> getQrCodes() {
        return qrCodes;
    }

    /**
     * Sets qr codes.
     *
     * @param qrCodes the qr codes
     */
    public void setQrCodes(ArrayList<PlayerQrCodeRepository> qrCodes) {
        this.qrCodes = qrCodes;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public long getScore() {
        return score;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * Gets max score qr.
     *
     * @return the max score qr
     */
    public long getMaxScoreQr() {
        return maxScoreQr;
    }

    /**
     * Sets max score qr.
     *
     * @param maxScoreQr the max score qr
     */
    public void setMaxScoreQr(long maxScoreQr) {
        this.maxScoreQr = maxScoreQr;
    }

    /**
     * Getter for contact info
     * @return the contact info
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Setter for contact info
     * @param contactInfo the contact info to set
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Gets qr codes size.
     *
     * @return the qr codes size
     */
    public long getQrCodesSize() {
        return qrCodesSize;
    }

    /**
     * Sets qr codes size.
     *
     * @param qrCodesSize the qr codes size
     */
    public void setQrCodesSize(long qrCodesSize) {
        this.qrCodesSize = qrCodesSize;
    }

    public ArrayList<DocumentReference> getQrCodeReferences() {
        return qrCodeReferences;
    }

    public void setQrCodeReferences(ArrayList<DocumentReference> qrCodeReferences) {
        this.qrCodeReferences = qrCodeReferences;
    }

    /**
     * Retrieve leaderboard player leaderboard player.
     *
     * @return the leaderboard player
     */
    public LeaderboardPlayer retrieveLeaderboardPlayer() {
        return new LeaderboardPlayer(username, score, maxScoreQr);
    }

    /**
     * Converts a String to a specific date format
     * @return The converted String
     */
    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CANADA, "{%s, %s, %s, %d}", username, firstName, lastName, qrCodes.size());
    }

    /**
     * Sets fields in player.
     *
     * @param player the player
     */
    public void setFieldsInPlayer(Player player) {
        player.setUsername(username);
        player.setFirstName(firstName);
        player.setLastName(lastName);
    }

    /**
     * Determines if the player repo contains the reference to the given qr
     * @param qrRef the reference to compare
     * @return true if it contains the reference
     */
    public boolean containsQrRef(DocumentReference qrRef) {
        for (PlayerQrCodeRepository qrRepo : qrCodes) {
            if (qrRepo.getQrCode().equals(qrRef)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds the qr to the player stats
     * @param playerQrCode the qr added
     */
    public void addQrToPlayerStats(PlayerQrCode playerQrCode) {
        score += playerQrCode.getQrCode().getScore();
        maxScoreQr = Math.max(maxScoreQr, playerQrCode.getQrCode().getScore());
        qrCodesSize += 1;
    }

    /**
     * Update the stats after updating
     * @param playerQrCode
     */
    public void updateStatsAfterRemoveQr(PlayerQrCode playerQrCode) {
        long qrScore = playerQrCode.getQrCode().getScore();
        score -= qrScore;
        if (maxScoreQr == qrScore) {
            // need to find new max qr
            long locMaxScore = 0;
            for (int i = 0; i < qrCodes.size(); i++) {
                PlayerQrCodeRepository qrCodeRepository = qrCodes.get(i);
                if (qrCodeRepository.getQrScore() > locMaxScore) {
                    locMaxScore = qrCodeRepository.getQrScore();
                }
            }
            maxScoreQr = locMaxScore;
        }
        qrCodesSize = qrCodes.size();
    }

    /**
     * Removes the qr at the given index
     * @param qrIndex the index to remove
     * @param playerQrCode the qr which is deleted
     */
    public void removeQrAt(int qrIndex, PlayerQrCode playerQrCode) {
        qrCodes.remove(qrIndex);
        qrCodeReferences.remove(qrIndex);
        updateStatsAfterRemoveQr(playerQrCode);
    }

    /**
     * Retrieves the PlayerRepo model for the player
     * Note: The qr related information cannot be mapped directly
     * @param player the player to map
     * @return the PlayerRepo representation of the model
     */
    public static PlayerRepository retrievePlayerRepo(Player player) {
        return new PlayerRepository(player.getUsername(), player.getFirstName(),
                player.getLastName(), player.getScore(), 0, new ArrayList<>(), 0, new ArrayList<>());
    }

    /**
     * Retrieves the player with the doc id
     * @param players the list of players
     * @param playerDocId the doc id of the player
     * @return the player model
     */
    public static LeaderboardPlayer retrievePlayerWithDocId(ArrayList<PlayerRepository> players, String playerDocId) {
        for (PlayerRepository p : players) {
            if (p.docId.equals(playerDocId)) {
                return p.retrieveLeaderboardPlayer();
            }
        }
        return null;
    }
}
