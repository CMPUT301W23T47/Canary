package com.cmput301w23t47.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.controller.ImageGenerator;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.fragment.QrCapturePreferenceFragment;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class imageCompressionTest {
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
    /**
     * Test the image compression
     */
    public void testImageCompression() {
        ImageClientMock mock1 = new ImageClientMock();
        //create a QR code
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock1);
        try {
            mock1.countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = mock1.bitmap;
        //compress the image
        Bitmap compressedBitmap = QrCapturePreferenceFragment.compressImage(bitmap);
        //check if the image is compressed
        assert (bitmap.getByteCount() > compressedBitmap.getByteCount());
    }
}

