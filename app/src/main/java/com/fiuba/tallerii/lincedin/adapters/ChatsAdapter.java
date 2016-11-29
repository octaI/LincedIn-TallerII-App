package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.ChatRow;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.parseTimestampToDatetime;

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

        final ChatRow currentChat = dataset.get(position);
        if (currentChat != null) {
            setUsersTextView(currentChat, convertView);
            ((TextView) convertView.findViewById(R.id.chat_row_last_message_textview)).setText(currentChat.getLastMessage());
            ((TextView) convertView.findViewById(R.id.chat_row_date_last_message_textview))
                    .setText(parseTimestampToDatetime(currentChat.getTimestamp()));
        }

        return convertView;
    }

    private void setUsersTextView(ChatRow chat, View convertView) {
        String users = "";
        for (int i = 0; i < chat.getUsers().size(); i++) {
            String userId = chat.getUsers().get(i);
            if (!SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.USER_ID, "").equals(userId)) {
                if (i > 0) {
                    users += ", ";
                }
                users += userId;
            }
        }
        ((TextView) convertView.findViewById(R.id.chat_row_username_textview)).setText(users);
    }
}
