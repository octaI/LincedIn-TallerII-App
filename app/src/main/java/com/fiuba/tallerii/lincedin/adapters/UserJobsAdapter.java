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

import static com.fiuba.tallerii.lincedin.utils.DateUtils.extractYearFromDatetime;
import static com.fiuba.tallerii.lincedin.utils.DateUtils.getActualDatetime;

public class UserJobsAdapter extends BaseAdapter {

    private final Context context;
    private List<UserJob> dataset = new ArrayList<>();

    public UserJobsAdapter(Context context) {
        this.context = context;
    }

    public UserJobsAdapter(Context context, List<UserJob> dataset) {
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
                    inflate(R.layout.user_job_row, parent, false);
        }

        UserJob currentJob = (UserJob) getItem(position);
        if (currentJob != null) {
            ((TextView) convertView.findViewById(R.id.user_job_row_company_textview)).setText(currentJob.company);
            ((TextView) convertView.findViewById(R.id.user_job_row_position_textview)).setText(currentJob.position.name);
            ((TextView) convertView.findViewById(R.id.user_job_row_date_range_textview))
                    .setText(
                            ((TextView) convertView.findViewById(R.id.user_job_row_date_range_textview)).getText().toString()
                                    .replace(":1", DateUtils.extractYearFromDatetime(currentJob.since))
                                    .replace(":2",
                                            currentJob.to != null && !currentJob.to.equals("") ?
                                                    DateUtils.extractYearFromDatetime(currentJob.to)
                                                    : context.getResources().getString(R.string.now).toLowerCase()
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
            return startDateJob2.compareTo(startDateJob1) == 0 ?
                    startDateJob2.compareTo(startDateJob1) : job2.since.compareTo(job1.since);
        }
    }
}
