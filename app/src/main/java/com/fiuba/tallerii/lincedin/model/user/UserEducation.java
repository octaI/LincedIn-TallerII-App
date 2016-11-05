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

}
