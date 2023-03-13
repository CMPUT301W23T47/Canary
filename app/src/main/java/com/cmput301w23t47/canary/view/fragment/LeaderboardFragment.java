package com.cmput301w23t47.canary.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmput301w23t47.canary.callback.UpdateLeaderboardCallback;
import com.cmput301w23t47.canary.controller.FirestoreController;
import com.cmput301w23t47.canary.controller.LeaderboardController;
import com.cmput301w23t47.canary.databinding.FragmentLeaderboardBinding;
import com.cmput301w23t47.canary.model.Leaderboard;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;
import com.cmput301w23t47.canary.view.adapter.LeaderboardRankListAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Fragment for the leaderboard
 */
public class LeaderboardFragment extends Fragment implements
        UpdateLeaderboardCallback {
    public static final String TAG = "LeaderboardFragment";
    public static final String title = "Leaderboard";
    private static final String progressBarTitle = "Loading Leaderboards";
    private static final String progressBarMessage = "Should take only a moment...";

    private Leaderboard leaderboard;
    private String playerUsername = "";

    private FragmentLeaderboardBinding binding;
    private FirestoreController firestoreController;
    private LeaderboardRankListAdapter leaderboardRankListAdapter;

    /**
     * Required empty public constructor.
     */
    public LeaderboardFragment() {}

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
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    /**
     * Initializes the UI for the page
     */
    private void init() {
        firestoreController = new FirestoreController();
        firestoreController.getLeaderboard(this);
        showLoadingBar();
        leaderboardRankListAdapter = new LeaderboardRankListAdapter(getContext(), new ArrayList<>());
        binding.rankingList.setAdapter(leaderboardRankListAdapter);
    }

    /**
     * Gets the player from firestore to show and also shows the loading bar
     * @param hidden True if the fragment is now hidden, false otherwise.
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden && leaderboard == null) {
            firestoreController.getLeaderboard(this);
            showLoadingBar();
        }
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
     * Updates all the details in the leaderboard from firestore
     * and displays on the screen
     * @param leaderboard Leaderboard to be updated
     */
    @Override
    public void updateLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        fillLeaderboardInfo();
    }

    /**
     * Fills in all the details in the leaderboard by getting them
     * from the firestore database
     */
    private void fillLeaderboardInfo() {
        // set Global Leaderboard val
        binding.scoreLeaderboardVal.setText(String.format(Locale.CANADA, "%s: %d Pt",
                leaderboard.getMaxScorePlayer(), leaderboard.getMaxScore()));
        binding.qrScannedLeaderboardVal.setText(String.format(Locale.CANADA, "%s: %d QR",
                leaderboard.getMaxQrPlayer(), leaderboard.getMaxQr()));

        // set personal ranking
        int playerScoreRank = LeaderboardController.getRankForPlayer(playerUsername, leaderboard.getByScore());
        int playerMaxQrRank = LeaderboardController.getRankForPlayer(playerUsername, leaderboard.getByHighestScoringQr());
        binding.scoreRankVal.setText(String.format(Locale.CANADA, "%d Out of %d",
                playerScoreRank, leaderboard.getByScore().size()));
        binding.highestScoringQrRankVal.setText(String.format(Locale.CANADA, "%d Out of %d",
                playerMaxQrRank, leaderboard.getByHighestScoringQr().size()));

        // update ranking list
        ArrayList<LeaderboardPlayer> rankPlayers = leaderboardRankListAdapter.getPlayersList();
        rankPlayers.clear();
        rankPlayers.addAll(leaderboard.getByScore());
        leaderboardRankListAdapter.notifyDataSetChanged();
        hideLoadingBar();
    }

    /**
     * Shows the loading bar
     */
    private void showLoadingBar() {
        binding.progressBarLayout.progressBarBox.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the loading bar
     */
    private void hideLoadingBar() {
        binding.progressBarLayout.progressBarBox.setVisibility(View.GONE);
    }
}