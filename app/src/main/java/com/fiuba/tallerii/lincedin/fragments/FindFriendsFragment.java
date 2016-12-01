package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.fiuba.tallerii.lincedin.R;

import java.util.ArrayList;


public class FindFriendsFragment extends Fragment {

    private static final String TAG = "FindFriendsFragment";
    private static final String ARG_USER_ID= "USER_ID";

    private View convertView;

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
        convertView = inflater.inflate(R.layout.fragment_searchfriends,container);
        SearchView searchView = (SearchView) getView().findViewById(R.id.searchfriends_view);

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return convertView;
    }
}
