package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.UserSkill;

import java.util.ArrayList;
import java.util.List;

public class UserSkillsAdapter extends BaseAdapter {

    private final Context context;
    private List<UserSkill> dataset = new ArrayList<>();

    public UserSkillsAdapter(Context context) {
        this.context = context;
    }

    public UserSkillsAdapter(Context context, List<UserSkill> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    public void setDataset(List<UserSkill> dataset) {
        this.dataset = dataset;
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
                    inflate(R.layout.user_skill_row, parent, false);
        }

        UserSkill currentSkill = (UserSkill) getItem(position);
        if (currentSkill != null) {
            ((TextView) convertView.findViewById(R.id.user_skill_row_name_textview)).setText(currentSkill.getName());
            ((TextView) convertView.findViewById(R.id.user_skill_row_category_textview)).setText(currentSkill.getCategory());
            ((TextView) convertView.findViewById(R.id.user_skill_row_description_textview)).setText(currentSkill.getDescription());
        }

        return convertView;
    }
}
