package com.fiuba.tallerii.lincedin.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
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

    private static final String ARG_RECEIVING_USER_ID = "ARG_USER_RECEIVING_USER_ID";
    private String receivingUserId;

    private OnChatFragmentInteractionListener mListener;

    public ChatFragment() {}

    public static ChatFragment newInstance(@NonNull String receivingUserId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIVING_USER_ID, receivingUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            receivingUserId = getArguments().getString(ARG_RECEIVING_USER_ID);
        }
    }

    private void retrieveChat(final View fragmentView) {
        refreshLoadingIndicator(fragmentView, true);
        LincedInRequester.getAllUserChats(
                getContext(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Log.d(TAG, gson.toJson(response));

                        List<Chat> chats = new ArrayList<>();
                        Type chatListType = new TypeToken<List<Chat>>() {}.getType();
                        try {
                            chats = gson.fromJson(response.getString("chats"), chatListType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getAlreadyExistentChat(chats);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        error.printStackTrace();
                        refreshLoadingIndicator(fragmentView, false);
                        mListener.onError();
                    }
                }
        );
    }

    private @Nullable Chat getAlreadyExistentChat(List<Chat> chats) {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        retrieveChat(fragmentView);
        return fragmentView;
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
}
