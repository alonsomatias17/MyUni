package com.alonso.myuniapplication.business;

public class UserDTO{

    private String userName;
    private String email;
    private String profileImage;

    public UserDTO() {
    }

    public UserDTO(String userName, String email, String profileImage) {
        this.userName = userName;
        this.email = email;
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
