package com.cmput301w23t47.canary;

import static org.junit.Assert.assertEquals;

import android.location.Location;

import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.QrCode;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/*
This Test should test the Map list view and the search range
ie make sure that the range test actually works

 */
public class SearchRangeTest {
	
	//test the search range when there is no range
	
	/**
	 * Test the search range without a limit
	 * without a limit all the qr codes should be in a list of qr codes
	 * that should be returned
	 *
	 * Either can do this naturally or will have to make the search range so high it is almost infinity
	 * in which case this test is kind of pointless
	 */
	@Test
	public void testSearchRangeNoLimit() {
		//this is a placeholder just in case we need to know the range
		int PlaceHolder = 100000;
		
		//initialize a location
		Location location = new Location("test");
		location.setLatitude( 52.5232 );
		location.setLongitude( -112.5263 );
		
		Location locaSearch = location;
		
		//change the locations and give each qr its own sepearte location
		location.setLatitude( 53.5232 );
		location.setLongitude( -113.5263 );
		QrCode qrCode1 = new QrCode("hash1", 0, location, "QrCode1", null);
		location.setLatitude( 53.0 );
		location.setLongitude( -113.0 );
		QrCode qrCode2 = new QrCode("hash2", 0, location, "QrCode2", null);
		location.setLatitude( 52.0 );
		location.setLongitude( -114.0 );
		QrCode qrCode3 = new QrCode("hash3", 0, location, "QrCode3", null);
		
		
		// so this initializes a starting list of QrCodes now check if the search range works
		ArrayList<QrCode> StartingList = new ArrayList<>();
		StartingList.add( qrCode1 );
		StartingList.add( qrCode2 );
		StartingList.add( qrCode3 );
		
		ArrayList<QrCode> FinalList = new ArrayList<>();
		
		double searchDistance;
		// run through each element and determine if their distance
		for(int i = 0; i< StartingList.size(); i++){
			searchDistance = StartingList.get(i).getLocation().distanceTo(locaSearch);
			if(searchDistance <= PlaceHolder){
				FinalList.add(StartingList.get(i));
			}
		}
		//Use the search range to find the QrCodes within the player range
	
		assertEquals(3, FinalList.size());
	
	}
	
	@Test
	public void testSearchRangeLimit() {
		//this is a placeholder just in case we need to know the range
		int PlaceHolder = 10000;
		
		//initialize a location
		Location location = new Location("test");
		location.setLatitude( 52.5232 );
		location.setLongitude( -112.5263 );
		
		Location locaSearch = location;
		
		//change the locations and give each qr its own sepearte location
		location.setLatitude( 53.5232 );
		location.setLongitude( -113.5263 );
		QrCode qrCode1 = new QrCode("hash1", 0, location, "QrCode1", null);
		location.setLatitude( 53.0 );
		location.setLongitude( -113.0 );
		QrCode qrCode2 = new QrCode("hash2", 0, location, "QrCode2", null);
		location.setLatitude( 10.0 );
		location.setLongitude( -1.0 );
		QrCode qrCode3 = new QrCode("hash3", 0, location, "QrCode3", null);
		
		
		// so this initializes a starting list of QrCodes now check if the search range works
		ArrayList<QrCode> StartingList = new ArrayList<>();
		StartingList.add( qrCode1 );
		StartingList.add( qrCode2 );
		StartingList.add( qrCode3 );
		
		ArrayList<QrCode> FinalList = new ArrayList<>();
		
		double searchDistance;
		// run through each element and determine if their distance
		for(int i = 0; i< StartingList.size(); i++){
			searchDistance = StartingList.get(i).getLocation().distanceTo(locaSearch);
			if(searchDistance <= PlaceHolder){
				FinalList.add(StartingList.get(i));
			}
		}
		//Use the search range to find the QrCodes within the player range
		
		assertEquals(2, FinalList.size());
	}
	
	@Test
	public void testSearchRangeOutOfLimit(){
		//this is a placeholder to know the range without calling the range from the location
		int PlaceHolder = 100;
		
		//initialize a location
		Location location = new Location("test");
		location.setLatitude( 52.5232 );
		location.setLongitude( -112.5263 );
		
		Location locaSearch = location;
		
		//change the locations and give each qr its own sepearte location
		location.setLatitude( 53.5232 );
		location.setLongitude( -113.5263 );
		QrCode qrCode1 = new QrCode("hash1", 0, location, "QrCode1", null);
		location.setLatitude( 53.0 );
		location.setLongitude( -113.0 );
		QrCode qrCode2 = new QrCode("hash2", 0, location, "QrCode2", null);
		location.setLatitude( 52.0 );
		location.setLongitude( -114.0 );
		QrCode qrCode3 = new QrCode("hash3", 0, location, "QrCode3", null);
		
		
		// so this initializes a starting list of QrCodes now check if the search range works
		ArrayList<QrCode> StartingList = new ArrayList<>();
		StartingList.add( qrCode1 );
		StartingList.add( qrCode2 );
		StartingList.add( qrCode3 );
		
		ArrayList<QrCode> FinalList = new ArrayList<>();
		
		double searchDistance;
		// run through each element and determine if their distance
		for(int i = 0; i< StartingList.size(); i++){
			searchDistance = StartingList.get(i).getLocation().distanceTo(locaSearch);
			if(searchDistance <= PlaceHolder){
				FinalList.add(StartingList.get(i));
			}
		}
		//Use the search range to find the QrCodes within the player range
		
		assertEquals(0, FinalList.size());
	}
	
	
}
