package com.fiuba.tallerii.lincedin.model.user;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserJobPosition implements Serializable {
    private String name;
    private String category;
    private String description;

    public UserJobPosition() {}

    public UserJobPosition(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserJobPosition userJobPosition = (UserJobPosition) o;

        if (!name.equals(userJobPosition.name)) return false;
        if (!category.equals(userJobPosition.category)) return false;
        return description != null ? description.equals(userJobPosition.description) : userJobPosition.description == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
