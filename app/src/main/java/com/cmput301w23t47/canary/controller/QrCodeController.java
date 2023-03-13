package com.cmput301w23t47.canary.controller;

import android.location.Location;
import android.util.Log;

import com.cmput301w23t47.canary.model.QrCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Util controller for interacting with the QR Code
 * @author Meharpreet Singh Nanda
 */
public class QrCodeController {
    private static final String dateFormat = "dd MMM yyyy";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.CANADA);

    /**
     * Gets the Hash for the QR
     * Uses the SHA-256 algorithm as the hashing algorithm
     * @param qrVal (String): the original raw value of the QR Code
     * @return byte[] sha256 hash
     */
    public static byte[] getHashForQr(String qrVal)  {
        try {
            return MessageDigest.getInstance("SHA-256").digest(qrVal.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException err) {
            Log.e("HASH ERR", "getHashForQr: Couldn't find hashing algorithm" );
            err.printStackTrace();
            return new byte[0];
        }
    }

    public static String getHashStrForQr(String qrVal) {
        byte[] qrHash = QrCodeController.getHashForQr(qrVal);
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < qrHash.length; i++) {
            str.append(qrHash[i]);
        }
        return str.toString();
    }

    /**
     * Gets the formatted Date time
     * @param date the date to format
     * @return the formatted str for date
     */
    public static String getFormattedDate(Date date) {
        return simpleDateFormat.format(date);
    }

    /**
     * Gets the title for the label on the map
     * @param qrCode the qrCode to parse
     * @return the string to display on map for QR
     */
    public static String getTitleForMapPin(QrCode qrCode) {
        String name;
        if (qrCode.getName().length() > 14) {
            name = String.format(Locale.CANADA, "%s...", qrCode.getName().substring(0, 13));
        } else {
            name = qrCode.getName();
        }
        return String.format(Locale.CANADA, "%s: %d", name, qrCode.getScore());
    }

    /**
     * Gets the display name for the qr (Truncates it if it is too long)
     * @param qrName the full name of the qr
     * @return the formatted name
     */
    public static String getDisplayName(String qrName) {
        if (qrName.length() <= 16) {
            return qrName;
        }
        return String.format(Locale.CANADA, "%s...", qrName.substring(0, 13));
    }

    /**
     * Gets the qrs within the specified distance
     * @param qrCodes the original list of qrs
     * @param from the reference point
     * @param maxDist the max distance to consider
     * @return the filtered list of Qrs
     */
    public static ArrayList<QrCode> getQrsWithinDistance(ArrayList<QrCode> qrCodes, Location from, double maxDist) {
        ArrayList<QrCode> filterQrs = new ArrayList<>();
        for (QrCode qrCode : qrCodes) {
            if (qrCode.hasLocation() && from.distanceTo(qrCode.getLocation()) <= maxDist) {
                filterQrs.add(qrCode);
            }
        }
        return filterQrs;
    }
}
