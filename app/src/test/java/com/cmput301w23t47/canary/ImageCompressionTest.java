package com.cmput301w23t47.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.view.fragment.QrCapturePreferenceFragment;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class ImageCompressionTest {
    @Test
    public void testImageCompression(){
        String url = "https://picsum.photos/200";
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert bitmap != null;
        int size = bitmap.getByteCount();
        Bitmap compressedBitmap = QrCapturePreferenceFragment.compressImage(bitmap);
        int compressedSize = compressedBitmap.getByteCount();
        assert compressedSize < size;
    }
}
