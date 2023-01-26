package com.example.proposal;

import java.util.List;

public class PhotoPOJO {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    private String name;

    private String description;

    private long datetoshow;

    private long datecreated;

    public void setDatelastviewed(long datelastviewed) {
        this.datelastviewed = datelastviewed;
    }

    private long datelastviewed;

    private String comment;

    public String getPtype() {
        return ptype;
    }

    private String ptype;

    private String image;

    private UserPOJO myuser;

    private FamilymemberPOJO familymember;
    private String hobbies;

    private TaskPOJO task;

    private RecordPOJO myrecord;

    public PhotoPOJO(long id, String name, String description, long datetoshow, long datelastviewed, long datecreated, String comment, String ptype) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.datetoshow = datetoshow;
        this.datelastviewed = datelastviewed;
        this.datecreated = datecreated;
        this.ptype = ptype;
        this.comment = comment;
        this.image = image;
    }

    public String getImage() { return image; }
}
