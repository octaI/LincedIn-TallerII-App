package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.recommendations.RecommendationReceived;
import com.fiuba.tallerii.lincedin.model.recommendations.RecommendationSent;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsMadeAdapter extends BaseAdapter {
    private final Context context;
    private List<RecommendationSent> dataset = new ArrayList<>();

    public RecommendationsMadeAdapter(Context context) {
        this.context = context;
    }

    public RecommendationsMadeAdapter(Context context, List<RecommendationSent> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<RecommendationSent> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
    }

    public boolean removeFromDataset(RecommendationSent recommendation) {
        return this.dataset.remove(recommendation);
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public RecommendationSent getItem(int position) {
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
                    inflate(R.layout.recommendation_made_row, parent, false);
        }

        RecommendationSent currentRecommendation = dataset.get(position);
        if (currentRecommendation != null) {
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.recommendation_made_row_description_textview);
            TextView toUserTextView = (TextView) convertView.findViewById(R.id.recommendation_made_row_for_user_textview);
            descriptionTextView.setText(currentRecommendation.text);
            toUserTextView.setText(
                    toUserTextView.getText().toString().toLowerCase().replace(":1", parseUserIdToUsername(currentRecommendation.userId))
            );
        }

        return convertView;
    }

    private String parseUserIdToUsername(@NonNull String userId) {
        String capitalizedId = userId.substring(0, 1).toUpperCase() + userId.substring(1).toLowerCase();
        return capitalizedId.replaceAll("\\d+.*", "");
    }
}
