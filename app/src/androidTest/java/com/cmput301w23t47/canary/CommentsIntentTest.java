package com.cmput301w23t47.canary;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CommentsIntentTest {
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
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {
        });
    }

    @Test
    public void checkPreviousComment(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnImage(6);
        solo.waitForText("msn", 1, 6000);
        RecyclerView qrCodeList = (RecyclerView) solo.getView(R.id.qrsScannedList);
        solo.clickOnView(qrCodeList.getChildAt(0));
        solo.waitForText("Comments", 1, 6000);
        RecyclerView commentsList = (RecyclerView) solo.getView(R.id.qr_comments_list);
        solo.sleep(2000);
        RecyclerView.Adapter commentsAdapter = commentsList.getAdapter();
        assertTrue(commentsAdapter.getItemCount() >= 1);
    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }

}