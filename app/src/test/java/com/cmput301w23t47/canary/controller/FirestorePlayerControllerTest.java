package com.cmput301w23t47.canary.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;

import java.util.Date;

class FirestorePlayerControllerTest implements GetPlayerCallback {
    // Test not working
    @Test
    public void deleteQrFromPlayer() {
//        FirestorePlayerController testController = new FirestorePlayerController();
//        testController.getPlayer("dsvj1o1gQOe8mYWFe1U5To", this);

        FirestorePlayerController testController = new FirestorePlayerController();
        Date testDate = new Date();

        Player testPlayer = new Player("dsingh", "Dhruvraj","Singh") ;
        testController.setPlayer(testPlayer);

        QrCode testCode = new QrCode("-67-126471-32-11-5810071-121-115-36-30-56-95-1238-21037344-6-32-491009716-117-35-76-93",83,null,"randomTest");

        PlayerQrCode testPlayerCode = new PlayerQrCode(testCode, testDate);

        testController.addQrToCurrentPlayer(testPlayerCode, (OperationStatusCallback) this);

        assertTrue(testController.doesPlayerHaveQr(testCode.getHash(), testPlayer.getUniquePlayerId()));
        testController.deleteQrFromPlayer(testPlayerCode, (OperationStatusCallback) this);

        assertFalse(testController.doesPlayerHaveQr(testCode.getHash(),testPlayer.getUniquePlayerId()));

    }

    @Override
    public void getPlayer(Player player) {

    }
}