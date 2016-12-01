package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.comparators.JobComparator;
import com.fiuba.tallerii.lincedin.model.user.UserJob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.*;

public class AllJobsAdapter extends BaseAdapter {

    private final Context context;
    private List<UserJob> dataset = new ArrayList<>();

    public AllJobsAdapter(Context context) {
        this.context = context;
    }

    public AllJobsAdapter(Context context, List<UserJob> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserJob> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
        Collections.sort(this.dataset, new JobComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public UserJob getItem(int position) {
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
                                    .replace(":1", extractYearFromDatetime(currentJob.date_since))
                                    .replace(":2",
                                            currentJob.date_to != null && !currentJob.date_to.equals("") ?
                                                    extractYearFromDatetime(currentJob.date_to)
                                                    : context.getResources().getString(R.string.now).toLowerCase()
                                    )
                    );
        }

        return convertView;
    }
}
