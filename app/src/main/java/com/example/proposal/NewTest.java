package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewTest {
    @POST("api/todo/testuser")
    Call<TestPOJO> newTest(@Body TestPOJO test, @Query("userid") long userid);
}
