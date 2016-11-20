package com.fiuba.tallerii.lincedin.model.comparators;

import com.fiuba.tallerii.lincedin.model.user.UserSkill;

import java.util.Comparator;

public class SkillComparator implements Comparator<UserSkill> {
    @Override
    public int compare(UserSkill skill1, UserSkill skill2) {
        return (skill1.category + skill1.name + skill1.description)
                .compareTo(skill2.category + skill2.name + skill2.description);
    }
}
