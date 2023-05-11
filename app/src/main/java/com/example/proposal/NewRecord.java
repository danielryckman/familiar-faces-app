package com.example.proposal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface NewRecord {
    @POST("api/todo/record")
    Call<RecordPOJO> newRecord(@Body RecordPOJO record, @Query("userid") long userid);

    @GET("api/todo/recordrange")
    Call<RecordPOJO[]> getRecordInRange( @Query("begin") long begin, @Query("end") long end, @Query("userid") long userid);

    @PUT("api/todo/record")
    Call<RecordPOJO> modifyRecord( @Body RecordPOJO record);
}
