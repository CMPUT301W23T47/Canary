package com.cmput301w23t47.canary;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
import com.cmput301w23t47.canary.view.fragment.NearbyQrCodeListFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * This is a test of the NearbyQrCodeListFragment
 * This will test that the search range is actually working
 * there is some potential issues with this test
 * but for now it is okay
 */
public class NearbyQrCodeListFragmentTest {
	
	private Solo solo;
	private FirebaseFirestore db;
	
	static {
		FirestoreController.testMode=true;}
	
	@Rule
	public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);
	
	@Before
	public void setUp() throws Exception{
		rule.getScenario().onActivity(activity -> {
			solo = new Solo( InstrumentationRegistry.getInstrumentation(), activity);
		});
	}
	@Test
	public void startActivity() throws Exception{
		rule.getScenario().onActivity(activity -> {
		});
	}
	
	
	@Test
	public void SearchRangeDefault() throws Exception {
		//get the recycler view
		RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list);
		//set the adapter
		SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter ) recyclerView.getAdapter();
		
		//check if the adapter is same size as list in database
		// this is the test case
		//assert(adapter.getItemCount() == 4);
		
		//this is for an actual copy ( ie using codes that players have scanned)
		
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
		
	}
	
	/**
	 * Test that the search range works
	 * do this by setting the search range to 0 and then checking that the list is empty
	 * this can possibly fail but only in cases where you are on top of the qr code
	 * @throws Exception
	 */
	@Test
	public void SearchRangeNoElements() throws Exception {
			rule.getScenario().onActivity(activity -> {
				//set the search range to 0
				//get the search range text box
				solo.clickOnView( solo.getView(R.id.search_qr_radius) );
				//enter 0
				solo.enterText( 0, "0" );
				//click search button
				solo.clickOnButton( "Search" );
				
				// Test that the list is empty
				
				//get the recycler view
				RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list);
				//set the adapter
				SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter ) recyclerView.getAdapter();
				//check if the adapter is same size as list in database
				// this is the test case
				assert(adapter.getItemCount() == 0);
			});
	}
	
	@After
	public void tearDown() throws Exception{
		solo.finishOpenedActivities();
	}
}
