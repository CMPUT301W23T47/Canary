package com.cmput301w23t47.canary.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.callback.GetIndexCallback;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.PlayerQrCode;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for viewing the qr items in the recycler view
 */
public class QRCodeListAdapter extends RecyclerView.Adapter<QRCodeListAdapter.ViewHolder> {
    private ArrayList<PlayerQrCode> playerQrCodesList;
    private GetIndexCallback callback;

    /**
     * Required public constructor.
     */
    public QRCodeListAdapter(ArrayList<PlayerQrCode> playerQrCodesList, GetIndexCallback callback) {
        this.playerQrCodesList = playerQrCodesList;
        this.callback = callback;
    }

    /**
     * The view holder for the recycler view
     */
    @NonNull
    @Override
    public QRCodeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_qr_code_list_item, parent, false);
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
        PlayerQrCode playerQrCode = playerQrCodesList.get(position);
        holder.qrName.setText(QrCodeController.getDisplayName(playerQrCode.getName()));
        holder.qrScore.setText(String.format(Locale.CANADA, "%d", playerQrCode.retrieveScore()));
        holder.qrLastScanDate.setText(playerQrCode.retrieveDateString());
    }


    /**
     * Gets the size of the list
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return playerQrCodesList.size();
    }

    /**
     * Updates the list to display the new list of items
     * @param newQrList the new list of Qrs
     */
    public void updateList(ArrayList<PlayerQrCode> newQrList) {
        playerQrCodesList.clear();
        playerQrCodesList.addAll(newQrList);
        notifyDataSetChanged();
    }

    /**
     * Gets the item at the given position
     * @param pos the position to get the item at
     * @return the QrCode object at that position
     */
    public PlayerQrCode getItemAt(int pos) {
        if (pos >= playerQrCodesList.size()) {
            return null;
        }
        return playerQrCodesList.get( pos );
    }

    /**
     * sets the list of qr codes
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView qrName;
        private TextView qrLastScanDate;
        private TextView qrScore;
        private ImageView qrImage;

        GetIndexCallback callback;

        /**
         * A class with a constructor which gives the view and initializes the qrdata
         */
        public ViewHolder(View itemView, GetIndexCallback callback){
            super(itemView);
            // get the views
            qrName = itemView.findViewById(R.id.listQrName);
            qrLastScanDate = itemView.findViewById(R.id.listQrCodeScanDateTime);
            qrScore = itemView.findViewById(R.id.listQrCodeScore);
            qrImage = itemView.findViewById(R.id.listQrCodeImage);
            this.callback = callback;


            itemView.setOnClickListener((view -> {
                callback.getIndex(getAdapterPosition());
            }));
        }
    }
}
