package com.cmput301w23t47.canary;

import androidx.fragment.app.Fragment;

import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.view.fragment.PlayerProfileFragment;

import org.junit.Test;

public class PlayerProfileTest {
	
	@Test
	public void testPlayerProfile() {
	
		// Write a test to verify that the player has a profile page that displays their username, score, etc
		// Write a test to verify that the player can edit their profile page
		
		String username = "officialTester";
		String firstName = "testy";
		String lastName = "tester";
		int score = 100;
		
		Player player = new Player(username, firstName, lastName);
		
		Fragment fragment = new PlayerProfileFragment().newInstance();
		
		fragment
		
		
		
	}
}
