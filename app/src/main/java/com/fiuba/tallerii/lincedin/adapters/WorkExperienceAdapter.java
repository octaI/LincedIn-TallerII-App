package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.UserJob;
import com.fiuba.tallerii.lincedin.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.*;

public class WorkExperienceAdapter extends BaseAdapter {

    private final Context context;
    private List<UserJob> dataset = new ArrayList<>();

    public WorkExperienceAdapter(Context context) {
        this.context = context;
    }

    public WorkExperienceAdapter(Context context, List<UserJob> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserJob> dataset) {
        this.dataset = dataset;
        Collections.sort(this.dataset, new JobComparator());
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
                    inflate(R.layout.work_experience_row, parent, false);
        }

        UserJob currentJob = (UserJob) getItem(position);
        if (currentJob != null) {
            ((TextView) convertView.findViewById(R.id.work_experience_row_company_textview)).setText(currentJob.company);
            ((TextView) convertView.findViewById(R.id.work_experience_row_position_name_textview)).setText(currentJob.position.name);
            ((TextView) convertView.findViewById(R.id.work_experience_row_position_category_textview)).setText(currentJob.position.category);
            ((TextView) convertView.findViewById(R.id.work_experience_row_position_description_textview)).setText(currentJob.position.description);
            ((TextView) convertView.findViewById(R.id.work_experience_row_date_range_textview))
                    .setText(
                            ((TextView) convertView.findViewById(R.id.work_experience_row_date_range_textview)).getText().toString()
                                    .replace(":1", extractYearFromDatetime(currentJob.since))
                                    .replace(":2",
                                            currentJob.to != null && !currentJob.to.equals("") ?
                                                    extractYearFromDatetime(currentJob.to)
                                                    : "now"
                                    )
                    );
        }

        return convertView;
    }

    private class JobComparator implements Comparator<UserJob> {

        @Override
        public int compare(UserJob job1, UserJob job2) {
            Integer startDateJob1 = job1.to != null && !job1.to.equals("") ?
                    Integer.valueOf(extractYearFromDatetime(job1.to))
                    : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
            Integer startDateJob2 = job2.to != null && !job2.to.equals("") ?
                    Integer.valueOf(extractYearFromDatetime(job2.to))
                    : Integer.valueOf(extractYearFromDatetime(getActualDatetime())) + 1;
            return startDateJob2.compareTo(startDateJob1);
        }
    }
}
