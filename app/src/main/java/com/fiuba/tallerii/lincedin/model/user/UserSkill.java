package com.fiuba.tallerii.lincedin.model.user;

public class UserSkill {

    public String name;

    public String category;

    public String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSkill skill = (UserSkill) o;

        if (name != null ? !name.equals(skill.name) : skill.name != null) return false;
        if (category != null ? !category.equals(skill.category) : skill.category != null)
            return false;
        return description != null ? description.equals(skill.description) : skill.description == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
