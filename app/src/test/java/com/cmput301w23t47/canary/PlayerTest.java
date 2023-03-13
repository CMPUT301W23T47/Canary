package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class PlayerTest {

    private Player mockPlayer(){
        Player player = new Player("testy", "Offical", "Tester");
        return player;
    }
    private PlayerQrCode mockQr(){
        QrCode qrCode = new QrCode("111-111-212", 619, new Location("GPS"), "testQrCode");
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCode,new Date());
        return playerQrCode;
    }
    private PlayerQrCode mockQrLow(){
        QrCode qrCode = new QrCode("211-111-212", 19, new Location("0"), "testQrCode2");
        PlayerQrCode playerQrCode = new PlayerQrCode(qrCode,new Date());
        return playerQrCode;
    }

    @Test
    public void testGetQrCodes(){
        Player player = mockPlayer();
        PlayerQrCode playerQrCode = mockQr();
        player.addQrCode(playerQrCode);
        assertEquals(1, player.getQrCodes().size());
    }

    @Test
    public void testSetQrCodes(){
        Player player = mockPlayer();
        PlayerQrCode playerQrCode = mockQr();
        ArrayList<PlayerQrCode> testList = new ArrayList<>();
        testList.add(playerQrCode);
        player.setQrCodes(testList);
        assertEquals(testList, player.getQrCodes());
    }

    @Test
    public void testAddQrCode(){
        Player player = mockPlayer();
        PlayerQrCode playerQrCode = mockQr();
        player.addQrCode(playerQrCode);
        assertEquals(playerQrCode, player.getQrCodes().get(0));
    }

    @Test
    public void testGetUsername(){
        Player player = mockPlayer();
        assertEquals("testy", player.getUsername());
    }

    @Test
    public void testSetUsername(){
        Player player = mockPlayer();
        player.setUsername("testBoss");
        assertEquals("testBoss", player.getUsername());
    }

    @Test
    public void testGetFirstName(){
        Player player = mockPlayer();
        assertEquals("Offical", player.getFirstName());
    }

    @Test
    public void testSetFirstName(){
        Player player = mockPlayer();
        player.setFirstName("officalBoss");
        assertEquals("officalBoss", player.getFirstName());
    }

    @Test
    public void testGetLastName(){
        Player player = mockPlayer();
        assertEquals("Tester", player.getLastName());
    }

    @Test
    public void testSetLastName(){
        Player player = mockPlayer();
        player.setLastName("TestBoss");
        assertEquals("TestBoss", player.getLastName());
    }

    @Test
    public void testGetScore(){
        Player player = mockPlayer();
        assertEquals(0, player.getScore());
    }

    @Test
    public void testSetScore(){
        Player player = mockPlayer();
        player.setScore(100);
        assertEquals(100, player.getScore());
    }

    @Test
    public void testGetHighestQr(){
        Player player = mockPlayer();
        player.addQrCode(mockQr());
        player.addQrCode(mockQrLow());
        assertEquals(619,player.getHighestQr());
    }

    @Test
    public void testGetLowestQr(){
        Player player = mockPlayer();
        player.addQrCode(mockQr());
        player.addQrCode(mockQrLow());
        assertEquals(19,player.getLowestQr());
    }

    @Test
    public void testRetriveStringToDraw(){
        Player player = mockPlayer();
        assertEquals("t",player.retrieveStringToDraw());
    }



}
