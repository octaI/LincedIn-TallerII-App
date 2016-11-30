package com.fiuba.tallerii.lincedin.model.chat;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ChatRow {
    private String chatId;
    private List<String> users;
    private String lastMessage;
    private String timestamp;

    public ChatRow(String chatId, List<String> users, String lastMessage, String timestamp) {
        this.chatId = chatId;
        this.users = users;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public List<String> getUsers() {
        return users;
    }

    public void addUser(String user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
