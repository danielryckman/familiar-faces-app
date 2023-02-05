package com.example.proposal;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFamilymemberByEmailRequest implements GetFamilymemberByEmail{
    private GetFamilymemberByEmail getFamilymemberByEmail;

    private FamilymemberPOJO response;

    private OnGetFamilymemberListener onGetFamilymemberListener;

    public void setOnGetFamilymemberListener(OnGetFamilymemberListener onGetFamilymemberListener) {
        this.onGetFamilymemberListener = onGetFamilymemberListener;
    }

    public OnGetFamilymemberListener getOnGetUserListener() {
        return onGetFamilymemberListener;
    }

    public Call<FamilymemberPOJO> getFamilymemberByEmail(String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getFamilymemberByEmail = retrofit.create(GetFamilymemberByEmail.class);
        Call<FamilymemberPOJO> call = getFamilymemberByEmail.getFamilymemberByEmail(email);
        call.enqueue(new Callback<FamilymemberPOJO>() {
            @Override
            public void onResponse(Call<FamilymemberPOJO> call, Response<FamilymemberPOJO> response) {

                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                FamilymemberPOJO postResponse = response.body();
                onGetFamilymemberListener.onGetFamilymember(postResponse);

            }
            @Override
            public void onFailure(Call<FamilymemberPOJO> call, Throwable t) {
                Log.i("Failure", "failed to get user by email" + t.getMessage());
                onGetFamilymemberListener.onGetFamilymember(null);
            }
        });
        return call;
    }
}
