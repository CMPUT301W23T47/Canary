package com.cmput301w23t47.canary.view.adapter;

import com.cmput301w23t47.canary.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w23t47.canary.model.QRCode;
import com.cmput301w23t47.canary.view.fragment.MapInteractiveQRViewHolder;
import com.cmput301w23t47.canary.viewmodel.QRCodeVMList;

import java.util.List;

/**
 * Class for the list of QR Codes on map
 * should have multiple accessible qr codes
 *
 */
public class MapQRLocationListAdapter extends RecyclerView.Adapter<MapInteractiveQRViewHolder>{

    Context context;
    QRCodeVMList qrCodeList;

    public MapQRLocationListAdapter(Context context, QRCodeVMList qrCodeList) {
        this.context = context;
        this.qrCodeList = qrCodeList;
    }

    @NonNull
    @Override
    public MapInteractiveQRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MapInteractiveQRViewHolder(LayoutInflater.from(context).inflate(R.layout.map_qr_recylcer_inlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MapInteractiveQRViewHolder holder, int position) {
        holder.imageView.setImageResource(qrCodeList.get(position).getPlayerPicture());
        holder.qrtextname.setText(qrCodeList.get(position).getQRname());
        holder.qrtextdate.setText(qrCodeList.get(position).getQRdate());
        //holder.qrtextpoint.setText(qrCodeList.get(position).getQRpoint());
    }

    @Override
    public int getItemCount() {
        return qrCodeList.size();
    }

}