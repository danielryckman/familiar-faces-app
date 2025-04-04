package com.example.proposal;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, GetAuthToken{
    TextView textView;
    EditText etText;
    ImageView ivMic,ivCopy;
    String lcode = "en-US";

    TextToSpeech speak;

    GetAuthToken getAuthToken;
    private NewTest newTest;

    private boolean firstTime = true;

    private Instant startActivityTime;

    // Languages included
    String[] languages = {"English","Tamil","Hindi","Spanish","French",
            "Arabic","Chinese","Japanese","German"};

    // Language codes
    String[] lCodes = {"en-US","ta-IN","hi-IN","es-CL","fr-FR",
            "ar-SA","zh-TW","jp-JP","de-DE"};
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
            RecordUtil.modifyRecord(MainActivity.recordToday);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivityTime = Instant.now();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // initialize views
        textView = findViewById(R.id.landingtextView);
        etText = findViewById(R.id.landingetSpeech);
        ivMic = findViewById(R.id.landingivSpeak);

        String greetings= "Welcome!";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings= "Good Morning,";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings = "Good Afternoon,";
        }else{
            greetings= "Good Evening,";
        }
        String username = MainActivity.currentUser.getNickname() != null && !MainActivity.currentUser.getNickname().isEmpty()? MainActivity.currentUser.getNickname():MainActivity.currentUser.getFirstname();
        String welcomeMsg = greetings + " " +username + "!\nWhat would you like to do next? Check out today's photos, play a puzzle or view an album?";
        textView.setText(welcomeMsg);
        etText.setText("");

        String userinfo = MainActivity.currentUser.getEmail() + "/" + MainActivity.currentUser.getPassword();
        byte[] data = new byte[0];
        try {
            data = userinfo.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String base64data = Base64.encodeToString(data, Base64.DEFAULT);
        getAuthToken(base64data);

        speak = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    speak.setLanguage(Locale.US);
                    speak.setSpeechRate(0.5f);
                    speak.speak(welcomeMsg, TextToSpeech.QUEUE_FLUSH,null, null);
                }
            }
        });




        // on click listener for mic icon
       ivMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating intent using RecognizerIntent to convert speech to text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,lcode);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak now!");
                // starting intent for result
                activityResultLauncher.launch(intent);
            }
        });
    }

    // activity result launcher to start intent
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // if result is not empty
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData()!=null) {
                        // get data and append it to editText
                        ArrayList<String> d=result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        etText.setText(etText.getText()+" "+d.get(0));
                        textChanged();
                    }
                }
            });
    public Call<AuthTokenPOJO> getAuthToken(String userinfo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getAuthToken = retrofit.create(GetAuthToken.class);
        Call<AuthTokenPOJO> call = getAuthToken.getAuthToken(userinfo);
        call.enqueue(new Callback<AuthTokenPOJO>() {
            @Override
            public void onResponse(Call<AuthTokenPOJO> call, Response<AuthTokenPOJO> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "Couldn't get Auth Token");
                    return;
                }
                AuthTokenPOJO postResponse = response.body();
                String auth_token = postResponse.getAuthToken();
                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                ActivityCompat.requestPermissions(LandingActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                OutputStream fo = null;
                try {
                    fo = openFileOutput("key.pub", MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                OutputStreamWriter outputWriter=new OutputStreamWriter(fo);
                try {
                    outputWriter.write(auth_token);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    outputWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fo.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onFailure(Call<AuthTokenPOJO> call, Throwable t) {
                Log.i("Failure", "failed to reach api" + t.getMessage());
            }
        });
        return call;
    }
    public void textChanged(){
        String command = String.valueOf(etText.getText());
        if(command.trim().isEmpty()){
            return;
        }
        Intent intent;
        if(command.contains("photo")){
            etText.setText("");
            intent = new Intent(this, ImageGenerationActivity.class);
            startActivity(intent);
        }else if(command.contains("album")){
            etText.setText("");
            intent = new Intent(this, AlbumActivity.class);
            //intent = new Intent(this,AutoAlbumActivity.class);
            intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivity(intent);
        }
        else if(command.contains("puzzle")){
            etText.setText("");
            intent = new Intent(this, PuzzleActivity.class);
            startActivity(intent); 
        }else if(command.contains("family member")){
            etText.setText("");
            intent = new Intent(this, AddUser.class);
            startActivity(intent);
        }else if(command.contains("upload")){
            etText.setText("");
            intent = new Intent(this, UploadActivity.class);
            startActivity(intent);
        }
        else if(command.contains("test")){
            etText.setText("");
            intent = new Intent(this, TestingActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // setting lcode corresponding
        // to the language selected
        lcode = lCodes[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // automatically generated method
        // for implementing onItemSelectedListener
    }

}
