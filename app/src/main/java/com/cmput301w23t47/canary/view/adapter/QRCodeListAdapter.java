package com.cmput301w23t47.canary.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w23t47.canary.R;
import com.cmput301w23t47.canary.controller.QrCodeController;
import com.cmput301w23t47.canary.model.PlayerQrCode;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter for QR Code list
 */
public class QRCodeListAdapter extends ArrayAdapter<PlayerQrCode> {

    private ArrayList<PlayerQrCode> playerQrCodesList;

    /**
     * Constructor to initialize the context and list
     * @param context the context to work on
     * @param qrCodes the list of qrcodes
     */
    public QRCodeListAdapter(Context context, ArrayList<PlayerQrCode> qrCodes){
        super(context, 0, qrCodes);
        this.playerQrCodesList = qrCodes;
    }

    /**
     * Gives the list of Qr codes
     * @return List of playerQrCode
     */
    public ArrayList<PlayerQrCode> getPlayerQrCodesList() {return playerQrCodesList;}

    /**
     * Sets the qr list in the adapter
     * @param playerQrCodes the new qr list to set
     */
    public void setQrList(ArrayList<PlayerQrCode> playerQrCodes) {
        this.playerQrCodesList.clear();
        this.playerQrCodesList.addAll(playerQrCodes);
    }

    /**
     * Gets the view and updates the data to be shown on the screen
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view the updated view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(super.getContext()).inflate(R.layout.content_qr_code_list_item,parent,false);
        }else{
            view = convertView;
        }
        PlayerQrCode qr = getItem(position);
        ImageView qrCodeImage = view.findViewById(R.id.listQrCodeImage);
        // qrCodeImage.setImageBitmap(qr.getSnapshot()); TODO: fix this later
        TextView qrUsername = view.findViewById(R.id.listQrName);
        qrUsername.setText(QrCodeController.getDisplayName(qr.getName()));
        TextView qrCodeScore = view.findViewById(R.id.listQrCodeScore);
        qrCodeScore.setText(String.format(Locale.CANADA, "%d", qr.retrieveScore()));
        TextView qrCodeScanDateTime = view.findViewById(R.id.listQrCodeScanDateTime);
        qrCodeScanDateTime.setText(qr.retrieveDateString());
        return view;
    }
}
