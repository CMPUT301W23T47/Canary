package com.cmput301w23t47.canary;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.util.FirestorePlayerTestUtil;
import com.cmput301w23t47.canary.view.activity.HomeActivity;
import com.cmput301w23t47.canary.view.activity.ScanQRCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.robotium.solo.Solo;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Intent tests for MainActivity
 */
public class MainActivityTest {
    private static final String TAG = "MainActivityTest";
    private Solo solo;
    private Player testPlayer = null;

    static {
        FirestoreController.switchToTestMode();
    }

    FirestorePlayerTestUtil firestorePlayerTestUtil;

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        firestorePlayerTestUtil = new FirestorePlayerTestUtil();
        Player player = firestorePlayerTestUtil.getTestPlayer();
        if (player != null) {
            testPlayer = player;
        }
        Intents.init();
        rule.getScenario().onActivity(activity -> {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        });
    }

    /**
     * Tests whether the Activity starts
     * @throws Exception Failure
     */
    @Test
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {
        });
    }

    /**
     * Navigates to the player home page
     */
    @Test
    public void test_navigateToPlayerHome() throws Exception{
        if (testPlayer == null) {
            // skip the tests
            return;
        }
        solo.waitForActivity(HomeActivity.class, 3000);
        solo.clickOnImage(3);
        assertTrue(solo.waitForText("SCAN QR", 1, 3000));
    }

    /**
     * Navigates to the player home page
     */
    @Test
    public void test_navigateToMapView() throws Exception{
        if (testPlayer == null) {
            // skip the tests
            return;
        }
        solo.waitForActivity(HomeActivity.class, 3000);
        solo.clickOnImage(3);
        assertTrue(solo.waitForText("Enter Search Radius", 1, 3000));
    }

    /**
     * Navigates to the player home page
     */
    @Test
    public void test_navigateToLeaderboardView() throws Exception{
        if (testPlayer == null) {
            // skip the tests
            return;
        }
        solo.waitForActivity(HomeActivity.class, 3000);
        solo.clickOnImage(4);
        assertTrue(solo.waitForText("Leaderboard", 1, 3000));
    }

    /**
     * Navigates to the player home page
     */
    @Test
    public void test_navigateToPlayerSearchView() throws Exception{
        if (testPlayer == null) {
            // skip the tests
            return;
        }
        solo.waitForActivity(HomeActivity.class, 3000);
        solo.clickOnImage(5);
        assertTrue(solo.waitForText("All Players", 1, 3000));
    }

    /**
     * Navigates to the player home page
     */
    @Test
    public void test_navigateToPlayerProfileView() throws Exception{
        if (testPlayer == null) {
            // skip the tests
            return;
        }
        solo.waitForActivity(HomeActivity.class, 3000);
        solo.clickOnImage(6);
        assertTrue(solo.waitForText("QRs Scanned", 1, 3000));
    }


    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }
}
