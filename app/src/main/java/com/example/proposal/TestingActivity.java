package com.example.proposal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class TestingActivity extends AppCompatActivity {
    private ImageView imageViewResult;
    private NewTaskApi newTaskApi;
    int selectedImageId;
    private GetImage getimage;
    Gallery simpleGallery;

    CustomizedGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;
    private UploadImage uploadImage;
    private ImageView ivMic;
    private float x1,x2;
    PhotoPOJO[] images;
    int l;
    PhotoPOJO selectedImage;
    int i = 0;
    static final int MIN_DISTANCE = 150;
    TextToSpeech speak;
    ViewFlipper imageFrame;
    RelativeLayout slideShowBtn;
    Handler handler;
    Runnable runnable;
    List<String> titles = new ArrayList<>();

    List<String> annotations = new ArrayList<>();

    PhotoPOJO[] photoList;

    int selectedPhotoId;

    boolean firstLaunch = true;

    private Instant startActivityTime;

    @Override
    protected void onResume() {
        super.onResume();
        startActivityTime=Instant.now();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(MainActivity.recordToday != null) {
            Instant now = Instant.now();
            Duration appTimeElapsed = Duration.between(startActivityTime, now);
            MainActivity.recordToday.setApptime(MainActivity.recordToday.getApptime() + appTimeElapsed.toMinutes());
            MainActivity.recordToday.setPhototime(MainActivity.recordToday.getPhototime() + appTimeElapsed.toMinutes());
            RecordUtil.modifyRecord(MainActivity.recordToday);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivityTime = Instant.now();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        // Our layout is activity_main
        // get the reference of Gallery. As we are showing
        // languages it is named as languagesGallery
        // meaningful names will be good for easier understanding
        simpleGallery = (Gallery) findViewById(R.id.languagesGallery);

        // get the reference of ImageView
        selectedImageView = (ImageView) findViewById(R.id.imageView);


        getImage(MainActivity.currentUser.getId());


        //songs= new int[] {R.raw.sound0,R.raw.sound1,R.raw.sound2,R.raw.song3};
    }

    public Call<PhotoPOJO[]> getImage(long userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        uploadImage = retrofit.create(UploadImage.class);
        Call<PhotoPOJO[]> call = uploadImage.getPhotoFromAlbum(userid, "test");
        call.enqueue(new Callback<PhotoPOJO[]>() {
            @Override
            public void onResponse(Call<PhotoPOJO[]> call, Response<PhotoPOJO[]> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                images = response.body();
                // initialize the adapter
                customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), images);

                if(images.length >0) {
                    selectedImageView.setImageBitmap(CustomizedGalleryAdapter.getBitmapFromString(images[0].getImage(0, 999999999,MainActivity.userid)));
                    selectedImageId =0;
                }

                // set the adapter for gallery
                simpleGallery.setAdapter(customGalleryAdapter);

                // Let us do item click of gallery and image can be identified by its position
                simpleGallery.setOnItemClickListener((parent, view, position, id) -> {
                    // Whichever image is clicked, that is set in the  selectedImageView
                    // position will indicate the location of image
                    selectedImage = images[position];
                    selectedImageId = position;
                    selectedImageView.setImageBitmap(CustomizedGalleryAdapter.getBitmapFromString(images[position].getImage(0, 999999999,MainActivity.userid)));
                });
            }
            @Override
            public void onFailure(Call<PhotoPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }

/*
    public Call<PhotoPOJO[]> getImage(long begin, long end, long userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getimage = retrofit.create(GetImage.class);
        Call<PhotoPOJO[]> call = getimage.getImage(begin, end, userid);
        call.enqueue(new Callback<PhotoPOJO[]>() {
            @Override
            public void onResponse(Call<PhotoPOJO[]> call, Response<PhotoPOJO[]> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                PhotoPOJO[] postResponse = response.body();
                photoList = postResponse;
                selectedPhotoId =0;
                int k;
                for (k=0; k < postResponse.length; k++) {
                    titles.add(postResponse[k].getTitle());
                    annotations.add(postResponse[k].getPersoninpic());
                }
                    addFlipperImages(imageFrame, postResponse);
                    Log.i("viewnum", "called");
                }
            @Override
            public void onFailure(Call<PhotoPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }
*/

    public void modifyImage(PhotoPOJO photo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getimage = retrofit.create(GetImage.class);
        Call<ResponseBody> call = getimage.updateImage(photo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(TestingActivity.this, "Image updated successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TestingActivity.this, "Image update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

//File path = getApplicationContext().getFilesDir();