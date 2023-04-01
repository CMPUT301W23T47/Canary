package com.cmput301w23t47.canary;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.model.QrCode;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mifmif.common.regex.Main;
import com.robotium.solo.Solo;

import org.checkerframework.common.subtyping.qual.Bottom;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class QRCodeViewFragmentTest {

    private Solo solo;


    static {
        FirestoreController.testMode = true;
    }

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception{
        Intents.init();
        rule.getScenario().onActivity(activity -> {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        });
    }

    @Test
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {});
    }

    @Test
    public void deleteQrTest() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //going to the player profile page
        View playerProfile = solo.getView(R.id.playerProfileFragment);
        solo.clickOnView(playerProfile);
        ListView qrsScannedList = (ListView) solo.getView(R.id.qrsScannedList);
        ListAdapter qrAdapter = qrsScannedList.getAdapter();
        int count = qrAdapter.getCount();
        //getting the first qr scanned in the list.
        solo.clickInList(1);

        //deleting the scanned qr.
        View deleteView = solo.getView(R.id.qr_delete_icon);
        solo.clickOnView(deleteView);
        solo.clickOnButton("Yes");
        solo.clickOnButton("Continue");
        solo.clickOnActionBarHomeButton();
        solo.clickOnView(playerProfile);
        ListView afterDeleteQrsScannedList = (ListView) solo.getView(R.id.qrsScannedList);
        int afterDeleteCount = afterDeleteQrsScannedList.getAdapter().getCount();
        int finalCount = count - afterDeleteCount;
        assert( finalCount == 1);

    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }
}
