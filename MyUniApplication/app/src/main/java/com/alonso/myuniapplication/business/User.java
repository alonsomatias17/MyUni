package com.alonso.myuniapplication.business;

import java.util.List;

public class User {

    private String userName;
    private String email;
    private Career career;
    private List<Subject> approvedSubjects;


    public User(String userName, String email, Career career) {
        this.userName = userName;
        this.email = email;
        this.career = career;
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
}
