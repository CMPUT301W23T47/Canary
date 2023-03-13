package com.cmput301w23t47.canary.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.callback.GetPlayerCallback;
import com.cmput301w23t47.canary.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for Player Search
 */
public class PlayerSearchAdapter extends RecyclerView.Adapter<PlayerSearchAdapter.ViewHolder>{
    private ArrayList<Player> players;
    private GetIndexCallback callback;

    /**
     * Required public constructor.
     */
    public PlayerSearchAdapter(ArrayList<Player> players, GetIndexCallback callback) {
        this.players = players;
        this.callback = callback;
    }

    /**
     * Updates the list view
     * @param filteredList the new list to display
     */
    public void filterList(ArrayList<Player> filteredList) {
        players = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Gets a view for searching an item
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return ViewHolder A new object with details about a player
     */
    @NonNull
    @Override
    public PlayerSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_search_item, parent, false);
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
    public void onBindViewHolder(@NonNull PlayerSearchAdapter.ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerName.setText(player.getUsername());
        holder.playerScore.setText(String.format(Locale.CANADA, "%d", player.getScore()));
    }

    /**
     * Gives the number of players in the list
     * @return int Size of the list of players
     */
    @Override
    public int getItemCount() {
        return players.size();
    }

    /**
     * Adds the details to the list
     * @param filteredModelList the list in which players are added
     */
    public void setFilterList(List<Player> filteredModelList) {
        players = new ArrayList<>();
        players.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    /**
     * Updates the list to display the new list of items
     * @param newPlayerList the new list of players
     */
    public void updateList(ArrayList<Player> newPlayerList) {
        players.clear();
        players.addAll(newPlayerList);
        notifyDataSetChanged();
    }

    /**
     * Gets the item at the given position
     * @param pos the position to get the item at
     * @return the player object at that position
     */
    public Player getItemAt(int pos) {
        if (pos >= players.size()) {
            return null;
        }
        return players.get(pos);
    }


    /**
     * A class with a constructor which gives the view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerName;
        private final TextView playerScore;

        /**
         * Initializes a view and sets the score of the player
         * @param itemView
         * @param getIndexCallback
         */
        public ViewHolder(@NonNull View itemView, GetIndexCallback getIndexCallback) {
            super(itemView);
            playerName=itemView.findViewById(R.id.player_list_username);
            itemView.setOnClickListener(view -> {
                getIndexCallback.getIndex(getAdapterPosition());
            });
            playerScore=itemView.findViewById(R.id.player_list_score);
        }
    }
}
