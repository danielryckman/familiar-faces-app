package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServerApi {
    @POST("api/todo/taskuser?userid=1")
    Call<TaskPOJO> createPostTask(@Body TaskPOJO post);
}