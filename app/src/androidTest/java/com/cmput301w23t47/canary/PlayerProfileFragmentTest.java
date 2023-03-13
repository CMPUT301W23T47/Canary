package com.cmput301w23t47.canary;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.cmput301w23t47.canary.view.fragment.SplashFragment.TAG;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

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

@RunWith(AndroidJUnit4.class)
public class PlayerProfileFragmentTest {
    private Solo solo;

    static {FirestoreController.testMode=true;}

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
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {
        });
    }

    @Test
    public void checkTestMode(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        boolean textAppeared = solo.waitForText("officalTester", 1, 2000);
        assertTrue("Text not found!", textAppeared);
    }
    
    

    @Test
    public void checkPastQrExists() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        View menuItem = solo.getView(BottomNavigationView.class).findViewById(R.id.bottom_navigation);
        solo.clickOnImage(6);
        solo.sleep(1000);
        ListView qrsScannedList = (ListView) solo.getView(R.id.qrsScannedList);
        ListAdapter qrAdapter = qrsScannedList.getAdapter();
        PlayerQrCode firstItem = (PlayerQrCode) qrAdapter.getItem(0);
        assertEquals("South Nebraska CollegeYuan Renminbi", firstItem.getName());

    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }

}
