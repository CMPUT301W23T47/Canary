package com.cmput301w23t47.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.cmput301w23t47.canary.controller.ImageCompression;
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
    /**
     * Mock class for the image client
     */
    public static Bitmap getBitMapFromURL(){
        try{
            URL url = new URL("https://picsum.photos/200");
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            return null;
        }
    }
    @Test
    /**
     * Test the image compression
     */
    public void testImageCompression()
    {
        Bitmap mock1 = getBitMapFromURL();
        //compress the image
        Bitmap compressedBitmap = ImageCompression.compressImage(mock1);
        //check if the image is compressed
        assert (mock1.getByteCount() > compressedBitmap.getByteCount());
    }
}

