package com.example.proposal;

public class User {
    private String firstname;

    private String lastname;

    private String dob;

    private String gender;

    private String nickname;

    private String hobbies;

    private String relationship;

    public User(String firstname, String lastname, String dob, String gender, String nickname, String hobbies, String relationship) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.gender = gender;
        this.nickname = nickname;
        this.hobbies = hobbies;
        this.relationship = relationship;
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
}
