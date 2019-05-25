package com.alonso.myuniapplication.business;

import java.util.ArrayList;
import java.util.List;

public class SingleChat {

    private String key;
    private List<UserDTO> participants;
    private List<ChatMessage> messages;

    public SingleChat() {
        key = "";
        participants = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public SingleChat(String key) {
        this.key = key;
        participants = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDTO> participants) {
        this.participants = participants;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage chatMessage) {
        messages.add(chatMessage);
    }
}
