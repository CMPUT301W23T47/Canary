package com.cmput301w23t47.canary.view.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.amulyakhare.textdrawable.TextDrawable;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentPlayerProfileBinding;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.view.adapter.QRCodeListAdapter;

import java.util.ArrayList;

/**
 * The fragment for the profile page of the player
 */
public class PlayerProfileFragment extends Fragment implements
        GetPlayerCallback {

    public static final String TAG = "PlayerProfileFragment";

    private FragmentPlayerProfileBinding binding;
    private FirestorePlayerController firestorePlayerController = new FirestorePlayerController();
    private Player player;
    private QRCodeListAdapter qrCodeListAdapter;
    private static final String progressBarTitle = "Loading Player Profile";
    private static final String progressBarMessage = "Should take only a moment...";

    /**
     * Required empty public constructor.
     */
    public PlayerProfileFragment() {}

    /**
     * Creates a new PlayerProfileFragment and returns it
     * @return fragment (PlayerProfileFragment): a new fragment created to work on
     */
    public static PlayerProfileFragment newInstance() {
        PlayerProfileFragment fragment = new PlayerProfileFragment();
        return fragment;
    }

    /**
     * Handles the layout of the activity, and called on activity creation.
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
        binding = FragmentPlayerProfileBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Makes the request to firestore to get the player
     */
    private void makeFirestoreReqForPlayer() {
        firestorePlayerController.getCompleteCurrentPlayer(this);
    }

    /**
     * Initializes the ui for the page
     */
    private void init(){
        showLoadingBar();
        makeFirestoreReqForPlayer();
        qrCodeListAdapter = new QRCodeListAdapter(getContext(), new ArrayList<>());
        binding.qrsScannedList.setAdapter(qrCodeListAdapter);
        binding.qrsScannedList.setOnItemClickListener((adapterView, view, i, l) -> {
            PlayerQrCode playerQrCode = (PlayerQrCode) adapterView.getItemAtPosition(i);
            navigateToSelectedQr(playerQrCode);
        });
    }

    /**
     * Gets the player from firestore to show and also shows the loading bar
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && player == null){
            makeFirestoreReqForPlayer();
            showLoadingBar();
        }
    }

    /**
     * Destroys a created view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player = null;
        binding = null;
    }

    /**
     * Updates the image of a player given
     * @param player Player whose image is to be updated
     */
    private void updatePlayerImage(Player player) {
        Bitmap playerImage = player.getPlayerImage();
        if (playerImage != null) binding.playerImage.setImageBitmap(playerImage);
        else {
            String firstLetter = String.valueOf(player.getFirstName().charAt(0)).toUpperCase();
            binding.playerImage.setImageDrawable(null);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(firstLetter, Color.BLACK);
            binding.playerImage.setImageDrawable(drawable);
        }
    }

    /**
     * updates all the details of the player from the firestore and
     * displays on the screen
     */
    private void updateView() {
        if (player == null) {
            return;
        }
        updatePlayerImage(this.player);
        binding.playerUsername.setText(player.getUsername());
        binding.playerScore.setText(Long.toString(player.getScore()));
        binding.playerQrsScanned.setText(Integer.toString(player.getQrCodes().size()));
        binding.highestQrScore.setText(Long.toString(player.getHighestQr()));
        binding.lowestQrScore.setText(Long.toString(player.getLowestQr()));
        qrCodeListAdapter.setQrList(this.player.getQrCodes());
        qrCodeListAdapter.notifyDataSetChanged();
        hideLoadingBar();
    }

    /**
     * Assigns a player and updates the screen
     * @param player
     */
    @Override
    public void getPlayer(Player player) {
        this.player = player;
        updateView();
    }

    /**
     * Shows the loading bar
     */
    private void showLoadingBar() {
        binding.progressBarBox.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the loading bar
     */
    private void hideLoadingBar() {
        binding.progressBarBox.setVisibility(View.GONE);
    }

    /**
     * Navigates to the selected qr
     * @param playerQrCode the selected qr code
     */
    private void navigateToSelectedQr(PlayerQrCode playerQrCode) {
        PlayerProfileFragmentDirections.ActionQrCodeViewFromPlayerProfile action =
                PlayerProfileFragmentDirections.actionQrCodeViewFromPlayerProfile(playerQrCode.retrieveHash());
        action.setOwner(true);
//        action.setOwner(true);
        Navigation.findNavController(getView()).navigate(action);
    }
}