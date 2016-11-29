package com.fiuba.tallerii.lincedin.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Chat {

    @SerializedName("chat_id")
    public String chatId;

    public List<String> participants;

    @SerializedName("last_message")
    public ChatMessage lastMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        if (chatId != null ? !chatId.equals(chat.chatId) : chat.chatId != null) return false;
        if (participants != null ? !participants.equals(chat.participants) : chat.participants != null)
            return false;
        return lastMessage != null ? lastMessage.equals(chat.lastMessage) : chat.lastMessage == null;

    }

    @Override
    public int hashCode() {
        int result = chatId != null ? chatId.hashCode() : 0;
        result = 31 * result + (participants != null ? participants.hashCode() : 0);
        result = 31 * result + (lastMessage != null ? lastMessage.hashCode() : 0);
        return result;
    }
}
