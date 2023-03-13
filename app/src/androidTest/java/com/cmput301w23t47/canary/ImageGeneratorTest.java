package com.cmput301w23t47.canary;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.app.Instrumentation;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.view.activity.ScanQRCodeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Intent tests for ImageGenerator
 */
public class ImageGeneratorTest {
    private Solo solo;
    static {
        FirestoreController.testMode=true;
    }


    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
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
     * Launches the QR Code scanner activity and confirms whether
     * QR Code is returned
     * @throws Exception Failure
     */
    @Test
    public void checkImageGenerator() throws Exception{
        solo.assertCurrentActivity("Err Wrong Activity", MainActivity.class);

        // set up the stubbing when scanQrCode page launched
        //solo.getImage()
        Instrumentation.ActivityResult resIntent = IntentTestUtil.getMockResultForScanQrCodeActivity();
        intending(hasComponent(ScanQRCodeActivity.class.getName()))
                .respondWith(resIntent);
        solo.clickOnView(solo.getView(R.id.scan_qr));
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
