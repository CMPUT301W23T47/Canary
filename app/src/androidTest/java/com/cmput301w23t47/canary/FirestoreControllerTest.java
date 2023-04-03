package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for verifying the information retrieved from firestore
 */
@RunWith(AndroidJUnit4.class)
public class FirestoreControllerTest {
    private Solo solo;

    static {
        FirestoreController.switchToTestMode();
    }

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception{
        Intents.init();
        rule.getScenario().onActivity(activity -> {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        });
    }

    @Test
    public void testGetCurrentPlayer(){
        FirestorePlayerController playerController = new FirestorePlayerController();
        String playerDocId = playerController.identifyPlayer();
        // verify the current player's info
        assertTrue(playerDocId != null && !playerDocId.equals(""));
        Player player = playerController.retrieveCompletePlayer(playerDocId);
        assertTrue(player != null);
        assertTrue(player.getUniquePlayerId().equals(playerDocId));
        assertTrue(!player.getUsername().equals(""));
        // verify the qr codes in the player model
        if (player.getScore() > 0) {
            assertTrue(player.getQrCodes().size() > 0);
            long tempScore = 0;
            for (PlayerQrCode qrCode : player.getQrCodes()) {
                tempScore += qrCode.retrieveScore();
            }
            assertEquals(tempScore, player.getScore());
        }
    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }
}
