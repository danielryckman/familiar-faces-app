package com.example.proposal;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetAlbumRequest implements GetAlbum{
    private GetAlbum gAlbum;
    List<String> albumItems = new ArrayList<String>();
    private OnGetAlbumListener onGetAlbumListener;

    public void setOnGetAlbumListener(OnGetAlbumListener onGetAlbumListener) {
        this.onGetAlbumListener = onGetAlbumListener;
    }
    public Call<List<String>> getAlbum(long id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gAlbum = retrofit.create(GetAlbum.class);
        Call<List<String>> call = gAlbum.getAlbum(id);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                List<String> postResponse = response.body();
                albumItems = postResponse;
                onGetAlbumListener.onAlbumChanged(albumItems);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.i("Failure", "j failed" + t.getMessage());
                onGetAlbumListener.onAlbumChanged(null);
            }
        });
        return call;
    }
    public Call<Void> newAlbum(long userid, String album) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gAlbum = retrofit.create(GetAlbum.class);
        Call<Void> call = gAlbum.newAlbum(userid, album);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                albumItems.add(album);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Failure", "j failed" + t.getMessage());
            }
        });
        return call;
    }
}
