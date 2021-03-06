package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.comparators.SkillComparator;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllSkillsAdapter extends BaseAdapter {

    private final Context context;
    private List<UserSkill> dataset = new ArrayList<>();

    public AllSkillsAdapter(Context context) {
        this.context = context;
    }

    public AllSkillsAdapter(Context context, List<UserSkill> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<UserSkill> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
        Collections.sort(this.dataset, new SkillComparator());
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public UserSkill getItem(int position) {
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
                    inflate(R.layout.skills_row, parent, false);
        }

        UserSkill currentSkill = getItem(position);
        if (currentSkill != null) {
            ((TextView) convertView.findViewById(R.id.skills_row_name_textview)).setText(currentSkill.name);
            ((TextView) convertView.findViewById(R.id.skills_row_category_textview)).setText(currentSkill.category);
            ((TextView) convertView.findViewById(R.id.skills_row_description_textview)).setText(currentSkill.description);
        }

        return convertView;
    }
}
