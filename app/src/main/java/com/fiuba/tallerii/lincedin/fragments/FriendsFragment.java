package com.fiuba.tallerii.lincedin.fragments;

import android.content.Intent;
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
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.UserFriendsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class FriendsFragment extends Fragment {

    private static final String TAG = "FriendsFragment";

    private static final String ARG_USER_ID = "USER_ID";

    private View convertView;

    private UserFriends userFriends = new UserFriends();

    private Boolean isOwnProfile = false;


    public static FriendsFragment newInstance(String userID){
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);


    }
    @Override
    public void onResume() {
        super.onResume();
        requestUserFriends();
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_friends,container,false);
        final ListView friendList = (ListView) convertView.findViewById(R.id.user_friend_list);
        UserFriendsAdapter friendListAdapter = new UserFriendsAdapter(userFriends,getContext());
        friendList.setAdapter(friendListAdapter);
        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserProfileFragment switchFragment =  UserProfileFragment.newInstance(userFriends.getUserFriends().get(i).toString());
                Intent switchUserProfileIntent = new Intent(getActivity(), UserProfileActivity.class);
                switchUserProfileIntent.putExtra("ARG_USER_ID",userFriends.getUserFriends().get(i).toString());
                getActivity().startActivity(switchUserProfileIntent);



            }
        });

        return convertView;
    }

    private void requestUserFriends() {
         LincedInRequester.getUserFriends(getContext(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray array = response.getJSONArray("friends");
                                    JSONArray onlinearray = response.getJSONArray("online");
                                    Log.d(TAG,array.toString());
                                    for(int i  = 0; i<array.length();i++) {
                                        userFriends.addUserFriend(array.get(i).toString());
                                    }
                                    for (int i = 0; i<onlinearray.length();i++) {
                                        userFriends.addOnlineUser(onlinearray.get(i).toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(),"Ha ocurrido un error al traer la lista de amigos, verifique su conexión.",Toast.LENGTH_SHORT).show();
                                Log.e(TAG,error.toString());
                                error.printStackTrace();
                            }
                        }
                , UserAuthenticationManager.getUserId(getContext()));
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
