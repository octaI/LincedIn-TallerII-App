package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.user.Recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationsReceivedAdapter extends BaseAdapter {

    private final Context context;
    private List<Recommendation> dataset = new ArrayList<>();

    public RecommendationsReceivedAdapter(Context context) {
        this.context = context;
    }

    public RecommendationsReceivedAdapter(Context context, List<Recommendation> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<Recommendation> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public Recommendation getItem(int position) {
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

        Recommendation currentRecommendation = dataset.get(position);
        if (currentRecommendation != null) {
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.recommendation_received_row_description_textview);
            TextView fromUserTextView = (TextView) convertView.findViewById(R.id.recommendation_received_row_from_user_textview);
            descriptionTextView.setText(currentRecommendation.getDescription());
            fromUserTextView.setText(
                    fromUserTextView.getText().toString().replace(":1", currentRecommendation.getFrom())
            );
        }

        return convertView;
    }
}
