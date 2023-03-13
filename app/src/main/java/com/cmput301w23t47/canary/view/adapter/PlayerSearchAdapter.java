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

public class PlayerSearchAdapter extends RecyclerView.Adapter<PlayerSearchAdapter.ViewHolder>{
    private ArrayList<Player> players;
    private GetIndexCallback callback;
    //constructor
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

    @NonNull
    @Override
    public PlayerSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_search_item, parent, false);
        return new ViewHolder(view, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerSearchAdapter.ViewHolder holder, int position) {
        Player player = players.get(position);
        holder.playerName.setText(player.getUsername());
        holder.playerScore.setText(String.format(Locale.CANADA, "%d", player.getScore()));
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerName;
        private final TextView playerScore;

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
