package com.speant.delivery.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.FireBase.FireBaseModels.ProviderLocation;
import com.speant.delivery.R;
import com.speant.delivery.ui.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import static com.speant.delivery.Common.SessionManager.KEY_DEVICE_TOKEN;
import static com.speant.delivery.Common.SessionManager.KEY_USER_ID;

public class LocationUpdateService extends Service {
    /* private static final String TAG = "LocationUpdateService";
    DatabaseReference mDatabaseAvailableProv;
    DatabaseReference mDatabaseProvLocation;
    SessionManager sessionManager;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    Context context;

    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;
    private static final String CHANNEL_ID = "channel_01";
    private boolean mChangingConfiguration = false;
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = "com.joldiasho.delivery.Services" + ".started_from_notification";
    private final IBinder mBinder = new LocalBinder();
    private static final int NOTIFICATION_ID = 12345678;
    private Location mLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Service started");

        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            Log.e(TAG, "startedFromNotification: stopSelf" );
            stopSelf();
        }

        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    private void removeLocationUpdates() {
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "removeLocationUpdates: stopSelf" );
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            *//*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             *//*
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    public class LocalBinder extends Binder {

        public LocationUpdateService getService() {
            Log.e(TAG, "getService: LocalBinder" );
            return LocationUpdateService.this;
        }
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdateService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_logo, getString(R.string.remove_location_updates),
                        activityPendingIntent)
                .addAction(R.drawable.ic_logo, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle("Title")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        sessionManager = new SessionManager(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        HashMap<String, String> userDetatils = sessionManager.getUserDetails();
        Log.e("TAG", "onCreate:mDatabaseNewRequest KEY_USER_ID " + userDetatils.get(KEY_USER_ID));
        mDatabaseAvailableProv = FirebaseDatabase.getInstance().getReference().child(CONST.Params.available_providers).child(userDetatils.get(KEY_USER_ID));
        mDatabaseProvLocation = FirebaseDatabase.getInstance().getReference().child(CONST.Params.prov_location).child(userDetatils.get(KEY_USER_ID));
        setLocationRequestCallBack();
        enablePermission();


        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    public boolean enablePermission() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.e("Giri ", "enablePermission: ");
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e("Giri ", "enablePermission: Enabled");
            requestLocationUpdates();
            return true;
        } else {
            Log.e("Giri ", "enablePermission: Disabled");
            EventBus.getDefault().post(new LocationEnableEvent());
            return false;
        }
    }


    public void requestLocationUpdates() {
        Utils.setRequestingLocationUpdates(this, true);
        try {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
            Log.e("Location", "Requesting location updates");
        } catch (Exception e) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e("Location", "Lost location permission. Could not request updates. " + e);
        }
    }

    private void setLocationRequestCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    super.onLocationResult(locationResult);
                    Location mLastLocation = locationResult.getLastLocation();
                    Log.e("TAG", "onLocationResult:mLastLocation " + mLastLocation);
                    ProviderLocation providerLocation = new ProviderLocation();
                    providerLocation.setLat("" + mLastLocation.getLatitude());
                    providerLocation.setLng("" + mLastLocation.getLongitude());
                    //send LatLong to activities to display the scooter marker
                    EventBus.getDefault().post(providerLocation);

                    mLocation = mLastLocation;
                    // Update notification content if running as a foreground service.
                    if (serviceIsRunningInForeground(context)) {
                        mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                    }

                    Log.e("Location", "onLocationResult:getOnlineStatus " + sessionManager.getOnlineStatus());
                    //save the latlng in two nodes in firebase
                    if (sessionManager.getOnlineStatus().equals(CONST.ONLINE)) {
                        Log.e("Location", "onLocationResult:Delivery is available ");
                        mDatabaseAvailableProv.setValue(providerLocation);
                        mDatabaseProvLocation.setValue(providerLocation);
                    }
                    //save the latlng in one node in firebase
                    else if (sessionManager.getOnlineStatus().equals(CONST.OFFLINE)) {
                        Log.e("Location", "onLocationResult:Delivery is not available ");
                        mDatabaseProvLocation.setValue(providerLocation);
                        mDatabaseAvailableProv.removeValue();
                    }

                }else{
                    Log.e("Giri ", "onLocationResult: null" );
                }
            }
        };

    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
        Log.e("ClearFromRecentService", "Service Destroyed");
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

   *//* @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("ClearFromRecentService", "Service Stopped");
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }*//*

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    Log.e(TAG, "serviceIsRunningInForeground: true" );
                    return true;
                }
            }
        }
        Log.e(TAG, "serviceIsRunningInForeground: false" );
        return false;
    }*/

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";

    private static final String TAG = LocationUpdateService.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;
    private SessionManager sessionManager;
    private LocationUpdateService context;
    private DatabaseReference mDatabaseAvailableProv;
    private DatabaseReference mDatabaseProvLocation;
    private String lat, lng;
    HashMap<String, String> userDetails;
    private DatabaseReference mDatabaseProvStatus;
    private DatabaseReference mDatabaseProvLocationqqq;
    private DatabaseReference mDatabaseDeviceToken;
    private boolean isLoggedIn;

    @Override
    public void onCreate() {
        context = this;
        sessionManager = new SessionManager(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        userDetails = sessionManager.getUserDetails();
        Log.e("TAG", "onCreate:mDatabaseNewRequest KEY_USER_ID " + userDetails.get(KEY_USER_ID));
        mDatabaseAvailableProv = FirebaseDatabase.getInstance().getReference().child(CONST.Params.available_providers).child(userDetails.get(KEY_USER_ID));
        mDatabaseProvStatus = FirebaseDatabase.getInstance().getReference().child(CONST.Params.providers_status).child(userDetails.get(KEY_USER_ID)).child(CONST.Params.status);
        mDatabaseProvLocation = FirebaseDatabase.getInstance().getReference().child(CONST.Params.prov_location).child(userDetails.get(KEY_USER_ID));
        mDatabaseDeviceToken = FirebaseDatabase.getInstance().getReference().child(CONST.Params.dev_token).child(userDetails.get(KEY_USER_ID));

        mDatabaseDeviceToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: mDatabaseDeviceToken Key" + dataSnapshot.getKey());
                Log.e(TAG, "onDataChange: mDatabaseDeviceToken Value" + dataSnapshot.getValue());
                if (!userDetails.get(KEY_DEVICE_TOKEN).equals(dataSnapshot.getValue())) {
                    isLoggedIn = false;
                    EventBus.getDefault().post(new LogoutEvent());
                } else {
                    isLoggedIn = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*mDatabaseProvLocationqqq = FirebaseDatabase.getInstance().getReference();
        Query phoneQuery = mDatabaseProvLocationqqq.child(CONST.Params.prov_location);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.e(TAG, "onDataChange:mDatabaseProvLocation "+singleSnapshot.getKey() );
                    Log.e(TAG, "onDataChange:mDatabaseProvLocation "+singleSnapshot.getChildren() );
                    Log.e(TAG, "onDataChange:mDatabaseProvLocation "+singleSnapshot.getValue() );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });*/

        setLocationRequestCallBack();

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service");
            /*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             */
            startForeground(NOTIFICATION_ID, getNotification());
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy:mServiceHandler Before ");
        mServiceHandler.removeCallbacksAndMessages(null);
        Log.e(TAG, "onDestroy:mServiceHandler ");
//        mDatabaseAvailableProv.removeValue();
    }

    private void setLocationRequestCallBack() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    super.onLocationResult(locationResult);
                    Location mLastLocation = locationResult.getLastLocation();
                    Log.e("TAG", "onLocationResult:mLastLocation " + mLastLocation);

                    ProviderLocation providerLocation = new ProviderLocation();
                    providerLocation.setLat("" + mLastLocation.getLatitude());
                    providerLocation.setLng("" + mLastLocation.getLongitude());
                    providerLocation.setUpdated_at(Global.getCurrentTime("yyyy-MM-dd HH:mm:ss"));

                   /* Map<String, Object> updates = new HashMap<String,Object>();
                    updates.put(CONST.Params.lat, ""+mLastLocation.getLatitude());
                    updates.put(CONST.Params.lng, ""+mLastLocation.getLongitude());*/

                    //send LatLong to activities to display the scooter marker
                    EventBus.getDefault().post(providerLocation);
                    mLocation = locationResult.getLastLocation();

//                    onNewLocation(locationResult.getLastLocation());

                    Log.e("Location", "onLocationResult:getOnlineStatus " + sessionManager.getOnlineStatus());
                    Log.e("Location", "onLocationResult:isLoggedIn " + isLoggedIn);

                    if (isLoggedIn) {
                        //save the latlng in two nodes in firebase
                        if (sessionManager.getOnlineStatus().equals(CONST.ONLINE)) {
                            Log.e("Location", "onLocationResult:Delivery is available ");
                            Log.e("Location", "onLocationResult:providerLocation" + mDatabaseAvailableProv);
                            Log.e("Location", "onLocationResult:providerLocation" + mDatabaseProvLocation);
                            Log.e("Location", "onLocationResult:providerLocation" + providerLocation.getLat());
                            Log.e("Location", "onLocationResult:providerLocation" + providerLocation.getLng());
                            mDatabaseAvailableProv.setValue(providerLocation);
                            mDatabaseProvStatus.setValue(CONST.ONLINE_STATUS);
                            mDatabaseProvLocation.setValue(providerLocation);
                        }
                        //save the latlng in one node in firebase
                        else if (sessionManager.getOnlineStatus().equals(CONST.OFFLINE)) {
                            Log.e("Location", "onLocationResult:Delivery is not available ");
                            Log.e("Location", "onLocationResult:providerLocation" + mDatabaseAvailableProv);
                            Log.e("Location", "onLocationResult:providerLocation" + mDatabaseProvLocation);
                            Log.e("Location", "onLocationResult:providerLocation" + providerLocation.getLat());
                            Log.e("Location", "onLocationResult:providerLocation" + providerLocation.getLng());
                            if (CONST.IS_IN_RIDE) {
                                mDatabaseProvStatus.setValue(CONST.ONRIDE_STATUS);
                            } else {
                                mDatabaseProvStatus.setValue(CONST.OFFLINE_STATUS);
                            }
                            mDatabaseProvLocation.setValue(providerLocation);
                            mDatabaseAvailableProv.removeValue();
                        }
                    }

                } else {
                    Log.e("Giri ", "onLocationResult: null");
                }
            }
        };

    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdateService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdateService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .addAction(R.drawable.ic_logo, getString(R.string.remove_location_updates),
                        activityPendingIntent)
                .addAction(R.drawable.ic_logo, getString(R.string.remove_location_updates),
                        servicePendingIntent)
                .setContentText(text)
                .setContentTitle("Title")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(text)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }*/


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setPriority(Notification.PRIORITY_MIN);


        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);

        mLocation = location;

        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(8000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {

        public LocationUpdateService getService() {
            Log.e(TAG, "getService: LocalBinder");
            return LocationUpdateService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    Log.e(TAG, "serviceIsRunningInForeground: true");
                    return true;
                }
            }
        }
        Log.e(TAG, "serviceIsRunningInForeground: false");
        return false;
    }
}
