package com.cmput301w23t47.canary.callback;

import com.cmput301w23t47.canary.model.PlayerQrCode;

/**
 * The interface Get player qr callback.
 */
public interface GetPlayerQrCallback {
    /**
     * Gets player qr.
     *
     * @param playerQrCode the player qr code
     */
    void getPlayerQr(PlayerQrCode playerQrCode);
}
