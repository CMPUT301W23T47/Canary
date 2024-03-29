package com.cmput301w23t47.canary.view.fragment;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.callback.GetQrListCallback;
import com.cmput301w23t47.canary.controller.FirestoreQrController;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.databinding.FragmentSearchNearbyQrMapBinding;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Fragment for displaying the nearby QrCodes
 * @author Meharpreet Singh Nanda, Andrew
 */
public class SearchNearbyQrMapFragment extends LocationBaseFragment implements OnMapReadyCallback,
        GetQrListCallback, GetIndexCallback {
    private GoogleMap googleMap;
    private final String playerLocTitle = "My Location";
    private ArrayList<QrCode> qrCodes = new ArrayList<>();

    private FirestoreQrController firestoreQrController;
    private FragmentSearchNearbyQrMapBinding binding;
    private SearchNearbyQrListAdapter qrCodeListAdapter;

    private Location selectedQrLocation;
    /**
     * Handles the layout of the activity, and called on activity creation.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchNearbyQrMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Sets the view of the page and calls the function to show the UI of the page.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
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
        qrCodeRecyclerViewInit();
        firestoreQrController.getAllQrs(this);
        showLoadingBar();
        askPermissions();

        selectedQrLocation = SearchNearbyQrMapFragmentArgs.fromBundle(getArguments()).getQrLocation();

        // this will send us to the search radius page
        binding.enterSearchRadiusText.setOnClickListener(view -> {
            navigateToSearchRadiusPage();
        });

        // this will send us to the search city page
        binding.enterSearchCityText.setOnClickListener(view -> {
            navigateToSearchCityPage();
        });
    }

    /**
     * Gets the player from firestore to show and also shows the loading bar
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && qrCodes.size() == 0){
            showLoadingBar();
            askPermissions();
        }
    }


    /**
     * Initializes the qrCode recycler view
     */
    private void qrCodeRecyclerViewInit() {
        // qr code list adapter init
        qrCodeListAdapter = new SearchNearbyQrListAdapter(qrCodes, this);
        binding.qrItemsList.setAdapter(qrCodeListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.qrItemsList.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_shape));
        binding.qrItemsList.addItemDecoration(dividerItemDecoration);
        binding.qrItemsList.setHasFixedSize(true);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng point;
        if (playerLocation != null) {
            point = new LatLng(playerLocation.getLatitude(), playerLocation.getLongitude());
            addMarker(point, playerLocTitle);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }
    }

    /**
     * Adds the marker to the map
     * @param point the point to add the marker to
     * @param title the title of the marker
     */
    private void addMarker(LatLng point, String title) {
        BitmapDescriptor icon = bitmapDescriptorFromVector( getContext(), R.drawable.map_pin_player_location );
        googleMap.addMarker(new MarkerOptions().position(point).title(title).icon( icon ));
    }

    /**
     * Centers the map to the given point on map
     * @param point the point to center the camera on
     */
    private void centerMapCamera(LatLng point) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
    }

    /**
     * Updates the current location of the player
     */
    @Override
    protected void updateLocation() {
        if (googleMap == null) {
            return;
        }
        LatLng point = getLatLng(playerLocation);
        addMarker(point, playerLocTitle);
        centerMapCamera(point);
        setCameraView();
        showSelectedQr();
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
        // TODO: Handle this case
    }

    /**
     * Gets the LatLng of the location
     * @param loc the location to convert
     * @return the LatLng form of the location
     */
    private LatLng getLatLng(Location loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }

    /**
     * Sets the list of Qrcodes
     * @param qrCodes
     */
    @Override
    public void getQrList(ArrayList<QrCode> qrCodes) {
        this.qrCodes = qrCodes;
        hideLoadingBar();
        setTheQrPinsOnMap();
        showSelectedQr();
    }

    /**
     * this is a way to bind the camera on the map
     * this is used to make sure that the camera when first opens
     * is centered on the user not elsewhere
     */
    private LatLngBounds setCameraView(){
        // I changed this to being a return statement because I assume that this means all I have to do is get the PlayerLocation and then compare to this function
        double bottomBoundary = playerLocation.getLatitude() - .1;
        double leftBoundary = playerLocation.getLongitude() - .1;
        double topBoundary = playerLocation.getLatitude() + .1;
        double rightBoundary = playerLocation.getLongitude() + .1;

        LatLngBounds mapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        googleMap.moveCamera( CameraUpdateFactory.newLatLngBounds(mapBoundary, 0));

        return mapBoundary;
    }

    private void setCameraViewToQr(Location qrLocation){
        if (qrLocation == null) {
            return;
        }
        double bottomBoundary = qrLocation.getLatitude() - .1;
        double leftBoundary = qrLocation.getLongitude() - .1;
            double topBoundary = qrLocation.getLatitude() + .1;
        double rightBoundary = qrLocation.getLongitude() + .1;

        LatLngBounds mapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );

        googleMap.moveCamera( CameraUpdateFactory.newLatLngBounds(mapBoundary, 0));
    }

    private void showSelectedQr(){
        if (selectedQrLocation == null){
            return;
        }
        setCameraViewToQr(selectedQrLocation);
    }

    /**
     * Sets the Qr pins on the map
     */
    private void setTheQrPinsOnMap() {
        if (qrCodes == null || qrCodes.size() == 0) {
            return;
        }

        // Get a bitmap from the drawable
        //doing this here so dont have to go the function every time
        BitmapDescriptor BitmapQRIcon = bitmapDescriptorFromVector( getContext(), R.drawable.map_pin_qr_code_actual);


        ArrayList<QrCode> qrsOnMap = new ArrayList<>();
        // this will place the markers on the map for the qr codes change this to being the qr icon
        for (QrCode qrCode : qrCodes) {
            if (!qrCode.hasLocation()) {
                continue;
            }
            qrsOnMap.add(qrCode);
            googleMap.addMarker(new MarkerOptions()
                    .position(retrieveLatLngForQr(qrCode))
                    .title(QrCodeController.getTitleForMapPin(qrCode))
                    .icon(BitmapQRIcon)
            );
        }
        qrCodeListAdapter.updateList(qrsOnMap);
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

    /**
     * Shows the loading bar
     */
    private void showLoadingBar() {
        binding.progressBarBox.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the loading bar
     */
    private void hideLoadingBar() {
        binding.progressBarBox.setVisibility(View.GONE);
    }

    /**
     * Called when item is selected in the list
     * @param ind the requested index
     */
    @Override
    public void getIndex(int ind) {
        QrCode qrCode = qrCodeListAdapter.getItemAt(ind);
        if (qrCode != null) {
            navigateToSelectedQr(qrCode);
        }
    }

    /**
     * Navigates to the selected qr
     * @param qrCode the qr code selected
     */
    private void navigateToSelectedQr(QrCode qrCode) {
        SearchNearbyQrMapFragmentDirections.ActionSearchNearbyQrsToQrPage action =
                SearchNearbyQrMapFragmentDirections.actionSearchNearbyQrsToQrPage(qrCode.getHash());
        Navigation.findNavController(getView()).navigate(action);
    }

    /**
     * Navigates to the search radius page
     */
    private void navigateToSearchRadiusPage() {
        Navigation.findNavController( getView() ).navigate( R.id.action_searchNearbyQrMapToDistList );
    }




    /**
     * Turns a vector image asset into a bitmap image asset with a background
     * does this by just overlaying the vector on top of the background
     * they both start at the same point so need to shift image onto the background properly
     * @param context the context
     * @param vectorDrawableResourceId the resource id of the vector asset
     * @param backgroundDrawableResourceId the resource id of the background
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context,@DrawableRes int vectorDrawableResourceId, @DrawableRes int backgroundDrawableResourceId) {
        // this makes the background
        Drawable background = ContextCompat.getDrawable(context, backgroundDrawableResourceId);
        //set tje bounds of the background
        // 0 means it will not be left shifted from start point and 0 means it will not be shifted down from the start point
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        //set the bounds of the vector
        // 20 and 10 are the left and top shift
        //change them or the size of the vector image to change the size and position of the vector
        vectorDrawable.setBounds(15, 7, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * Turns a vector asset into a bitmap
     * this one does not need the background because found goog default asset for the pin
     * @param context the context
     * @param vectorResId the resource id of the vector
     * @return the updated bitmap -m ade from the vector
     */
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //ToDo: make sure correct
    /**
     * Navigates to the search City page
     */
    private void navigateToSearchCityPage() {
        // my understanding says this will be right
        Navigation.findNavController( getView() ).navigate(R.id.action_searchNearbyQrMapFragment_to_citySearchQrCodeListFragment);
    }
}