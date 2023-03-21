package com.cmput301w23t47.canary;

import android.location.Location;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.util.FirestorePlayerTestUtil;
import com.cmput301w23t47.canary.view.activity.HomeActivity;
import com.cmput301w23t47.canary.view.fragment.SearchNearbyQrMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This will test the search range of the qr code
 * PLEASE NOTE THAT THIS WILL NOT TEST THE MAP
 * It will only test the List of Qrs
 */
@RunWith(AndroidJUnit4.class)
public class SearchNearbyQrMapFragmentTest {
	private Solo solo;
	
	
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
	
	@Test
	public void testSearchRangeListNone() {
		
		LatLng latLng = get;
		
		// gives the search_qr_radius the value of 1000 and clicks the search button
		RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list);
		solo.enterText( R.id.search_qr_radius, "0");
		//click on the assert button
		solo.clickOnButton("Search");
		
		assert(recyclerView.getAdapter().getItemCount() == 0);
		
		
	}
	
}
