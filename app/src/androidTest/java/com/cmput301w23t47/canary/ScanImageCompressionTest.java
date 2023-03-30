package com.cmput301w23t47.canary;

import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.widget.CheckBox;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.ImageCompression;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.util.FirestorePlayerTestUtil;
import com.cmput301w23t47.canary.view.activity.HomeActivity;
import com.cmput301w23t47.canary.view.activity.ScanQRCodeActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class ScanImageCompressionTest {
    private Solo solo;
    private static Player testPlayer = null;
    private static FirestorePlayerTestUtil firestorePlayerTestUtil;

    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<>(HomeActivity.class, true, true);
    @BeforeClass
    public static void beforeAll() {
        FirestoreController.switchToTestMode();
        firestorePlayerTestUtil = new FirestorePlayerTestUtil();
        testPlayer = firestorePlayerTestUtil.getTestPlayer();
    }
    @Before
    public void setUp() throws Exception {
        FirestoreController.switchToTestMode();
        Intents.init();
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    /**
     * Test if QR code is scanned and compressed
     */
    @Test
    public void testScanQrCompression() {
        if (testPlayer == null) {
            return;
        }
        solo.waitForText(testPlayer.getUsername());
        // mock intent of taking a snapshot
        Instrumentation.ActivityResult resIntent = IntentTestUtil.getMockResultForScanQrCodeActivity();
        intending(hasComponent(ScanQRCodeActivity.class.getName()))
                .respondWith(resIntent);
        solo.clickOnView(solo.getView(R.id.scan_qr));
        assertTrue(solo.waitForText("Save Location"));
        // check default value of checkbox
        CheckBox checkBox = (CheckBox) solo.getView(R.id.saveLocationCheckbox);
        assertTrue(checkBox.isChecked());
        ConstraintLayout progressBarLayout = (ConstraintLayout) solo.getView(R.id.progress_bar_box);
        IntentTestUtil.waitForProgressBarToHide(progressBarLayout, solo);
        // save the snapshot
        solo.clickOnView(solo.getView(R.id.no_snap));
        assertTrue(solo.waitForText("Snapshot"));
        // check if the snapshot is compressed
        Bitmap bitmap = (Bitmap) solo.getView(R.id.no_snap).getTag();
        ImageCompression imageCompression = new ImageCompression();
        int size = imageCompression.compressImage(bitmap).getByteCount();
        assertTrue(size<bitmap.getByteCount());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
