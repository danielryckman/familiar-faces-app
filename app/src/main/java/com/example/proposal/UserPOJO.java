package com.example.proposal;

import java.util.List;

public class UserPOJO {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setGender(String geneder) {
        this.gender = geneder;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public void setPhoto(List<PhotoPOJO> photo) {
        this.photo = photo;
    }

    public void setTests(List<TestPOJO> tests) {
        this.tests = tests;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getHobbies() {
        return hobbies;
    }

    public List<PhotoPOJO> getPhoto() {
        return photo;
    }

    public List<TestPOJO> getTests() {
        return tests;
    }

    private String firstname;

    private String lastname;

    private String dob;

    private String gender;

    private String nickname;

    private String hobbies;

    private List<PhotoPOJO> photo;

    private List<TestPOJO> tests;

    private List<RecordPOJO> records;

    private List<TaskPOJO> tasks;
    private String image;

    public UserPOJO(long id, String firstname, String lastname, String gender, String dob, String nickname, String hobbies, int repeat) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.nickname = nickname;
        this.hobbies = hobbies;
        this.gender = gender;
    }
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImage() { return image; }
}
