package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.ChatsAdapter;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private static final String TAG = "Chats";

    private View fragmentView;
    private List<Chat> chats = new ArrayList<>();
    private ChatsAdapter chatsAdapter;

    public ChatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_chats, container, false);
        setAdapter();
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestAllChats();
    }

    private void requestAllChats() {
        if (fragmentView != null) {
            boolean isUserLogged = SharedPreferencesUtils.getBooleanFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_LOGGED_IN, false);
            refreshUserNotLoggedMessage(fragmentView, isUserLogged);
            if (isUserLogged) {
                refreshLoadingIndicator(fragmentView, true);
                LincedInRequester.getAllUserChats(
                        getContext(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson gson = new Gson();
                                Log.d(TAG, gson.toJson(response));

                                String chatsJson = null;
                                try {
                                    chatsJson = gson.toJson(response.getJSONArray("chats"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Type chatListType = new TypeToken<List<Chat>>() {}.getType();
                                if (chatsJson != null) {
                                    chats = gson.fromJson(chatsJson, chatListType);
                                }
                                populateChats();

                                refreshLoadingIndicator(fragmentView, false);
                                hideErrorScreen(fragmentView);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.toString());
                                error.printStackTrace();
                                refreshLoadingIndicator(fragmentView, false);
                                setErrorScreen(fragmentView);
                            }
                        }
                );
            }
        }
    }

    private void populateChats() {
        if (chatsAdapter != null) {
            chatsAdapter.setDataset(chats);
            chatsAdapter.notifyDataSetChanged();
        }
    }

    private void setAdapter() {
        if (fragmentView != null) {
            chatsAdapter = new ChatsAdapter(getContext(), chats);
            ((ListView) fragmentView.findViewById(R.id.fragment_chats_listview)).setAdapter(chatsAdapter);
        }
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.fragment_chats_layout).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.fragment_chats_loading_circular_progress).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_chats_network_error_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.fragment_chats_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_chats_loading_circular_progress).setVisibility(View.GONE);
        }
    }

    private void refreshUserNotLoggedMessage(View v, boolean isUserLogged) {
        if (isUserLogged) {
            v.findViewById(R.id.fragment_chats_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_chats_user_not_logged_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.fragment_chats_layout).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.fragment_chats_user_not_logged_layout).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_chats_network_error_layout).setVisibility(View.GONE);
        }
    }

    private void setErrorScreen(final View parentView) {
        parentView.findViewById(R.id.fragment_chats_layout).setVisibility(View.INVISIBLE);

        RelativeLayout errorScreen = (RelativeLayout) parentView.findViewById(R.id.fragment_chats_network_error_layout);
        errorScreen.setVisibility(View.VISIBLE);
        errorScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorScreen(parentView);
                requestAllChats();
            }
        });
    }

    private void hideErrorScreen(View v) {
        v.findViewById(R.id.fragment_chats_network_error_layout).setVisibility(View.GONE);
    }
}
