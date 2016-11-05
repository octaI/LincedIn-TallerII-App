package com.fiuba.tallerii.lincedin.model.user;

import com.fiuba.tallerii.lincedin.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("full_name")
    public String fullName;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    public String description;

    @SerializedName("date_of_birth")
    public String dateOfBirth;

    public String email;

    @SerializedName("profile_picture")
    public String profilePicture;

    public List<UserJob> jobs;

    public List<UserEducation> education;

    @SerializedName("recommendatios_received")
    public List<UserRecommendation> recommendationsReceived;

    public List<UserSkill> skills;

    public boolean isCurrentlyWorking() {
        if (jobs != null && !jobs.isEmpty()) {
            for (UserJob job : jobs) {
                if (job.to == null || job.to.equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    public UserJob getCurrentWork() {
        if (isCurrentlyWorking()) {
            for (UserJob job : jobs) {
                if (job.to == null || job.to.equals("")) {
                    return job;
                }
            }
        }
        return null;
    }
}
