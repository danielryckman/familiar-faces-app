package com.example.proposal;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetUserByEmailRequest implements GetUserByEmail{
    private GetUserByEmail getUserByEmail;

    private UserPOJO response;

    private OnGetUserListener onGetUserListener;

    public void setOnGetUserListener(OnGetUserListener onGetUserListener) {
        this.onGetUserListener = onGetUserListener;
    }

    public OnGetUserListener getOnGetUserListener() {
        return onGetUserListener;
    }

    public Call<UserPOJO> getUserByEmail(String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getUserByEmail = retrofit.create(GetUserByEmail.class);
        Call<UserPOJO> call = getUserByEmail.getUserByEmail(email);
        call.enqueue(new Callback<UserPOJO>() {
            @Override
            public void onResponse(Call<UserPOJO> call, Response<UserPOJO> response) {

                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                UserPOJO postResponse = response.body();
                onGetUserListener.onGetUser(postResponse);

            }
            @Override
            public void onFailure(Call<UserPOJO> call, Throwable t) {
                Log.i("Failure", "failed to get user by email" + t.getMessage());
                onGetUserListener.onGetUser(null);
            }
        });
        return call;
    }
}
