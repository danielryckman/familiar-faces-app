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
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ImageView;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

public class ImageGenerationActivity extends AppCompatActivity implements GetImage, OnNewTaskListener {
    private ImageView imageViewResult;
    private NewTaskApi newTaskApi;
    private GetImage getimage;
    private TextView descriptionText;

    private TextView annotationView;

    private EditText etText;
    private ImageView ivMic;
    private float x1,x2;
    int l;
    int i = 0;
    static final int MIN_DISTANCE = 150;
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
        startActivityTime=Instant.now();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_generation);
        descriptionText = findViewById(R.id.descriptionText);
        annotationView =findViewById(R.id.annotation);
        etText = findViewById(R.id.photoetSpeech);
        //imageViewResult = findViewById(R.id.image_view_result);
        ivMic = findViewById(R.id.ivSpeak);
        Log.i("view", "finished");
        etText.setText("Press the microphone image to leave a comment.");
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
                if(firstLaunch){
                    etText.setText("");
                    firstLaunch=false;
                }
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
                        etText.setText(etText.getText()+" "+d.get(0));
                        textChanged();
                    }
                }
            });

    public void textChanged() {
    //save the comment for the photo object and invoke the rest service to update
        if(String.valueOf(etText.getText()).toLowerCase().contains("save to album")){
            PhotoPOJO selectedPhoto = photoList[selectedPhotoId];
            selectedPhoto.setUploaddir("./upload/" +MainActivity.currentUser.getId()+"/test");
            modifyImage(selectedPhoto);
        }else if(String.valueOf(etText.getText()).toLowerCase().contains("go back")) {
            Intent intent = new Intent(this,LandingActivity.class);
            startActivity(intent);
        }else {
            PhotoPOJO selectedPhoto = photoList[selectedPhotoId];
            selectedPhoto.setComment(String.valueOf(etText.getText()));
            MainActivity.recordToday.setCommentnumber(MainActivity.recordToday.getCommentnumber() + 1);
            modifyImage(selectedPhoto);
        }
        etText.setText("");
    }
    public Call<PhotoPOJO[]> getImage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getimage = retrofit.create(GetImage.class);
        Call<PhotoPOJO[]> call = getimage.getImage();
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
                l = postResponse.length;
                if(l>0) {
                    String imageDescription = postResponse[0].getTitle();
                    descriptionText.setText(imageDescription);
                    setImageAnnotation(annotations.get(0));
                    //SpannableString str=new SpannableString("hello");
                    //str.setSpan(new BackgroundColorSpan(Color.WHITE), 0, str.length(), 0);
                    //annotationView.setText(str);
                    //annotationView.setBackgroundColor(Color.WHITE);
                    addFlipperImages(imageFrame, postResponse);
                    Log.i("viewnum", "called");
                }
            }
            @Override
            public void onFailure(Call<PhotoPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }

    @Override
    public Call<ResponseBody> updateImage(PhotoPOJO photo) {
        return null;
    }

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
                    Toast.makeText(ImageGenerationActivity.this, "Image updated successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ImageGenerationActivity.this, "Image update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addFlipperImages(ViewFlipper flipper, PhotoPOJO[] array) {
        int imageCount = array.length;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        int b = array.length;
        String c = String.valueOf(b);
        Log.i("numimage", c);
        for (int count = 0; count < imageCount; count++) {
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

    private void setImageAnnotation(String annotation){
        if(annotation ==null || annotation.isEmpty()) {
            annotationView.setText("");
            return;
        }
        SpannableString str=new SpannableString(annotation);
        str.setSpan(new BackgroundColorSpan(Color.WHITE), 0, annotation.length(), 0);
        annotationView.setText(annotation);
        annotationView.setTextColor(Color.WHITE);
        etText.setText("");
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
                            descriptionText.setText(titles.get(l-1));
                            setImageAnnotation(annotations.get(l-1));
                            selectedPhotoId =l-1;
                            i = l-1;
                        }
                        else {
                            descriptionText.setText(titles.get(i-1));
                            setImageAnnotation(annotations.get(i-1));
                            //Log.i("setting", titles.get(i-1));
                            selectedPhotoId =i-1;
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
                            descriptionText.setText(titles.get(0));
                            setImageAnnotation(annotations.get(0));
                            selectedPhotoId =0;
                            i = 0;
                        }
                        else {
                            descriptionText.setText(titles.get(i+1));
                            setImageAnnotation(annotations.get(i+1));
                            selectedPhotoId =l+1;
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

    @Override
    public void onNewTask(TaskPOJO task) {
        getImage();
    }
}

//File path = getApplicationContext().getFilesDir();