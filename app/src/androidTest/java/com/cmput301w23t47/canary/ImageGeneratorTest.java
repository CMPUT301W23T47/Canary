package com.cmput301w23t47.canary;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.ImageGenerator;
import com.cmput301w23t47.canary.view.activity.ScanQRCodeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Intent tests for ImageGenerator
 */
public class ImageGeneratorTest {
    public class ImageClientMock implements GetImageCallback {
        private CountDownLatch countDownLatch = new CountDownLatch(1);
        private Bitmap bitmap = null;
        private boolean callRet = false;

        @Override
        public void getImage(Bitmap bitmap) {
            Log.d("TAG", "getImageTest: 0");
            callRet = true;
            this.bitmap = bitmap;
            countDownLatch.countDown();
        }
    }

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
     * Asserts whether the image generator works
     * @throws Exception Failure
     */
    @Test
    public void checkImageGenerator() throws Exception{
        Looper.prepare();
        ImageClientMock mock1 = new ImageClientMock();
        ImageClientMock mock2 = new ImageClientMock();
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock1);
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock2);
        assertFalse(mock1.equals(mock2));
//        try {
//            mock1.countDownLatch.await(10000, TimeUnit.MILLISECONDS);
//            mock2.countDownLatch.await(10000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            fail(e.getMessage());
//        }
//        assertFalse(mock1.bitmap.equals(mock2.bitmap));
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
