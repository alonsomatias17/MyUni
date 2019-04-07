package com.alonso.myuniapplication.business;

import java.util.ArrayList;
import java.util.List;

public class SingleChat {

    private List<UserDTO> participants;
    private List<ChatMessage> messages;

    public SingleChat() {
        participants = new ArrayList<>();
        messages = new ArrayList<>();
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
