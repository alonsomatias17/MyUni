package com.alonso.myuniapplication.business;

public class Subject {

    private int code;
    private String name;
    private String description;
    private String state;
    private int year;

    private static final String APPROVED = "Aprobada";
    private static final String ONGOING = "En curso";
    private static final String NOT_APPROVED = "No aprobada";

    public Subject() {
    }

    public Subject(int code, String name, String description, int year) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.year = year;
        this.state = NOT_APPROVED;
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
}
