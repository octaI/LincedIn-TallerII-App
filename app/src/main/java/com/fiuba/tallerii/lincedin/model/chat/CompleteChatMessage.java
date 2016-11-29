package com.fiuba.tallerii.lincedin.model.chat;

import com.google.gson.annotations.SerializedName;

public class CompleteChatMessage {

    @SerializedName("user_id")
    public String userId;

    @SerializedName("user_name")
    public String userName;

    public String message;

    public long timestamp;

}
