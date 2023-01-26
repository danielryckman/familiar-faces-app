package com.example.proposal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ViewFlipper;


public class MainActivity extends AppCompatActivity {
    Button submit;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        Log.i("login reached", "true");
        String user = username.getText().toString();

        if (user.equals("ShirleyF")) {
            Intent intent = new Intent(this, ImageGenerationActivity.class);
            username.getText().clear();
            password.getText().clear();
            startActivity(intent);
        }
        else if (user.equals("SallyS")) {
            Intent intent = new Intent(this, FamilyActivity.class);
            username.getText().clear();
            password.getText().clear();
            startActivity(intent);
        }
        else {
            return;
        }
    }
}