package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetFamilymemberByEmail {
    @GET("api/todo/familymember")
    Call<FamilymemberPOJO> getFamilymemberByEmail(@Query("email") String email);
}
