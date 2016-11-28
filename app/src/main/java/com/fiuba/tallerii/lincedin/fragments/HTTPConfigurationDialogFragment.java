package com.fiuba.tallerii.lincedin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.events.MessageEvent;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesKeys;
import com.fiuba.tallerii.lincedin.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HTTPConfigurationDialogFragment extends DialogFragment {

    private static final String TAG = HTTPConfigurationDialogFragment.class.getName();

    public static final String DEFAULT_SERVER_IP = "192.168.0.14";
    public static final String DEFAULT_PORT_EXPOSED = "8081";

    private AlertDialog alertDialog;

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_http_configuration, null))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog d = (Dialog) dialog;
                        EditText serverIPEditText = (EditText) d.findViewById(R.id.dialog_http_configuration_server_ip_edit_text);
                        EditText serverPortEditText = (EditText) d.findViewById(R.id.dialog_http_configuration_port_edit_text);

                        String serverIP = TextUtils.isEmpty(serverIPEditText.getText().toString()) ?
                                DEFAULT_SERVER_IP : serverIPEditText.getText().toString();
                        String serverPort = TextUtils.isEmpty(serverPortEditText.getText().toString()) ?
                                DEFAULT_PORT_EXPOSED : serverPortEditText.getText().toString();

                        saveChanges(
                                !((SwitchCompat) alertDialog.findViewById(R.id.dialog_http_configuration_server_local_or_remote_switch)).isChecked(),
                                serverIP,
                                serverPort
                        );
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });

        alertDialog = builder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                setLocalOrRemoteSwitchListener();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                ((EditText) alertDialog.findViewById(R.id.dialog_http_configuration_server_ip_edit_text))
                        .setText(preferences.getString(SharedPreferencesKeys.SERVER_IP, DEFAULT_SERVER_IP));
                ((EditText) alertDialog.findViewById(R.id.dialog_http_configuration_port_edit_text))
                        .setText(preferences.getString(SharedPreferencesKeys.SERVER_PORT, DEFAULT_PORT_EXPOSED));
            }
        });
        return alertDialog;
    }

    private void saveChanges(boolean isLocal, String serverIP, String serverPort) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        if (isLocal) {
            editor.putBoolean(SharedPreferencesKeys.SERVER_IS_LOCAL, true);
        } else {
            editor.putBoolean(SharedPreferencesKeys.SERVER_IS_LOCAL, false);
        }
        editor.putString(SharedPreferencesKeys.SERVER_IP, serverIP);
        editor.putString(SharedPreferencesKeys.SERVER_PORT, serverPort);
        editor.apply();

        Toast.makeText(getContext(), getString(R.string.http_configuration_dialog_saved_toast), Toast.LENGTH_SHORT).show();
    }

    private void setLocalOrRemoteSwitchListener() {
        final SwitchCompat localOrRemoteSwitch = (SwitchCompat) alertDialog.findViewById(R.id.dialog_http_configuration_server_local_or_remote_switch);
        if (!SharedPreferencesUtils.getBooleanFromSharedPreferences(getContext(), SharedPreferencesKeys.SERVER_IS_LOCAL, false)) {
            localOrRemoteSwitch.setChecked(true);
            localOrRemoteSwitch.setText(getString(R.string.remote));
            alertDialog.findViewById(R.id.dialog_http_configuration_server_local_layout).setVisibility(View.GONE);
        }
        localOrRemoteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (localOrRemoteSwitch.isChecked()) {
                    localOrRemoteSwitch.setText(getString(R.string.remote));
                    alertDialog.findViewById(R.id.dialog_http_configuration_server_local_layout).setVisibility(View.GONE);
                } else {
                    localOrRemoteSwitch.setText(getString(R.string.local));
                    alertDialog.findViewById(R.id.dialog_http_configuration_server_local_layout).setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
