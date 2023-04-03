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
}
