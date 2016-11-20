package com.fiuba.tallerii.lincedin.model.user.login;

public class LincedInLogInUser extends LogInUser {

    private String email;
    private String password;

    public LincedInLogInUser(String email, String password) {
        super("Normal");
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
