package com.cmput301w23t47.canary.view.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
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
    private final String TAG = "NearbyQrCodeListFragment";
    
    // the list of qr codes
    private ArrayList<QrCode> qrCodes = new ArrayList<>();
    // the search radius in meters
    private double searchRadius = 0;

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
     * The actions performed when the search button is pressed
     */
    private void searchButtonPressed() {
        String searchText = binding.searchQrRadius.getText().toString();
        if (searchText.equals("")) {
            showAllQrs();
            return;
        }
        searchRadius = Double.parseDouble(searchText);
        binding.searchResultHeading.setText( String.format( Locale.CANADA, "Within %.1f meters", searchRadius ) );
        updateQrList();
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