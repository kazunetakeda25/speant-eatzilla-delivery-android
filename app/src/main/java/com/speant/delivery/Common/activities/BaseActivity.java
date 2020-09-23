package com.speant.delivery.Common.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.EventModels.LocationEnableEvent;
import com.speant.delivery.Services.LocationUpdateService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private LocationUpdateService mService;
    private boolean mBound = false;
    private Global global;
    private SessionManager sessionManager;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdateService.LocalBinder binder = (LocationUpdateService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.e(TAG, "onServiceConnected: ");
            mService.requestLocationUpdates();

            /*if (!global.isMyServiceRunning(LocationUpdateService.class, BaseActivity.this)) {
                startService(new Intent(BaseActivity.this, LocationUpdateService.class));
            }*/
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
            Log.e(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        global = new Global();
        sessionManager = new SessionManager(this);
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdateService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            Log.e(TAG, "onStop:mBound ");
            unbindService(mServiceConnection);
            mBound = false;
        }

        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationEnableEvent locationEnableEvent) {
        Log.e("Giri ", "onMessageEvent: ShoW Dialog");
        stopService(new Intent(this, LocationUpdateService.class));
        showGPSDisabledAlertToUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        sessionManager.setOnlineStatus(CONST.OFFLINE);
        stopService(new Intent(this, LocationUpdateService.class));
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setTitle("Goto Settings Page To Enable GPS")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                                dialog.dismiss();
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
