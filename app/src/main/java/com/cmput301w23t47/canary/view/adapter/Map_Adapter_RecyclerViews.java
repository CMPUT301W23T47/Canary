package com.cmput301w23t47.canary.view.adapter;

import com.cmput301w23t47.canary.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmput301w23t47.canary.callback.RecyclerViewInterface;
import com.cmput301w23t47.canary.model.Qrcodem;
import com.cmput301w23t47.canary.view.fragment.FragmentMapScreenMapListSearch;

import java.util.ArrayList;

//
/**
 * A adapter that holds the recycler view for the map list ( fragmentmapscreenmaplistsearch )
 * displays this in the format of a list
 * the xml file which determines how elements are stored is map_list_element_placement
 */
public class Map_Adapter_RecyclerViews extends RecyclerView.Adapter<Map_Adapter_RecyclerViews.ViewHolder>{
    private static final String TAG = "MapQRLocationListAdapter";
      private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    private ArrayList<Qrcodem> mqrCodes = new ArrayList<>();
    private MapRecyclerClickListener mmapQRLocationListAdapterClickListener;

    /**
     * constructor for the adapter
     * @param qrcodes the list of qr codes
     * @param recyclerViewInterface the interface for the recycler view
     */
    public Map_Adapter_RecyclerViews(ArrayList<Qrcodem> qrcodes, RecyclerViewInterface recyclerViewInterface) {
        this.mqrCodes = qrcodes;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    //could use help dont know what the error is here
    /**
     * the view holder for the recycler view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.map_list_element_placement, parent, false);
        final ViewHolder holder = new ViewHolder(view, mmapQRLocationListAdapterClickListener,recyclerViewInterface);
        return holder;
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

        ((ViewHolder)holder).qrname.setText(mqrCodes.get(position).getName());
        ((ViewHolder)holder).qrpoint.setText(mqrCodes.get(position).getScore());
    }


    /**
     * gets the size of the list
     * @return the size of the list
     */
    @Override
    public int getItemCount() {
        return mqrCodes.size();
    }
    
    /**
     * sets the list of qr codes
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView qrname,qrpoint;

        MapRecyclerClickListener clickListener;
    
        /**
         * sets the text views of the items in the list
         * @param itemView
         * @param clickListener
         */
        public ViewHolder(View itemView, MapRecyclerClickListener clickListener,RecyclerViewInterface recyclerViewInterface){
            super(itemView);

            // get the text views
            qrname = itemView.findViewById(R.id.qr_name_list);
            qrpoint = itemView.findViewById(R.id.qr_point_total_list);
            // get the image view

            this.clickListener = clickListener;
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
    
                        if ( pos != RecyclerView.NO_POSITION ) {
                            recyclerViewInterface.onItemClicked( pos );
                        }
                    }
                }
            });
        }

        /**
         * sets the click listener for the item
         * @param v - the view that was clicked
         */
        @Override
        public void onClick(View v) {
            clickListener.onMapSelected(getAdapterPosition());
        }
    }

    /**
     * sets the click listener for the recycler view
     * interface - of a method that will
     */
    public interface MapRecyclerClickListener {
        public void onMapSelected(int position);
    }


    /**
     * sets the click listener for the recycler view
     * @param qrCode - a qr code you wish to be added to the list
     */
    public void addQRCode(Qrcodem qrCode) {
        mqrCodes.add(qrCode);
        notifyDataSetChanged();
    }
    public void addQRList(ArrayList<Qrcodem> qrCode) {
        mqrCodes.addAll(qrCode);
        notifyDataSetChanged();
    }

}
/*
map_list_element_placement.xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_marginTop="10dp"
    android:layout_height="wrap_content">

    <ImageView
        android:id="@+id/qr_image_list"
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:layout_marginRight="10dp" />

    <TextView
        android:id="@+id/qr_name_list"
        android:layout_width="219dp"
        android:layout_height="wrap_content"
        android:layout_toEndOf="@+id/qr_image_list"
        android:text="TextView" />

    <TextView
        android:id="@+id/qr_date_found_list"
        android:layout_width="224dp"
        android:layout_height="29dp"
        android:layout_below="@+id/qr_name_list"
        android:layout_toEndOf="@+id/qr_image_list"
        android:text="TextView" />

    <TextView
        android:id="@+id/qr_point_total_list"
        android:layout_width="135dp"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:text="Points"
        android:textSize="24sp" />


</RelativeLayout>

 */