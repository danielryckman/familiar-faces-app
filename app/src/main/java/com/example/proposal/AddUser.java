package com.example.proposal;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AddUser extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText dob;

    private CheckBox isAdminCheckBox;
    private EditText emailEdit;

    private EditText passwordEdit;

    private EditText descriptionEdit;

    private RadioGroup gender;
    private EditText nickname;
    private EditText hobbies;
    private EditText relationship;
    String fn;
    String ln;

    String email;

    String password;
    String birthdate;
    String g;
    String n;
    String h;
    String r;

    String description;

    int isAdmin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        firstName = (EditText) findViewById(R.id.firstname);
        lastName = (EditText) findViewById(R.id.lastname);
        emailEdit = (EditText) findViewById(R.id.email);
        passwordEdit = (EditText) findViewById(R.id.password);
        dob = (EditText) findViewById(R.id.dob);
        gender = (RadioGroup) findViewById(R.id.groupradio);
        nickname = (EditText) findViewById(R.id.nickname);
        hobbies = (EditText) findViewById(R.id.hobbies);
        relationship = (EditText) findViewById(R.id.relationship);
        descriptionEdit = (EditText) findViewById(R.id.description);
        isAdminCheckBox = (CheckBox) findViewById(R.id.isadmin);
        isAdminCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAdmin = b ? 1: 0;
            }
        });
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            // Check which radio button has been clicked
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                g = radioButton.getText().equals("M") ? "m" : "f";
            }
        });
    }
    public void addUser(View view) throws FileNotFoundException {
        fn = firstName.getText().toString();
        ln = lastName.getText().toString();
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        birthdate = dob.getText().toString();
        n = nickname.getText().toString();
        h = hobbies.getText().toString();
        r = relationship.getText().toString();
        description = descriptionEdit.getText().toString();
        InputStream in = openFileInput("key.pub");
        InputStreamReader ir = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(ir);
        String auth_key = "";
        try {
            auth_key = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(MainActivity.currentUser == null) {
            User user = new User(fn, ln, birthdate, g, n, h, r, email, password, description, null);
            try {
                UserApi userApi = new ServerRequest();
                userApi.createUser(auth_key, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            FamilymemberPOJO familyMember = new FamilymemberPOJO(fn, ln, g, birthdate, n, h, email, password, description, r, isAdmin);
            try {
                NewFamilymemberApi newFamilymember = new NewFamilymemberRequest();
                newFamilymember.newFamilyMember(familyMember, MainActivity.currentUser.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
