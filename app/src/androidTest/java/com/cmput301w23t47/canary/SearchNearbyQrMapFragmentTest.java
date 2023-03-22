package com.cmput301w23t47.canary;


import android.location.Location;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;



// NOte that this is a very flawed test because it is dependent on the database
// and also becuase I'm not certain it will even work
/**
 * This will test the search range of the qr code
 *
 * test that the list displays the correct qr codes
 * 		- in a default just loadded in
 * test that the map displays the correct qr codes
 * 		- by checking that both exist
 */
@RunWith(AndroidJUnit4.class)
public class SearchNearbyQrMapFragmentTest {
	// this can be copied and pasted into another fragment with search
	private Solo solo;
	
	//This will be for the firestore
	private FirebaseFirestore db;
	
	// sets the test mode to true
	// needed because dont want to run the tests on the official database
	static {FirestoreController.testMode=true;}
	
	
	//using the main activity because the player profile is built off of it so I assume that other fragments will be built off of it
	/**
	 * The rule
	 * Navigates to the activity we need to test on
	 * with this we can test the fragments
	 */
	@Rule
	public ActivityScenarioRule<MainActivity> rule =
			new ActivityScenarioRule<MainActivity>(MainActivity.class);
	
	//with this it should be able to test the fragments
	@Before
	public void setUp() throws Exception{
		Intents.init();
		rule.getScenario().onActivity(activity -> {
			solo = new Solo(InstrumentationRegistry.getInstrumentation(), activity);
		});
	}
	
	@Test
	public void startActivity() throws Exception{
		rule.getScenario().onActivity(activity -> {});
	}
	

	
	
	// This should be the default state of the fragmen
	
	/**
	 *  when first opening the fragment in test mode it should display a list of all the qr codes in the database
	 *  unless test mode changes that
	 */
	@Test
	public void testSearchRangeListNone() throws Exception{
		RecyclerView recyclerView = (RecyclerView ) solo.getView(R.id.qr_items_list);
		
		//one of these tests is right
		
		//make sure that the List is the size of the database of qr codes
		// using non test mode this can change so maybe count them
		db = FirebaseFirestore.getInstance();
		//get all of the current qr codes
		db.collection("QRCode").get().addOnCompleteListener(task -> {
			// chceck if we succesfulyl got them
			if (task.isSuccessful()) {
				// if we did then get the list of qr codes
				// in the form of the size of the list
				 int qrCodes = task.getResult().getDocuments().size();
				// make sure that the list is the same size as the database
				assert( qrCodes == recyclerView.getAdapter().getItemCount() );
			}
		});

		//////////////////////////
		
		//For the test qr code just use this
		// this might change as well but I think the database is pretty set on 4 qr codes
		//assert( 4 == recyclerView.getAdapter().getItemCount() );
		
	}
	
	
	/**
	 *  Check that the google map is displaying markers of the list of qr codes
	 *  If one works then the others should work
	 *  so this will only test the one marker
	 *
	 *  not much to test here because this is pretty much testing if one line of code works
	 */
	@Test
	public void testSearchRangeMapNone() throws Exception {
		// assert that the map view exists
		// click on the map
		View googlemap = solo.getView(R.id.map);
		assert( googlemap != null );
		
		// get the location from one of the recycler view items and then check if the map has a marker there
		// this will only test one marker
		// if one works then the others should work
		RecyclerView recyclerView = (RecyclerView ) solo.getView(R.id.qr_items_list);
		SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter) recyclerView.getAdapter();
		// get the location of the first item in the list
		Location locaQrCode = adapter.getItemAt( 0 ).getLocation();
		
		// make sure it holds a valid location
		assert( locaQrCode != null );

		
//		// then try to place it on a map
//		GoogleMap map = ((SupportMapFragment) searchNearbyQrMapFragment.getChildFragmentManager().findFragmentById(R.id.map)).getView();
//		// make sure that the map is not null
//		assert( map != null );
//
//		// place the locaqrcode on the map
//
//		// check if it has done this
	
	}
	
	@After
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
	
}
