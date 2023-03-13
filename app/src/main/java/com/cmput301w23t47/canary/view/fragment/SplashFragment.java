package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.OperationStatusCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;

/**
 * Fragment for showing the splash screen
 */
public class SplashFragment extends Fragment implements OperationStatusCallback {
    public static final String TAG = "SplashFragment";

    public SplashFragment() {}

    FirestorePlayerController firestorePlayerController = new FirestorePlayerController();

    /**
     *  Handles the layout of the activity, and called on activity creation.
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    /**
     * Defines what to do on resuming the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        firestorePlayerController.doesCurrentPlayerExist(this);
    }

    /**
     * Navigates to the home screen; The player exists
     */
    private void navigateToHome() {
        Navigation.findNavController(getView()).navigate(R.id.action_splashToHome);
    }

    /**
     * Navigates to the create profile page; The player does not exist
     */
    private void navigateToCreateProfile() {
        Navigation.findNavController(getView()).navigate(R.id.action_splashToCreateProfile);
    }

    /**
     * Determines whether the player exists
     * Navigates to home if player exists; createProfile page otherwise
     * @param status whether player exists
     */
    @Override
    public void operationStatus(boolean status) {
        if (status) {
            navigateToHome();
        } else {
            navigateToCreateProfile();
        }
    }
}