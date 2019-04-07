package com.alonso.myuniapplication.business;

public class ChatMessage {

    private String senderEmail;
    private String date;
    private String message;
    private String name;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String senderEmail, String date, String message, String name, String time) {
        this.senderEmail = senderEmail;
        this.date = date;
        this.message = message;
        this.name = name;
        this.time = time;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
