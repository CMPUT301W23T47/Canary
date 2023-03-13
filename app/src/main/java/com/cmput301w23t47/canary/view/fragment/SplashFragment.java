package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        firestorePlayerController.doesCurrentPlayerExist(this);
    }

    /**
     * Navigates to the home screen; The player exists
     */
    private void navigateToHome() {
        Navigation.findNavController(getView()).navigate(SplashFragmentDirections.actionSplashToHome());
    }

    /**
     * Navigates to the create profile page; The player does not exist
     */
    private void navigateToCreateProfile() {
        Navigation.findNavController(getView()).navigate(SplashFragmentDirections.actionSplashToCreateProfile());
    }

    /**
     * Determines whether the player exists
     * Navigates to home if player exists; createProfile page otherwise
     * @param status whether player exists
     */
    @Override
    public void operationStatus(boolean status) {
        Handler handler = new Handler();
        new Thread(() -> {
            View view = null;
            while (view == null) {
                // wait for the view to be non null
                view = getView();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
            handler.post(() -> {
                if (status) {
                    navigateToHome();
                } else {
                    navigateToCreateProfile();
                }
            });
        }).start();

    }
}