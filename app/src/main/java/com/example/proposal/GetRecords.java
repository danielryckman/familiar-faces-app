package com.example.proposal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetRecords {
    @GET("api/todo/recordrange")
    Call<List<RecordPOJO>> getRecords(@Query("userid") long userid, @Query("begin") long begin, @Query("end") long end);
}
