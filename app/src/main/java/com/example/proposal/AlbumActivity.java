package com.example.proposal;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumActivity extends AppCompatActivity {

    Gallery simpleGallery;

    // CustomizedGalleryAdapter is a java/kotlin
    // class which extends BaseAdapter
    // and implement the override methods.
    CustomizedGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;

    TextView annotationText;

    TextView albumTitle;

    TextView descriptionText;

    Button deleteButton;

    EditText etText;

    ImageView ivMic;
    PhotoPOJO[] images;

    PhotoPOJO selectedImage;

    int selectedImageId;

    int current_index =0;

    private UploadImage uploadImage;

    private Instant startActivityTime;


    private MediaPlayer player;

    private int[] songs;
    //private GetImage getimage;

    // To show the selected language, we need this
    // array of images, here taken 10 different kind of
    // most popular programming languages
    //String[] strImages;

    @Override
    protected void onPause() {
        super.onPause();
        if(MainActivity.recordToday != null) {
            Instant now = Instant.now();
            Duration appTimeElapsed = Duration.between(startActivityTime, now);
            MainActivity.recordToday.setApptime(MainActivity.recordToday.getApptime() + appTimeElapsed.getSeconds());
            MainActivity.recordToday.setPhototime(MainActivity.recordToday.getPhototime() + appTimeElapsed.getSeconds());
            RecordUtil.modifyRecord(MainActivity.recordToday);
        }

        stopPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    private void stopPlayer(){
        if(player != null){
            player.release();
            player=null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivityTime = Instant.now();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        // Our layout is activity_main
        // get the reference of Gallery. As we are showing
        // languages it is named as languagesGallery
        // meaningful names will be good for easier understanding
        simpleGallery = (Gallery) findViewById(R.id.languagesGallery);

        // get the reference of ImageView
        selectedImageView = (ImageView) findViewById(R.id.imageView);

        annotationText = (TextView) findViewById(R.id.annotationText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        albumTitle = (TextView) findViewById(R.id.albumTextView);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        etText = findViewById(R.id.albumetSpeech);
        ivMic = findViewById(R.id.albumivSpeak);

        getImage(MainActivity.currentUser.getId());

        songs= new int[] {R.raw.sound0,R.raw.sound1,R.raw.sound2,R.raw.song3};


        if (player == null) {
            player = MediaPlayer.create(this, songs[0]);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    play();
                }
            });
        }

        player.start();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhotoFromAlbum(view);
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // if result is not empty
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            // get data and append it to editText
                            ArrayList<String> d = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            etText.setText(etText.getText() + " " + d.get(0));
                            textChanged();
                        }
                    }
                });

        ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating intent using RecognizerIntent to convert speech to text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!");
                // starting intent for result
                activityResultLauncher.launch(intent);
            }
        });
    }
    private void play()
    {
        current_index = (current_index +1)% 4;
        AssetFileDescriptor afd = this.getResources().openRawResourceFd(songs[current_index]);

        try
        {
            player.reset();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
            player.prepare();
            player.start();
            afd.close();
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IllegalStateException e){
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
        catch (IOException e){
            Log.e(TAG, "Unable to play audio queue do to exception: " + e.getMessage(), e);
        }
    }

    public void textChanged(){
        String command = String.valueOf(etText.getText());
        if(command.trim().isEmpty()){
            return;
        }
        Intent intent;
        if(command.contains("go back")){
            intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
        }else if(command.contains("next")){
            //intent = new Intent(this, ImageGenerationActivity.class);
            if(selectedImageId > images.length -1){
                selectedImageId = 0;
            }else{
                selectedImageId = selectedImageId +1;
            }
            selectedImage = images[selectedImageId];
            selectedImageView.setImageBitmap(CustomizedGalleryAdapter.getBitmapFromString(images[selectedImageId].getImage(0, 999999999,MainActivity.userid)));
            annotationText.setText(images[selectedImageId].getPersoninpic());
            descriptionText.setText(images[selectedImageId].getDescription());
        }
        etText.setText("");
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
                    annotationText.setText(images[0].getPersoninpic());
                    descriptionText.setText(images[0].getDescription());
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
                    annotationText.setText(images[position].getPersoninpic());
                    descriptionText.setText(images[position].getDescription());
                });
            }
            @Override
            public void onFailure(Call<PhotoPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }

    public void deletePhotoFromAlbum(View view) {
        String photoName=selectedImage.getName();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        uploadImage = retrofit.create(UploadImage.class);
        Call<ResponseBody> upload = uploadImage.deletePhotoFromAlbum(MainActivity.currentUser.getId(), "test",photoName);
        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(AlbumActivity.this, "Image deleted from album", Toast.LENGTH_SHORT).show();
                    List<PhotoPOJO> imageList = new LinkedList();

                    for(PhotoPOJO item : images) {
                        if (!item.getName().equals(photoName)) {
                            imageList.add(item);
                        }
                    }
                    images = new PhotoPOJO[imageList.size()];
                    for(int i=0; i< imageList.size(); i++) {
                        images[i] = imageList.get(i);
                    }
                    customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), images);

                    selectedImageView.setImageBitmap(CustomizedGalleryAdapter.getBitmapFromString(images[0].getImage(0, 999999999,MainActivity.userid)));
                    annotationText.setText(images[0].getPersoninpic());
                    descriptionText.setText(images[0].getDescription());

                    // set the adapter for gallery
                    simpleGallery.setAdapter(customGalleryAdapter);

                    // Let us do item click of gallery and image can be identified by its position
                    simpleGallery.setOnItemClickListener((parent, view, position, id) -> {
                        // Whichever image is clicked, that is set in the  selectedImageView
                        // position will indicate the location of image
                        selectedImage = images[position];
                        selectedImageView.setImageBitmap(CustomizedGalleryAdapter.getBitmapFromString(images[position].getImage(0, 999999999,MainActivity.userid)));
                        annotationText.setText(images[position].getPersoninpic());
                        descriptionText.setText(images[position].getDescription());
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AlbumActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}