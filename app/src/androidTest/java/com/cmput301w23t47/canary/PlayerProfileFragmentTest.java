package com.cmput301w23t47.canary;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.cmput301w23t47.canary.view.fragment.SplashFragment.TAG;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.fragment.PlayerProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.installations.FirebaseInstallations;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * The type Player profile fragment test.
 */
@RunWith(AndroidJUnit4.class)
public class PlayerProfileFragmentTest {
    private Solo solo;

    static {FirestoreController.testMode=true;}

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
     * Start activity.
     *
     * @throws Exception the exception
     */
    @Test
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {
        });
    }

    /**
     * Check test mode.
     */
    @Test
    public void checkTestMode(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        boolean textAppeared = solo.waitForText("msn", 1, 2000);
        assertTrue("Text not found!", textAppeared);
    }


    /**
     * Check past qr exists.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void checkPastQrExists() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImage(6);
        solo.waitForText("msn", 1, 6000);
        RecyclerView qrsScannedList = (RecyclerView) solo.getView(R.id.qrsScannedList);
        RecyclerView.Adapter qrAdapter = qrsScannedList.getAdapter();
        assertTrue( qrAdapter.getItemCount() > 0) ;
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
