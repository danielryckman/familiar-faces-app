package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetImage {
    @GET("api/todo/photos")
    Call<PhotoPOJO[]> getImage();
}
