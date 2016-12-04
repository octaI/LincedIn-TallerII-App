package com.fiuba.tallerii.lincedin.model.user;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class UserFriends {

    @SerializedName("friends")
    public ArrayList<String> userFriendsIDs = new ArrayList<>();
    @SerializedName("online_users")
    public ArrayList<String> onlineUsers = new ArrayList<>();

    public void addUserFriend(String userid) {
        if ( userFriendsIDs.contains(userid)){
            return;
        }
        userFriendsIDs.add(userid);
    }

    public void addOnlineUser(String userId){
        if (onlineUsers.contains(userId)){
            return;
        }
        onlineUsers.add(userId);
    }


    public ArrayList<String> getUserFriends(){
        return userFriendsIDs;
    }

    public ArrayList<String> getOnlineUserFriends() {
        return onlineUsers;
    }
}
