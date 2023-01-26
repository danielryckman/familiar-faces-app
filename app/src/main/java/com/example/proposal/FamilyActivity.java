package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class FamilyActivity extends AppCompatActivity {
    private Button recordButton;
    private Button settings;
    private Button adduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_family);
        recordButton = (Button) findViewById(R.id.button);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecordActivity();
            }
        });
        settings = (Button) findViewById(R.id.button4);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfigActivity();
            }
        });
        adduser = (Button) findViewById(R.id.button2);
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openAddUser(); }
        });
    }
    public void openRecordActivity(){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
    public void openConfigActivity(){
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void openAddUser(){
        Intent intent = new Intent(this, AddUser.class);
        startActivity(intent);
    }
}