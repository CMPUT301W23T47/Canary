package com.cmput301w23t47.canary.controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import java.io.IOException;
import java.util.List;
import android.location.Location;

import com.cmput301w23t47.canary.model.QrCode;
import com.google.android.gms.maps.model.LatLng;

/**
 * Controller for interacting the location
 */
public class LocationController {
    /**
     * Retrieves the city name
     * @param location the location to parse
     * @return the city name if applicable, empty string "" otherwise
     */
    public static String retrieveCityName(Location location, Context context) {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.isEmpty()) {
                return "";
            }
            return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
