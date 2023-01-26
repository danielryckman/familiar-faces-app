package com.example.proposal;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestRequest implements NewTest{
    private NewTest newTest;

    private TestPOJO response;

    public void setOnNewTestListener(OnNewTestListener onNewTestListener) {
        this.onNewTestListener = onNewTestListener;
    }

    public OnNewTestListener getOnNewTestListener() {
        return onNewTestListener;
    }

    private OnNewTestListener onNewTestListener;
    public Call<TestPOJO> newTest(TestPOJO test, long userid){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.214:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newTest = retrofit.create(NewTest.class);
        Call<TestPOJO> call = newTest.newTest(test, userid);
        call.enqueue(new Callback<TestPOJO>() {
            @Override
            public void onResponse(Call<TestPOJO> call, Response<TestPOJO> response) {
                //OnNewTestListener onNewTestListener;

                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                TestPOJO postResponse = response.body();
                onNewTestListener.onNewTest(postResponse);

            }
            @Override
            public void onFailure(Call<TestPOJO> call, Throwable t) {
                Log.i("Failure", "failed to reach api");
            }
        });
        return call;
    }
}
