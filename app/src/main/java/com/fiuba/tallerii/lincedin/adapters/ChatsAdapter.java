package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.model.chat.ChatRow;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends BaseAdapter {

    private Context context;
    private List<ChatRow> dataset = new ArrayList<>();

    public ChatsAdapter(Context context) {
        this.context = context;
    }

    public ChatsAdapter(Context context, List<ChatRow> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<ChatRow> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
    }

    public void addToDataset(ChatRow chat) {
        this.dataset.add(chat);
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public ChatRow getItem(int position) {
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
                    inflate(R.layout.chat_row, parent, false);
        }

        return convertView;
    }
}
