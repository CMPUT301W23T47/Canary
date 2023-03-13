package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cmput301w23t47.canary.controller.ImageGenerator;

import org.junit.Test;

public class ImageGeneratorTest {
    ImageGenerator image = new ImageGenerator();

    @Test
    public void image_isDifferent() {

        ImageGenerator image1 = new ImageGenerator();
        ImageGenerator image2 = new ImageGenerator();

        assertNotEquals(image1, image2);
    }
}