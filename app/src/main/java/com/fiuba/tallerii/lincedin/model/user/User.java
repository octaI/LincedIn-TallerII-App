package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    public String id;

    @SerializedName("firebase_id")
    public String firebaseId;

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

    public List<UserSkill> skills;

    public boolean isCurrentlyWorking() {
        if (jobs != null && !jobs.isEmpty()) {
            for (UserJob job : jobs) {
                if (job.date_to == null || job.date_to.equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    public UserJob getCurrentWork() {
        if (isCurrentlyWorking()) {
            for (UserJob job : jobs) {
                if (job.date_to == null || job.date_to.equals("")) {
                    return job;
                }
            }
        }
        return null;
    }
}
