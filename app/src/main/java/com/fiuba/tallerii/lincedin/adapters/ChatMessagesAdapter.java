package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.CompleteChatMessage;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.parseTimestampToDatetime;

public class ChatMessagesAdapter extends BaseAdapter {

    private Context context;
    private List<CompleteChatMessage> dataset = new ArrayList<>();

    public ChatMessagesAdapter(Context context) {
        this.context = context;
    }

    public ChatMessagesAdapter(Context context, List<CompleteChatMessage> dataset) {
        this.context = context;
        setDataset(dataset);
    }

    public void setDataset(List<CompleteChatMessage> dataset) {
        if (dataset == null) {
            dataset = new ArrayList<>();
        }
        this.dataset = dataset;
    }

    public void addToDataset(CompleteChatMessage message) {
        dataset.add(message);
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public CompleteChatMessage getItem(int position) {
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
                    inflate(R.layout.chat_message_row, parent, false);
        }

        CompleteChatMessage currentMessage = dataset.get(position);
        if (currentMessage != null) {
            selectBubbleSide(convertView, currentMessage);
            setMessages(convertView, currentMessage);
            setDates(convertView, currentMessage);
        }

        return convertView;
    }

    private void selectBubbleSide(View convertView, CompleteChatMessage message) {
        if (isOwnMessage(message)) {
            convertView.findViewById(R.id.chat_message_row_left_bubble).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.chat_message_row_right_bubble).setVisibility(View.GONE);
        } else {
            convertView.findViewById(R.id.chat_message_row_left_bubble).setVisibility(View.GONE);
            convertView.findViewById(R.id.chat_message_row_right_bubble).setVisibility(View.VISIBLE);
        }
    }

    private void setMessages(View convertView, CompleteChatMessage message) {
        ((TextView) convertView.findViewById(R.id.chat_message_row_message_left_textview)).setText(message.message);
        ((TextView) convertView.findViewById(R.id.chat_message_row_message_right_textview)).setText(message.message);
    }

    private void setDates(View convertView, CompleteChatMessage message) {
        ((TextView) convertView.findViewById(R.id.chat_message_row_date_left_textview))
                .setText(parseTimestampToDatetime(message.timestamp));
        ((TextView) convertView.findViewById(R.id.chat_message_row_date_right_textview))
                .setText(parseTimestampToDatetime(message.timestamp));
    }

    private boolean isOwnMessage(CompleteChatMessage message) {
        return SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.USER_ID, "").equals(message.userId);
    }
}
