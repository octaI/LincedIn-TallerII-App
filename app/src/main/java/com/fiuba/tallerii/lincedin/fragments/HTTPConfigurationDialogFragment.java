package com.fiuba.tallerii.lincedin.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.activities.HomeActivity;
import com.fiuba.tallerii.lincedin.network.HttpRequestHelper;
import com.fiuba.tallerii.lincedin.activities.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HTTPConfigurationDialogFragment extends DialogFragment {

    private static final String TAG = HTTPConfigurationDialogFragment.class.getName();

    public static final String DEFAULT_LOCAL_IP = "192.168.1.33";
    public static final String DEFAULT_PORT_EXPOSED = "8081";

    private ArrayAdapter adapter;

    public void setAdapter(ArrayAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_http_configuration, null))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog d = (Dialog) dialog;
                        EditText localIpEditText = (EditText) d.findViewById(R.id.dialog_http_configuration_local_ip_edit_text);
                        EditText portEditText = (EditText) d.findViewById(R.id.dialog_http_configuration_port_edit_text);

                        String localIp = TextUtils.isEmpty(localIpEditText.getText().toString()) ?
                                DEFAULT_LOCAL_IP : localIpEditText.getText().toString();
                        String port = TextUtils.isEmpty(portEditText.getText().toString()) ?
                                DEFAULT_PORT_EXPOSED : portEditText.getText().toString();

                        sendDummyHTTPRequestToAppServer(localIp, port);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getDialog().cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                ((EditText) dialog.findViewById(R.id.dialog_http_configuration_local_ip_edit_text)).setText(DEFAULT_LOCAL_IP);
                ((EditText) dialog.findViewById(R.id.dialog_http_configuration_port_edit_text)).setText(DEFAULT_PORT_EXPOSED);
            }
        });
        return dialog;
    }

    private void sendDummyHTTPRequestToAppServer(String localIp, String port) {
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("appName", "LincedIn");
        requestParams.put("testing", Boolean.TRUE.toString());
        final String url = "http://" + localIp + ":" + port + "/skills";



        HttpRequestHelper.get(
                url,
                requestParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            JSONArray skills = response.getJSONArray("skills");
                            for (int i=0; i < skills.length(); i++) {
                                JSONObject skill = skills.getJSONObject(i);
                                adapter.add(skill.getString("name"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().post(new MessageEvent("No hay skills :("));
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        EventBus.getDefault().post(new MessageEvent(error.toString()));
                    }
                },
                "TEST_REQUEST"
        );
    }
}
