package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;



public class FamilyActivity extends AppCompatActivity {
    private Button patientview;
    private Button settings;
    private Button adduser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_family);
        patientview = (Button) findViewById(R.id.patientview);
        patientview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageActivity();
            }
        });
        settings = (Button) findViewById(R.id.createEvent);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfigActivity();
            }
        });
        adduser = (Button) findViewById(R.id.addFamilyMember);
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openAddUser(); }
        });
    }
    public void openImageActivity(){
        Intent intent = new Intent(this, ImageGenerationActivity.class);
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