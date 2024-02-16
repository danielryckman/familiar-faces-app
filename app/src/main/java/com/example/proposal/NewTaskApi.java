package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewTaskApi {
     @POST("api/todo/taskuser")
    Call<TaskPOJO> newTask(@Body TaskPOJO test, @Query("userid") long userid);

    @DELETE("api/todo/task")
    Call<Void> deleteTask(@Query("userid") long userid, @Query("taskid") long taskid);
}