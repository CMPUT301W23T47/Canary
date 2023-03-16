package com.cmput301w23t47.canary;

import androidx.test.rule.ActivityTestRule;

import com.cmput301w23t47.canary.util.FirestorePlayerTestUtil;
import com.robotium.solo.Solo;

import org.junit.Rule;
import org.junit.Test;

public class SearchNearbyQrMapFragment {
	private Solo solo;
	private FirestorePlayerTestUtil firestorePlayerTestUtil;
	
	@Rule
	public ActivityTestRule<SearchNearbyQrMapFragment> rule =
			new ActivityTestRule<>(SearchNearbyQrMapFragment.class, true, true);
	
	/**
	 * Test the camera view of the map fragment
	 * make sure that it is
	 */
	@Test
	public void setCameraViewTest(){
	
	}
}
