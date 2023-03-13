package com.cmput301w23t47.canary;

import static org.junit.Assert.assertTrue;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The type Create profile test.
 */
@RunWith(AndroidJUnit4.class)
public class CreateProfileTest {
    private Solo solo;

    static {FirestoreController.firstTimeTester=true;}

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
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception{
        Intents.release();
        solo.finishOpenedActivities();
    }


    /**
     * Check if create profile prompted for new user.
     */
    @Test
    public void checkIfCreateProfilePromptedForNewUser(){
        boolean textAppeared = solo.waitForText("Create Profile", 1, 2000);
        assertTrue("Text not found!", textAppeared);

    }



}
