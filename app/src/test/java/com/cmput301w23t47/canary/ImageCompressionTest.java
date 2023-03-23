package com.cmput301w23t47.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.view.fragment.QrCapturePreferenceFragment;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class ImageCompressionTest {
    @Test
    public void testImageCompression() throws IOException {
        URL url = new URL("https://picsum.photos/200");
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        int bmpSize = bmp.getByteCount();
        Bitmap compressed=QrCapturePreferenceFragment.compressImage(bmp);
        int compressedSize = compressed.getByteCount();
        // The compressed image should be smaller than the original
        assert compressedSize < bmpSize;
    }
}