package com.example.proposal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class AddUser extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText dob;
    private EditText gender;
    private EditText nickname;
    private EditText hobbies;
    private EditText relationship;
    String fn;
    String ln;
    String birthdate;
    String g;
    String n;
    String h;
    String r;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        dob = (EditText) findViewById(R.id.dob);
        gender = (EditText) findViewById(R.id.gender);
        nickname = (EditText) findViewById(R.id.nickname);
        hobbies = (EditText) findViewById(R.id.hobbies);
        relationship = (EditText) findViewById(R.id.relationship);
    }
    public void addUser(View view){
        fn = firstName.getText().toString();
        ln = lastName.getText().toString();
        birthdate = dob.getText().toString();
        g = gender.getText().toString();
        n = nickname.getText().toString();
        h = hobbies.getText().toString();
        r = relationship.getText().toString();
        User user = new User(fn, ln, birthdate, g, n, h, r);
        try {
            UserApi userApi = new ServerRequest();
            userApi.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
