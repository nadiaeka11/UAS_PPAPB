package com.example.cinemate.admin;

public class DataClass {
    private String imageURL, caption, title;
    public DataClass(){
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public DataClass(String imageURL, String title, String caption) {
        this.imageURL = imageURL;
        this.title = title;
        this.caption = caption;
    }
}