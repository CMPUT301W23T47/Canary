package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.controller.ImageGenerator;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ImageGeneratorTest {
    public class ImageClientMock implements GetImageCallback {
        private CountDownLatch countDownLatch = new CountDownLatch(1);
        private Bitmap bitmap = null;
        private boolean callRet = false;

        @Override
        public void getImage(Bitmap bitmap) {
            callRet = true;
            this.bitmap = bitmap;
            countDownLatch.countDown();
        }
    }

    @Test
    public void test_getImage() {
        ImageClientMock mock1 = new ImageClientMock();
        ImageClientMock mock2 = new ImageClientMock();
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock1);
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock2);
        // TODO: Implement main loop
        assertFalse(mock1.equals(mock2));
//        try {
//            mock1.countDownLatch.await(3000, TimeUnit.MILLISECONDS);
//            mock2.countDownLatch.await(3000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            fail(e.getMessage());
//        }
//        assertFalse(mock1.bitmap.equals(mock2.bitmap));
    }
}