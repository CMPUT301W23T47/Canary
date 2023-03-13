package com.cmput301w23t47.canary.controller;

import static com.cmput301w23t47.canary.view.fragment.LeaderboardFragment.TAG;

import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageGenerator{

//    private PlayerQrCode currentPlayerQRCode;
//    private TextView currentQRCodeName;
//    private TextView currentQRCodeScore;
//    private TextView currentQRCodeScanDateTime;
//    private TextView currentQRCodeScanLocation;
//    private ImageView currentQRCodeImage;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_qr_code_view, container, false);
////        currentPlayerQRCode = getArguments().getParcelable("playerQRCode");
//        currentQRCodeName = view.findViewById(R.id.QRCodeName);
//        currentQRCodeScore = view.findViewById(R.id.QRCodeScore);
//        currentQRCodeScanLocation = view.findViewById(R.id.QRCardLocation);
//        currentQRCodeScanDateTime = view.findViewById(R.id.QRCodeScanDateTime);
//        currentQRCodeImage = view.findViewById(R.id.QRCodeImage);
//        updateFragmentData();
//        getImage(getString(R.string.random_image_generator));
//        return view;
//    }

    public RequestCreator getImage(String url) {
        Log.d(TAG, "getImage: " + url);
        return Picasso.get().load(url);

       // Picasso.get().load(url).into(currentQRCodeImage);
    }
}
