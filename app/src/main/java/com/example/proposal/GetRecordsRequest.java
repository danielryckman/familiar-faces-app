package com.example.proposal;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRecordsRequest implements GetRecords {
    private GetRecords getRecords;

    private List<RecordPOJO> response;

    public void setOnGetRecordsListener(OnGetRecordsListener onGetRecordsListener) {
        this.onGetRecordsListener = onGetRecordsListener;
    }

    public OnGetRecordsListener OnGetRecordsListener() {
        return onGetRecordsListener;
    }

    private OnGetRecordsListener onGetRecordsListener;
    public Call<List<RecordPOJO>> getRecords(long userid, long begin, long end){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getRecords = retrofit.create(GetRecords.class);
        Call<List<RecordPOJO>> call = getRecords.getRecords(userid, begin, end);
        call.enqueue(new Callback<List<RecordPOJO>>() {
            @Override
            public void onResponse(Call<List<RecordPOJO>> call, Response<List<RecordPOJO>> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                List<RecordPOJO> postResponse = response.body();
                onGetRecordsListener.onGetRecords(postResponse);

            }
            @Override
            public void onFailure(Call<List<RecordPOJO>> call, Throwable t) {
                Log.i("Failure", "failed to reach record api");
            }
        });
        return call;
    }
}
