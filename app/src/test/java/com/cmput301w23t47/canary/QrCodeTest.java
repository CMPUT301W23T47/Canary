package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;

import org.junit.Test;

import java.util.Date;

public class QrCodeTest {


    private QrCode mockQr() {
        QrCode qrCode = new QrCode("111-111-212", 619, new Location("GPS"), "testQrCode");
        return qrCode;
    }

    @Test
    public void testGetHash(){
        QrCode qrCode = mockQr();
        assertEquals("111-111-212",qrCode.getHash());
    }

    @Test
    public void testSetHash(){
        QrCode qrCode = mockQr();
        qrCode.setHash("222-222-333");
        assertEquals("222-222-333", qrCode.getHash());
    }

    @Test
    public void testGetScore(){
        QrCode qrCode = mockQr();
        assertEquals(619, qrCode.getScore());
    }

    @Test
    public void testSetScore(){
        QrCode qrCode = mockQr();
        qrCode.setScore(720);
        assertEquals(720, qrCode.getScore());
    }

    @Test
    public void testGetName(){
        QrCode qrCode = mockQr();
        assertEquals("testQrCode", qrCode.getName());
    }

    @Test
    public void testSetName(){
        QrCode qrCode = mockQr();
        qrCode.setName("newQrCode");
        assertEquals("newQrCode", qrCode.getName());
    }

    @Test
    public void hasLocationTrue(){
        QrCode qrCode = mockQr();
        assertEquals(true, qrCode.hasLocation());
    }

    @Test
    public void hasLocationFalse(){
        QrCode qrCode = mockQr();
        qrCode.setLocation(null);
        assertEquals(false, qrCode.hasLocation());
    }
}
