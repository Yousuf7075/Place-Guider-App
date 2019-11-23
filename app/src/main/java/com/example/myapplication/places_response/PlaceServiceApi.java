package com.example.myapplication.places_response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PlaceServiceApi {
    @GET
    Call<PlacesResponse> getNearByPlaces(@Url String url);
}
