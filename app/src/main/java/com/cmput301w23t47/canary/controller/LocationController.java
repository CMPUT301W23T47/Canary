package com.cmput301w23t47.canary.controller;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.List;

/**
 * Controller for interacting the location
 */
public class LocationController {
    // the Geocoder object used to parse the location
    private static Geocoder geocoder = new Geocoder(null);

    /**
     * Retrieves the city name
     * @param location the location to parse
     * @return the city name if applicable, empty string "" otherwise
     */
    public static String retrieveCityName(Location location) {
        try {
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
