package com.fiuba.tallerii.lincedin.model.user.login;

import com.google.gson.annotations.SerializedName;

public class FacebookLogInUser extends LogInUser {

    @SerializedName("fb_token")
    private String fbToken;

    public FacebookLogInUser(String fbToken) {
        super("Facebook");
        this.fbToken = fbToken;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
}
