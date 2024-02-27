package com.example.proposal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetAuthToken {
    @POST("api/todo/login")
    Call<AuthTokenPOJO> getAuthToken(@Query("userinfo") String userinfo);
}
