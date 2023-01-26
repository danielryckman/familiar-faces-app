package com.example.proposal;

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
}
