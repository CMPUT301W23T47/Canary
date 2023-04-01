package com.cmput301w23t47.canary;

import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewOtherPlayerTest {

    private Solo solo;
    static {
        FirestoreController.testMode=true;}
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
    @Before
    public void setUp() throws Exception{
        rule.getScenario().onActivity(activity -> {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
        });
    }
    @Test
    public void startActivity() throws Exception{
        rule.getScenario().onActivity(activity -> {
        });
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void viewOtherPlayerProfileTest(){
        solo.clickOnImage(5);;
        solo.clickOnText("msn1");
        boolean textAppeared = solo.waitForText("msn1", 1, 2000);
        assertTrue("Text not found!", textAppeared);
    }
}
