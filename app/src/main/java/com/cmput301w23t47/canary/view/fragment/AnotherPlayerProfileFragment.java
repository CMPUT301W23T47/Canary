package com.cmput301w23t47.canary.view.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentAnotherPlayerProfileBinding;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerQrCode;
import com.cmput301w23t47.canary.view.adapter.QRCodeListAdapter;

import java.util.ArrayList;

/**
 * Another player Fragment
 */
public class AnotherPlayerProfileFragment extends Fragment implements
        GetPlayerCallback {
    public static final String TAG = "PlayerProfileFragment";

    private FragmentAnotherPlayerProfileBinding binding;
    private FirestorePlayerController firestorePlayerController = new FirestorePlayerController();
    private Player player;
    private QRCodeListAdapter qrCodeListAdapter;

    public AnotherPlayerProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnotherPlayerProfileBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Makes the request to firestore to get the player
     */
    private void makeFirestoreReqForPlayer() {
        // foreign player
        String playerDocId = AnotherPlayerProfileFragmentArgs.fromBundle(getArguments()).getPlayerId();
        firestorePlayerController.getCompleteForeignPlayer(playerDocId, this);
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

    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && player == null){
            makeFirestoreReqForPlayer();
            showLoadingBar();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        player = null;
        binding = null;
    }

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
        AnotherPlayerProfileFragmentDirections.ActionAnotherPlayerToQrPage action =
                AnotherPlayerProfileFragmentDirections.actionAnotherPlayerToQrPage(playerQrCode.retrieveHash());
        Navigation.findNavController(getView()).navigate(action);
    }
}