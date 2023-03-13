package com.cmput301w23t47.canary.callback;

import com.cmput301w23t47.canary.model.QrCode;

import java.util.ArrayList;

/**
 * Callback for getting the list of Qrs
 */
public interface GetQrListCallback {
    /**
     * Gets the list of all qrs
     * @param qrCodes
     */
    void getQrList(ArrayList<QrCode> qrCodes);
}
