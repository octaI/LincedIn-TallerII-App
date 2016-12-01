package com.fiuba.tallerii.lincedin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.FindUserAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FindFriendsFragment extends Fragment {

    private static final String TAG = "FindFriendsFragment";
    private static final String ARG_USER_ID= "USER_ID";

    private View convertView;

    private UserFriends userFriends = new UserFriends();

    private ArrayList<String> returnedQueryIDs = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public static FindFriendsFragment newInstance(String userID) {
        FindFriendsFragment fragment = new FindFriendsFragment();
        Bundle args = new Bundle();



        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_searchfriends,container,false);
        SearchView searchView = (SearchView) convertView.findViewById(R.id.searchfriends_view);
        final ListView strangerList = (ListView) convertView.findViewById(R.id.found_friends_listview);
        strangerList.setAdapter(new FindUserAdapter(userFriends,getContext()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                final String query = s;
                LincedInRequester.submitSearchQuery(getContext(), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                Toast.makeText(getContext(),"Se ha enviado la query "+query,Toast.LENGTH_SHORT).show();
                            JSONArray responsearray = response.getJSONArray("users_found");
                            Log.d("FOUND USERS",responsearray.toString());
                            for (int i = 0; i < responsearray.length(); i++) {
                                userFriends.addUserFriend(responsearray.get(i).toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                },s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        strangerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}
