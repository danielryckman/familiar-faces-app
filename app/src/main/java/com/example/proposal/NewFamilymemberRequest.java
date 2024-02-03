package com.example.proposal;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewFamilymemberRequest implements NewFamilymemberApi{
    private NewFamilymemberApi newFamilymemberApi;

    private FamilymemberPOJO response;

    private OnGetFamilymemberListener onGetFamilymemberListener;

    public void setOnGetUserListener(OnGetFamilymemberListener onGetFamilymemberListener) {
        this.onGetFamilymemberListener = onGetFamilymemberListener;
    }

    public OnGetFamilymemberListener getOnGetUserListener() {
        return onGetFamilymemberListener;
    }

    public Call<FamilymemberPOJO> newFamilyMember(FamilymemberPOJO familymember, long userid){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newFamilymemberApi = retrofit.create(NewFamilymemberApi.class);
        Call<FamilymemberPOJO> call = newFamilymemberApi.newFamilyMember(familymember, userid);
        call.enqueue(new Callback<FamilymemberPOJO>() {
            @Override
            public void onResponse(Call<FamilymemberPOJO> call, Response<FamilymemberPOJO> response) {

                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                FamilymemberPOJO postResponse = response.body();
               // onGetFamilymemberListener.onGetFamilymember(postResponse);

            }
            @Override
            public void onFailure(Call<FamilymemberPOJO> call, Throwable t) {
                Log.i("Failure", "failed to create new family member" + t.getMessage());
                //onGetFamilymemberListener.onGetFamilymember(null);
            }
        });
        return call;
    }
}
