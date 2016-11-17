package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.UserEducation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.extractYearFromDatetime;
import static com.fiuba.tallerii.lincedin.utils.DateUtils.getActualDatetime;

public class AllEducationAdapter extends BaseAdapter {
    private final Context context;
    private List<UserEducation> dataset = new ArrayList<>();

    public AllEducationAdapter(Context context) {
        this.context = context;
    }

    public AllEducationAdapter(Context context, List<UserEducation> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserEducation> dataset) {
        this.dataset = dataset;
        Collections.sort(this.dataset, new EducationComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public UserEducation getItem(int position) {
        return dataset.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.education_row, parent, false);
        }

        UserEducation currentEducation = getItem(position);
        if (currentEducation != null) {
            ((TextView) convertView.findViewById(R.id.education_row_school_textview)).setText(currentEducation.schoolName);
            ((TextView) convertView.findViewById(R.id.education_row_degree_textview)).setText(currentEducation.degree);
            ((TextView) convertView.findViewById(R.id.education_row_date_range_textview))
                    .setText(
                            ((TextView) convertView.findViewById(R.id.education_row_date_range_textview)).getText().toString()
                                    .replace(":1", extractYearFromDatetime(currentEducation.startDate))
                                    .replace(":2",
                                            currentEducation.endDate != null && !currentEducation.endDate.equals("") ?
                                                    extractYearFromDatetime(currentEducation.endDate)
                                                    : context.getResources().getString(R.string.now).toLowerCase()
                                    )
                    );
        }

        return convertView;
    }

    private class EducationComparator implements Comparator<UserEducation> {

        @Override
        public int compare(UserEducation education1, UserEducation education2) {
            Integer startDateJob1 = education1.endDate != null && !education1.endDate.equals("") ?
                    Integer.valueOf(extractYearFromDatetime(education1.endDate))
                    : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
            Integer startDateJob2 = education2.endDate != null && !education2.endDate.equals("") ?
                    Integer.valueOf(extractYearFromDatetime(education2.endDate))
                    : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
            return startDateJob2.compareTo(startDateJob1) == 0 ?
                    startDateJob2.compareTo(startDateJob1) : education2.startDate.compareTo(education1.startDate);
        }
    }
}
