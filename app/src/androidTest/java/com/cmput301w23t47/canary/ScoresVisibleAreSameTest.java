package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ScoresVisibleAreSameTest {

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
    public void scoresVisibleAreSameTest(){
        solo.waitForText("msn1", 1, 5000);
        TextView score = (TextView) solo.getView(R.id.playerScore);
        int scoreHome = Integer.parseInt(score.getText().toString());
        TextView highScore = (TextView) solo.getView(R.id.highestQrScore);
        int highScoreHome =  Integer.parseInt(highScore.getText().toString());
        View playerProfile = solo.getView(R.id.playerProfileFragment);
        TextView scorePlayerProfile = (TextView) solo.getView(R.id.playerScore);
        TextView highScorePlayerProfile = (TextView) solo.getView(R.id.highestQrScore);
        assertEquals(scoreHome,Integer.parseInt(scorePlayerProfile.getText().toString()));
        assertEquals(highScoreHome, Integer.parseInt(highScorePlayerProfile.getText().toString()));
    }
}
