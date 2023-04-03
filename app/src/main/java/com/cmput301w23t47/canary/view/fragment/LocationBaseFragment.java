package com.cmput301w23t47.canary.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Base fragment for dealing with locations
 * Follows the Template Class Design Pattern
 * Reference: https://developer.android.com/training/location/retrieve-current#java
 *      https://developer.android.com/training/location/permissions
 *      https://developer.android.com/training/location/permissions#upgrade-to-precise
 * @author Meharpreet Singh Nanda
 */
public abstract class LocationBaseFragment extends Fragment {
    protected String[] LOC_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    // provider for the location
    FusedLocationProviderClient fusedLocationClient;
    // the location of the current player
    protected Location playerLocation;

    /**
     * Determines whether the location permission has been granted to the app
     * @return true if permissions are already granted
     */
    protected boolean arePermissionsGranted() {
        // determine if all the permissions have been granted or not
        for (String perm : LOC_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Asks for the permissions if required
     * Reference: https://developer.android.com/training/location/retrieve-current#java
     */
    protected void askPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                locationPermissionGranted();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                locationPermissionGranted();
                            } else {
                                locationPermissionNotGranted();
                            }
                        }
                );
        if (arePermissionsGranted()) {
            locationPermissionGranted();
            return;
        }
        // ask for permissions if not already granted
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    /**
     * launches the request to get the current location
     */

    protected void getLocationRequest() {
        if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), (Location loc) -> {
                        if (loc != null) {
                            this.playerLocation = loc;
                            updateLocation();
                        }
                    }).addOnFailureListener((Exception err) -> {
                        err.printStackTrace();
                    });
        }
    }

    /**
     * Update the location on the view
     * The player location is already set to valid value
     */
    abstract protected void updateLocation();

    /**
     * Handles the case when the location permissions have been granted
     */
    abstract protected void locationPermissionGranted();

    /**
     * Handles the case when the location permission are not granted
     */
    abstract protected void locationPermissionNotGranted();
}
