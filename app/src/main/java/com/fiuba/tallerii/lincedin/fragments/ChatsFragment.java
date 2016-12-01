package com.fiuba.tallerii.lincedin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.ChatActivity;
import com.fiuba.tallerii.lincedin.activities.LogInActivity;
import com.fiuba.tallerii.lincedin.adapters.ChatsAdapter;
import com.fiuba.tallerii.lincedin.model.chat.Chat;
import com.fiuba.tallerii.lincedin.model.chat.ChatRow;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
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
    //private boolean errorOnRetrievingUsers;

    public ChatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_chats, container, false);
        setAdapter();
        setListeners();
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
                        getActivity(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson gson = new Gson();
                                Log.d(TAG, gson.toJson(response));

                                Type chatListType = new TypeToken<List<Chat>>() {}.getType();
                                try {
                                    chats = gson.fromJson(response.getString("chats"), chatListType);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                populateChats();
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

    /*@Deprecated
    private void populateChats(final List<Chat> chats) {
        for (final Chat chat : chats) {
            if (chat.lastMessage != null && chat.lastMessage.message != null) {
                final ChatRow inflatedChat = new ChatRow(chat.chatId, new ArrayList<String>(), chat.lastMessage.message, chat.lastMessage.timestamp);
                for (final String userId : chat.participants) {
                    LincedInRequester.getUserProfile(
                            userId,
                            getContext(),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    User user = new Gson().fromJson(response.toString(), User.class);
                                    inflatedChat.addUser(user.id);

                                    if (userId.equals(chat.participants.get(chat.participants.size() - 1))) {
                                        inflatedChats.add(inflatedChat);
                                    }

                                    if (chat.equals(chats.get(chats.size() - 1))) {
                                        if (!errorOnRetrievingUsers) {
                                            if (chatsAdapter != null) {
                                                chatsAdapter.setDataset(inflatedChats);
                                                chatsAdapter.notifyDataSetChanged();

                                                refreshLoadingIndicator(fragmentView, false);
                                                hideErrorScreen(fragmentView);
                                            }
                                        } else {
                                            errorOnRetrievingUsers = false;
                                            inflatedChats.remove(inflatedChat);
                                            refreshLoadingIndicator(fragmentView, false);
                                            setErrorScreen(fragmentView);
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, error.toString());
                                    error.printStackTrace();
                                    HttpRequestHelper.cancelPendingRequests("GetUserProfile");
                                    errorOnRetrievingUsers = true;

                                    if (chat.equals(chats.get(chats.size() - 1))) {
                                        refreshLoadingIndicator(fragmentView, false);
                                        setErrorScreen(fragmentView);
                                    }
                                }
                            }
                    );
                }
            } else {
                if (chat.equals(chats.get(chats.size() - 1))) {
                    if (chatsAdapter != null) {
                        chatsAdapter.setDataset(inflatedChats);
                        chatsAdapter.notifyDataSetChanged();

                        refreshLoadingIndicator(fragmentView, false);
                        hideErrorScreen(fragmentView);
                    }
                }
            }
        }
    }*/

    private void populateChats() {
        List<Chat> chatsToShow = new ArrayList<>();
        for (final Chat chat : chats) {
            if (chat.lastMessage != null && chat.lastMessage.message != null) {
                chatsToShow.add(chat);
            }
        }
        if (chatsAdapter != null) {
            chatsAdapter.setDataset(chatsToShow);
            chatsAdapter.notifyDataSetChanged();
        }
        refreshLoadingIndicator(fragmentView, false);
        hideErrorScreen(fragmentView);
    }

    private void setAdapter() {
        if (fragmentView != null) {
            chatsAdapter = new ChatsAdapter(getContext(), chats);
            ListView chatsListView = (ListView) fragmentView.findViewById(R.id.fragment_chats_listview);
            chatsListView.setAdapter(chatsAdapter);
            chatsListView.setEmptyView(fragmentView.findViewById(android.R.id.empty));
        }
    }

    private void setListeners() {
        if (fragmentView != null) {
            fragmentView.findViewById(R.id.fragment_chats_login_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLogin();
                }
            });

            if (chatsAdapter != null) {
                ((ListView) fragmentView.findViewById(R.id.fragment_chats_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        openChat(chatsAdapter.getItem(position));
                    }
                });
            }
        }
    }

    private void openChat(Chat chat) {
        Intent chatIntent = new Intent(getActivity(), ChatActivity.class);
        chatIntent.putExtra(ChatActivity.ARG_CHAT_ID, chat.chatId);

        // TODO: 29/11/16 It only supports 1-1 conversations!
        String receivingUserId = null;
        for (String userId : chat.participants) {
            if (!userId.equals(SharedPreferencesUtils.getStringFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_ID, ""))) {
                receivingUserId = userId;
                break;
            }
        }
        chatIntent.putExtra(ChatActivity.ARG_RECEIVING_USER_ID, receivingUserId);

        startActivity(chatIntent);
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

    private void openLogin() {
        Intent loginIntent = new Intent(getActivity(), LogInActivity.class);
        startActivity(loginIntent);
    }
}
