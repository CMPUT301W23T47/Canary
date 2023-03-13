package com.cmput301w23t47.canary;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PlayerSearchFragmentTest {
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
    @Test
    public void testSearch() throws Exception {
        rule.getScenario().onActivity(activity -> {
                    solo.clickOnView(solo.getView(R.id.search_player_text));
                    solo.enterText(0, "ajharris");
                    solo.clickOnButton("Search");
                    //user is ound
                    solo.clickOnButton("View Profile");
                    if (solo.waitForText("ajharris", 1, 2000)) {
                        assert(true);
                    }
                    else {
                        assert(false);
                    }
                }

        );
    }
    @Test
    public void testSearchFail() throws Exception {
        rule.getScenario().onActivity(activity -> {
                    solo.clickOnView(solo.getView(R.id.search_player_text));
                    solo.enterText(0, "Adrian Harris");
                    solo.clickOnButton("Search");
                    //user is ound
                    solo.clickOnButton("View Profile");
                    if (solo.waitForText("ajharris", 1, 2000)) {
                       assert(true);
                    }
                    else {
                        assert(false);
                    }
                }

        );
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
