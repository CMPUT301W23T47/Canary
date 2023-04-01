package com.cmput301w23t47.canary.controller;

import android.graphics.Bitmap;
import android.os.Handler;

import com.cmput301w23t47.canary.callback.GetImageCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;

/**
 * Image generator controller; Makes a get request
 */
public class ImageGenerator{
    public static String imageUrl = "https://picsum.photos/200";

    /**
     * Gets the image as an async operation
     * @param url the url to get the image from
     * @param callback the csallback function
     * @return
     */
    public static void getImage(String url, GetImageCallback callback) {
        Handler handler = new Handler();
        new Thread(() -> {
            RequestCreator requestCreator = Picasso.get().load(url);
            Bitmap bitmap = null;
            try {
                bitmap = requestCreator.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap finalBitmap = bitmap;
            handler.post(() -> {
                callback.getImage(finalBitmap);
            });
        }).start();
    }
}
