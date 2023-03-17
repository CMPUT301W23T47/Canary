package com.cmput301w23t47.canary;

import androidx.test.rule.ActivityTestRule;

import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.util.FirestorePlayerTestUtil;
import com.cmput301w23t47.canary.view.activity.HomeActivity;
import com.robotium.solo.Solo;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * This will test the search range of the qr code
 * PLEASE NOTE THAT THIS WILL NOT TEST THE MAP
 * It will only test the List of Qrs
 */
public class SearchNearbyQrMapFragmentTest {
	private Solo solo;
	private static FirestorePlayerTestUtil firestorePlayerTestUtil;
	private static Player testPlayer = null;
	
	
	@Rule
	public ActivityTestRule<HomeActivity> rule =
			new ActivityTestRule<>(HomeActivity.class, true, true);
	
	@BeforeClass
	public static void beforeAll() {
		FirestoreController.switchToTestMode();
		firestorePlayerTestUtil = new FirestorePlayerTestUtil();
		testPlayer = firestorePlayerTestUtil.getTestPlayer();
	}
	

}
