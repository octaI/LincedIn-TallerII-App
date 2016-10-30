package com.fiuba.tallerii.lincedin.model;

import com.google.gson.Gson;

import java.net.URL;

public class UserAccount {
    private String userId;
    private String sessionToken;
    private String fullName;
    private String firstName;
    private String lastName;
    private String email;
    private String birthday;
    private String location;
    public UserAccount() {}

    public UserAccount(String userId, String sessionToken, String fullName, String firstName, String lastName, String email, String birthday, String location) {
        this.userId = userId;
        this.sessionToken = sessionToken;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (sessionToken != null ? !sessionToken.equals(that.sessionToken) : that.sessionToken != null)
            return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null)
            return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null)
            return false;
        return location != null ? location.equals(that.location) : that.location == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (sessionToken != null ? sessionToken.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
