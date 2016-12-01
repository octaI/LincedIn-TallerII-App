package com.fiuba.tallerii.lincedin.model.comparators;

import com.fiuba.tallerii.lincedin.model.user.UserJob;

import java.util.Comparator;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.extractYearFromDatetime;
import static com.fiuba.tallerii.lincedin.utils.DateUtils.getActualDatetime;

public class JobComparator implements Comparator<UserJob> {
    @Override
    public int compare(UserJob job1, UserJob job2) {
        Integer startDateJob1 = job1.date_to != null && !job1.date_to.equals("") ?
                Integer.valueOf(extractYearFromDatetime(job1.date_to))
                : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
        Integer startDateJob2 = job2.date_to != null && !job2.date_to.equals("") ?
                Integer.valueOf(extractYearFromDatetime(job2.date_to))
                : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
        return startDateJob2.compareTo(startDateJob1) == 0 ?
                startDateJob2.compareTo(startDateJob1) : job2.date_since.compareTo(job1.date_since);
    }
}
