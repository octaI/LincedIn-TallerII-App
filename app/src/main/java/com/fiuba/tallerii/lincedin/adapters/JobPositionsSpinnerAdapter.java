package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.UserJobPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobPositionsSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context context;
    private List<UserJobPosition> dataset = new ArrayList<>();

    public JobPositionsSpinnerAdapter(Context context) {
        this.context = context;
    }

    public JobPositionsSpinnerAdapter(Context context, List<UserJobPosition> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserJobPosition> dataset) {
        this.dataset = dataset;
        Collections.sort(this.dataset, new UserJobPositionComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public UserJobPosition getItem(int position) {
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
                    inflate(R.layout.user_job_position_spinner_row, parent, false);
        }

        UserJobPosition currentPosition = getItem(position);
        if (currentPosition != null) {
            ((TextView) convertView.findViewById(R.id.user_job_position_spinner_row_name_textview)).setText(currentPosition.name);
            ((TextView) convertView.findViewById(R.id.user_job_position_spinner_row_category_textview)).setText(currentPosition.category);
        }

        return convertView;
    }

    private class UserJobPositionComparator implements Comparator<UserJobPosition> {

        @Override
        public int compare(UserJobPosition position1, UserJobPosition position2) {
            return (position1.category + position1.name + position1.description)
                    .compareTo(position2.category + position2.name + position2.description);
        }
    }
}
