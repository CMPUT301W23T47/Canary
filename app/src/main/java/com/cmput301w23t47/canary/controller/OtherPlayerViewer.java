package com.cmput301w23t47.canary.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class OtherPlayerViewer extends FirestoreController {
    public static final String TAG = "OtherPlayerViewer";

    public void otherPlayerWithSameQr(String qrHash){

//        db.collection("QrCode").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()) {
//                }
//            }
//        });

        CollectionReference hashRef = db.collection("QRCode");
        Query query = hashRef.whereEqualTo("hash", qrHash);

        db.collection("QRCode")
                .whereEqualTo("hash", qrHash)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting hash: ", task.getException());
                        }
                    }
                });
    }
}
