package com.cmput301w23t47.canary.controller;

import android.location.Location;

import com.cmput301w23t47.canary.model.Qrcodem;

import java.util.ArrayList;

public class MapSearchRange {
	public ArrayList<Qrcodem> SearchList = new ArrayList<>();
	public ArrayList<Qrcodem> finalList = new ArrayList<>();
	
	public MapSearchRange(Location loca,ArrayList<Qrcodem> list, String range) {
		SearchList = list;
		// this should set the final list to the list of qr codes within the range
		int rangeint = 0;
		// this is valid because the range is guraranteed to be one of these
		switch(range){
			case "200m":
				rangeint = 200;
				break;
			case "500m":
				rangeint = 500;
				break;
			case "1km":
				rangeint = 1000;
				break;
			case "2km":
				rangeint = 2000;
				break;
			case "5km":
				rangeint = 5000;
				break;
			case "NO LIMIT":
				rangeint = 10000;
				break;
			default:
				rangeint = 100;
				break;
		}
		
		
		
		qrWithinRange( loca, SearchList, rangeint );
		
	}
	

	
	public void qrWithinRange(Location userLocation, ArrayList<Qrcodem> givenList, int SearchRange){
		Location qrLocation = new Location("");
		
		for(int i =0; i<givenList.size(); i++){
			qrLocation.setLatitude(givenList.get(i).getLocation().getLatitude());
			qrLocation.setLongitude(givenList.get(i).getLocation().getLongitude());
			
			if(userLocation.distanceTo(qrLocation) <= SearchRange){
				finalList.add(givenList.get(i));
			}
		}
		
		
	}
	
	
	public ArrayList<Qrcodem> getSearchList() {
		return SearchList;
	}
	public ArrayList<Qrcodem> getFinalList() {
		return finalList;
	}
}


////get the device position so that we can set up the search
//        if(mdevicePosition == null){
//				getLastLocation();
//				}
//
//
//				mSearchBarRange = (AutoCompleteTextView) getActivity().findViewById(R.id.map_search_range_dropdown_menu);
//				mSearchSpecifiedRange = mSearchBarRange.getText().toString();
//				mSearchResultsCopy.clear();
//
//				switch(mSearchSpecifiedRange){
//				case "200m":
//				mSearchRangeDouble = 200;
//				break;
//				case "500m":
//				mSearchRangeDouble = 500;
//				break;
//				case "1km":
//				mSearchRangeDouble = 1000;
//				break;
//				case "2km":
//				mSearchRangeDouble = 2000;
//				break;
//				case "5km":
//				mSearchRangeDouble = 5000;
//				break;
//				case "NO LIMIT":
//				mSearchRangeDouble = 10000;
//				break;
//default:
//		mSearchRangeDouble = 100;
//		break;
//		}
//
//
//		for(int i = 0; i < mglobalQRList.size(); i++){
//
//		LatituteQrCodeLocation = mglobalQRList.get(i).getLocation().getLatitude();
//		LongitudeQrCodeLocation = mglobalQRList.get(i).getLocation().getLongitude();
//		QRCodeLocation.setLatitude(LatituteQrCodeLocation);
//		QRCodeLocation.setLongitude(LongitudeQrCodeLocation);
//
//		distance = mdevicePosition.distanceTo(QRCodeLocation);
//		if(distance <= mSearchRangeDouble || mSearchRangeDouble == 10000){
//
//		mSearchResultsCopy.add(mglobalQRList.get(i));
//		//one of these isn't needed I dont know which one
//		mMapAdapterRecyclerViews.addQRCode(mglobalQRList.get(i));
//		// though I assume that it is this one
//		//mSearchResults.add(mglobalQRList.get(i));
//		}
//
//		}
//