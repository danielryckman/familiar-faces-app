package com.example.proposal;

import java.util.List;

public class FamilymemberPOJO {

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

    public String getRelationship() {
        return relationship;
    }

    public String getDob() {
        return dob;
    }

    public int isadmin;

    public String getGender() {
        return gender;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getHobbies() {
        return hobbies;
    }

    public List<PhotoPOJO> getPhoto() {
        return photo;
    }

    private String firstname;

    private String lastname;

    private String dob;

    private String description;

    private String gender;

    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    private String nickname;

    private String hobbies;

    private String relationship;

    private List<PhotoPOJO> photo;

    private UserPOJO user;

    public FamilymemberPOJO(long id, String firstname, String lastname, String gender, String dob, String nickname, String hobbies,String email, String password, String description, int isAdmin) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.nickname = nickname;
        this.hobbies = hobbies;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.description = description;
        this.relationship = relationship;
        this.isadmin = isAdmin;
    }

    public FamilymemberPOJO(String firstname, String lastname, String gender, String dob, String nickname, String hobbies, String email, String password, String description, String relationship,int isAdmin) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.nickname = nickname;
        this.hobbies = hobbies;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.description = description;
        this.relationship = relationship;
        this.isadmin = isAdmin;
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

}
