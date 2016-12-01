package com.fiuba.tallerii.lincedin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.RecommendationsReceivedAdapter;
import com.fiuba.tallerii.lincedin.events.RecommendationPostedEvent;
import com.fiuba.tallerii.lincedin.model.recommendations.RecommendationReceived;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecommendationsReceivedFragment extends Fragment {

    public interface OnRecommendationsReceivedFragmentInteractionListener {
        void onUserNotRecommended();
    }
    private OnRecommendationsReceivedFragmentInteractionListener mListener;

    private static final String TAG = "RecommendationsReceived";

    private static final String ARG_USER_ID = "ARG_USER_ID";
    private String recommendedUserId;

    private View fragmentView;

    private List<RecommendationReceived> recommendations = new ArrayList<>();

    private RecommendationsReceivedAdapter recommendationsReceivedAdapter;

    public RecommendationsReceivedFragment() {}

    public static RecommendationsReceivedFragment newInstance(String recommendedUserId) {
        RecommendationsReceivedFragment fragment = new RecommendationsReceivedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, recommendedUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        if (getArguments() != null) {
            recommendedUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_recommendations_received, container, false);
        requestRecommendationsReceived();
        setListeners(fragmentView);
        return fragmentView;
    }

    private void requestRecommendationsReceived() {
        if (fragmentView != null) {
            refreshLoadingIndicator(fragmentView, true);
            LincedInRequester.getUserRecommendations(
                    recommendedUserId,
                    getActivity(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, new Gson().toJson(response));
                            Type recommendationListType = new TypeToken<List<RecommendationReceived>>() {
                            }.getType();
                            try {
                                recommendations = new Gson().fromJson(response.getString("recommendations_received"), recommendationListType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setAdapter(fragmentView);

                            boolean isUserAlreadyRecommended = false;
                            for (RecommendationReceived recommendation : recommendations) {
                                if (recommendation.recommender.equals(UserAuthenticationManager.getUserId(getContext()))) {
                                    isUserAlreadyRecommended = true;
                                    break;
                                }
                            }
                            if (!isUserAlreadyRecommended) {
                                mListener.onUserNotRecommended();
                            }

                            refreshLoadingIndicator(fragmentView, false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            refreshLoadingIndicator(fragmentView, false);
                            Log.e(TAG, "Error retrieving recommendations received: " + error.toString());
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                Log.e(TAG, new String(error.networkResponse.data));
                            }
                            ViewUtils.setSnackbar(
                                    fragmentView.findViewById(R.id.fragment_recommendations_received_listview),
                                    R.string.error_retrieving_recommendations,
                                    Snackbar.LENGTH_LONG
                            );
                            mListener.onUserNotRecommended();
                        }
                    }
            );
        }
    }

    private void setAdapter(View v) {
        recommendationsReceivedAdapter = new RecommendationsReceivedAdapter(getContext(), recommendations);
        ListView recommendationsListView = (ListView) v.findViewById(R.id.fragment_recommendations_received_listview);
        recommendationsListView.setAdapter(recommendationsReceivedAdapter);
        recommendationsListView.setEmptyView(v.findViewById(android.R.id.empty));
    }

    private void setListeners(View v) {
        setRecommendationRowOnClickListener(v);
    }

    private void setRecommendationRowOnClickListener(View v) {
        if (getArguments() != null) {
            ((ListView) v.findViewById(R.id.fragment_recommendations_received_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (recommendationsReceivedAdapter != null) {
                        openUserProfile(recommendationsReceivedAdapter.getItem(position).recommender);
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendationPosted(RecommendationPostedEvent event) {
        requestRecommendationsReceived();
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

    private void openUserProfile(String userId) {
        Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
        userProfileIntent.putExtra(UserProfileActivity.ARG_USER_ID, userId);
        startActivity(userProfileIntent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecommendationsReceivedFragmentInteractionListener) {
            mListener = (OnRecommendationsReceivedFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecommendationsReceivedFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
