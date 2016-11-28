package com.fiuba.tallerii.lincedin.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.adapters.RecommendationsReceivedAdapter;
import com.fiuba.tallerii.lincedin.model.user.Recommendation;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecommendationsReceivedFragment extends Fragment {

    private static final String TAG = "RecommendationsReceived";

    private static final String ARG_USER_ID = "ARG_USER_ID";
    private static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private List<Recommendation> recommendations = new ArrayList<>();

    private RecommendationsReceivedAdapter recommendationsReceivedAdapter;

    public RecommendationsReceivedFragment() {}

    public static RecommendationsReceivedFragment newInstance(String userId, boolean isOwnProfile) {
        RecommendationsReceivedFragment fragment = new RecommendationsReceivedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_OWN_PROFILE, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations_received, container, false);
        requestRecommendationsReceived(v);
        setListeners(v);
        setButtonVisibility(v);
        return v;
    }

    private void requestRecommendationsReceived(final View v) {
        if (getArguments() != null) {
            String userId = getArguments().getString(ARG_USER_ID) != null ? getArguments().getString(ARG_USER_ID) : "me";
            refreshLoadingIndicator(v, true);
            LincedInRequester.getUserRecommendations(
                    userId,
                    getContext(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, new Gson().toJson(response));
                            Type recommendationListType = new TypeToken<List<Recommendation>>() {}.getType();
                            recommendations = new Gson().fromJson(response.toString(), recommendationListType);
                            setAdapter(v);
                            refreshLoadingIndicator(v, false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            refreshLoadingIndicator(v, false);
                            Log.e(TAG, "Error retrieving recommendations received: " + error.toString());
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                Log.e(TAG, new String(error.networkResponse.data));
                            }
                            ViewUtils.setSnackbar(
                                    v.findViewById(R.id.fragment_recommendations_received_add_recommendation_fab),
                                    R.string.error_retrieving_recommendations,
                                    Snackbar.LENGTH_LONG
                            );
                        }
                    }
            );
        }
    }

    private void setAdapter(View v) {
        recommendationsReceivedAdapter = new RecommendationsReceivedAdapter(getContext(), recommendations);
        ((ListView) v.findViewById(R.id.fragment_recommendations_received_listview)).setAdapter(recommendationsReceivedAdapter);
    }

    private void setListeners(View v) {
        setAddRecommendationButtonListener(v);
        setRecommendationRowOnLongClickListener(v);
    }

    private void setButtonVisibility(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                v.findViewById(R.id.fragment_recommendations_received_add_recommendation_fab).setVisibility(View.GONE);
            } else {
                v.findViewById(R.id.fragment_recommendations_received_add_recommendation_fab).setVisibility(View.VISIBLE);
            }
        }
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.fragment_recommendations_received_loading_circular_progress).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_recommendations_received_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.fragment_recommendations_received_loading_circular_progress).setVisibility(View.GONE);
            v.findViewById(R.id.fragment_recommendations_received_layout).setVisibility(View.VISIBLE);
        }
    }

    private void setAddRecommendationButtonListener(View parentView) {
        parentView.findViewById(R.id.fragment_recommendations_received_add_recommendation_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 27/11/16 Call activity method via interface
            }
        });
    }

    private void setRecommendationRowOnLongClickListener(View v) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                ((ListView) v.findViewById(R.id.fragment_recommendations_received_listview)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        // TODO: 27/11/16 Call activity method via interface
                        return false;
                    }
                });
            }
        }
    }

}
