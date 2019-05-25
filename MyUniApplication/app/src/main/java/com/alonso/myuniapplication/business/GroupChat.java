package com.alonso.myuniapplication.business;

import java.util.ArrayList;
import java.util.List;

public class GroupChat {

    private List<String> participants;
    private List<ChatMessage> messages;

    public GroupChat() {
        participants = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
