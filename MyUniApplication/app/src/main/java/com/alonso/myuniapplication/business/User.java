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


    public User(){
    }

    public User(String userName, String email, Career career) {
        this.userName = userName;
        this.email = email;
        this.career = career;
        approvedSubjects = new ArrayList<>();
    }

    protected User(Parcel in) {
        userName = in.readString();
        email = in.readString();
//        career = in.readParcelable(Career.class.getClassLoader());
//        approvedSubjects = in.createTypedArrayList(Subject.CREATOR);
        this.career = in.readParcelable(Career.class.getClassLoader());

        // read list by using User.CREATOR
        this.approvedSubjects = new ArrayList<Subject>();
        in.readTypedList(approvedSubjects, Subject.CREATOR);
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

    public void addSubject(Subject subject){
        approvedSubjects.add(subject);
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
//        parcel.writeParcelable(career, i);
//        parcel.writeTypedList(approvedSubjects);
        parcel.writeParcelable(career, i);
        parcel.writeTypedList(approvedSubjects);
    }
}
