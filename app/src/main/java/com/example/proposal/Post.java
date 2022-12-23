package com.example.proposal;
import com.google.gson.annotations.SerializedName;

public class Post {

    private String prompt;

    private Integer steps;

    private String[] images;

    private String info;

    public Post(String prompt, Integer steps) {
        this.prompt = prompt;
        this.steps = steps;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getSteps() {
        return steps;
    }

    public String[] getImages(){
        return images;
    }

    public String getInfo(){
        return info;
    }
}