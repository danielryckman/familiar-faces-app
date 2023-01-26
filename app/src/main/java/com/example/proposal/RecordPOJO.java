package com.example.proposal;

import java.util.List;

public class RecordPOJO {

    private long id;

    public long getRdate() {
        return rdate;
    }

    private long rdate;

    public void setId(long id) {
        this.id = id;
    }

    private long apptime;

    private long phototime;

    private long testtime;

    public void setRdate(long rdate) {
        this.rdate = rdate;
    }

    public long getId() {
        return id;
    }

    public long getApptime() {
        return apptime;
    }

    public long getPhototime() {
        return phototime;
    }

    public long getTesttime() {
        return testtime;
    }

    public long getAveragescore() {
        return averagescore;
    }

    public long getCommentnumber() {
        return commentnumber;
    }

    public long getTestnumber() {
        return testnumber;
    }

    public UserPOJO getMyuser() {
        return myuser;
    }

    public List<PhotoPOJO> getPhoto() {
        return photo;
    }

    public List<TestPOJO> getTests() {
        return tests;
    }

    public void setApptime(long apptime) {
        this.apptime = apptime;
    }

    public void setPhototime(long phototime) {
        this.phototime = phototime;
    }

    public void setTesttime(long testtime) {
        this.testtime = testtime;
    }

    public void setAveragescore(long averagescore) {
        this.averagescore = averagescore;
    }

    public void setCommentnumber(long commentnumber) {
        this.commentnumber = commentnumber;
    }

    public void setTestnumber(long testnumber) {
        this.testnumber = testnumber;
    }

    public void setMyuser(UserPOJO myuser) {
        this.myuser = myuser;
    }

    public void setPhoto(List<PhotoPOJO> photo) {
        this.photo = photo;
    }

    public void setTests(List<TestPOJO> tests) {
        this.tests = tests;
    }

    private long averagescore;

    private long commentnumber;

    private long testnumber;

    private UserPOJO myuser;

    private List<PhotoPOJO> photo;

    private List<TestPOJO> tests;

    public RecordPOJO(String name, long rdate, long apptime, long phototime, long testtime) {
        this.rdate = rdate;
        this.apptime = apptime;
        this.testtime = testtime;
        this.phototime = phototime;
    }

}
