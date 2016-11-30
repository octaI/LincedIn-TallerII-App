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

import java.util.ArrayList;
import java.util.List;

public class RecommendationsReceivedAdapter extends BaseAdapter {

    private final Context context;
    private List<RecommendationReceived> dataset = new ArrayList<>();

    public RecommendationsReceivedAdapter(Context context) {
        this.context = context;
    }

    public RecommendationsReceivedAdapter(Context context, List<RecommendationReceived> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<RecommendationReceived> dataset) {
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
    public RecommendationReceived getItem(int position) {
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
                    inflate(R.layout.recommendation_received_row, parent, false);
        }

        RecommendationReceived currentRecommendation = dataset.get(position);
        if (currentRecommendation != null) {
            TextView descriptionTextView = (TextView) convertView.findViewById(R.id.recommendation_received_row_description_textview);
            TextView fromUserTextView = (TextView) convertView.findViewById(R.id.recommendation_received_row_from_user_textview);
            descriptionTextView.setText(currentRecommendation.text);
            fromUserTextView.setText(
                    fromUserTextView.getText().toString().toLowerCase().replace(":1", parseUserIdToUsername(currentRecommendation.recommender))
            );
        }

        return convertView;
    }

    private String parseUserIdToUsername(@NonNull String userId) {
        String capitalizedId = userId.substring(0, 1).toUpperCase() + userId.substring(1).toLowerCase();
        return capitalizedId.replaceAll("\\d+.*", "");
    }
}
