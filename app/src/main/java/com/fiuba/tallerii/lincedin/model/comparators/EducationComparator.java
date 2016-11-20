package com.fiuba.tallerii.lincedin.model.comparators;

import com.fiuba.tallerii.lincedin.model.user.UserEducation;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import java.util.Comparator;

public class EducationComparator implements Comparator<UserEducation> {
    @Override
    public int compare(UserEducation education1, UserEducation education2) {
        return -1 * ( Integer.valueOf(DateUtils.extractYearFromDatetime(education1.startDate))
                .compareTo(Integer.valueOf(DateUtils.extractYearFromDatetime(education2.startDate))) );
    }
}
