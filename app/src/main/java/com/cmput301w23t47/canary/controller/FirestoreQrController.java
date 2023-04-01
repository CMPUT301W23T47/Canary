package com.cmput301w23t47.canary.controller;

import android.os.Handler;

import com.cmput301w23t47.canary.callback.DoesResourceExistCallback;
import com.cmput301w23t47.canary.callback.GetPlayersScannedQrCallBack;
import com.cmput301w23t47.canary.callback.GetQrListCallback;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.repository.PlayerRepository;
import com.cmput301w23t47.canary.repository.QrCodeRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Controller for interacting with the qr model on firestore
 */
public class FirestoreQrController extends FirestoreController {

    /**
     * Verifies if the qr with the given hash exists
     * @param qrHash the hash of the qrCode
     * @param resExistCallback callback for informing about the result
     */
    public void doesQrWithHashExist(String qrHash, DoesResourceExistCallback resExistCallback) {
        Handler handler = new Handler();
        new Thread(() -> {
            boolean qrExists = doesQrWithHashExist(qrHash);
            // call the callback when the result is available
            handler.post(() -> {
                resExistCallback.doesResourceExists(qrExists);
            });
        }).start();
    }

    /**
     * Gives the list of the usernames of players who scanned a particular qrcode
     * @param qrCode The qrcode which players scanned
     * @param getPlayersScannedQrCallBack Callback for informing about the result
     */
    public void getUsernamesOfPlayersWhoScanned(QrCode qrCode, GetPlayersScannedQrCallBack getPlayersScannedQrCallBack){
        Handler handler = new Handler();
        new Thread(() -> {
            ArrayList <String> usernameList = getUsernamesOfPlayersWhoScanned(qrCode.getHash());
            handler.post(() ->{
                getPlayersScannedQrCallBack.getPlayersWhoScannedQr(usernameList);
            });
        }).start();
    }

    /**
     * Get the list of all QRs
     * @param callback used to return the result
     */
    public void getAllQrs(GetQrListCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            Task<QuerySnapshot> qrTask = qrCodes.get();
            waitForQuery(qrTask);
            ArrayList<QrCode> qrList = new ArrayList<>();
            // map all Qrs to object
            for (DocumentSnapshot qrDoc : qrTask.getResult().getDocuments()) {
                qrList.add(qrDoc.toObject(QrCodeRepository.class).retrieveParsedQrCode());
            }
            handler.post(() -> {
               callback.getQrList(qrList);
            });
        }).start();
    }
}
