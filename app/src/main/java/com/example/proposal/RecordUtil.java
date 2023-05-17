package com.example.proposal;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordUtil {
    private static NewRecord newRecord;
    public static void modifyRecord(RecordPOJO record) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newRecord = retrofit.create(NewRecord.class);
        Call<RecordPOJO> call = newRecord.modifyRecord(record);

        call.enqueue(new Callback<RecordPOJO>() {
            @Override
            public void onResponse(Call<RecordPOJO> call, Response<RecordPOJO> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
            }
            @Override
            public void onFailure(Call<RecordPOJO> call, Throwable t) {
                Log.i("Failure", "failed to update record " + t.getMessage());
            }
        });
    }

}
