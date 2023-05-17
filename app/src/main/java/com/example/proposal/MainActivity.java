package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements OnGetUserListener, OnGetFamilymemberListener {
    public static UserPOJO currentUser;

    public static FamilymemberPOJO currentFamilyMember;

    //public static String wsUrl = "https://familiar-faces-service.azurewebsites.net/";

    public static String WS_URL = "http://192.168.4.214:8082/";

    private NewRecord newRecord;

    public static RecordPOJO recordToday;

    private Instant startAppTime;
    Button submit;

    Button addUser;
    EditText username;
    EditText password;

    TextView message;
    int authCheckUser = 0;
    int authCheckFamily = 0;
    @Override
    protected void onResume() {
        super.onResume();
        message.setText("");
        startAppTime=Instant.now();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(recordToday != null) {
            Instant now = Instant.now();
            Duration appTimeElapsed = Duration.between(startAppTime, now);
            recordToday.setApptime(recordToday.getApptime() + appTimeElapsed.toMinutes());
            modifyRecord(recordToday);
        }
    }
    //@Override
   /* protected void onStop() {
        super.onStop();
        if(recordToday != null) {
            Instant now = Instant.now();
            Duration appTimeElapsed = Duration.between(startAppTime, now);
            recordToday.setApptime(recordToday.getApptime() + appTimeElapsed.getSeconds());
            modifyRecord(recordToday);
        }
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        submit = (Button) findViewById(R.id.submit);
        addUser = (Button) findViewById(R.id.addUser);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        message = (TextView) findViewById(R.id.message);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message.setText("");
                login();
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    public void addUser(){
        Intent intent = new Intent(this, AddUser.class);
        startActivity(intent);
    }
    public void login(){
        Log.i("login reached", "true");
        String user = username.getText().toString();
        //get user by email
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GetUserByEmail getUserByEmail = retrofit.create(GetUserByEmail.class);
            GetUserByEmailRequest getUserByEmailRequest = new GetUserByEmailRequest();
            getUserByEmailRequest.getUserByEmail(user);
            getUserByEmailRequest.setOnGetUserListener(this);

            GetFamilymemberByEmail getFamilymemberByEmail = retrofit.create(GetFamilymemberByEmail.class);
            GetFamilymemberByEmailRequest getFamilymemberByEmailRequest = new GetFamilymemberByEmailRequest();
            getFamilymemberByEmailRequest.getFamilymemberByEmail(user);
            getFamilymemberByEmailRequest.setOnGetFamilymemberListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //if(authCheckUser && authCheckFamily && (currentFamilyMember==null && currentUser==null)){
       //     message.setText("Invalid username or password. Please try again.");
        //    authCheckUser = false;
       //     authCheckFamily =false;
       // }
    }

    @Override
    public void onGetUser(UserPOJO user) {
        if(user != null && user.getPassword().equals(String.valueOf(password.getText()))){
            authCheckUser=1;
            Intent intent = new Intent(this, LandingActivity.class);
            username.getText().clear();
            password.getText().clear();
            currentUser = user;
            currentFamilyMember= null;
            getRecord(user.getId());
            startAppTime = Instant.now();
            startActivity(intent);
        }else{
            authCheckUser = -1;
            if(authCheckFamily <0) {
                message.setText("Invalid username or password. Please try again.");
            }
        }
    }

    @Override
    public void onGetFamilymember(FamilymemberPOJO familymember) {
        if(familymember != null && familymember.getPassword().equals(String.valueOf(password.getText()))){
            authCheckFamily =1;
            Intent intent = new Intent(this, FamilyActivity.class);
            username.getText().clear();
            password.getText().clear();
            currentUser = null;
            currentFamilyMember = familymember;
            startActivity(intent);
        }else{
            authCheckFamily =-1;
            if(authCheckUser <0) {
                message.setText("Invalid username or password. Please try again.");
            }
        }
    }

    //get today's user record if any, otherwise create a new record and return
    public Call<RecordPOJO[]> getRecord(long userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newRecord = retrofit.create(NewRecord.class);
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        Instant end = todayStart.toInstant(ZoneOffset.ofHours(18));
        Instant begin = todayStart.toInstant(ZoneOffset.ofHours(0));
        Call<RecordPOJO[]> call = newRecord.getRecordInRange(begin.toEpochMilli(), end.toEpochMilli(), userid);
        call.enqueue(new Callback<RecordPOJO[]>() {
            @Override
            public void onResponse(Call<RecordPOJO[]> call, Response<RecordPOJO[]> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                RecordPOJO[] recordList = response.body();
                if(recordList == null || recordList.length==0){
                    createRecord(userid);
                }else {
                    recordToday = recordList[0];
                }
            }
            @Override
            public void onFailure(Call<RecordPOJO[]> call, Throwable t) {
                Log.i("Failure", "failed to get record for user " + t.getMessage());
            }
        });
        return call;
    }

    public Call<RecordPOJO> createRecord(long userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newRecord = retrofit.create(NewRecord.class);
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        String recordName = MainActivity.currentUser.getFirstname() + MainActivity.currentUser.getLastname() + today.toString();
        RecordPOJO record = new RecordPOJO(recordName, todayStart.toInstant(ZoneOffset.ofHours(0)).toEpochMilli(), 0, 0, 0);
        Call<RecordPOJO> call = newRecord.newRecord(record, userid);

        call.enqueue(new Callback<RecordPOJO>() {
            @Override
            public void onResponse(Call<RecordPOJO> call, Response<RecordPOJO> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
                RecordPOJO record = response.body();
                recordToday = record;
            }
            @Override
            public void onFailure(Call<RecordPOJO> call, Throwable t) {
                Log.i("Failure", "failed to create record for user " + t.getMessage());
            }
        });
        return call;
    }

    public void modifyRecord(RecordPOJO record) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.WS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newRecord = retrofit.create(NewRecord.class);
        Call<RecordPOJO> call = newRecord.modifyRecord(record);

        call.enqueue(new Callback<RecordPOJO>() {
            @Override
            public void onResponse(Call<RecordPOJO> call, Response<RecordPOJO> response) {
                if (!response.isSuccessful()) {
                    Log.i("responsecode", "i failed");
                    return;
                }
            }
            @Override
            public void onFailure(Call<RecordPOJO> call, Throwable t) {
                Log.i("Failure", "failed to update record " + t.getMessage());
            }
        });
    }

}