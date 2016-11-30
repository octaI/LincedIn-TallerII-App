package com.fiuba.tallerii.lincedin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.RecommendationPostedEvent;
import com.fiuba.tallerii.lincedin.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

public class RecommendUserDialogFragment extends DialogFragment {
    @Override
    public @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_post_recommendation, null))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog d = (Dialog) dialog;
                        EditText recommendationMessageEditText = (EditText) d.findViewById(R.id.dialog_post_recommendation_message_edittext);

                        if (recommendationMessageEditText.getText() != null && !recommendationMessageEditText.getText().toString().equals("")) {
                            EventBus.getDefault().post(
                                    new RecommendationPostedEvent(recommendationMessageEditText.getText().toString())
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
