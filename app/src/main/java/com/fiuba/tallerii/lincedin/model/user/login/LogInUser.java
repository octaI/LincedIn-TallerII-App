package com.fiuba.tallerii.lincedin.model.user.login;

import org.json.JSONObject;

public abstract class LogInUser {
    private final String type;

    public LogInUser(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
