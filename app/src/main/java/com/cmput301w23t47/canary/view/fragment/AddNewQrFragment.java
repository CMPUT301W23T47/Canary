package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.databinding.FragmentAddNewQrBinding;
import com.cmput301w23t47.canary.view.contract.ScanQrContract;

/**
 * Represents a fragment for scanning a new QR code
 */
public class AddNewQrFragment extends Fragment {
    public static final String TAG = "AddNewQrFragment";

    private FragmentAddNewQrBinding binding;

    private ActivityResultLauncher<Object> qrActivityLauncher;

    String qrHash = null;

    /**
     * Initializes the view and creates a bundle object for this view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the binding object on which we can work
     */
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAddNewQrBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /**
     * Sets the view of the page and calls the function to show the UI of the page.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /**
     * Destroys a created view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Initializes the UI for this page.
     */
    void init() {
        // register contract for QR Activity
        qrActivityLauncher = registerForActivityResult(new ScanQrContract(),
                this::receivedQrCode);
        if (qrHash == null) {
            qrActivityLauncher.launch(null);
        }
//        binding.scanQrButton.setOnClickListener(view -> {
//            Navigation.findNavController(getView()).navigate(R.id.action_addNewQrFragment_to_blankFragment);
//        });
    }

    /**
     * Receives the QR Code scanned
     * @param qrCodeVal (String): The raw value of the scanned QR Code
     */
    private void receivedQrCode(String qrCodeVal) {
        if (qrCodeVal == null) {
            // value not received
            return;
        }
        // Get Preferences for QR
        qrHash = QrCodeController.getHashStrForQr(qrCodeVal);
        // pass the obtained hash to the capture preference fragment
        AddNewQrFragmentDirections.ActionQrCapturePreference action = AddNewQrFragmentDirections.actionQrCapturePreference(qrHash);
        Navigation.findNavController(getView()).navigate(action);
    }

}