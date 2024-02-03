package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewAlbumActivity extends AppCompatActivity {

    TextView selectImage;
    private static final int PICK_IMAGE_REQUEST = 9544;
    ImageView image;
    Uri selectedImage;
    String part_image;
    TextView createAlbum;

    EditText albumName;

    private GetAlbum gAlbum;

    private UploadImage uploadimage;
    // Permissions for accessing the storage
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_new);
        albumName = findViewById(R.id.albumtext);
    }
    public Call<Void> newAlbum(View view) {
        String album = String.valueOf(albumName.getText());
        int userid = 1;
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
                    try {
                        String responsecode = response.errorBody().string();
                        if(responsecode.equals("\"User already exists\"")) {
                            Toast.makeText(getApplicationContext(), responsecode, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                Toast.makeText(getApplicationContext(), "Album created", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("Failure", "j failed" + t.getMessage());
            }
        });
        return call;
    }
}