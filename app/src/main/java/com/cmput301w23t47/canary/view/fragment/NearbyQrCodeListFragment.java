package com.cmput301w23t47.canary.view.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.callback.GetQrListCallback;
import com.cmput301w23t47.canary.controller.FirestoreQrController;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.databinding.FragmentNearbyQrCodeListBinding;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * List the nearby qrs on a list
 */
public class NearbyQrCodeListFragment extends LocationBaseFragment implements GetQrListCallback,
        GetIndexCallback {
    // the list of qr codes
    private ArrayList<QrCode> qrCodes = new ArrayList<>();
    // the search radius in meters
    private double searchRadius = 0;
    private String searchCity;

    private FragmentNearbyQrCodeListBinding binding;
    private FirestoreQrController firestoreQrController;
    private SearchNearbyQrListAdapter qrCodeListAdapter;

    public NearbyQrCodeListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNearbyQrCodeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /**
     * Initializes the ui
     */
    private void init() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        firestoreQrController = new FirestoreQrController();
        qrCodeRecyclerViewInit();

        firestoreQrController.getAllQrs(this);
        showLoadingBar();
        askPermissions();
        playerLocation = new Location("");
        binding.searchQrButton.setOnClickListener(view -> {
            searchButtonPressed();
        });
    }

    /**
     * Initializes the qrCode recycler view
     */
    private void qrCodeRecyclerViewInit() {
        qrCodeListAdapter = new SearchNearbyQrListAdapter(qrCodes, this);
        binding.searchResultList.setAdapter(qrCodeListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.searchResultList.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_shape));
        binding.searchResultList.addItemDecoration(dividerItemDecoration);
        binding.searchResultList.setHasFixedSize(true);
    }

    /**
     * Update the location on the view
     * The player location is already set to valid value
     */
    @Override
    protected void updateLocation() {
        updateQrList();
    }

    @Override
    protected void locationPermissionGranted() {
        getLocationRequest();
    }

    @Override
    protected void locationPermissionNotGranted() {

    }

    @Override
    public void getQrList(ArrayList<QrCode> qrCodes) {
        this.qrCodes = qrCodes;
        hideLoadingBar();
        updateQrList();
    }

    @Override
    public void getIndex(int ind) {
        QrCode qrCode = qrCodeListAdapter.getItemAt(ind);
        if (qrCode != null) {
            navigateToSelectedQr(qrCode);
        }
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
     * Updates the qr list
     * to be accurate with the search radius
     */
    private void updateQrList() {
        if (qrCodes == null || qrCodes.size() == 0) {
            return;
        }
        if (searchRadius == 0 || playerLocation == null) {
            showAllQrs();
            return;
        }
        ArrayList<QrCode> filterQrs = QrCodeController.getQrsWithinDistance(qrCodes, playerLocation, searchRadius);
        qrCodeListAdapter.updateList(filterQrs);
    }
    
    /**
     * Updates the qr list based on the information in the search bar
     * this is for when the user enters a city name or part of a city name
     * ie "Edmonton" or "Edm"
     * this will then search for all the qr codes that are in the city of Edmonton
     * or all cities that start with Edm
     * this will then update the list to show all the qr codes that are in that city
     */
    private void updateQrListCity(){
        // if nothing is in the search bar then we want to return dont want to do all this work for nothing
        if(qrCodes == null || qrCodes.size() == 0){
            return;
        }
        // if the search bar is empty then just return and show all the qr codes
        if(searchCity == null || playerLocation == null){
            showAllQrs();
            return;
        }
        // will need these variables as
        Geocoder geocoder = new Geocoder(getContext()); // the geocoder will hold the Address of the qr code
        ArrayList<QrCode> filteredQrs = new ArrayList<>(); // this will show which ones you want to sort by
        
        // have to loop through all the qr codes because they are all in a list with unique addresses
        for(QrCode qrind : qrCodes){
            try{
                // this will get the address of the qr code
                List<Address> addresses = geocoder.getFromLocation(qrind.getLocation().getLatitude(), qrind.getLocation().getLongitude(), 1);
                // this will get the city of the address of the qr code
                String city = addresses.get(0).getLocality().toLowerCase();
                // if the city of the qr code is the same as the city that the user entered then add it to the list
                if(city.charAt(0) == searchCity.charAt(0)  && city.contains(searchCity)){
                    // will check if the first letter of the city is the same as the first letter of the search city
                    // this will hopefully prevent situations where the user enters "Edm" and it shows qrs in "monEdm"
                    filteredQrs.add(qrind);
                }
            }catch( IOException e){
                e.printStackTrace();
            }
        }
        // This is practically the return statement for the function
        qrCodeListAdapter.updateList(filteredQrs);
    }
    
    /**
     * The actions performed when the search button is pressed
     */
    private void searchButtonPressed() {
        String searchText = binding.searchQrRadius.getText().toString();
        if (searchText.equals("")) {
            showAllQrs();
            return;
        }
        searchRadius = Double.parseDouble(searchText);
        searchCity = searchText;
        // we want to make sure that the only time we go to this is when we have a valid number
        if(searchCity.contains("[a-zA-Z]+") == true && searchRadius == 0) {
            // will only enter this part of the code if the search bar contains letters and no numbers
            binding.searchResultHeading.setText( String.format(Locale.CANADA, "Within this city: %s", searchCity) );
            searchCity = searchCity.toLowerCase();
            updateQrListCity();
        }
        else{ // otherwise that means we have a string that does contain one of those
            binding.searchResultHeading.setText( String.format( Locale.CANADA, "Within %.1f meters", searchRadius ) );
            updateQrList();
        }
        
        
    }

    /**
     * Shows all the qrs available
     */
    private void showAllQrs() {
        qrCodeListAdapter.updateList(qrCodes);
        binding.searchResultHeading.setText("All QRs");
    }

    /**
     * Navigates to the selected qr
     * @param qrCode the qr code selected
     */
    private void navigateToSelectedQr(QrCode qrCode) {
        NearbyQrCodeListFragmentDirections.ActionNearbyQrListToQrCodePage action =
                NearbyQrCodeListFragmentDirections.actionNearbyQrListToQrCodePage(qrCode.getHash());
        Navigation.findNavController(getView()).navigate(action);
    }

}