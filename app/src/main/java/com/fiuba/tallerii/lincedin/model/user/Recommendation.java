package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.annotations.SerializedName;

public class Recommendation {

    private String description;

    @SerializedName("from_user")
    private String fromUser;

    @SerializedName("for_user")
    private String forUser;

    public Recommendation(String description, String fromUser, String forUser) {
        this.description = description;
        this.fromUser = fromUser;
        this.forUser = forUser;
    }

    public String getDescription() {
        return description;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getForUser() {
        return forUser;
    }
}
