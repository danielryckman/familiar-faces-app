package com.example.proposal;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UploadImage {
    @Multipart
    // POST request to upload an image from storage
    @POST("api/todo/photoupload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image,@Part MultipartBody.Part photo, @Query("userid") long userid,@Query("album") String album);

    @DELETE("api/todo/photoupload")
    Call<ResponseBody> deletePhotoFromAlbum( @Query("userid") long userid,@Query("album") String album,@Query("photoname") String photoname);

    @GET("api/todo/photoupload")
    Call<PhotoPOJO[]> getPhotoFromAlbum( @Query("userid") long userid,@Query("album") String album);

}
