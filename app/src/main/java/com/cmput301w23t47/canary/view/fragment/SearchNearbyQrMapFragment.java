package com.cmput301w23t47.canary.view.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetQrListCallback;
import com.cmput301w23t47.canary.controller.FirestoreQrController;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.QrCode;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for displaying the nearby QrCodes
 * @author Meharpreet Singh Nanda, Andrew
 */
public class SearchNearbyQrMapFragment extends LocationBaseFragment implements OnMapReadyCallback,
        GetQrListCallback {
    private GoogleMap googleMap;
    private final String playerLocTitle = "My Location";
    private ArrayList<QrCode> qrCodes;

    private FirestoreQrController firestoreQrController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_nearby_qr_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        init();
    }

    /**
     * Initializes the UI
     */
    private void init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        firestoreQrController = new FirestoreQrController();
        firestoreQrController.getAllQrs(this);
        askPermissions();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng point;
        if (playerLocation != null) {
            point = new LatLng(playerLocation.getLatitude(), playerLocation.getLongitude());
        } else {
            point = new LatLng(37, -122);
        }
        addMarker(point, playerLocTitle);
        googleMap.addMarker(new MarkerOptions().position(point).title(playerLocTitle));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
    }

    /**
     * Adds the marker to the map
     * @param point the point to add the marker to
     * @param title the title of the marker
     */
    private void addMarker(LatLng point, String title) {
        googleMap.addMarker(new MarkerOptions().position(point).title(title));
    }

    /**
     * Centers the map to the given point on map
     * @param point the point to center the camera on
     */
    private void centerMapCamera(LatLng point) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
    }



    @Override
    protected void updateLocation() {
        if (googleMap == null) {
            return;
        }
        LatLng point = getLatLng(playerLocation);
        addMarker(point, playerLocTitle);
        centerMapCamera(point);
        setCameraView();
    }

    /**
     * Handles the case when the location permissions have been granted
     */
    @Override
    protected void locationPermissionGranted() {
        getLocationRequest();
    }

    /**
     * Handles the case when the location permission are not granted
     */
    @Override
    protected void locationPermissionNotGranted() {

    }

    /**
     * Gets the LatLng of the location
     * @param loc the location to convert
     * @return the LatLng form of the location
     */
    private LatLng getLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void getQrList(ArrayList<QrCode> qrCodes) {
        this.qrCodes = qrCodes;
        setTheQrPinsOnMap();
    }

    /**
     * this is a way to bind the camera on the map
     * this is used to make sure that the camera when first opens
     * is centered on the user not elsewhere
     */
    private void setCameraView(){
        double bottomBoundary = playerLocation.getLatitude() - .1;
        double leftBoundary = playerLocation.getLongitude() - .1;
        double topBoundary = playerLocation.getLatitude() + .1;
        double rightBoundary = playerLocation.getLongitude() + .1;

        LatLngBounds mapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        googleMap.moveCamera( CameraUpdateFactory.newLatLngBounds(mapBoundary, 0));
    }

    /**
     * Sets the Qr pins on the map
     */
    private void setTheQrPinsOnMap() {
        Log.d("TAG", "setTheQrPinsOnMap: " + qrCodes.size());
        if (qrCodes == null || qrCodes.size() == 0) {
            return;
        }
        for (QrCode qrCode : qrCodes) {
            if (!qrCode.hasLocation()) {
                continue;
            }
            googleMap.addMarker(new MarkerOptions()
                    .position(retrieveLatLngForQr(qrCode))
                    .title(QrCodeController.getTitleForMapPin(qrCode))
            );
        }
    }

    /**
     * Gets the LatLng for the qr code
     * @param qrCode the qr code to find the location of
     * @return the LatLng position of the qrcode
     */
    public LatLng retrieveLatLngForQr(QrCode qrCode) {
        Location location = qrCode.getLocation();
        if (location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}