package com.fiuba.tallerii.lincedin.adapters;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.fragments.UserProfileFragment;
import com.fiuba.tallerii.lincedin.model.user.User;
import com.fiuba.tallerii.lincedin.model.user.UserFriends;
import com.fiuba.tallerii.lincedin.network.LincedInRequester;
import com.fiuba.tallerii.lincedin.utils.ImageUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;


public class UserFriendsAdapter extends ArrayAdapter<Object>  {

    private static final String TAG = "UserFriendsAdapter";
    private UserFriends userFriendsObject;
    private ArrayList<String> userFriends;
    Context mContext;




    //View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtJob;
        ImageView friendPicture;
        String friendID;
    }

    public UserFriendsAdapter(UserFriends friends, Context ctx) {
        super(ctx, R.layout.friend_row_item);
        this.userFriendsObject = friends;
        this.userFriends = friends.getUserFriends();
        this.mContext = ctx;
    }

    @Override
    public int getCount() {
        return userFriendsObject.getUserFriends().size();
    }

    @Override
    public Object getItem(int i)
    {
        return userFriendsObject.getUserFriends().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View result;
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.friend_row_item,viewGroup,false);
            viewHolder.txtName = (TextView) view.findViewById(R.id.friend_name);
            viewHolder.txtJob = (TextView) view.findViewById(R.id.friend_job);
            viewHolder.friendPicture = (ImageView) view.findViewById(R.id.friend_image);

        }else {
            viewHolder = (ViewHolder) view.getTag();
            result = view;

        }
        LincedInRequester.getUserProfile(userFriends.get(i).toString(),mContext, new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                          Gson parser = new Gson();
                          User userData = parser.fromJson(response.toString(), User.class);
                          viewHolder.txtName.setText(userData.fullName);
                          viewHolder.txtJob.setText(userData.getCurrentWork().company);
                          ImageUtils.setBase64ImageFromString(getContext(),userData.profilePicture,viewHolder.friendPicture);
                          viewHolder.friendID = userData.id;

                      }
                  },
                  new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          Log.e(TAG,error.toString());
                          error.printStackTrace();
                          Toast.makeText(mContext,"Ha ocurrido un error en la transferencia de datos.",Toast.LENGTH_LONG).show();
                      }
                  });

        return view;
    }

    private void setUserData(String tempName, String tempJob, String tempB64img, User userData) {
        tempName = userData.fullName;
        tempJob = userData.getCurrentWork().company;
        tempB64img = userData.profilePicture;
    }


}
