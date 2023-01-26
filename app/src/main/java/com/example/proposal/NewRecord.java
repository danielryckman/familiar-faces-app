package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewRecord {
    @POST("api/todo/record")
    Call<RecordPOJO> newRecord(@Body RecordPOJO record, @Query("userid") long userid);
}
