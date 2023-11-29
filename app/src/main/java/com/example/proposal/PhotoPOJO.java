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

    private String title;

    private String personinpic;


    private long datetoshow;

    private long datecreated;

    public void setDatelastviewed(long datelastviewed) {
        this.datelastviewed = datelastviewed;
    }

    private long datelastviewed;

    public String getUploaddir() {
        return uploaddir;
    }

    public void setUploaddir(String uploaddir) {
        this.uploaddir = uploaddir;
    }

    private String uploaddir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPersoninpic(String personinpic) {
        this.personinpic = personinpic;
    }

    public void setDatetoshow(long datetoshow) {
        this.datetoshow = datetoshow;
    }

    public void setDatecreated(long datecreated) {
        this.datecreated = datecreated;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMyuser(UserPOJO myuser) {
        this.myuser = myuser;
    }

    public void setFamilymember(FamilymemberPOJO familymember) {
        this.familymember = familymember;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public void setTask(TaskPOJO task) {
        this.task = task;
    }

    public void setMyrecord(RecordPOJO myrecord) {
        this.myrecord = myrecord;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getPersoninpic() {
        return personinpic;
    }

    public long getDatetoshow() {
        return datetoshow;
    }

    public long getDatecreated() {
        return datecreated;
    }

    public long getDatelastviewed() {
        return datelastviewed;
    }

    public String getComment() {
        return comment;
    }

    public UserPOJO getMyuser() {
        return myuser;
    }

    public FamilymemberPOJO getFamilymember() {
        return familymember;
    }

    public String getHobbies() {
        return hobbies;
    }

    public TaskPOJO getTask() {
        return task;
    }

    public RecordPOJO getMyrecord() {
        return myrecord;
    }

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

    public PhotoPOJO(){}
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

    public String getImage(long begin, long end, long userid) { return image; }

}
