package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetImage {
    @GET("api/todo/tasks")
    Call<TaskPOJO[]> getImage();
}
