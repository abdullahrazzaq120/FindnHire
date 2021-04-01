package com.sortscript.findnhire.Worker.WorkerModels;

public class ModelWorkerClass {

    String CurrentDate, UserCity, UserDescription, UserName, UserVideo, UserImage, UserId;

    public ModelWorkerClass() {
    }

    public ModelWorkerClass(String currentDate, String userCity, String userDescription, String userName, String userVideo, String userImage, String userId) {
        CurrentDate = currentDate;
        UserCity = userCity;
        UserDescription = userDescription;
        UserName = userName;
        UserVideo = userVideo;
        UserImage = userImage;
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getUserCity() {
        return UserCity;
    }

    public void setUserCity(String userCity) {
        UserCity = userCity;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserVideo() {
        return UserVideo;
    }

    public void setUserVideo(String userVideo) {
        UserVideo = userVideo;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
