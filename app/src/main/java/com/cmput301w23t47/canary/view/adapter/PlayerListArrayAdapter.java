package com.cmput301w23t47.canary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.model.Player;
import com.cmput301w23t47.canary.model.PlayerList;
import com.cmput301w23t47.canary.repository.PlayerRepository;

public class PlayerListArrayAdapter extends ArrayAdapter<PlayerList>
{
    private PlayerList playerList;
    public PlayerListArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public PlayerListArrayAdapter(@NonNull Context context, int resource, PlayerList playerList) {
        super(context, resource);
        this.playerList = playerList;
    }
    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
   //fill the list with the player list
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).
                    inflate(R.layout.content_player_list_item, parent, false);
        } else {
            view = convertView;
        }
        TextView usernameView = view.findViewById(R.id.player_username);
        TextView scoreView = view.findViewById(R.id.player_score);
        PlayerRepository player;
        player = new PlayerRepository();
        usernameView.setText(player.getUsername());
        scoreView.setText(String.format("%d Pt", player.getScore()));
        return view;
    }
    /**
     * Getter for the ArrayList used in Ranking
     * @return ArrayList used by adapter
     */
    public PlayerList getPlayerList() {
        return playerList;
    }


}
