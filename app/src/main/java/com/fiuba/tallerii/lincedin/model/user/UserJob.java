package com.fiuba.tallerii.lincedin.model.user;

public class UserJob {

    public String company;

    public UserJobPosition position;

    public String since;

    public String to;

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
}
