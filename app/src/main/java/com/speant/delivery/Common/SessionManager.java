package com.speant.delivery.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


import com.speant.delivery.FireBase.FireBaseListeners.NewRequestListener;
import com.speant.delivery.R;
import com.speant.delivery.ui.ActivityLogin;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Locale;

public class SessionManager {
    public static final String POST_CODE_ID = "ID";
    private static final String TAG_TOKEN = "DeviceTocken";
    private static final String KEY_REQUEST_ID = "requestId";
    private static final String KEY_AVAILABLE_STATUS = "online_status";
    private static final String KEY_DEFAULT_TIMEOUT = "default_timeout";
    private static final String ORDERCOMPLETE = "ORDERCOMPLETE";
    private static final String SOCKET_ID = "SOCKET_ID";
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;


    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Sangavi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_AUTH_TOKEN = "auth_token";

    //User Details

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_ADDRESS = "user_address";
    public static final String KEY_USER_IMAGE = "user_image";
    public static final String KEY_PARTNER_ID = "partner_id";
    public static final String KEY_DEVICE_TOKEN = "device_token_login";
    public static final String KEY_USER_STRIPE_ID = "stripe_id";
    public static final String KEY_REFFERAL_CODE = "refferal_code";
    public static final String KEY_REFFERAL_LINK = "refferal_link";
    public static final String KEY_DISCOUNT = "discount_amount";
    public static final String KEY_CURRENCY = "currency";
    private NewRequestListener newRequestListener;
    private static final String KEY_AVAILABLE_DEFAULT_STATUS = "online_default_status";
    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(int userId, String authToken, String userName, String userEmail,
                                   String userMob, String userImg, String partner_id,String deviceToken) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID, String.valueOf(userId));
        editor.putString(KEY_AUTH_TOKEN, authToken);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_MOBILE, userMob);
        editor.putString(KEY_USER_IMAGE, userImg);
        editor.putString(KEY_PARTNER_ID, partner_id);
        editor.putString(KEY_DEVICE_TOKEN, deviceToken);
        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, ActivityLogin.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    public void putSocketUinqueId(String id) {

        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SOCKET_ID, id);
        editor.apply();
        editor.commit();

    }

    public String getSocketUniqueId() {
        return pref.getString(SOCKET_ID, "");

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_AUTH_TOKEN, pref.getString(KEY_AUTH_TOKEN, null));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, null));
        user.put(KEY_USER_EMAIL, pref.getString(KEY_USER_EMAIL, null));
        user.put(KEY_USER_MOBILE, pref.getString(KEY_USER_MOBILE, null));
        user.put(KEY_USER_ADDRESS, pref.getString(KEY_USER_ADDRESS, null));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, null));
        user.put(KEY_USER_STRIPE_ID, pref.getString(KEY_USER_STRIPE_ID, null));
        user.put(KEY_REFFERAL_CODE, pref.getString(KEY_REFFERAL_CODE, ""));
        user.put(KEY_REFFERAL_LINK, pref.getString(KEY_REFFERAL_LINK, ""));
        user.put(KEY_PARTNER_ID, pref.getString(KEY_PARTNER_ID, ""));
        user.put(KEY_DEVICE_TOKEN, pref.getString(KEY_DEVICE_TOKEN, ""));
        // return user
        return user;
    }


    /**
     * Get stored session data
     * Content-Type=application/json, clientId=8056359277, authToken=6571731612, authId=20
     */
    public HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
//		header.put("Content-Type", pref.getString("application/json", "application/json"));
        /*header.put("authId", pref.getString(KEY_USER_ID, KEY_USER_ID));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, KEY_AUTH_TOKEN));*/


        header.put("authId", pref.getString(KEY_USER_ID, "1"));
        header.put("authToken", pref.getString(KEY_AUTH_TOKEN, "s4nbp5FibJpfEY9q"));

        return header;
    }

    /**
     * Clear session details
     */
    public void logoutUser(Activity activity) {
        String token = getDeviceToken();

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        saveDeviceToken(token);
        // After logout redirect user to Loing Activity
        Intent i = new Intent(activity, ActivityLogin.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       /* // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        // Staring Login Activity
        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        activity.finishAffinity();
    }

    public void logout() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getCurrency() {
//        return PreferenceManager.getDefaultSharedPreferences(_context).getString(KEY_CURRENCY, "र");
        return PreferenceManager.getDefaultSharedPreferences(_context).getString(KEY_CURRENCY, "$");
    }
    public String getCurrentLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    public void setCyrrency(String currency) {
        PreferenceManager.getDefaultSharedPreferences(_context).edit().putString(KEY_CURRENCY, currency).apply();
    }


    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token) {
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken() {
        SharedPreferences sharedPreferences = (_context).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String newToken = sharedPreferences.getString(TAG_TOKEN, "");
        if(newToken.isEmpty()){
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    saveDeviceToken(token);
                }
            });
           newToken = sharedPreferences.getString(TAG_TOKEN, "");
        }
        return newToken;
    }

    public void setRequestId(String request_id) {
        editor.putString(KEY_REQUEST_ID, request_id);
        editor.commit();
    }

    public void removeRequestId() {
        editor.remove(KEY_REQUEST_ID);
    }

    public String getRequestId() {
        return pref.getString(KEY_REQUEST_ID, "");
    }

    public void setOnlineStatus(String status) {
        editor.putString(KEY_AVAILABLE_STATUS, status);
        editor.commit();
    }

    public String getOnlineStatus() {
        return pref.getString(KEY_AVAILABLE_STATUS, "");
    }

    public void setStatus(String status) {
        editor.putString(ORDERCOMPLETE, status);
        editor.commit();
    }

    public void setDefaultOnlineStatus(String status) {
        editor.putString(KEY_AVAILABLE_DEFAULT_STATUS, status);
        editor.commit();
    }

    public String getDefaultOnlineStatus() {
        return pref.getString(KEY_AVAILABLE_DEFAULT_STATUS, "");
    }

    public void setDefaultTimeOut(int countDownLimit) {
        editor.putInt(KEY_DEFAULT_TIMEOUT, countDownLimit);
        editor.commit();
    }

    public int getDefaultTimeOut() {
        return pref.getInt(KEY_DEFAULT_TIMEOUT, 60);
    }

    public String getStatus() {
        return pref.getString(ORDERCOMPLETE, "");
    }
}
