package com.cmput301w23t47.canary.view.fragment;
		
		import android.location.Address;
		import android.location.Geocoder;
		import android.location.Location;
		import android.os.Bundle;
		import android.util.Log;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.view.ViewGroup;
		
		import androidx.annotation.NonNull;
		import androidx.annotation.Nullable;
		import androidx.core.content.ContextCompat;
		import androidx.navigation.Navigation;
		import androidx.recyclerview.widget.DividerItemDecoration;
		
		import com.cmput301w23t47.canary.R;
		import com.cmput301w23t47.canary.callback.GetIndexCallback;
		import com.cmput301w23t47.canary.callback.GetQrListCallback;
		import com.cmput301w23t47.canary.controller.FirestoreQrController;
		import com.cmput301w23t47.canary.controller.QrCodeController;
		import com.cmput301w23t47.canary.databinding.FragmentCitySearchQrCodeListBinding;
		import com.cmput301w23t47.canary.databinding.FragmentNearbyQrCodeListBinding;
		import com.cmput301w23t47.canary.model.QrCode;
		import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
		import com.google.android.gms.location.LocationServices;
		
		import java.io.IOException;
		import java.util.ArrayList;
		import java.util.List;
		import java.util.Locale;

public class CitySearchQrCodeListFragment extends LocationBaseFragment implements GetQrListCallback,
		GetIndexCallback {
	
	private final String TAG = "NearbyQrCodeListFragment";
	
	// the list of qr codes
	private ArrayList<QrCode> qrCodes = new ArrayList<>();
	// the search radius in meters
	private String searchCity;
	private FragmentCitySearchQrCodeListBinding binding;
	private FirestoreQrController firestoreQrController;
	private SearchNearbyQrListAdapter qrCodeListAdapter;
	
	public CitySearchQrCodeListFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		binding = FragmentCitySearchQrCodeListBinding.inflate(inflater, container, false);
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
		//do we need to know the player location?
		fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
		firestoreQrController = new FirestoreQrController();
		qrCodeRecyclerViewInit();
		
		firestoreQrController.getAllQrs(this);
		showLoadingBar();
		askPermissions();
		//playerLocation = new Location("");
		binding.searchQrButtonCity.setOnClickListener(view -> {
			searchButtonPressed();
		});
	}
	
	/**
	 * Initializes the qrCode recycler view
	 */
	private void qrCodeRecyclerViewInit() {
		qrCodeListAdapter = new SearchNearbyQrListAdapter(qrCodes, this);
		binding.searchResultListCity.setAdapter(qrCodeListAdapter);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.searchResultListCity.getContext(),
				DividerItemDecoration.VERTICAL);
		dividerItemDecoration.setDrawable( ContextCompat.getDrawable(getContext(), R.drawable.divider_shape));
		binding.searchResultListCity.addItemDecoration(dividerItemDecoration);
		binding.searchResultListCity.setHasFixedSize(true);
	}
	
	/**
	 * Update the location on the view
	 * The player location is already set to valid value
	 */
	@Override
	protected void updateLocation() {
		// I dont think we need to do anything here becuase what city gets searched for doesn't matter if
		// the player is moving
		//updateQrList();
	}
	
	@Override
	protected void locationPermissionGranted() {
		getLocationRequest();
	}
	
	@Override
	protected void locationPermissionNotGranted() {
	
	}
	
	/**
	 * This method is called when the search button is pressed
	 * it will then get the city name from the search bar
	 * and then update the qr list to show all the qr codes that are in that city
	 */
	@Override
	public void getQrList(ArrayList<QrCode> qrCodes) {
		this.qrCodes = qrCodes;
		hideLoadingBar();
		updateQrList();
	}
	
	/**
	 * gets the qr code at the requested index
	 * from the qr code list adapter
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
	 * Updates the qr list based on the information in the search bar
	 * this is for when the user enters a city name or part of a city name
	 * ie "Edmonton" or "Edm"
	 * this will then search for all the qr codes that are in the city of Edmonton
	 * or all cities that start with Edm
	 * this will then update the list to show all the qr codes that are in that city
	 */
	private void updateQrList(){
		// if nothing is in the search bar then we want to return dont want to do all this work for nothing
		if(qrCodes == null || qrCodes.size() == 0){
			return;
		}
		// if the search bar is empty then just return and show all the qr codes
		// dont think we need to keep track of player location but just in case
		if(searchCity == null ){
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
				String message = "Error: " + e.getMessage();
				Log.d( TAG, message);
				e.printStackTrace();
			}
		}
		// This is practically the return statement for the function
		// this will update the qr list to show all the qr codes that are in the specified city list
		qrCodeListAdapter.updateList(filteredQrs);
	}
	
	/**
	 * The actions performed when the search button is pressed
	 * this will get the text from the search bar
	 * and then update the qr list to show all the qr codes that are in that city
	 */
	private void searchButtonPressed() {
		String searchText = binding.searchQrCity.getText().toString();
		if (searchText.equals("")) {
			showAllQrs();
			return;
		}
		// this will set the search city to the text that the user entered
		searchCity = searchText;
		// this will update the heading to show the city that the user entered
		binding.searchResultHeadingCity.setText( String.format( Locale.CANADA, "Within this city: %s", searchCity) );
		// this will make the search city all lowercase so that we can have a more consistent search
		searchCity = searchCity.toLowerCase();
		// this will update the qr list to show all the qr codes that are in the city that the user entered
		updateQrList();
		
		
		
	}
	
	/**
	 * Shows all the qrs that are available
	 * should act like a reset where you can see all the qrs again
	 */
	private void showAllQrs() {
		qrCodeListAdapter.updateList(qrCodes);
		binding.searchResultHeadingCity.setText("All QRs");
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
