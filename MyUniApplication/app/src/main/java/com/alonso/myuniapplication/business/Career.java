package com.alonso.myuniapplication.business;

import java.util.ArrayList;
import java.util.List;

public class Career {
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

    public Career(String name, String description) {
        this.name = name;
        this.description = description;
        this.subjects = new ArrayList<>();
    }

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
}
