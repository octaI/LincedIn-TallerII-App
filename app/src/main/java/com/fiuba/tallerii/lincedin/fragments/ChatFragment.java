package com.fiuba.tallerii.lincedin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.ChatMessagesAdapter;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.model.chat.ChatMessage;
import com.fiuba.tallerii.lincedin.model.chat.CompleteChat;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    public interface OnChatFragmentInteractionListener {
        void onError();
    }

    private static final String TAG = "Chat";

    private static final String ARG_CHAT_ID = "ARG_CHAT_ID";
    private static final String ARG_RECEIVING_USER_ID = "ARG_USER_RECEIVING_USER_ID";

    private static final int PAGING_SIZE = 20;

    private String chatId;
    private String receivingUserId;

    private ChatMessagesAdapter chatMessagesAdapter;
    private View fragmentView;

    private OnChatFragmentInteractionListener mListener;

    public ChatFragment() {}

    public static ChatFragment newInstance(@Nullable String chatId, @NonNull String receivingUserId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHAT_ID, chatId);
        args.putString(ARG_RECEIVING_USER_ID, receivingUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatId = getArguments().getString(ARG_CHAT_ID);
            receivingUserId = getArguments().getString(ARG_RECEIVING_USER_ID);
        }
    }

    private void retrieveChat() {
        refreshLoadingIndicator(fragmentView, true);
        if (chatId != null) {
            getChatById(chatId, PAGING_SIZE);
        } else {
            createChat(receivingUserId, PAGING_SIZE);
        }
    }

    private void getChatById(String chatId, int size) {
        LincedInRequester.getChat(
                chatId,
                size,
                getContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onChatRetrieved(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorRetrievingChat(error);
                    }
                }
        );
    }

    private void createChat(String receivingUserId, final int pagingSize) {
        LincedInRequester.createChatWithUser(
                receivingUserId,
                getContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, new Gson().toJson(response));
                            chatId = response.getString("chat_id");
                            getChatById(chatId, pagingSize);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            refreshLoadingIndicator(fragmentView, false);
                            mListener.onError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorRetrievingChat(error);
                    }
                }
        );
    }

    private void onChatRetrieved(JSONObject response) {
        Gson gson = new Gson();
        Log.d(TAG, gson.toJson(response));
        Log.i(TAG, "Chat with id " + chatId + " retrieved successfully!");

        CompleteChat chat = gson.fromJson(response.toString(), CompleteChat.class);
        loadMessages(chat);
        refreshLoadingIndicator(fragmentView, false);
    }

    private void onErrorRetrievingChat(VolleyError error) {
        Log.e(TAG, error.toString());
        error.printStackTrace();
        refreshLoadingIndicator(fragmentView, false);
        mListener.onError();
    }

    private void loadMessages(CompleteChat chat) {
        chatMessagesAdapter.setDataset(chat.messages);
        chatMessagesAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        setAdapter();
        setListeners();
        retrieveChat();
        return fragmentView;
    }

    private void setAdapter() {
        chatMessagesAdapter = new ChatMessagesAdapter(getContext());
        ListView messageListView = (ListView) fragmentView.findViewById(R.id.fragment_chat_messages_listview);
        messageListView.setAdapter(chatMessagesAdapter);
        messageListView.setEmptyView(fragmentView.findViewById(android.R.id.empty));

        TextView noMessagesTextView = (TextView) fragmentView.findViewById(android.R.id.empty);
        noMessagesTextView.setText(
                noMessagesTextView.getText().toString().replace(":1", parseUserIdToUsername(receivingUserId))
        );
    }

    private void setListeners() {
        setUsernamesHeader();
        setSendButtonListener();
    }

    private void setUsernamesHeader() {
        TextView otherUsernameTextView = (TextView) fragmentView.findViewById(R.id.fragment_chat_other_username_textview);
        otherUsernameTextView.setText(parseUserIdToUsername(receivingUserId));
        otherUsernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile(receivingUserId);
            }
        });

    }

    private void setSendButtonListener() {
        final EditText messageEditText = (EditText) fragmentView.findViewById(R.id.fragment_chat_message_edittext);
        final ImageButton sendButton = (ImageButton) fragmentView.findViewById(R.id.fragment_chat_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageEditText.getText() != null
                        && messageEditText.getText().toString() != null
                        && !messageEditText.getText().toString().equals("")) {
                    sendNewMessage(messageEditText.getText().toString());
                }
            }
        });
    }

    private void sendNewMessage(final String message) {
        LincedInRequester.sendMessageToChat(
                chatId,
                message,
                getContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.message = message;
                        chatMessage.userId = SharedPreferencesUtils.getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_ID, "");
                        chatMessage.timestamp = Long.toString(System.currentTimeMillis() / 1000L);

                        chatMessagesAdapter.addToDataset(chatMessage);
                        chatMessagesAdapter.notifyDataSetChanged();

                        clearMessage();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        ViewUtils.setSnackbar(fragmentView, R.string.error_send_message_try_again, Snackbar.LENGTH_LONG);
                    }
                }
        );
    }

    private void clearMessage() {
        ((EditText) fragmentView.findViewById(R.id.fragment_chat_message_edittext)).setText(null);

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openUserProfile(String userId) {
        Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
        userProfileIntent.putExtra(UserProfileActivity.ARG_USER_ID, userId);
        startActivity(userProfileIntent);
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.fragment_chat_layout).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.fragment_chat_loading_circular_progress).setVisibility(View.VISIBLE);
        } else {
            v.findViewById(R.id.fragment_chat_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_chat_loading_circular_progress).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatFragmentInteractionListener) {
            mListener = (OnChatFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String parseUserIdToUsername(@NonNull String userId) {
        String capitalizedId = userId.substring(0, 1).toUpperCase() + userId.substring(1).toLowerCase();
        return capitalizedId.replaceAll("\\d+.*", "");
    }
}
