package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserJob implements Serializable {
    private String company;
    private UserJobPosition position;
    private String since;
    private String to;

    public UserJob() {}

    public UserJob(String company, UserJobPosition position, String since, String to) {
        this.company = company;
        this.position = position;
        this.since = since;
        this.to = to;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public UserJobPosition getPosition() {
        return position;
    }

    public void setPosition(UserJobPosition position) {
        this.position = position;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJob userJob = (UserJob) o;

        if (company != null ? !company.equals(userJob.company) : userJob.company != null)
            return false;
        if (position != null ? !position.equals(userJob.position) : userJob.position != null)
            return false;
        if (since != null ? !since.equals(userJob.since) : userJob.since != null) return false;
        return to != null ? to.equals(userJob.to) : userJob.to == null;

    }

    @Override
    public int hashCode() {
        int result = company != null ? company.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (since != null ? since.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
