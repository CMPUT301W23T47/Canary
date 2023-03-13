package com.cmput301w23t47.canary;

import android.location.Location;
import android.util.Log;

import com.cmput301w23t47.canary.controller.MapSearchRange;
import com.cmput301w23t47.canary.model.Qrcodem;

import org.junit.Test;

import java.util.ArrayList;

public class SearchRangeTest {
	@Test
	public void testSearchingQrCodeRangeAllCases() {
		Location location = new Location("");
		// start making the location
		location.setLatitude( 10.0 );
		location.setLongitude( 10.0 );
		ArrayList<Qrcodem> SearchList = new ArrayList<>();
		ArrayList<Qrcodem> FinalList;
		
		// make a list of qr codes that can be searched
		for(int i=0; i<100; i++){
			
			Qrcodem qr = new Qrcodem();
			Location qrLocation = new Location("");
			qrLocation.setLatitude( 10.0 + ( ((double)i) /10000) );
			qrLocation.setLongitude( 10.0 + ( ((double)i) /10000) );
			qr.setLocation( qrLocation );
			SearchList.add( qr );
		}
		
		//Test the No limit range
		// this should return a range of 100
		MapSearchRange mapSearchRange = new MapSearchRange( location, SearchList, "NO LIMIT" );
		FinalList = mapSearchRange.getFinalList();
		assert(FinalList.size() == 100);
		
		
		FinalList.clear();
		
		//Test A range where all of them are guarranteed to be out of range
		mapSearchRange.qrWithinRange( location, SearchList, 0 );
		FinalList = mapSearchRange.getFinalList();
		Log.d("test", "FinalList size: " + FinalList.size());
		assert(FinalList.size() == 0);
		
		//clear just in case
		if(FinalList.size() != 0)
			FinalList.clear();
		
		//Test A range where all of them are guarranteed to be in range
		// the way we will do this is similar to the above but check before being added to the list
		ArrayList<Qrcodem> FinalList2 = new ArrayList<>();
		
		for(int i=0; i<SearchList.size(); i++){
			Location qrLocation = SearchList.get(i).getLocation();
			float distance = location.distanceTo( qrLocation );
			if(distance <= 1000){
				FinalList2.add( SearchList.get(i) );
			}
		}
		
		mapSearchRange.qrWithinRange( location, SearchList, 1000 );
		FinalList = mapSearchRange.getFinalList();
		assert(FinalList.size() == FinalList2.size());
	}
}
