package com.cmput301w23t47.canary.view.adapter;

import android.util.Log;
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
import com.cmput301w23t47.canary.model.QrCode;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for viewing the qr items in the recycler view
 */
public class SearchNearbyQrListAdapter extends RecyclerView.Adapter<SearchNearbyQrListAdapter.ViewHolder> {
    private static final String TAG = "MapQRLocationListAdapter";

    private ArrayList<QrCode> qrCodes;
    private GetIndexCallback callback;

    public SearchNearbyQrListAdapter(ArrayList<QrCode> qrcodes, GetIndexCallback callback) {
        this.qrCodes = qrcodes;
        this.callback = callback;
    }

    /**
     * the view holder for the recycler view
     */
    @NonNull
    @Override
    public SearchNearbyQrListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_qr_code_list_item, parent, false);
        return new ViewHolder(view, callback);
    }

    /**
     * sets the text for the qr name and qr point total
     * this acts like an update function
     * as well as a listen for if an item was clicked
     * @param holder the view holder for the recycler view this is an item
     * @param position the position of the item that was clicked
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QrCode qrCode = qrCodes.get(position);
        holder.qrName.setText(QrCodeController.getDisplayName(qrCode.getName()));
        holder.qrScore.setText(String.format(Locale.CANADA, "%d", qrCode.getScore()));
    }


    /**
     * gets the size of the list
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return qrCodes.size();
    }

    /**
     * Updates the list to display the new list of items
     * @param newQrList the new list of Qrs
     */
    public void updateList(ArrayList<QrCode> newQrList) {
        qrCodes.clear();
        qrCodes.addAll(newQrList);
        Log.d(TAG, "updateList: " + qrCodes.size());
        notifyDataSetChanged();
    }

    /**
     * Gets the item at the given position
     * @param pos the position to get the item at
     * @return the QrCode object at that position
     */
    public QrCode getItemAt(int pos) {
        if (pos >= qrCodes.size()) {
            return null;
        }
        return qrCodes.get(pos);
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
            // hide the scan date; not required for this view
            qrLastScanDate.setVisibility(View.GONE);
        }
    }
}
