package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Header;

public interface GetUserByEmail {
    @GET("api/todo/user")
    Call<UserPOJO> getUserByEmail(@Query("email") String email);
}
