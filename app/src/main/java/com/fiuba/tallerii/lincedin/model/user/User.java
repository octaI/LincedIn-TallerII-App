package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.Gson;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

public class User implements Serializable {
    private String fullName;
    private String firstName;
    private String lastName;
    private String description;
    private String dateOfBirth;
    private String email;
    private URL profilePicture;
    private List<UserJob> jobs;
    private List<UserEducation> education;
    private List<UserRecommendation> recommendationsReceived;
    private List<UserSkill> skills;

    public User() {}

    public User(String fullName, String firstName, String lastName, String description, String dateOfBirth, String email, URL profilePicture, List<UserJob> jobs, List<UserEducation> education, List<UserRecommendation> recommendationsReceived, List<UserSkill> skills) {
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.profilePicture = profilePicture;
        this.jobs = jobs;
        this.education = education;
        this.recommendationsReceived = recommendationsReceived;
        this.skills = skills;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public URL getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(URL profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<UserJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<UserJob> jobs) {
        this.jobs = jobs;
    }

    public List<UserEducation> getEducation() {
        return education;
    }

    public void setEducation(List<UserEducation> education) {
        this.education = education;
    }

    public List<UserRecommendation> getRecommendationsReceived() {
        return recommendationsReceived;
    }

    public void setRecommendationsReceived(List<UserRecommendation> recommendationsReceived) {
        this.recommendationsReceived = recommendationsReceived;
    }

    public List<UserSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<UserSkill> skills) {
        this.skills = skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null)
            return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null)
            return false;
        if (description != null ? !description.equals(user.description) : user.description != null)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(user.dateOfBirth) : user.dateOfBirth != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (profilePicture != null ? !profilePicture.equals(user.profilePicture) : user.profilePicture != null)
            return false;
        if (jobs != null ? !jobs.equals(user.jobs) : user.jobs != null) return false;
        if (education != null ? !education.equals(user.education) : user.education != null)
            return false;
        if (recommendationsReceived != null ? !recommendationsReceived.equals(user.recommendationsReceived) : user.recommendationsReceived != null)
            return false;
        return skills != null ? skills.equals(user.skills) : user.skills == null;

    }

    @Override
    public int hashCode() {
        int result = fullName != null ? fullName.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (profilePicture != null ? profilePicture.hashCode() : 0);
        result = 31 * result + (jobs != null ? jobs.hashCode() : 0);
        result = 31 * result + (education != null ? education.hashCode() : 0);
        result = 31 * result + (recommendationsReceived != null ? recommendationsReceived.hashCode() : 0);
        result = 31 * result + (skills != null ? skills.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
