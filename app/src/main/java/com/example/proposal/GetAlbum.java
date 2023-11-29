package com.example.proposal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetAlbum {
    @GET("api/todo/albums")
    Call<List<String>> getAlbum(@Query("userid") long userid);

    @POST("api/todo/album")
    Call<Void> newAlbum(@Query("userid") long userid, @Query("album") String album);
}
