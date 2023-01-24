package com.example.proposal;

import java.util.List;

public class RecordPOJO {

    private long id;

    public String getRdate() {
        return rdate;
    }

    private String rdate;

    public void setId(long id) {
        this.id = id;
    }

    private long apptime;

    private long phototime;

    private long testtime;

    private UserPOJO myuser;

    private List<PhotoPOJO> photo;

    private List<TestPOJO> tests;

    public RecordPOJO(String name, String rdate, long apptime, long phototime, long testtime) {
        this.rdate = rdate;
        this.apptime = apptime;
        this.testtime = testtime;
        this.phototime = phototime;
    }

}
