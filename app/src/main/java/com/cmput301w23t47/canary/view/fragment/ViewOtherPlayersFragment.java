package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.callback.GetPlayerListCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentPlayerOtherBinding;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.view.adapter.PlayerSearchAdapter;


import java.util.ArrayList;

/**
 * Fragment for viewing & searching other players
 */
public class ViewOtherPlayersFragment extends Fragment implements GetPlayerListCallback,
        GetIndexCallback{

    /**
     * Default Constructor
     */
    public ViewOtherPlayersFragment() {
    }

    FragmentPlayerOtherBinding binding;

    FirestorePlayerController firestorePlayerController = new FirestorePlayerController();
    PlayerSearchAdapter viewAdapter;
    ArrayList<Player> players = new ArrayList<>();

    private String qrHash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerOtherBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Initializes the UI
     */
    private void init(){
        qrHash = QRCodeViewFragmentArgs.fromBundle(getArguments()).getQrHash();
        viewAdapter = new PlayerSearchAdapter(players, this);
        binding.playersWithSameQrList.setAdapter(viewAdapter);
        binding.playersWithSameQrList.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.playersWithSameQrList.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_shape));
        binding.playersWithSameQrList.addItemDecoration(dividerItemDecoration);

        firestorePlayerController.otherPlayerWithSameQr(qrHash, this);
        showLoadingBar();
    }

    /**
     * Shows all the players on the list
     */
    private void showAllOtherPlayers() {
        binding.searchResultHeading.setText("Players with Same QR");
        viewAdapter.updateList(players);
    }

    /**
     * navigates to the profile of the selected player
     * @param player
     */
    private void navigateToSelectedPlayer(Player player) {
        ViewOtherPlayersFragmentDirections.ActionViewOtherPlayersFragmentToAnotherPlayerProfileFragment action =
                ViewOtherPlayersFragmentDirections.actionViewOtherPlayersFragmentToAnotherPlayerProfileFragment(player.getUniquePlayerId());
        Navigation.findNavController(getView()).navigate(action);
    }


    /**
     * Gets the player from firestore to show and also shows the loading bar
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && players == null){
            firestorePlayerController.otherPlayerWithSameQr(qrHash, this);
            showLoadingBar();
        }
    }

    /**
     * Callback for getting the list of players
     * @param players the list of players
     */
    @Override
    public void getPlayerList(ArrayList<Player> players) {
        this.players = players;
        updateOtherPlayerList();
    }

    /**
     * Updates the info on the UI for the player list
     */
    private void updateOtherPlayerList(){
        if (this.players == null){
            return;
        }
        showAllOtherPlayers();
        hideLoadingBar();
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
     * callback for determining which player card was clicked on the list
     * @param ind the requested index
     */
    @Override
    public void getIndex(int ind) {
        Player selPlayer = viewAdapter.getItemAt(ind);
        if (selPlayer != null){
            navigateToSelectedPlayer(selPlayer);
        }
    }
}
