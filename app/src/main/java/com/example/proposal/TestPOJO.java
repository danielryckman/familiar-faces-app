package com.example.proposal;

import java.util.List;

public class TestPOJO {

    private long id;

    private String name;

    private long starttime;

    public long getId() {
        return id;
    }

    private long endtime;

    public void setId(long id) {
        this.id = id;
    }

    private String score;

    private String subscores;

    private List<QuestionPOJO> question;

    private UserPOJO myuser;

    private RecordPOJO record;

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setSubscores(String subscores) {
        this.subscores = subscores;
    }

    public void setQuestion(List<QuestionPOJO> question) {
        this.question = question;
    }

    public void setMyuser(UserPOJO myuser) {
        this.myuser = myuser;
    }

    public void setRecord(RecordPOJO record) {
        this.record = record;
    }

    public long getEndtime() {
        return endtime;
    }

    public String getScore() {
        return score;
    }

    public String getSubscores() {
        return subscores;
    }

    public List<QuestionPOJO> getQuestion() {
        return question;
    }

    public UserPOJO getMyuser() {
        return myuser;
    }

    public RecordPOJO getRecord() {
        return record;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStarttime() {
        return starttime;
    }

    public TestPOJO(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}
