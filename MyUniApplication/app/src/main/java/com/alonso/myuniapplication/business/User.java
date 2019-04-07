package com.alonso.myuniapplication.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private String userName;
    private String email;
    private Career career;
    private List<Subject> approvedSubjects;
    private List<Subject> onGoingSubjects;
    private String profileImageUri;
    private List<String> singleChatsKeys;
    private List<String> groupChatsKeys;

    public User(){
    }

    public User(String x){
        this.userName = "";
        this.email = "";
        this.career = new Career();
        this.approvedSubjects = new ArrayList<>();
        this.onGoingSubjects = new ArrayList<>();
        this.profileImageUri = "";
        this.singleChatsKeys = new ArrayList<>();
        this.groupChatsKeys = new ArrayList<>();
    }

    public User(String userName, String email, Career career) {
        this.userName = userName;
        this.email = email;
        this.career = career;
        this.approvedSubjects = new ArrayList<>();
        this.onGoingSubjects = new ArrayList<>();
        this.profileImageUri = "";
        this.singleChatsKeys = new ArrayList<>();
        this.groupChatsKeys = new ArrayList<>();
    }

    protected User(Parcel in) {
        this.userName = in.readString();
        this.email = in.readString();
        this.career = in.readParcelable(Career.class.getClassLoader());

        // read list by using User.CREATOR
        this.approvedSubjects = new ArrayList<>();
        in.readTypedList(approvedSubjects, Subject.CREATOR);

        this.onGoingSubjects = new ArrayList<>();
        in.readTypedList(onGoingSubjects, Subject.CREATOR);

        this.profileImageUri = in.readString();

        this.singleChatsKeys = new ArrayList<>();
        in.readStringList(singleChatsKeys);

        this.groupChatsKeys = new ArrayList<>();
        in.readStringList(groupChatsKeys);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public List<Subject> getApprovedSubjects() {
        return approvedSubjects;
    }

    public void setApprovedSubjects(List<Subject> approvedSubjects) {
        this.approvedSubjects = approvedSubjects;
    }

    public List<Subject> getOnGoingSubjects() {
        return onGoingSubjects;
    }

    public void setOnGoingSubjects(List<Subject> onGoingSubjects) {
        this.onGoingSubjects = onGoingSubjects;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public List<String> getSingleChatsKeys() {
        return singleChatsKeys;
    }

    public void setSingleChatsKeys(List<String> singleChatsKeys) {
        this.singleChatsKeys = singleChatsKeys;
    }

    public List<String> getGroupChatsKeys() {
        return groupChatsKeys;
    }

    public void setGroupChatsKeys(List<String> groupChatsKeys) {
        this.groupChatsKeys = groupChatsKeys;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(userName)
                .append(" - ")
                .append(email)
                .toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeParcelable(career, i);
        parcel.writeTypedList(approvedSubjects);
        parcel.writeTypedList(onGoingSubjects);
        parcel.writeString(profileImageUri);
        parcel.writeStringList(singleChatsKeys);
        parcel.writeStringList(groupChatsKeys);
    }
}
