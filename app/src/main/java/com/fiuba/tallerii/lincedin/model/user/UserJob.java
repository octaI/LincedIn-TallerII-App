package com.fiuba.tallerii.lincedin.model.user;

public class UserJob {

    public String company;

    public UserJobPosition position;

    public String date_since;

    public String date_to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJob userJob = (UserJob) o;

        if (company != null ? !company.equals(userJob.company) : userJob.company != null)
            return false;
        if (position != null ? !position.equals(userJob.position) : userJob.position != null)
            return false;
        if (date_since != null ? !date_since.equals(userJob.date_since) : userJob.date_since != null) return false;
        return date_to != null ? date_to.equals(userJob.date_to) : userJob.date_to == null;

    }

    @Override
    public int hashCode() {
        int result = company != null ? company.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (date_since != null ? date_since.hashCode() : 0);
        result = 31 * result + (date_to != null ? date_to.hashCode() : 0);
        return result;
    }
}
