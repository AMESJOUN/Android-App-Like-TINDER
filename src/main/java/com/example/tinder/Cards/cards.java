package com.example.tinder.Cards;

public class cards {
    private String userId;
    private String name;
    private String profileImageurl;

    public cards(String userId,String name,String profileImageurl){
        this.userId=userId;
        this.name=name;
        this.profileImageurl=profileImageurl;
    }

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

    public String getProfileImageurl() {
        return profileImageurl;
    }

    public void setProfileImageurl(String profileImageurl) {
        this.profileImageurl = profileImageurl;
    }
}

