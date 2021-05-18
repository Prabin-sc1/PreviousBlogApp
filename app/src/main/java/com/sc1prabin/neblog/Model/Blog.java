package com.sc1prabin.neblog.Model;

public class Blog {
    public String title;
    public String description;
    public String image;
    public String timestamp;
    public String userid;

    public Blog(){

    }

    public Blog(String title, String description, String image, String timestamp, String userid) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public Blog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Blog setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Blog setImage(String image) {
        this.image = image;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Blog setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Blog setUserid(String userid) {
        this.userid = userid;
        return this;
    }
}
