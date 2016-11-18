package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.annotations.SerializedName;

public class UserEducation {

    public String degree;

    @SerializedName("school_name")
    public String schoolName;

    @SerializedName("start_date")
    public String startDate;

    @SerializedName("end_date")
    public String endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEducation education = (UserEducation) o;

        if (degree != null ? !degree.equals(education.degree) : education.degree != null)
            return false;
        if (schoolName != null ? !schoolName.equals(education.schoolName) : education.schoolName != null)
            return false;
        if (startDate != null ? !startDate.equals(education.startDate) : education.startDate != null)
            return false;
        return endDate != null ? endDate.equals(education.endDate) : education.endDate == null;

    }

    @Override
    public int hashCode() {
        int result = degree != null ? degree.hashCode() : 0;
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
