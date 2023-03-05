package com.cmput301w23t47.canary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.viewmodel.QRCodeVMList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.type.LatLng;

import java.util.ArrayList;

/**
 * THIS IS THE MAP FRAGMENT
 * IT CAN BE ADD3ED TO THE MAP_ACTIVITY_SCREEN_MAP_AND_LIST
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_maps_interactive#newInstance} factory method to
 * create an instance of this fragment.
 *
 * Description
 * - THIS IS THE MAP FRAGMENT THAT WILL BE USED FOR THE INTERACTIVE MAP
 * - WILL NEED TO PASS THE LIST OF THINGS TO BE STORED TO THIS PLACE
 */
public class fragment_maps_interactive extends Fragment{

   ArrayList<LatLng> markers = new ArrayList<>();


   private OnMapReadyCallback callback = new OnMapReadyCallback() {
      
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
         //this makes a marker to be used //LatLng sydney = new LatLng(-34, 151);
         //this places the marker on the map //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         //this moves your camera to the position on the map //googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
      }
   };
   
   /**
    *
    * @param inflater The LayoutInflater object that can be used to inflate
    * any views in the fragment,
    *
    * @param container If non-null, this is the parent view that the fragment's
    * UI should be attached to.  The fragment should not add the view itself,
    * but this can be used to generate the LayoutParams of the view.
    * @param savedInstanceState If non-null, this fragment is being re-constructed
    * from a previous saved state as given here.
    *
    * @return view The View for the fragment's UI, or null.
    *
    * Description
    * This method is called to have the fragment instantiate its user interface view.
    * The fragment will initialize with the player location as the default location (marker)
    * Issue of the map not loading is resolved by using the SupportMapFragment instead of the
    * MapFragment.
    *
    * WILL NEED TO ADD THE OTHER PLAYER MARKERS LATER
    */
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
      
      //The bundle should hold the list of QR codes based on location
      // INITIALIZE VIEW
      View view = inflater.inflate(R.layout.fragment_maps_interactive, container, false);
      //initialize map fragment
      SupportMapFragment mapFragment =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
      // this leaves a location marker on the map at the players position
      //async map
      mapFragment.getMapAsync(new  OnMapReadyCallback() {
         @Override
         public void onMapReady(@NonNull GoogleMap googleMap) {

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
               @Override
               public void onMapClick(@NonNull LatLng latLng) {
                  //when clicke on map
                  //initialize marker options
                  MarkerOptions markerOptions = new MarkerOptions();
                  //set position of marker
                  markerOptions.position(latLng);
                  //set title of marker
                  markerOptions.title("Player Location");
                  //remova all marker
                  googleMap.clear();
                  //animating to zoom the marker
                  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                  //add marker on map
                  googleMap.addMarker(markerOptions);
               }
            });
         }
      }
      
      //return the view
      return view;
   }
   
   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      SupportMapFragment mapFragment =
            (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
      if ( mapFragment != null ) {
         mapFragment.getMapAsync(callback);
      }
   }
}