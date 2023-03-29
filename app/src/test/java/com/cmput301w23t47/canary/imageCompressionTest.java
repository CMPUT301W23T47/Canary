package com.cmput301w23t47.canary;

import android.graphics.Bitmap;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.controller.ImageCompression;
import com.cmput301w23t47.canary.controller.ImageGenerator;

import org.junit.Test;

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
    public void testImageCompression()
    {
        ImageClientMock mock1 = new ImageClientMock();
        //get the image from the url
        ImageGenerator.getImage(ImageGenerator.imageUrl, mock1);
        //compress the image
        Bitmap compressedBitmap = ImageCompression.compressImage(mock1.bitmap);
        //check if the image is compressed
        assert (mock1.bitmap.getByteCount() > compressedBitmap.getByteCount());
    }
}

