package com.alonso.myuniapplication.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UserDTO implements Parcelable {

    public static final String USER_KEY = "FBUser";

    private String userName;
    private String email;
//    private Career career;
//    private List<Subject> approvedSubjects;


    public UserDTO(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public UserDTO(Parcel in) {
        userName = in.readString();
        email = in.readString();
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(email);
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
