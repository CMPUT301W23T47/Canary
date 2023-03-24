package com.cmput301w23t47.canary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.fragment.QrCapturePreferenceFragment;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class imageCompressionTest {
    private QrCode mockQr() throws IOException {
        URL imageUrl = new URL("https://picsum.photos/200");
        Bitmap bitmap= BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        QrCode qrCode = new QrCode("111-111-212", 619, null, "testQrCode",bitmap, null);
        return qrCode;
    }
    @Test
    /**
     * Test if the image is compressed
     */
    public void testImageCompression() throws IOException {
        QrCode qrCode = mockQr();
        Bitmap image1= qrCode.getQrImage();
        int size1 = image1.getByteCount();
        int size2= QrCapturePreferenceFragment.compressImage(image1).getByteCount();
        assert(size1>size2);
    }

}

