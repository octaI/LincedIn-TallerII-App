package com.fiuba.tallerii.lincedin.model.chat;

import com.google.gson.annotations.SerializedName;

public class ChatMessage {

    public String message;

    @SerializedName("user_id")
    public String userId;

    public String timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
