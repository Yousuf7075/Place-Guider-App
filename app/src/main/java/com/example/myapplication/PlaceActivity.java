package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.places_response.PlaceServiceApi;
import com.example.myapplication.places_response.PlacesResponse;
import com.example.myapplication.places_response.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceActivity extends AppCompatActivity {
    private double latitude, longitude, position;
    private String placeType;
    RecyclerView recyclerView;
    PlaceAdapter adapter;
    Context context;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        recyclerView = findViewById(R.id.placeRecycler);
        progressBar = findViewById(R.id.progressBar);

        Intent getData = getIntent();
        position = getData.getDoubleExtra("pos", 0.0);
        latitude = getData.getDoubleExtra("lat",0.0);
        longitude = getData.getDoubleExtra("lon",0.0);

        if (position == 0.0){
            placeType = "restaurant";
        }
        else if(position == 1.0){
            placeType = "cafe";
        }

        final String api_key = "AIzaSyBXqujeYkhAjfuQe1gKeZfNdyEXgwTVxiQ";
        final String endUrl = String.format("place/nearbysearch/json?location=%f,%f&radius=5000&type=%s&key=%s",latitude,longitude,placeType,api_key);
        PlaceServiceApi serviceApi = RetrofitClient.getClient()
                .create(PlaceServiceApi.class);
        serviceApi.getNearByPlaces(endUrl)
                .enqueue(new Callback<PlacesResponse>() {
                    @Override
                    public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                        if (response.isSuccessful()) {
                            PlacesResponse apiResponse = response.body();
                            List<Result> placeList = apiResponse.getResults();
                            int resultSize = placeList.size();
                            if (resultSize > 0){
                                adapter = new PlaceAdapter(placeList, getApplicationContext());
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(llm);
                                recyclerView.setAdapter(adapter);
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesResponse> call, Throwable t) {
                        Log.e("failed",t.getLocalizedMessage());
                        Toast.makeText(PlaceActivity.this, "api is not response", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
