package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.places_response.Result;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {
    List<Result> placeList;
    Context context;

    public PlaceAdapter(List<Result> placeList, Context context) {
        this.placeList = placeList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_list_row,parent,false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        String placeName = placeList.get(position).getName();
        holder.placeName.setText(placeName);

        double lat = placeList.get(position).getGeometry().getLocation().getLat();
        double lon = placeList.get(position).getGeometry().getLocation().getLng();

        try {
            List<Address> addressList =  holder.geocoder.getFromLocation(lat,lon,1);
            Address address = addressList.get(0);
            String addressLine = address.getAddressLine(0);
            holder.streetAddress.setText(addressLine);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView placeName,streetAddress;
        Geocoder geocoder;
        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeNameTV);
            geocoder = new Geocoder(context, Locale.getDefault());
            streetAddress = itemView.findViewById(R.id.addressTv);
        }
    }
}
