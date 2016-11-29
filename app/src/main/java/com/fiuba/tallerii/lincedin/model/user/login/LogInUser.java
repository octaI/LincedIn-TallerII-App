package com.fiuba.tallerii.lincedin.model.user.login;

import com.google.gson.annotations.SerializedName;

public abstract class LogInUser {
    private final String type;

    @SerializedName("firebase_id")
    private final String firebaseId;

    public LogInUser(String type, String firebaseId) {
        this.type = type;
        this.firebaseId = firebaseId;
    }

    public String getType() {
        return type;
    }

    public String getFirebaseId() {
        return firebaseId;
    }
}
