package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.UserFriendsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;


public class FriendsFragment extends Fragment {

    private static final String TAG = "FriendsFragment";

    private static final String ARG_USER_ID = "USER_ID";

    private View convertView;

    private UserFriends userFriends = new UserFriends();

    private Boolean isOwnProfile = false;

    private UserFriendsAdapter userFriendsAdapter;

    public static FriendsFragment newInstance(String userID){
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        requestUserFriends();


    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_friends,container,false);
        final ListView friendList = (ListView) convertView.findViewById(R.id.user_friend_list);
        friendList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                this.currentScrollState = i;
                isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                this.currentFirstVisibleItem = i;
                this.currentVisibleItemCount = i1;
                this.totalItem = i2;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    for (int i = currentFirstVisibleItem; i< currentVisibleItemCount; i++) {
                        friendList.getAdapter().getView(i,null,friendList);
                    }
                }
            }
        });
        UserFriendsAdapter friendListAdapter = new UserFriendsAdapter(userFriends,getContext());
        friendList.setAdapter(friendListAdapter);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserProfileFragment switchFragment =  UserProfileFragment.newInstance(userFriends.getUserFriends().get(i).toString());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,switchFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(getContext(),"Clickeaste en el user" + userFriends.getUserFriends().get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    private void requestUserFriends() {
        String userId;
        if (isOwnProfile) {
            userId = null; // this is your own user
        } else {
            userId = ARG_USER_ID; //get the ID of the currently viewed profile
        }
        if (convertView != null) {
            boolean isUserLogged = SharedPreferencesUtils.getBooleanFromSharedPreferences(getContext(), SharedPreferencesKeys.USER_LOGGED_IN, false);

            if (isUserLogged) {
                refreshLoadingIndicator(convertView, true);
                LincedInRequester.getUserFriends(getContext(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson parser = new Gson();
                                Log.d(TAG, parser.toJson(response));
                                userFriends = parser.fromJson(response.toString(), UserFriends.class);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),"Ha ocurrido un error al establecer la conexión con el servidor.",Toast.LENGTH_LONG).show();
                                Log.e(TAG,error.toString());
                                error.printStackTrace();
                            }
                        }
                ,userId);
            }
        }
    }




    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.INVISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_network_error_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.user_profile_main_container_nestedscrollview).setVisibility(View.VISIBLE);
            v.findViewById(R.id.user_profile_loading_circular_progress).setVisibility(View.GONE);
        }
    }

}
