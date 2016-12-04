package com.fiuba.tallerii.lincedin.services;

import android.content.Intent;
import android.util.Log;

import com.fiuba.tallerii.lincedin.activities.ChatActivity;
import com.fiuba.tallerii.lincedin.activities.RecommendationsActivity;
import com.fiuba.tallerii.lincedin.activities.UserProfileActivity;
import com.fiuba.tallerii.lincedin.events.MessageReceivedEvent;
import com.fiuba.tallerii.lincedin.network.UserAuthenticationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class LincedInFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessaging";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            switch (data.get("action")) {
                case "CHAT":
                    EventBus.getDefault().post(new MessageReceivedEvent(null));

                    Intent chatIntent = new Intent(this, ChatActivity.class);
                    chatIntent.putExtra(ChatActivity.ARG_RECEIVING_USER_ID, data.get("id"));
                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(chatIntent);
                    break;
                case "RECOMMENDATION":
                    Intent recommendationsIntent = new Intent(this, RecommendationsActivity.class);
                    recommendationsIntent.putExtra(RecommendationsActivity.ARG_USER_ID, UserAuthenticationManager.getUserId(this));
                    recommendationsIntent.putExtra(RecommendationsActivity.ARG_IS_OWN_PROFILE, true);
                    recommendationsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(recommendationsIntent);
                    break;
                case "FRIEND_REQUEST":
                    Intent userProfileIntent = new Intent(this, UserProfileActivity.class);
                    userProfileIntent.putExtra(UserProfileActivity.ARG_USER_ID, data.get("id"));
                    userProfileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(userProfileIntent);
                    break;
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
}
