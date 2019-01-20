package com.alonso.myuniapplication.business;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Career implements Parcelable {
    private int code;
    private String name;
    private String description;
    private List<Subject> subjects;

    public Career() {
    }

    public Career(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.subjects = new ArrayList<>();
    }

    public Career(int code, String name, String description, ArrayList<Subject> subjects) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.subjects = subjects;
    }


    protected Career(Parcel in) {
        code = in.readInt();
        name = in.readString();
        description = in.readString();
//        subjects = in.createTypedArrayList(Subject.CREATOR);

        this.subjects = new ArrayList<Subject>();
        in.readTypedList(subjects, Subject.CREATOR);
    }

    public static final Creator<Career> CREATOR = new Creator<Career>() {
        @Override
        public Career createFromParcel(Parcel in) {
            return new Career(in);
        }

        @Override
        public Career[] newArray(int size) {
            return new Career[size];
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

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(name)
                .toString();
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
        parcel.writeTypedList(subjects);
    }
}
