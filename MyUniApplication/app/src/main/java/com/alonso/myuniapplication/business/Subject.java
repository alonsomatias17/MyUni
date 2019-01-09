package com.alonso.myuniapplication.business;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {

    private int code;
    private String name;
    private String description;
    private String state;
    private int year;

    public static final String APPROVED = "Aprobada";
    public static final String ONGOING = "En curso";
    public static final String NOT_APPROVED = "No aprobada";

    public Subject() {
    }

    public Subject(int code, String name, String description, int year) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.year = year;
        this.state = NOT_APPROVED;
    }

    protected Subject(Parcel in) {
        code = in.readInt();
        name = in.readString();
        description = in.readString();
        state = in.readString();
        year = in.readInt();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(state);
        parcel.writeInt(year);
    }

    public void changeState() {
        if(this.state.equals(NOT_APPROVED)){
            this.state = APPROVED;
            return;
        }
        if(this.state.equals(APPROVED)){
            this.state = NOT_APPROVED;
            return;
        }

    }

    public boolean isApproved() {
        return this.state.equals(APPROVED);
    }
}
