package com.cmput301w23t47.canary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.location.Location;
import android.util.Log;

import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.QrCode;

import org.junit.Test;

import java.util.ArrayList;

public class QrCodeControllerTest {
    @Test
    public void testGetHash() {
        String qrVal = "testQrText";
        String qrHash = "600-9035103115-123-8-30238330-30-4693-1066861-26-11875-9196-2275127-307311512086-123";
        assertEquals(QrCodeController.getHashStrForQr(qrVal), qrHash);
    }

    @Test
    public void testGetQrsWithinDistance() {
        ArrayList<QrCode> qrCodes = new ArrayList<>();
        Location location = new Location("");
        location.setLatitude(0);
        location.setLongitude(0);
        Location loc2 = new Location("");
        loc2.setLongitude(100);
        loc2.setLongitude(100);
        qrCodes.add(new QrCode("", 0, location, "", null, null));
        qrCodes.add(new QrCode("", 0, location, "", null, null));
        qrCodes.add(new QrCode("", 0, location, "", null, null));
        qrCodes.add(new QrCode("", 0, loc2, "", null, null));
        qrCodes.add(new QrCode("", 0, loc2, "", null, null));
        qrCodes.add(new QrCode("", 0, loc2, "", null, null));
        assertEquals(3, QrCodeController.getQrsWithinDistance(qrCodes, location, 100).size());
    }
}
