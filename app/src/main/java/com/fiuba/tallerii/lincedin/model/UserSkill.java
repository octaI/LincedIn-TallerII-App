package com.fiuba.tallerii.lincedin.model;

import java.io.Serializable;

public class UserSkill implements Serializable {
    private String name;
    private String category;
    private String description;

    public UserSkill() {}

    public UserSkill(String name, String category, String description) {
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

        UserSkill userSkill = (UserSkill) o;

        if (!name.equals(userSkill.name)) return false;
        if (!category.equals(userSkill.category)) return false;
        return description != null ? description.equals(userSkill.description) : userSkill.description == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
