package com.example.proposal;

public class User {
    private String firstname;

    private String lastname;

    private String dob;

    private String gender;

    private String nickname;

    private String hobbies;

    private String relationship;

    private String email;

    private String password;

    private String description;

    private String auth_token;

    public User(String firstname, String lastname, String dob, String gender, String nickname, String hobbies, String relationship, String email, String password, String description, String auth_token) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        this.nickname = nickname;
        this.hobbies = hobbies;
        this.relationship = relationship;
        this.email = email;
        this.password=password;
        this.description = description;
        this.auth_token = auth_token;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDob() { return dob; }

    public String getGender() { return gender; }

    public String getNickname() { return nickname; }

    public String getHobbies() { return hobbies; }

    public String getAuth_token() { return auth_token; }
}
