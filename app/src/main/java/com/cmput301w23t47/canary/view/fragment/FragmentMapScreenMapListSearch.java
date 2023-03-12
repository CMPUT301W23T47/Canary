//gotten from https://github.com/mitchtabian/Google-Maps-2018/blob/c61f6359b470bdb3bb9cd32de8102b15087d79dd/app/src/main/java/com/codingwithmitch/googlemaps2018/ui/UserListFragment.java#L142
package com.cmput301w23t47.canary.view.fragment;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import android.Manifest;
import android.location.Location;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.RecyclerViewInterface;
import com.cmput301w23t47.canary.model.Qrcodem;
import com.cmput301w23t47.canary.model.WorldQRLIST;
import com.cmput301w23t47.canary.view.adapter.Map_Adapter_RecyclerViews;
import com.cmput301w23t47.canary.view.adapter.ViewWeightAnimationWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static com.cmput301w23t47.canary.Constants.MAPVIEW_BUNDLE_KEY;
//User_list_fragment
/**
 *  A fragment that holds a mapview and a recyckler view
 *  The mapview is used to display the location of the qr codes
 *  The recycler view is used to display the list of qr codes
 *  The recycler view is also used to display the map results
 *
 *
 */
public class FragmentMapScreenMapListSearch extends Fragment implements OnMapReadyCallback, View.OnClickListener, RecyclerViewInterface{

    //constants
    private static final String TAG = "Map_Activity_Screen_map_and_list";
    private static final int MAP_LAYOUT_STATE_CONTRACTED =0;
      private static final int MAP_LAYOUT_STATE_EXPANDED =1;
      
    
    //base list
    //this is the list that holds all the qr codes
    private ArrayList<Qrcodem> mglobalQRList = new WorldQRLIST().getQrList();
    //this is the list that holds the qr codes that are displayed
    private ArrayList<Qrcodem> mSearchResults = new ArrayList<>();
    private final ArrayList<Qrcodem> mSearchResultsCopy = new ArrayList<>();

    
    // recylerviews and lists
    private RecyclerView mMapListRecyclerView;
    private Map_Adapter_RecyclerViews mMapAdapterRecyclerViews;
    private Map_Adapter_RecyclerViews.MapRecyclerClickListener mMapRecyclerClickListener;

    //search bar
    //private Location mUserLocation;
    private AutoCompleteTextView mSearchBar;
    private ArrayAdapter<String> mSearchRangeAdapter;
    private final String[] mSearchRange = {"200m", "500m", "1km", "2km", "5km", "NO LIMIT" };
    private double distance;
    private double LatituteQrCodeLocation;
    private double LongitudeQrCodeLocation;
    private Location QRCodeLocation;
    private int mSearchRangeDouble;
    private String mSearchSpecifiedRange;
    private AutoCompleteTextView mSearchBarRange;
    
    
    //Map
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    
    private LatLngBounds mMapBoundary;
    private Location mdevicePosition;
    //this is for the updates
    private final Handler mHandler = new Handler();
    private Runnable mRunnable;
    private RelativeLayout mMapContainer;
    private int mMapLayoutState = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    
    public static FragmentMapScreenMapListSearch newInstance() {
        return new FragmentMapScreenMapListSearch();
    }
    
    
    /**
     * This is the first method that is called when the fragment is created
     * it will get the basics started
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mdevicePosition = getUserPositionFromMain();
    
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    mdevicePosition = location;
                    Log.d(TAG, "onLocationResult: " + mdevicePosition);
                }
            }
        };
        
        if (getArguments() != null) {
            // this is supposed to be something else but I don't know what
            // I think it has something to do with our firestore
            mSearchResults = getArguments().getParcelableArrayList(getString(R.string.intent_recycler_list_map));
            getCurrentDevicePosition();
            initDropDownMenuItems();
            initMapSearchRangeCheck();
         
            
        }

    }
    
    
    //Change activity if item is clicked
    /**
     * This method is called when an item in the recycler view is clicked
     * should change the activity to the qr code view
     * @param position the position of the item that was clicked
     */
    @Override
    public void onItemClicked(int position){
        // Need to change the error with the qr activitiy
        Intent intent = new Intent(getActivity(), QRCodeViewFragment.class );
        
        // if you want to send the whole qr code then use this one
        intent.putExtra(getString(R.string.intent_qr_code), mSearchResults.get(position));
        
        //if you want to break up the intents into seperate data then use this one
        //intent.putExtra(getString(R.string.intent_qr_name), mSearchResults.get(position).getName());
        // intent.putExtra( getString(R.string.intent_qr_location), mSearchResults.get(position).getLocation());
        // intent.putExtra( getString(R.string.intent_qr_hash), mSearchResults.get(position).getQrHash());
       //  intent.putExtra( getString(R.string.intent_qr_score), mSearchResults.get(position).getScore());
        
        startActivity(intent);
    }

      /**
      * This is used to initialize the map , recycler view, dropdown menu
      * and allow realtime updates
      */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        //gets the userposition to being the found Location in the other class
        //mdevicePosition = getUserPositionFromMain();
        
        // sets the view
        View view = inflater.inflate(R.layout.fragment_map_screen_map_and_list, container, false);
        mMapListRecyclerView = view.findViewById(R.id.user_list_recycler_view);
        mMapView = view.findViewById(R.id.map_display);
        mMapContainer = view.findViewById(R.id.map_container);
        
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        
        view.findViewById( R.id.btn_full_screen_map).setOnClickListener(this);
        
        getCurrentDevicePosition();
        initMapListRecyclerView();
        initGoogleMap(savedInstanceState);
        
        
        return view;
    }



    /**
     * This is used to initialize the map
     * and allow realtime updates
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        //startUserLocationsRunnable();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }



    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    /**
     * used to destroy the map
     * when it is no longer needed likely on the exiting of the app
     * this is used to make sure that the map is not updating
     */
    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        //stopLocationUpdates();
    }

    /**
     * this is used to make sure that the map is not updating
     * when the app is not in use
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    
    //map methods
    
    /**
     * this is a way to bind the camera on the map
     * this is used to make sure that the camera when first opens
     * is centered on the user not elsewhere
     */
    private void setCameraView(){
        Log.d(TAG, "setCameraView------------------------------------------------------------------------------------");
    
        if(mdevicePosition == null){
            
            getCurrentDevicePosition();
        }
        Log.d(TAG, "setCameraView: setting camera position , lat: " + mdevicePosition.getLatitude() + " long: " + mdevicePosition.getLongitude() + "");
        double bottomBoundary = mdevicePosition.getLatitude() - .1;
        double leftBoundary = mdevicePosition.getLongitude() - .1;
        double topBoundary = mdevicePosition.getLatitude() + .1;
        double rightBoundary = mdevicePosition.getLongitude() + .1;
    
        mMapBoundary = new LatLngBounds(
              new LatLng(bottomBoundary, leftBoundary),
              new LatLng(topBoundary, rightBoundary)
        );
    
        mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }
    
    private void getCurrentDevicePosition() {
        Log.d(TAG, "getCurrentDevicePosition------------------------------------------------------------------------------------");
        // this is the conditional statement that checks if the user has given permission to use the location
        if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            Log.d( TAG, "getCurrentDevicePosition: PERMISSION GRANTED" );
            //initialize location manager
            LocationManager locationManager = ( LocationManager ) getActivity().getSystemService( Context.LOCATION_SERVICE );
            
            //check condition
            if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) || locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
                Log.d( TAG, "getCurrentDevicePosition: GPS is enabled" );
                //get last location
                mFusedLocationClient.getLastLocation().addOnCompleteListener( new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d( TAG, "onComplete: TASK STARTED");
                        Log.d( TAG, "onComplete: TASK SUCCESFUL" );
                        Location loca = ( Location ) task.getResult();
                        
                        if ( loca != null ) {
                            Log.d( TAG, "onComplete: location found" );
                            
                            //mdevicePosition = ( Location ) loca;
                            mdevicePosition.setLatitude( loca.getLatitude() );
                            mdevicePosition.setLongitude( loca.getLongitude() );
                            Log.d( TAG, "onComplete: latitude: " + mdevicePosition.getLatitude() );
                            Log.d( TAG, "onComplete: longitude: " + mdevicePosition.getLongitude() );
                        } else {
                            Log.d( TAG, "onComplete: current location is null" );
                            
                            LocationRequest locationRequest = new LocationRequest()
                                    .setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY )
                                    .setInterval( 10000 )
                                    .setFastestInterval( 1000 )
                                    .setNumUpdates( 1 );
                            
                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    
                                    Location loca1 = locationResult.getLastLocation();
                                    
                                    
                                    mdevicePosition.setLatitude( loca1.getLatitude() );
                                    mdevicePosition.setLongitude( loca1.getLongitude() );
                                    
                                    Log.d( TAG, "onComplete: latitude: " + mdevicePosition.getLatitude() );
                                    Log.d( TAG, "onComplete: longitude: " + mdevicePosition.getLongitude() );
                                }
                            };
                            if( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                                mFusedLocationClient.requestLocationUpdates( locationRequest, locationCallback, Looper.myLooper() );
                            }
                        }
                    }
                });
            }else{
                //when location service is not enable
                //open location setting
                startActivity( new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ).setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ) );
            }
        }else{
            //when location permission are not enable
            //request location permission
            ActivityCompat.requestPermissions( getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100 );
        }
        Log.d(TAG, "getCurrentDevicePosition: END");
        Log.d(TAG, "getCurrentDevicePosition: mdevicePosition: " + mdevicePosition + " || mdevicePosition.getLatitude(): " + mdevicePosition.getLatitude() + " || mdevicePosition.getLongitude(): " + mdevicePosition.getLongitude());
    }
    
    
    
      /**
      * this is used to set the user position
       * this is used to get the devices's Location
      */

//    private Location getUserPosition(){
//    //this will be implemented from the tutorial
//    }
    
    /**
     * this is where the map is set up
     * makes sure that we have all required permissions and then sets the map up
     * also places the markers on the map
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        
        //this is where to set the list of the users location
        for (int i = 0; i < mSearchResultsCopy.size(); i++) {
            map.addMarker(new MarkerOptions()
                  .position(new LatLng(mSearchResultsCopy.get(i).getLocation().getLatitude(), mSearchResultsCopy.get(i).getLocation().getLongitude()))
                  .title( mSearchResultsCopy.get(i).getScoreString() ));
            
        }
        
        
        mGoogleMap = map;
        getCurrentDevicePosition();
        setCameraView();
    }
    
    /**
     * Saves the state of the map when the activity is paused.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        
        mMapView.onSaveInstanceState(mapViewBundle);
    }
    
    
    //init methods
    /**
     * This method is used to initialize the drop down menu items
     */
    public void initMapSearchRangeCheck(){
        //get the device position so that we can set up the search
        if(mdevicePosition == null)
        getCurrentDevicePosition();
        
        mglobalQRList = new WorldQRLIST().getQrList();
        mSearchBarRange = (AutoCompleteTextView) getActivity().findViewById(R.id.map_search_range_dropdown_menu);
        mSearchSpecifiedRange = mSearchBarRange.getText().toString();
        mSearchResultsCopy.clear();
        
        switch(mSearchSpecifiedRange){
            case "200m":
                mSearchRangeDouble = 200;
                break;
            case "500m":
                mSearchRangeDouble = 500;
                break;
            case "1km":
                mSearchRangeDouble = 1000;
                break;
            case "2km":
                mSearchRangeDouble = 2000;
                break;
            case "5km":
                mSearchRangeDouble = 5000;
                break;
            case "NO LIMIT":
                mSearchRangeDouble = 10000;
                break;
            default:
                mSearchRangeDouble = 100;
                break;
        }
        
        
        for(int i = 0; i < mglobalQRList.size(); i++){
            
            LatituteQrCodeLocation = mglobalQRList.get(i).getLocation().getLatitude();
            LongitudeQrCodeLocation = mglobalQRList.get(i).getLocation().getLongitude();
            QRCodeLocation.setLatitude(LatituteQrCodeLocation);
            QRCodeLocation.setLongitude(LongitudeQrCodeLocation);
            
            distance = mdevicePosition.distanceTo(QRCodeLocation);
            if(distance <= mSearchRangeDouble || mSearchRangeDouble == 10000){
                
                mSearchResultsCopy.add(mglobalQRList.get(i));
                //one of these isn't needed I dont know which one
                mMapAdapterRecyclerViews.addQRCode(mglobalQRList.get(i));
                // though I assume that it is this one
                //mSearchResults.add(mglobalQRList.get(i));
            }
            
        }
        
    }
    /**
     * Initialize the recycler view that will display the list of qr codes
     * this will need to be changed to display the list of qr codes based on search range
     */
    private void initMapListRecyclerView() {
        mMapAdapterRecyclerViews = new Map_Adapter_RecyclerViews(mSearchResults,this);
        mMapListRecyclerView.setAdapter(mMapAdapterRecyclerViews);
        mMapListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    
    /**
     * Initialize the google map on the screen
     * @param savedInstanceState
     */
    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        
        mMapView.onCreate(mapViewBundle);
        
        mMapView.getMapAsync(this);
    }
    
    /**
     * Show a dropdown menu of the search range options
     * allows the user to select a range
     */
    public void initDropDownMenuItems(){
        mSearchBar = (AutoCompleteTextView) getActivity().findViewById(R.id.map_search_range_dropdown_menu);
        mSearchRangeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSearchRange);
        
        mSearchBar.setAdapter(mSearchRangeAdapter);
        mSearchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    

    //change the size of the map
    
    
    /**
     * this method is called whenever the user clicks on the button to expand or contract the map
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //did you click on the button to make it full screen
            case R.id.btn_full_screen_map:
                if(mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED){
                    expandMapAnimation();
                    mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                }else{
                    contractMapAnimation();
                    mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                }
                break;
                
        }
    }
    
    /**
     * This method expands the map and contracts the recycler view
     * so that instead of the map and recycler sharing a screen the map is full screen
     */
    private void expandMapAnimation(){
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
              "weight",
              55,
              108);
        mapAnimation.setDuration(800);
    
        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mMapListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
              "weight",
              35,
              0);
        recyclerAnimation.setDuration(800);
    
        recyclerAnimation.start();
        mapAnimation.start();
    }
    
    /**
     * This method contracts the map and expands the recycler view
     * so that instead of the map being full screen it and the recycler view share a screen
     */
    private void contractMapAnimation(){
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
              "weight",
              108,
              55);
        mapAnimation.setDuration(800);
        
        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(mMapListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
              "weight",
              0,
              35);
        recyclerAnimation.setDuration(800);
        
        recyclerAnimation.start();
        mapAnimation.start();
    }
    
    

}



//This is old code that can be deleted when being merged with the main branch

//    public Location getUserPositionFromMain(){
//        MainActivity activity = (MainActivity) getActivity();
//        return activity.getUserLocation();
//    }


// Real Time Updates

//update the location every 5 seconds
//private static final int LOCATION_UPDATE_INTERVAL = 5000;
/**
 * starts the runnable that retrieves the users location
 * ie with this on the map will update your location as you move
 */
//    private void startUserLocationsRunnable(){
//        Log.d(TAG, "startUserLocationsRunnable: starting runnable for retrieving updated locations.");
//        mHandler.postDelayed(mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                // this code might not need to be here but it is a good idea to have it here
//                // this will take the qr locations and update them ( I think)
//                initMapSearchRangeCheck();
//                retrieveUserLocation();
//                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
//            }
//        }, LOCATION_UPDATE_INTERVAL);
//    }
//
//    private void retrieveUserLocation(){
//        Log.d( TAG, "retrieveUsserLocation: called." );
//
//
//        if ( ActivityCompat.checkSelfPermission( FragmentMapScreenMapListSearch.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//            Log.d(TAG, "retrieveUserLocation: permission not granted");
//            return;
//        }
//
//        //code wont work if the fused location client is null
//        if(mFusedLocationClient != null) {
//            //does as expected which is get the last known location
//            mFusedLocationClient.getLastLocation().addOnCompleteListener( new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if ( task.isSuccessful() ) {
//                        Location location = task.getResult();
//                        mdevicePosition.setGeoPoint( new GeoPoint( location.getLatitude(), location.getLongitude() ) );
//                        mdevicePosition.setTimestamp( null );
//                        //savePlayerLocation();
//                    }
//                }
//            } );
//        }
//        else{
//            Log.d(TAG, "retrieveUserLocation: mFusedLocationClient is null");
//        }
//
//    }
//
//    /**
//     * stops the runnable that retrieves the users location
//     * ie with this off you could walk to the end of the country but the map wouldnt update your location
//     */
//    private void stopLocationUpdates(){
//        mHandler.removeCallbacks(mRunnable);
//    }
//