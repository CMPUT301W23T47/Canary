package com.cmput301w23t47.canary.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.callback.GetPlayerListCallback;
import com.cmput301w23t47.canary.controller.FirestorePlayerController;
import com.cmput301w23t47.canary.databinding.FragmentPlayerSearchBinding;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.view.adapter.PlayerSearchAdapter;

import java.util.ArrayList;

/**
 * The fragment for searching the player
 */
public class PlayerSearchFragment extends Fragment implements GetPlayerListCallback,
        GetIndexCallback {
    /**
     * Required empty public constructor.
     */
    public PlayerSearchFragment() {
    }

    FragmentPlayerSearchBinding binding;

    FirestorePlayerController firestorePlayerController = new FirestorePlayerController();
    PlayerSearchAdapter searchAdapter;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Player> filteredPlayers = new ArrayList<>();

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
        binding = FragmentPlayerSearchBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Initializes the view
     */
    private void init() {
        searchAdapter = new PlayerSearchAdapter(players, this);
        binding.searchResultList.setAdapter(searchAdapter);
        binding.searchResultList.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.searchResultList.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_shape));
        binding.searchResultList.addItemDecoration(dividerItemDecoration);
        setOnClickListeners();
        firestorePlayerController.getListOfPlayers(this);
        showLoadingBar();
    }

    /**
     * Sets the click listeners to the views
     */
    private void setOnClickListeners() {
        binding.searchResultList.setOnClickListener(view -> {
            Log.d("TAG", "setOnClickListeners: Clicked");
            int itemPos = binding.searchResultList.getChildLayoutPosition(view);
            Player selectedPlayer = searchAdapter.getItemAt(itemPos);
            if (selectedPlayer != null) {
                navigateToSelectedPlayer(selectedPlayer);
            }
        });

        binding.searchPlayerButton.setOnClickListener(view -> {
            searchPlayerList(binding.searchPlayerText.getText().toString());
        });
    }

    /**
     * Shows all the players
     */
    private void showAllPlayers() {
        binding.searchResultHeading.setText("All Players");
        searchAdapter.updateList(players);
    }

    /**
     * Searches the player list and updates the view
     */
    private void searchPlayerList(String searchText) {
        if (searchText.equals("")) {
            showAllPlayers();
            return;
        }
        filteredPlayers.clear();
        for (Player player : players) {
            if (player.getUsername().contains(searchText)) {
                filteredPlayers.add(player);
            }
        }
        binding.searchResultHeading.setText(searchText);
        binding.searchPlayerText.setText("");
        searchAdapter.updateList(filteredPlayers);
    }

    /**
     * Navigates to the selected player
     */
    private void navigateToSelectedPlayer(Player player) {
        PlayerSearchFragmentDirections.ActionAllPlayersToOtherPlayerProfile action =
                PlayerSearchFragmentDirections.actionAllPlayersToOtherPlayerProfile(player.getUniquePlayerId());
        Navigation.findNavController(getView()).navigate(action);
    }

    /**
     * Gets the player from firestore to show and also shows the loading bar
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden){
        if(!hidden && players == null){
            firestorePlayerController.getListOfPlayers(this);
            showLoadingBar();
        }
    }

    /**
     * Gets the players list when they are available
     * @param players the list of players
     */
    @Override
    public void getPlayerList(ArrayList<Player> players) {
        this.players = players;
        filteredPlayers.clear();
        filteredPlayers.addAll(players);
        updatePlayersList();
    }

    /**
     * Update the view of the players list
     */
    private void updatePlayersList() {
        if (this.players == null) {
            return;
        }
        showAllPlayers();
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
     * Takes to the selected player at the given index number
     * @param ind the requested index
     */
    @Override
    public void getIndex(int ind) {
        Player selPlayer = searchAdapter.getItemAt(ind);
        if (selPlayer != null) {
            navigateToSelectedPlayer(selPlayer);
        }
    }
}