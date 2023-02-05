package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewFamilymemberApi {
    @POST("api/todo/familymemberuser")
    Call<FamilymemberPOJO> newFamilyMember(@Body FamilymemberPOJO post,@Query("userid") long userid);
}