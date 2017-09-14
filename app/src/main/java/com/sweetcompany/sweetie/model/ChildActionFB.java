package com.sweetcompany.sweetie.model;

import com.google.firebase.database.Exclude;
import com.sweetcompany.sweetie.actionInfo.ActionInfoVM;


public abstract class ChildActionFB implements ActionInfoVM {

    private String title;
    private String uriCover;
    private boolean uploadingImg = false;
    private int progress;
    private String creationDate;


    public boolean isUploadingImg() {
        return uploadingImg;
    }

    public void setUploadingImg(boolean uploadingImg) {
        this.uploadingImg = uploadingImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUriCover() {
        return uriCover;
    }

    public void setUriCover(String uriCover) {
        this.uriCover = uriCover;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Exclude
    @Override
    public String toString() {
        return "{\n" +
                " title: " + title + "\n" +
                " uriCover: " + uriCover + "\n" +
                " uploadingImg: " + uploadingImg + "\n" +
                " progress: " + progress + "\n" +
                "}";

    }
}
