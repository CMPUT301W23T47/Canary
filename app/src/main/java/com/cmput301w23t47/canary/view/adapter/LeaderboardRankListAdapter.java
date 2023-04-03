package com.cmput301w23t47.canary.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.model.LeaderboardPlayer;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for viewing the qr items in the recycler view
 */
public class LeaderboardRankListAdapter extends RecyclerView.Adapter<LeaderboardRankListAdapter.ViewHolder> {
    private ArrayList<LeaderboardPlayer> playersList;
    private GetIndexCallback callback;

    /**
     * Required public constructor.
     */
    public LeaderboardRankListAdapter(ArrayList<LeaderboardPlayer> playersList, GetIndexCallback callback) {
        this.playersList = playersList;
        this.callback = callback;
    }

    /**
     * The view holder for the recycler view
     */
    @NonNull
    @Override
    public LeaderboardRankListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_leaderboard_rank_list_item, parent, false);
        return new ViewHolder(view, callback);
    }

    /**
     * Sets the text for the qr name and qr point total
     * this acts like an update function
     * as well as a listen for if an item was clicked
     * @param holder the view holder for the recycler view this is an item
     * @param position the position of the item that was clicked
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardPlayer player = playersList.get(position);
        holder.rank.setText(String.format(Locale.CANADA, "%d", position+1));
        holder.username.setText(player.getUsername());
        holder.score.setText(String.format(Locale.CANADA, "%d Pt", player.getScore()));
    }


    /**
     * Gets the size of the list
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return playersList.size();
    }

    /**
     * Updates the list to display the new list of items
     * @param newPlayerList the new list of LeaderboardPlayer
     */
    public void updateList(ArrayList<LeaderboardPlayer> newPlayerList) {
        playersList.clear();
        playersList.addAll(newPlayerList);
        notifyDataSetChanged();
    }

    /**
     * Gets the item at the given position
     * @param pos the position to get the item at
     * @return the QrCode object at that position
     */
    public LeaderboardPlayer getItemAt(int pos) {
        if (pos >= playersList.size()) {
            return null;
        }
        return playersList.get( pos );
    }

    /**
     * sets the list of qr codes
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rank;
        private TextView username;
        private TextView score;

        GetIndexCallback callback;

        /**
         * A class with a constructor which gives the view and initializes the qrdata
         */
        public ViewHolder(View itemView, GetIndexCallback callback){
            super(itemView);
            // get the views
            rank = itemView.findViewById(R.id.player_list_rank);
            username = itemView.findViewById(R.id.player_username);
            score = itemView.findViewById(R.id.player_score);
            this.callback = callback;

            itemView.setOnClickListener((view -> {
                callback.getIndex(getAdapterPosition());
            }));
        }
    }
}
