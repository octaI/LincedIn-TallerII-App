package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends BaseAdapter {

    private Context context;
    private List<Chat> dataset = new ArrayList<>();

    public ChatsAdapter(Context context) {
        this.context = context;
    }

    public ChatsAdapter(Context context, List<Chat> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<Chat> dataset) {
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
    public Chat getItem(int position) {
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
                    inflate(R.layout.education_row, parent, false);
        }

        return convertView;
    }
}
