package com.fiuba.tallerii.lincedin.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.adapters.RecommendationsMadeAdapter;
import com.fiuba.tallerii.lincedin.model.recommendations.RecommendationReceived;
import com.fiuba.tallerii.lincedin.model.recommendations.RecommendationSent;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecommendationsMadeFragment extends Fragment {

    private static final String TAG = "RecommendationsMade";

    private static final String ARG_USER_ID = "ARG_USER_ID";
    private static final String ARG_IS_OWN_PROFILE = "IS_OWN_PROFILE";

    private View fragmentView;

    private List<RecommendationSent> recommendations = new ArrayList<>();

    private RecommendationsMadeAdapter recommendationsMadeAdapter;

    public RecommendationsMadeFragment() {}

    public static RecommendationsMadeFragment newInstance(String userId, boolean isOwnProfile) {
        RecommendationsMadeFragment fragment = new RecommendationsMadeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_OWN_PROFILE, isOwnProfile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_recommendations_made, container, false);
        requestRecommendationsMade();
        setListeners(fragmentView);
        return fragmentView;
    }

    private void requestRecommendationsMade() {
        if (fragmentView != null) {
            String userId = getArguments().getString(ARG_USER_ID);
            refreshLoadingIndicator(fragmentView, true);
            LincedInRequester.getUserRecommendations(
                    userId,
                    getActivity(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, new Gson().toJson(response));
                            Type recommendationListType = new TypeToken<List<RecommendationSent>>() {}.getType();
                            try {
                                recommendations = new Gson().fromJson(response.getString("recommendations_sent"), recommendationListType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setAdapter(fragmentView);
                            refreshLoadingIndicator(fragmentView, false);
                            hideErrorScreen(fragmentView);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            refreshLoadingIndicator(fragmentView, false);
                            Log.e(TAG, "Error retrieving recommendations made: " + error.toString());
                            error.printStackTrace();
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                Log.e(TAG, new String(error.networkResponse.data));
                            }
                            showErrorScreen(fragmentView);

                        }
                    }
            );
        }
    }

    private void setAdapter(View v) {
        recommendationsMadeAdapter = new RecommendationsMadeAdapter(getContext(), recommendations);
        ListView recommendationsListView = (ListView) v.findViewById(R.id.fragment_recommendations_made_listview);
        recommendationsListView.setAdapter(recommendationsMadeAdapter);
        recommendationsListView.setEmptyView(v.findViewById(android.R.id.empty));
    }

    private void setListeners(View v) {
        setRecommendationRowOnClickListener(v);
        setRecommendationRowOnLongClickListener(v);
        setErrorScreen(v);
    }

    private void refreshLoadingIndicator(View v, boolean loading) {
        if (loading) {
            v.findViewById(R.id.fragment_recommendations_made_loading_circular_progress).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_recommendations_made_listview).setVisibility(View.GONE);
            v.findViewById(R.id.fragment_recommendations_made_network_error_layout).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.fragment_recommendations_made_loading_circular_progress).setVisibility(View.GONE);
            v.findViewById(R.id.fragment_recommendations_made_listview).setVisibility(View.VISIBLE);
        }
    }

    private void setErrorScreen(final View parentView) {
        parentView.findViewById(R.id.fragment_recommendations_made_network_error_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorScreen(parentView);
                requestRecommendationsMade();
            }
        });
    }

    private void showErrorScreen(View v) {
        v.findViewById(R.id.fragment_recommendations_made_network_error_layout).setVisibility(View.VISIBLE);
        v.findViewById(R.id.fragment_recommendations_made_listview).setVisibility(View.GONE);
        v.findViewById(R.id.fragment_recommendations_made_loading_circular_progress).setVisibility(View.GONE);
    }

    private void hideErrorScreen(View v) {
        v.findViewById(R.id.fragment_recommendations_made_network_error_layout).setVisibility(View.GONE);
        v.findViewById(R.id.fragment_recommendations_made_listview).setVisibility(View.VISIBLE);
    }

    private void openUserProfile(String userId) {
        Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
        userProfileIntent.putExtra(UserProfileActivity.ARG_USER_ID, userId);
        startActivity(userProfileIntent);
    }

    private void setRecommendationRowOnClickListener(View v) {
        if (getArguments() != null) {
            ((ListView) v.findViewById(R.id.fragment_recommendations_made_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (recommendationsMadeAdapter != null) {
                        openUserProfile(recommendationsMadeAdapter.getItem(position).userId);
                    }
                }
            });
        }
    }

    private void setRecommendationRowOnLongClickListener(final View parentView) {
        if (getArguments() != null) {
            if (getArguments().getBoolean(ARG_IS_OWN_PROFILE, false)) {
                ((ListView) parentView.findViewById(R.id.fragment_recommendations_made_listview)).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        openConfirmationDialog(parentView, recommendationsMadeAdapter.getItem(position));
                        return true;
                    }
                });
            }
        }
    }

    private void openConfirmationDialog(final View parentView, final RecommendationSent recommendationToDelete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.are_you_sure_you_want_to_delete_recommendation))
                .setMessage(getString(R.string.this_change_cannot_be_reverted))
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmRecommendationDeletion(parentView, recommendationToDelete);
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }
        });
        dialog.show();
    }

    private void confirmRecommendationDeletion(final View parentView, final RecommendationSent recommendation) {
        if (getArguments() != null) {
            String userId = getArguments().getString(ARG_USER_ID);
            refreshLoadingIndicator(parentView, true);
            LincedInRequester.deleteRecommendation(
                    userId,
                    getActivity(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, new Gson().toJson(response));
                            Log.i(TAG, "La recomendación fue eliminada exitosamente.");

                            recommendationsMadeAdapter.removeFromDataset(recommendation);
                            recommendationsMadeAdapter.notifyDataSetChanged();
                            refreshLoadingIndicator(parentView, false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error retrieving recommendations made: " + error.toString());
                            error.printStackTrace();
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                Log.e(TAG, new String(error.networkResponse.data));
                            }
                            refreshLoadingIndicator(parentView, false);
                            ViewUtils.setSnackbar(
                                    parentView.findViewById(R.id.fragment_recommendations_made_listview),
                                    R.string.error_delete_recommendation,
                                    Snackbar.LENGTH_LONG
                            );
                        }
                    }
            );
        }
    }

}
