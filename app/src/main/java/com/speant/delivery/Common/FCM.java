package com.speant.delivery.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.speant.delivery.Common.global.Global;
import com.speant.delivery.R;
import com.speant.delivery.ui.ActivitySplash;
import com.speant.delivery.ui.InboxActivity;
import com.speant.delivery.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.speant.delivery.Common.global.Global.ADMIN;
import static com.speant.delivery.Common.global.Global.USER;

public class FCM extends FirebaseMessagingService {

    private static final String TAG = "FCM";
    private static final String GROUP_KEY = "com.speant.provider.GROUP_KEY";
    private String notifyTitle;
    private String notifyMessage;
    private String notifyRequestId;
    private String notifyFromType;
    private String receiverType;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e(TAG, "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }



    /*@Override
    public void onNewToken() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }*/

    private void sendRegistrationToServer(String refreshedToken) {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveDeviceToken(refreshedToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getPackageName() + "/" + R.raw.sound);

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.e("JSON_OBJECT", object.toString());
        SessionManager sessionManager = new SessionManager(this);

        try {
            notifyTitle = object.getString("title");
            notifyMessage = object.getString("message");
            notifyRequestId = object.getString("request_id");
            notifyFromType = object.getString("provider_type");
            if (notifyFromType.equalsIgnoreCase("1")) {
                receiverType = USER;
            }else{
                receiverType = ADMIN;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String NOTIFICATION_CHANNEL_ID = "boga_channel";

        long pattern[] = {0, 1000, 500, 1000};

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Your Notifications",
                    NotificationManager.IMPORTANCE_HIGH);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            notificationChannel.setDescription(" ");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.enableVibration(true);
//            notificationChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // to diaplay notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
            channel.canBypassDnd();
        }

        Intent intent;
        if (sessionManager.isLoggedIn()) {
            if (notifyMessage.contains("New Order Received")) {
                Log.e(TAG, "onMessageReceived:New Order Received ");
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
            } else if(notifyTitle.contains("New chat received")){
                intent = new Intent(this, InboxActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(Global.ORDER_ID, notifyRequestId);
                intent.putExtra(Global.FROM_TYPE, receiverType);
                intent.putExtra(Global.USER_ID, 0);
            }else {
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
            }
        } else {
            intent = new Intent(this, ActivitySplash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
        }

        /*if (new SessionManager(this.getApplicationContext()).isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
        } else {
            intent = new Intent(this, ActivitySplash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
        }*/


      /*  intent = new Intent("android.intent.category.LAUNCHER");
        intent.setClassName(getPackageName(), "com.speant.delivery.ui.MainActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);*/

        /*if(notifyRequestId != null && !notifyRequestId.isEmpty() && !notifyRequestId.equals("0")) {
            intent = new Intent(this, ActivitySplash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(CONST.REQUEST_ID, notifyRequestId);
        }else{
            intent = new Intent("android.intent.category.LAUNCHER");
            intent.setClassName(getPackageName(), "com.speant.delivery.ui.ActivitySplash");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setColor(ContextCompat.getColor(this, R.color.white))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notifyMessage))
//                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(notifyTitle)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setGroup(GROUP_KEY)
                .setContentIntent(pendingIntent)
//                .setSound(sound)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);


        mNotificationManager.notify(1000, notificationBuilder.build());


    }


}
