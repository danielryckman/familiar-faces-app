package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface CommentApi {
    @PUT("api/todo/photos")
    Call<TaskPOJO> createComment(@Body TaskPOJO post);
}
