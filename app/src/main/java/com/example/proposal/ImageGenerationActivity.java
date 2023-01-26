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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

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

public class ImageGenerationActivity extends AppCompatActivity implements GetImage {
    private ImageView imageViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private GetImage getimage;
    private TextView descriptionText;
    private ImageView ivMic;
    private float x1,x2;
    int l;
    int i = 0;
    static final int MIN_DISTANCE = 150;
    ViewFlipper imageFrame;
    RelativeLayout slideShowBtn;
    Handler handler;
    Runnable runnable;
    List<String> descriptions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_generation);
        descriptionText = findViewById(R.id.descriptionText);
        //imageViewResult = findViewById(R.id.image_view_result);
        ivMic = findViewById(R.id.ivSpeak);
        Log.i("view", "finished");
        getImage();
        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating intent using RecognizerIntent to convert speech to text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now!");
                // starting intent for result
                activityResultLauncher.launch(intent);
            }
        });
        imageFrame = (ViewFlipper) findViewById(R.id.imageFrames);
        // Gesture detection
        handler = new Handler();
        slideShowBtn = (RelativeLayout) findViewById(R.id.slideShowBtn);
        slideShowBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                runnable = new Runnable() {

                    @Override
                    public void run() {
                        handler.postDelayed(runnable, 3000);
                        imageFrame.showNext();

                    }
                };
                handler.postDelayed(runnable, 500);
            }
        });
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is not empty
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
                        // get data and append it to editText
                        ArrayList<String> d=result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        String responseComment = d.get(0);
                        //etText.setText(etText.getText()+" "+d.get(0));
                    }
                }
            });

    public Call<TaskPOJO[]> getImage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.4.214:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getimage = retrofit.create(GetImage.class);
        Call<TaskPOJO[]> call = getimage.getImage();
        call.enqueue(new Callback<TaskPOJO[]>() {
            @Override
            public void onResponse(Call<TaskPOJO[]> call, Response<TaskPOJO[]> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                TaskPOJO[] postResponse = response.body();
                int k;
                for (k=0; k < postResponse.length; k++) {
                    descriptions.add(postResponse[k].getDescription());
                }
                l = postResponse.length;
                String imageDescription = postResponse[0].getDescription();
                descriptionText.setText(imageDescription);
                addFlipperImages(imageFrame, postResponse);
                Log.i("viewnum", "called");
            }
            @Override
            public void onFailure(Call<TaskPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }
    private void addFlipperImages(ViewFlipper flipper, TaskPOJO[] array) {
        int imageCount = array.length;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        int b = array.length;
        String c = String.valueOf(b);
        Log.i("numimage", c);
        for (int count = 0; count < imageCount; count++) {
            Log.i("description", array[count].getDescription());
            ImageView imageView = new ImageView(this);
            String image = array[count].getImage();
            byte[] decodedBytes = android.util.Base64.decode(image, Base64.DEFAULT);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length, options);
            imageView.setImageBitmap(bmp);
            imageView.setLayoutParams(params);
            flipper.addView(imageView);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 > x1)
                    {
                        imageFrame.setInAnimation(this, R.anim.slide_in_right);
                        imageFrame.setOutAnimation(this, R.anim.slide_out_left);
                        imageFrame.showPrevious();
                        if (i == 0){
                            descriptionText.setText(descriptions.get(l-1));
                            i = l-1;
                        }
                        else {
                            descriptionText.setText(descriptions.get(i-1));
                            Log.i("setting", descriptions.get(i-1));
                            i = i-1;
                        }
                        //Toast.makeText(this, "Left to Right swipe [Previous]", Toast.LENGTH_SHORT).show ();
                    }

                    // Right to left swipe action
                    else
                    {
                        imageFrame.setOutAnimation(this, android.R.anim.slide_in_left);
                        imageFrame.setInAnimation(this, android.R.anim.slide_out_right);
                        imageFrame.showNext();
                        if (i+1 == l){
                            descriptionText.setText(descriptions.get(0));
                            i = 0;
                        }
                        else {
                            descriptionText.setText(descriptions.get(i+1));
                            i = i+1;
                        }
                        //Toast.makeText(this, "Right to Left swipe [Next]", Toast.LENGTH_SHORT).show ();
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

//File path = getApplicationContext().getFilesDir();