package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.comparators.EducationComparator;
import com.fiuba.tallerii.lincedin.model.user.UserEducation;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserEducationAdapter extends BaseAdapter {
    private final Context context;
    private List<UserEducation> dataset = new ArrayList<>();

    public UserEducationAdapter(Context context) {
        this.context = context;
    }

    public UserEducationAdapter(Context context, List<UserEducation> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserEducation> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
        Collections.sort(this.dataset, new EducationComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Object getItem(int position) {
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
                    inflate(R.layout.user_education_row, parent, false);
        }

        UserEducation currentEducation = (UserEducation) getItem(position);
        if (currentEducation != null) {
            ((TextView) convertView.findViewById(R.id.user_education_row_degree_textview)).setText(currentEducation.degree);
            ((TextView) convertView.findViewById(R.id.user_education_row_institute_textview)).setText(currentEducation.schoolName);
            if (currentEducation.endDate != null) {
                ((TextView) convertView.findViewById(R.id.user_education_row_date_range_textview))
                        .setText(
                                ((TextView) convertView.findViewById(R.id.user_education_row_date_range_textview)).getText().toString()
                                        .replace(":1", DateUtils.extractYearFromDatetime(currentEducation.startDate))
                                        .replace(":2", DateUtils.extractYearFromDatetime(currentEducation.endDate))
                        );
            } else {
                ((TextView) convertView.findViewById(R.id.user_education_row_date_range_textview))
                        .setText(
                                ((TextView) convertView.findViewById(R.id.user_education_row_date_range_textview)).getText().toString()
                                        .replace(":1", DateUtils.extractYearFromDatetime(currentEducation.startDate))
                                        .replace(":2", context.getString(R.string.now).toLowerCase())
                        );
            }

        }

        return convertView;
    }
}
