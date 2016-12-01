package com.fiuba.tallerii.lincedin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.model.chat.ChatRow;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.fiuba.tallerii.lincedin.utils.DateUtils.parseTimestampToDatetime;

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

    public void addToDataset(Chat chat) {
        this.dataset.add(chat);
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
                    inflate(R.layout.chat_row, parent, false);
        }

        final Chat currentChat = dataset.get(position);
        if (currentChat != null && currentChat.lastMessage != null) {
            setUsersTextView(currentChat, convertView);
            TextView lastMessageTextView = (TextView) convertView.findViewById(R.id.chat_row_last_message_textview);
            lastMessageTextView.setText(
                    lastMessageTextView.getText().toString()
                            .replace(":1", SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.USER_ID, "").equals(currentChat.lastMessage.userId) ? context.getString(R.string.you) : parseUserIdToUsername(currentChat.lastMessage.userId))
                            .replace(":2", currentChat.lastMessage.message)
            );
            ((TextView) convertView.findViewById(R.id.chat_row_date_last_message_textview))
                    .setText(parseTimestampToDatetime(currentChat.lastMessage.timestamp));
        }

        return convertView;
    }

    private void setUsersTextView(Chat chat, View convertView) {
        String users = "";
        for (int i = 0; i < chat.participants.size(); i++) {
            String userId = chat.participants.get(i);
            if (!SharedPreferencesUtils.getStringFromSharedPreferences(context, SharedPreferencesKeys.USER_ID, "").equals(userId)) {
                if (i > 0 && !users.equals("")) {
                    users += ", ";
                }
                users += parseUserIdToUsername(userId);
            }
        }
        ((TextView) convertView.findViewById(R.id.chat_row_username_textview)).setText(users);
    }

    private String parseUserIdToUsername(@NonNull String userId) {
        String capitalizedId = userId.substring(0, 1).toUpperCase() + userId.substring(1).toLowerCase();
        return capitalizedId.replaceAll("\\d+.*", "");
    }
}
