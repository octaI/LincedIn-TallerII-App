package com.fiuba.tallerii.lincedin.model.user.login;

public class LincedInLogInUser extends LogInUser {

    private String email;
    private String password;

    public LincedInLogInUser(String email, String password, String firebaseId) {
        super("Normal", firebaseId);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
