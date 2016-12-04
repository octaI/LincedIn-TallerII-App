package com.fiuba.tallerii.lincedin.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.UserFriendsAdapter;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PendingRequestsFragment extends Fragment {
    private UserFriends pendingRequests = new UserFriends();
    private View convertView;



    public static PendingRequestsFragment newInstance(UserFriends userPending) {
        PendingRequestsFragment fragment = new PendingRequestsFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPending();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_pending_requests,container,false);
        final ListView pending_list = (ListView) convertView.findViewById(R.id.pending_req_listview);
        pending_list.setAdapter(new UserFriendsAdapter(pendingRequests,getContext()));
        pending_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent switchUserProfileIntent = new Intent(getActivity(), UserProfileActivity.class);
                switchUserProfileIntent.putExtra("ARG_USER_ID", pendingRequests.getUserFriends().get(i));
                getActivity().startActivity(switchUserProfileIntent);
            }
        });
        requestPending();



        return convertView;
    }

    private void requestPending(){
        LincedInRequester.getPendingRequests(getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pendingRequests.getUserFriends().clear();
                    JSONObject friendsjson = response.getJSONObject("friends");
                    JSONArray friends_pending = friendsjson.getJSONArray("pending_for_him");
                    for(int i = 0; i < friends_pending.length(); i++){
                        pendingRequests.getUserFriends().add(friends_pending.get(i).toString());
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
        });
    }
}
