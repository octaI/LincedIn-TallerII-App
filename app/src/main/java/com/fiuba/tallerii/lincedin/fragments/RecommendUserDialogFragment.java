package com.fiuba.tallerii.lincedin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.RecommendationPostedEvent;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class RecommendUserDialogFragment extends DialogFragment {

    private static final String TAG = "RecommendUserDialog";

    private static final String ARG_RECOMMENDED_USER_ID = "ARG_RECOMMENDED_USER_ID";
    private String recommendedUserId;

    public static RecommendUserDialogFragment newInstance(String recommendedUserId) {
        RecommendUserDialogFragment dialogFragment = new RecommendUserDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECOMMENDED_USER_ID, recommendedUserId);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recommendedUserId = getArguments().getString(ARG_RECOMMENDED_USER_ID);
        }
    }

    @Override
    public @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_post_recommendation, null))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final Dialog d = (Dialog) dialog;
                        EditText recommendationMessageEditText = (EditText) d.findViewById(R.id.dialog_post_recommendation_message_edittext);

                        if (recommendationMessageEditText.getText() != null && !recommendationMessageEditText.getText().toString().equals("")) {
                            String message = recommendationMessageEditText.getText().toString();
                            LincedInRequester.recommendUser(
                                    recommendedUserId,
                                    message,
                                    getActivity(),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d(TAG, new Gson().toJson(response));
                                            try {
                                                EventBus.getDefault().post(new RecommendationPostedEvent(response.getString("message")));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                EventBus.getDefault().post(new RecommendationPostedEvent());
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e(TAG, "Error posting recommendation: " + error.toString());
                                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                                Log.e(TAG, new String(error.networkResponse.data));
                                            }
                                            ViewUtils.setSnackbar(
                                                    d.findViewById(R.id.dialog_post_recommendation_message_edittext),
                                                    R.string.error_recommend_user,
                                                    Snackbar.LENGTH_LONG
                                            );
                                        }
                                    }
                            );
                        } else {
                            ViewUtils.setToast(getContext(), getString(R.string.cannot_be_empty), Toast.LENGTH_LONG);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            }
        });
        return alertDialog;
    }
}
