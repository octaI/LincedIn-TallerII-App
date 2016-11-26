package com.fiuba.tallerii.lincedin.model.user;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFriends {

    @SerializedName("friends")
    public ArrayList<String> userFriendsIDs = new ArrayList<>();

    public void addUserFriend(String userid) {
        userFriendsIDs.add(userid);
    }


    public ArrayList<String> getUserFriends(){
        return userFriendsIDs;
    }
}
