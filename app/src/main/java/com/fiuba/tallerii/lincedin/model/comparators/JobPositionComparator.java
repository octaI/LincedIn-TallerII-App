package com.fiuba.tallerii.lincedin.model.comparators;

import com.fiuba.tallerii.lincedin.model.user.UserJobPosition;

import java.util.Comparator;

public class JobPositionComparator implements Comparator<UserJobPosition> {
    @Override
    public int compare(UserJobPosition position1, UserJobPosition position2) {
        return (position1.category + position1.name + position1.description)
                .compareTo(position2.category + position2.name + position2.description);
    }
}
