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
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.view.View;
import android.widget.TextView;

public class ImageGenerationActivity extends AppCompatActivity implements GetImage {
    private ImageView imageViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private GetImage getimage;
    private TextView descriptionText;
    private ImageView ivMic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_generation);
        descriptionText = findViewById(R.id.descriptionText);
        imageViewResult = findViewById(R.id.image_view_result);
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
    public void viewImage(byte[] bytearray){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length, options);
        imageViewResult.setImageBitmap(bmp);
    }

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
                String image = "";
                TaskPOJO imageResponse = postResponse[1];
                String imageDescription = postResponse[1].getDescription();
                descriptionText.setText(imageDescription);
                image += imageResponse.getImage();
                Log.i("image", image);
                byte[] decodedBytes = android.util.Base64.decode(image, Base64.DEFAULT);
                viewImage(decodedBytes);
                Log.i("viewnum", "called");
            }
            @Override
            public void onFailure(Call<TaskPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }
}

//File path = getApplicationContext().getFilesDir();