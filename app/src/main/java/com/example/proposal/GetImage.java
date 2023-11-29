package com.example.proposal;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GetImage {
    @GET("api/todo/photorange")
    Call<PhotoPOJO[]> getImage(@Query("begin") long begin, @Query("end") long end, @Query("userid") long userid);

    @PUT("api/todo/photo")
    Call<ResponseBody> updateImage(@Body PhotoPOJO photo);
}
