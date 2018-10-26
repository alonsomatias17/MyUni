package com.alonso.myuniapplication.business;

import java.util.ArrayList;
import java.util.List;

public class University {

    private int code;
    private String name;
    private String description;
    private List<Career> careers;

    public University() {
    }

    public University(int code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.careers = new ArrayList<>();
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

    public List<Career> getCareers() {
        return careers;
    }

    public void setCareers(List<Career> careers) {
        this.careers = careers;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(name)
                .toString();
    }
}
