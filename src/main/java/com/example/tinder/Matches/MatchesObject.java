package com.example.tinder.Matches;

public class MatchesObject {
    //private String userId;
    //private String name;
    // private String profileImageUrl;


    private String message;
    private Boolean currenUser;


    public MatchesObject(String message,Boolean currenUser){
        this.currenUser=currenUser;
        this.message=message;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCurrenUser(Boolean currenUser) {
        this.currenUser = currenUser;
    }

    public Boolean getCurrenUser() {
        return currenUser;
    }
/*
    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }*/
}
