package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserEducation implements Serializable {
    private String degree;
    private String schoolName;
    private String startDate;
    private String endDate;

    public UserEducation() {}

    public UserEducation(String degree, String schoolName, String startDate, String endDate) {
        this.degree = degree;
        this.schoolName = schoolName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEducation that = (UserEducation) o;

        if (degree != null ? !degree.equals(that.degree) : that.degree != null) return false;
        if (schoolName != null ? !schoolName.equals(that.schoolName) : that.schoolName != null)
            return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        return endDate != null ? endDate.equals(that.endDate) : that.endDate == null;

    }

    @Override
    public int hashCode() {
        int result = degree != null ? degree.hashCode() : 0;
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
