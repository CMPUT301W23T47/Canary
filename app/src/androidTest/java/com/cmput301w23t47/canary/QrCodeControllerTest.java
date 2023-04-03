package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.QrCode;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests the QrCodeController class
 */
public class QrCodeControllerTest {
    private Solo solo;

    static {
        FirestoreController.testMode=true;}

    /**
     * The Rule.
     */
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception{
        Intents.init();
        rule.getScenario().onActivity(activity -> {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        });
    }

    /**
     * Test for Filtering the qrs within the specified distance
     */
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

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }
}
