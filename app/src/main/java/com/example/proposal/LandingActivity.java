package com.example.proposal;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LandingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView textView;
    EditText etText;
    ImageView ivMic,ivCopy;
    String lcode = "en-US";

    private NewTest newTest;

    private boolean firstTime = true;

    // Languages included
    String[] languages = {"English","Tamil","Hindi","Spanish","French",
            "Arabic","Chinese","Japanese","German"};

    // Language codes
    String[] lCodes = {"en-US","ta-IN","hi-IN","es-CL","fr-FR",
            "ar-SA","zh-TW","jp-JP","de-DE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // initialize views
        textView = findViewById(R.id.landingtextView);
        etText = findViewById(R.id.landingetSpeech);
        ivMic = findViewById(R.id.landingivSpeak);
        ivCopy = findViewById(R.id.landingivCopy);

        String greetings= "Welcome!";
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            greetings= "Good Morning!";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetings = "Good Afternoon!";
        }else{
            greetings= "Good Evening!";
        }
        textView.setText(greetings + " Shirley!\n I am very happy to see you here.\n What would you like to do next? You can say 'photo' to look at today's new photos or 'puzzle' to play a new puzzle.");
        etText.setText("");


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

        // on click listener to copy the speech
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to copy to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label",etText.getText().toString().trim()));
                Toast.makeText(LandingActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
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

    public void textChanged(){
        String command = String.valueOf(etText.getText());
        Intent intent;
        if(command.contains("photo")){
            intent = new Intent(this, ImageGenerationActivity.class);
            startActivity(intent);
        }else if(command.contains("puzzle")){
            intent = new Intent(this, PuzzleActivity.class);
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
