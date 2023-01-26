package com.example.proposal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PuzzleActivity extends AppCompatActivity implements OnNewTestListener, AdapterView.OnItemSelectedListener{
    TextView textView;
    EditText etText;
    ImageView ivMic,ivCopy;

    Button button1, button2, button3, button4;

    String lcode = "en-US";

    private TestPOJO testPOJO;

    private int qid =0 ;

    private int mid =0 ;

    private int retry =0;

    private Context activityContext;
    private List<String> memoryTestSolution = new ArrayList<>();
    private String relationshipQuestionTemplate="How are a %s and a %s similar? How they are alike. They both are ... what?";

    private String memoryQuestionTemplate ="Memory Test. Remember these 3 words. Repeat them at the end of the test: \n %s \n Say any word to go to the next question.";

    private String finalMemoryQuestionTemplate ="Great Job! It is the end of the test. Remember the 3 words earlier. Can you repeat them now?\n";

    private NewTest newTest;

    // Languages included
    String[] languages = {"English","Tamil","Hindi","Spanish","French",
            "Arabic","Chinese","Japanese","German"};

    // Language codes
    String[] lCodes = {"en-US","ta-IN","hi-IN","es-CL","fr-FR",
            "ar-SA","zh-TW","jp-JP","de-DE"};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        activityContext = this.getApplicationContext();
        // initialize views
        textView = findViewById(R.id.testtextView);
        etText = findViewById(R.id.testetSpeech);
        ivMic = findViewById(R.id.testivSpeak);
        ivCopy = findViewById(R.id.testivCopy);
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);
        button4 = (Button)findViewById(R.id.button4);
        //create a new Test
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.4.214:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            newTest = retrofit.create(NewTest.class);
            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MM-dd-YYYY");
            TestPOJO test = new TestPOJO("ShirleyF-" + dateObj.format(formatter) );
            TestRequest newTestRequest = new TestRequest();
            long id= 1;
            newTestRequest.newTest(test, id);
            newTestRequest.setOnNewTestListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                Toast.makeText(PuzzleActivity.this, "Copied!", Toast.LENGTH_SHORT).show();
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

    public void textChanged() {
        String command = String.valueOf(etText.getText());
        Intent intent;
        if(etText.getText().length()<1 || testPOJO.getQuestion() == null){
            return;
        }
        if(command.toLowerCase().contains("back")){
            intent = new Intent(this, LandingActivity.class);
            startActivity(intent);
            return;
        }else if(command.toLowerCase().contains("photo")){
            intent = new Intent(this, ImageGenerationActivity.class);
            startActivity(intent);
            return;
        }
        if(qid>testPOJO.getQuestion().size()-1){
            int result = checkSolution(-1);
            if(result==1) {
                testPOJO.getQuestion().get(mid).setScore("1");
                testPOJO.setScore(testPOJO.getScore() + 1);
            }else if(retry <5 ){
                retry ++;
                return;
            }
            etText.setText("");
            textView.setText("Thanks for playing the puzzle. What do you want to do next? \"go back\" or see \"photo\"?");
            return;
        }
        int result = checkSolution(qid);
        if(result==1){
            textView.append("\n Excellent Job!!");
            testPOJO.getQuestion().get(qid).setScore("1");
            testPOJO.setScore(testPOJO.getScore() +1);
            try {
                Thread.sleep(1000);
            }catch(Exception ex) {

            }
        }else if(result == -1 && retry < 3 ){
            textView.setText(textView.getText() + "\n Hmm. Not quite right. Try again?");
            retry ++;
            return;
        }
        retry =0;
        qid ++;
        if(qid<testPOJO.getQuestion().size()) {
            displayQuestion(qid);
        }else if(qid<testPOJO.getQuestion().size() +1) {
            etText.setText("");
            displayMemoryQuestion();
        }
    }

    private void setButtonColorGreen(int qid){
        if(qid ==0){
            button1.setBackgroundColor(Color.GREEN);
        }else if(qid==1){
            button2.setBackgroundColor(Color.GREEN);
        }else if(qid==2){
            button3.setBackgroundColor(Color.GREEN);
        }else if(qid==3){
            button4.setBackgroundColor(Color.GREEN);
        }
    }
    private void setButtonColorRed(int qid){
        if(qid ==0){
            button1.setBackgroundColor(Color.RED);
        }else if(qid==1){
            button2.setBackgroundColor(Color.RED);
        }else if(qid==2){
            button3.setBackgroundColor(Color.RED);
        }else if(qid==4){
            button4.setBackgroundColor(Color.RED);
        }
    }

    private int checkSolution(int qid){
        if(qid == -1){
            int correctCount =0;
            for(String key: memoryTestSolution) {
                String response = String.valueOf(etText.getText());
                if (response.toLowerCase().contains(key)){
                    correctCount++;
                }
            }
            if(correctCount ==3){
                setButtonColorGreen(mid);
                return 1;
            }else{
                setButtonColorRed(mid);
                return -1;
            }
        }
        QuestionPOJO question = testPOJO.getQuestion().get(qid);
        if(question.getCategory().equals("memory")){
            mid=qid;
            return 0;
        }else if(question.getCategory().equals("relationship") ||question.getCategory().equals("money")){
            String solution = question.getSolution();
            String[] keys = solution.split(",");
            for(String key: keys) {
                if (String.valueOf(etText.getText()).toLowerCase().contains(key)){
                    setButtonColorGreen(qid);
                    return 1;
                }
            }
            setButtonColorRed(qid);
            return -1;
        }else if(question.getCategory().equals("general")){
            String description = question.getDescription();
            LocalDate today = LocalDate.now();
            if(description.contains("day of the week")){
                DayOfWeek dayOfWeek =today.getDayOfWeek();
                if(String.valueOf(etText.getText()).toLowerCase().contains(dayOfWeek.toString().toLowerCase())){
                    setButtonColorGreen(qid);
                    return 1;
                }
                setButtonColorRed(qid);
                return -1;
            }else if(description.contains("month of the year")){
                Month month =today.getMonth();
                if(String.valueOf(etText.getText()).toLowerCase().contains(month.toString().toLowerCase())){
                    setButtonColorGreen(qid);
                    return 1;
                }
                setButtonColorRed(qid);
                return -1;
            }else{
                if(String.valueOf(etText.getText()).toLowerCase().contains("2023")){
                    setButtonColorGreen(qid);
                    return 1;
                }
                setButtonColorRed(qid);
                return -1;
            }
        }else{
            //unknown
        }
        return -1;
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

    @Override
    public void onNewTest(TestPOJO test) {
        memoryTestSolution.clear();
        testPOJO = test;
        mid=0;
        qid=0;
        displayQuestion(qid);
    }

    private void displayQuestion(int id){
        etText.setText("");
        if(testPOJO.getQuestion() !=null && !testPOJO.getQuestion().isEmpty()) {
            QuestionPOJO question = testPOJO.getQuestion().get(id);
            String description = question.getDescription();
            if (question.getCategory().equals("relationship")) {
                String[] items = description.split("/");
                description = String.format(relationshipQuestionTemplate, items[0], items[1]);
            } else {
                if (question.getCategory().equals("memory")) {
                    mid = qid;
                    description = String.format(memoryQuestionTemplate, description);
                    String[] solution = question.getSolution().split(",");
                    for(String key:solution) {
                        memoryTestSolution.add(key.trim().substring(1));
                    }
                }
            }
            textView.setText(description);
        }
    }

    private void displayMemoryQuestion(){
            textView.setText(finalMemoryQuestionTemplate);
    }
}
