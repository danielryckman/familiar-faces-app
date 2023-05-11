package com.example.proposal;

import java.util.List;

public class TaskPOJO {

    private long id;

    private String name;

    private String description;

    private String url;

    private long schedule;

    private int repeat;

    private long repeatstart;

    private long repeatend;

    private String image;

    private String prompt;

    private List<PhotoPOJO> photo;

    private UserPOJO user;

    public TaskPOJO(String name, String description, long schedule, int repeat) {
        this.name = name;
        this.description = description;
        this.schedule = schedule;
        this.repeat = repeat;
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl(){
        return url;
    }

    public long getSchedule(){
        return schedule;
    }

    public int getRepeat() { return repeat; }

    public String getImage() { return image; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSchedule(long schedule) {
        this.schedule = schedule;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public long getRepeatstart() {
        return repeatstart;
    }

    public void setRepeatstart(long repeatstart) {
        this.repeatstart = repeatstart;
    }

    public long getRepeatend() {
        return repeatend;
    }

    public void setRepeatend(long repeatend) {
        this.repeatend = repeatend;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<PhotoPOJO> getPhoto() {
        return photo;
    }

    public void setPhoto(List<PhotoPOJO> photo) {
        this.photo = photo;
    }

    public UserPOJO getUser() {
        return user;
    }

    public void setUser(UserPOJO user) {
        this.user = user;
    }
}
