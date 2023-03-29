package com.cmput301w23t47.canary;

import static androidx.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertTrue;

import android.location.Address;
import android.location.Geocoder;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cmput301w23t47.canary.MainActivity;
import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.model.QrCode;
import com.cmput301w23t47.canary.view.adapter.SearchNearbyQrListAdapter;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CitySearchQrSearchTest {
	private Solo solo;
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
	
	
	/**
	 * Test to see if the search function works when the full city name is entered
	 * @throws Exception
	 */
	@Test
	public void testSearchCityName() throws Exception {
		rule.getScenario().onActivity(activity -> {
					// click on the search button
					solo.clickOnView(solo.getView( R.id.search_qr_city));
					// enter a city name
					solo.enterText(0, "Edmonton");
					//click on the search button
					solo.clickOnButton("Search");
					//user is ound
					RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list_city);
					SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter) recyclerView.getAdapter();
					assert(adapter.getItemCount() > 0);
			
			
					Geocoder geocoder = new Geocoder( getContext() , Locale.getDefault()); // the geocoder will hold the Address of the qr code
					QrCode qrcode = adapter.getItemAt(0);
					String city = ""; // this will hold the city of the qr code
					List<Address> QrAddress; // this will hold the address of the qr code
					
					try {
						QrAddress = geocoder.getFromLocation(qrcode.getLocation().getLatitude(), qrcode.getLocation().getLongitude(), 1);
						city = QrAddress.get(0).getLocality().toLowerCase();
						assertTrue(city.contains("edm"));
						
					} catch ( IOException e ) {
						assert(false);
						throw new RuntimeException( e );
					}
				}
		
		);
	}
	
	/**
	 * Test to see if the search function works when a partial city name is entered
	 * @throws Exception
	 */
	@Test
	public void testSearchCityPartialInput(){
		rule.getScenario().onActivity( activity -> {
					// click on the search button
					solo.clickOnView(solo.getView( R.id.search_qr_city));
					// enter a city name
					solo.enterText(0, "Edm");
					//click on the search button
					solo.clickOnButton("Search");
					//user is ound
					RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list_city);
					SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter) recyclerView.getAdapter();
					assertTrue(adapter.getItemCount() > 0);
					
					Geocoder geocoder = new Geocoder( getContext() , Locale.getDefault()); // the geocoder will hold the Address of the qr code
					QrCode qrcode = adapter.getItemAt(0);
					String city = ""; // this will hold the city of the qr code
					List<Address> QrAddress; // this will hold the address of the qr code
					
					try {
						QrAddress = geocoder.getFromLocation(qrcode.getLocation().getLatitude(), qrcode.getLocation().getLongitude(), 1);
						city = QrAddress.get(0).getLocality().toLowerCase();
						assertTrue(city.charAt( 0 ) == 'e' && city.contains("edm"));
						
					} catch ( IOException e ) {
						assert(false);
						throw new RuntimeException( e );
					}
					
				}
		);
	}
	
	/**
	 * Test to see if the search function works when a city name that does not exist is entered
	 * @throws Exception
	 */
	@Test
	public void testSearchCityNameFail() throws Exception{
		rule.getScenario().onActivity( activity -> {
			// click on the search button
			solo.clickOnView(solo.getView( R.id.search_qr_city));
			// enter a city name
			solo.enterText(0, "asdfghjklqwetryuioxvzxcv");
			//click on the search button
			solo.clickOnButton("Search");
			//user is ound
			RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list_city);
			SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter) recyclerView.getAdapter();
			assert(adapter.getItemCount() == 0);
		}
		);
	}
	
	/**
	 * Test to see if the search function works when no city name is entered
	 * @throws Exception
	 */
	@Test
	public void testSearchCityNoInput() throws Exception{
		rule.getScenario().onActivity( activity -> {
			// click on the search button
			solo.clickOnView(solo.getView( R.id.search_qr_city));
			// enter a city name
			solo.enterText(0, "");
			//click on the search button
			solo.clickOnButton("Search");
			//user is ound
			RecyclerView recyclerView = (RecyclerView) solo.getView(R.id.search_result_list_city);
			SearchNearbyQrListAdapter adapter = (SearchNearbyQrListAdapter) recyclerView.getAdapter();
			assert(adapter.getItemCount() > 0);
		}
		);
	}
	
}
