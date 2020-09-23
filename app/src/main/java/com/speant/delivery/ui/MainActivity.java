package com.speant.delivery.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.RoundedImageView;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.activities.BaseActivity;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.FireBase.FireBaseListeners.DeliveryStatusListener;
import com.speant.delivery.FireBase.FireBaseListeners.NewRequestListener;
import com.speant.delivery.FireBase.FireBaseModels.CurrentRequestFirebase;
import com.speant.delivery.FireBase.FireBaseModels.LatLngModel;
import com.speant.delivery.FireBase.FireBaseModels.ProviderLocation;
import com.speant.delivery.Models.RequestDetailPojo;
import com.speant.delivery.Models.SuccessPojo;
import com.speant.delivery.Models.UpdateRequestPojo;
import com.speant.delivery.R;
import com.speant.delivery.Services.LocationUpdateService;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.speant.delivery.Common.CONST.DELIVERY_REQUEST_ACCEPTED;
import static com.speant.delivery.Common.CONST.ORDER_CANCELLED;
import static com.speant.delivery.Common.CONST.REFRESH_ACTIVITY;
import static com.speant.delivery.Common.SessionManager.KEY_PARTNER_ID;
import static com.speant.delivery.Common.SessionManager.KEY_USER_ID;
import static com.speant.delivery.Common.SessionManager.KEY_USER_IMAGE;
import static com.speant.delivery.Common.SessionManager.KEY_USER_NAME;
import static com.speant.delivery.Common.global.Global.USER;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    private static final int MY_REQUEST_CODE = 108;
    View actionView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btm_status_img)
    ImageView btmStatusImg;
    @BindView(R.id.btm_status_txt)
    AppCompatTextView btmStatusTxt;
    @BindView(R.id.btm_status_relative)
    RelativeLayout btmStatusRelative;
    @BindView(R.id.btm_order_id_txt)
    AppCompatTextView btmOrderIdTxt;
    @BindView(R.id.btm_order_time_txt)
    AppCompatTextView btmOrderTimeTxt;
    @BindView(R.id.order_id_relative)
    RelativeLayout orderIdRelative;
    @BindView(R.id.btm_pickup_call_img)
    ImageView btmPickupCallImg;
    @BindView(R.id.btm_pickup_gps_img)
    ImageView btmPickupGpsImg;
    @BindView(R.id.btm_pickup_hotel_img)
    ImageView btmPickupHotelImg;
    @BindView(R.id.btm_pickup_hotel_txt)
    AppCompatTextView btmPickupHotelTxt;
    @BindView(R.id.btm_pickup_location_txt)
    AppCompatTextView btmPickupLocationTxt;
    @BindView(R.id.btm_delivery_call_img)
    ImageView btmDeliveryCallImg;
    @BindView(R.id.btm_delivery_gps_img)
    ImageView btmDeliveryGpsImg;
    @BindView(R.id.btm_delivery_hotel_img)
    ImageView btmDeliveryHotelImg;
    @BindView(R.id.btm_delivery_hotel_txt)
    AppCompatTextView btmDeliveryHotelTxt;
    @BindView(R.id.btm_delivery_location_txt)
    AppCompatTextView btmDeliveryLocationTxt;
    @BindView(R.id.btm_accept_reject_btn_linear)
    LinearLayout btmAcceptRejectBtnLinear;
    @BindView(R.id.btm_complete_card)
    CardView btmCompleteCard;
    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheet;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.appBar)
    AppBarLayout appBar;

    //BootomSheet
    BottomSheetBehavior behavior;
    @BindView(R.id.work_on_process_linear)
    LinearLayout workOnProcessLinear;
    @BindView(R.id.btm_status_update_txt)
    AppCompatTextView btmStatusUpdateTxt;
    @BindView(R.id.pickup_relative)
    RelativeLayout pickupRelative;
    @BindView(R.id.drop_relative)
    RelativeLayout dropRelative;
    @BindView(R.id.btm_reject_txt)
    AppCompatButton btmRejectTxt;
    @BindView(R.id.btm_accept_txt)
    AppCompatButton btmAcceptTxt;
    NavigationView navigationView;
    DrawerLayout drawer;
    @BindView(R.id.txt_time)
    AppCompatTextView txtTime;
    @BindView(R.id.txt_direction)
    TextView txtDirection;
    @BindView(R.id.btm_paid_by)
    AppCompatTextView btmPaidBy;
    @BindView(R.id.tv_chat)
    AppCompatTextView tvChat;
    private GoogleMap mGoogleMap;
    ActionBarDrawerToggle toggle;
    Marker marker;
    Marker markerDirection;
    MarkerOptions markerOptions;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Polyline polyline = null;

    int timeOutLimit = 60;
    APIInterface apiInterface;
    APIInterface apiService;
    SessionManager sessionManager;
    private LatLng fromLatLng;
    private LatLng toLatLng;
    private LatLng trackLatLng;
    private LatLng deliverBoyLatLng;

    private SwitchCompat switcher;

    //FireBase
    DatabaseReference mDatabaseLatLng;
    DatabaseReference mDatabaseCurrentRequest;

    private String addressStr;
    private String latitude, longitude;
    private boolean gps = false;
    private String provider_id = "0";
    private String status;
    private String userId;
    boolean isFirst = true;
    int count = 20;
    HashMap<String, String> userDetatils;
    boolean isStatusTwoFirst = false;
    private AppCompatTextView nav_header_name;
    private AppCompatTextView nav_header_id;
    private RoundedImageView nav_header_prf_img;
    Global global;
    public Marker bikeMarker, dropMarker, pickupMarker;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private NewRequestListener newRequestListener;
    private DeliveryStatusListener deliveryStatusListener;
    private LocationUpdateService locationUpdateService;
    private String requestId;
    private int requestedUserId;
    static boolean active = false;
    RequestDetailPojo.UserDetail userPojo;
    RequestDetailPojo.RestaurantDetail restaurantDetailPojo;
    private CountDownTimer countDownTimer;
    MediaPlayer mediaPlayer;
    boolean isCountDownRunning = false;
    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        SetActionBar();

        //intialise Map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();


        global = new Global();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);

        sessionManager = new SessionManager(this);
        userDetatils = sessionManager.getUserDetails();

        userId = userDetatils.get(KEY_USER_ID);
        Log.e("Nive ", "onCreate: " + userId);
        toolbar.setTitle("SPEANT Delivery");
        setSupportActionBar(toolbar);
        appBar.bringToFront();

        setBottomView();
        setNavigationDrawer();
        setNavigationHeader();

        //start current Request Listener
        deliveryStatusListener = new DeliveryStatusListener(MainActivity.this, userId);

        // INFO TODO implement Chat
        connectWebSocket();
    }

    // INFO TODO implement Chat
    private void connectWebSocket() {


        Log.e(TAG, "connectWebSocket:is true ");

        String url = Global.SOCKET_URL;

        URI uri;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "connectWebSocket: " + e.toString());
            return;
        }


        mWebSocketClient = new

                WebSocketClient(uri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.i("Websocket", "Opened");

                        JSONObject jsonObject = new JSONObject();

                        try {
                            jsonObject.put("type", "init");
                            jsonObject.put("id", "Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mWebSocketClient.send(String.valueOf(jsonObject));

                        sessionManager.putSocketUinqueId("Provider_" + sessionManager.getUserDetails().get(KEY_USER_ID));

                        Log.e(TAG, "onOpen:getSocketUniqueId() " + sessionManager.getSocketUniqueId());


                    }

                    @Override
                    public void onMessage(String s) {

                        Log.e(TAG, "message: " + s);

                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.i("Websocket", "Closed " + s);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Websocket", "Error " + e.getMessage());
                    }
                };


        mWebSocketClient.connect();


    }


    @Override
    protected void onResume() {
        super.onResume();
        active = true;
       /* //Location service will be started on resume if the service is not already started
        if (!global.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
            startService(new Intent(this, LocationUpdateService.class));
        }*/

        //this service will be called if the deliveryboy location is not null while resume
        /*if (deliverBoyLatLng != null) {
            getCurrentBooking();
            Log.e(TAG, "onResume:getCurrentBooking ");
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        active = false;
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
         if (deliverBoyLatLng != null) {
            getCurrentBooking();
            Log.e(TAG, "onResume:getCurrentBooking ");
        }
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        removeFirebaseValue();
        deliveryStatusListener.removeListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        removeFirebaseValue();
    }*/


    public void getCurrentBooking() {
        SessionManager sessionManager = new SessionManager(MainActivity.this);
        Call<RequestDetailPojo> call = apiInterface.getCurrentBooking(sessionManager.getHeader(), sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<RequestDetailPojo>() {
            @Override
            public void onResponse(Call<RequestDetailPojo> call, Response<RequestDetailPojo> response) {
                if (response.code() == CONST.SUCCESS_CODE) {
                    if (response.body().getStatus()) {
                        userPojo = response.body().getUserDetail();
                        restaurantDetailPojo = response.body().getRestaurantDetail();
                        Log.e("Nive ", "response.body(): " + response.body());
                        RequestDetailPojo.UserDetail userPojo = response.body().getUserDetail();
                        RequestDetailPojo.RestaurantDetail restaurantDetailPojo = response.body().getRestaurantDetail();
                        RequestDetailPojo.AddressDetail dataPojo = response.body().getAddressDetail().get(0);


                        requestedUserId = response.body().getUserDetail().getId();
                        requestId = response.body().getRequestId();
                        status = response.body().getRequest_status();
                        btmPickupHotelTxt.setText(restaurantDetailPojo.getRestaurantName());
                        btmPickupLocationTxt.setText(restaurantDetailPojo.getAddress());
                        btmDeliveryHotelTxt.setText(userPojo.getName());
                        btmDeliveryLocationTxt.setText(dataPojo.getDAddress());
                        if (response.body().getBillDetail().get(0).getPaid_type() == 1) {
                            btmPaidBy.setText("Paid by Cash");
                        } else {
                            btmPaidBy.setText("Paid by Card");
                        }

                        Log.e("Nive ", "onResponse: " + restaurantDetailPojo.getPhone());

                        btmOrderIdTxt.setText("Order ID #" + response.body().getOrderId());

                        fromLatLng = new LatLng(dataPojo.getSLat(), dataPojo.getSLng());
                        toLatLng = new LatLng(dataPojo.getDLat(), dataPojo.getDLng());

                        Log.e(TAG, "onResponse:fromLatLng " + fromLatLng + "toLatLng" + toLatLng);
                        Log.e(TAG, "onResponse:deliverBoyLatLng " + deliverBoyLatLng);

                        String date = Global.setDate(response.body().getOrderedTime(), "yyyy-MM-dd HH:mm:ss", "dd MMM , hh:mm a");
                        btmOrderTimeTxt.setText(date);

                        Glide.with(getApplicationContext())
                                .load(restaurantDetailPojo.getImage())
                                .into(btmPickupHotelImg);

                        Picasso.get().load(userPojo.getProfileImage()).placeholder(R.drawable.ic_user).into(btmDeliveryHotelImg);

                        //to restrict calling cancelApi after cancelling order from user or restaurant
                        if (response.body().getRequest_status().equals(ORDER_CANCELLED)
                                || response.body().getRequest_status().equals(DELIVERY_REQUEST_ACCEPTED)) {
                            stopCountDownTimer();
                        }

//                        timeOutLimit = Global.getTimeOutSeconds(response.body().getAssigned_time(), response.body().getNotification_time());
                        bottomSheetVisibilitychange(response.body().getRequest_status());
                        setStatusView();
                    } else {
//                        CommonFunctions.shortToast(getApplicationContext(), "No current bookings");
                        status = null;
                        requestId = null;
                        bottomSheetVisibilitychange(CONST.NO_ORDER);
                        stopCountDownTimer();
                        setStatusView();

                    }
                } else if (response.code() == 401) {
                    Log.e("Nive ", "onResponse: 401 " + response.message());
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                    sessionManager.logoutUser(MainActivity.this);
                } else {
                    Log.e("Nive ", "onResponse: response.message()" + response.message());
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

                CommonFunctions.removeProgressDialog();
            }

            @Override
            public void onFailure(Call<RequestDetailPojo> call, Throwable t) {
                Log.e("Nive ", "onFailure: GetOrder" + t.toString());
                CommonFunctions.removeProgressDialog();
                CommonFunctions.shortToast(getApplicationContext(), CONST.SERVICE_FAILED);
            }
        });
    }

    private void setNavigationDrawer() {
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setNavigationHeader() {
        View header = navigationView.getHeaderView(0);
        /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        nav_header_name = header.findViewById(R.id.nav_user_name_txt);
        nav_header_id = header.findViewById(R.id.nav_user_id_txt);
        nav_header_prf_img = header.findViewById(R.id.nav_prof_img);
        nav_header_name.setText(sessionManager.getUserDetails().get(KEY_USER_NAME));
        nav_header_id.setText(sessionManager.getUserDetails().get(KEY_PARTNER_ID));
        Log.e("Giri ", "setNavigationHeader: " + sessionManager.getUserDetails().get(KEY_USER_IMAGE));
        Glide.with(this)
                .load(sessionManager.getUserDetails().get(KEY_USER_IMAGE))
                .into(nav_header_prf_img);
    }


    public void setBottomView() {

        behavior = BottomSheetBehavior.from(bottomSheet);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.MaterialDialogSheet);
        dialog.setContentView(R.layout.activity_main);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //if you want the modal to be dismissed when user drags the bottomsheet down
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    public void SetActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        positionMap();
    }

    private void positionMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        //set my location button and its position
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 60);

        //get my current Location Once
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = null;
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                setDeliveryBoyMarker(location.getLatitude(), location.getLongitude());
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Log.e(TAG, "backppressed:removeValue btn CHnage ");
//                            mDatabaseAvailableProv.removeValue();
                            switcher.setChecked(false);
                            finishAffinity();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.avalability_switch);
        actionView = MenuItemCompat.getActionView(item);
        switcher = (SwitchCompat) actionView.findViewById(R.id.switchForActionBar);
//        sessionManager.setOnlineStatus(CONST.OFFLINE);
        btmStatusImg.setBackgroundResource(R.drawable.offline_img);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnlineSwitch();
            }
        });
        Log.e(TAG, "onCreateOptionsMenu:switcherStatus " + status);
        setSwitcherVisibility(status);
        return true;
    }

    private void setOnlineSwitch() {
        Log.e(TAG, "setOnlineSwitch: Service Run Check" + global.isMyServiceRunning(LocationUpdateService.class, MainActivity.this));
        if (switcher.isChecked()) {
            updateFirebase();
            Log.e("Eatzilla ", "switcher.isChecked(): " + switcher.isChecked());
            //used to check if the user wish to have which status while selecting the switch button
            sessionManager.setDefaultOnlineStatus(CONST.ONLINE);
            sessionManager.setOnlineStatus(CONST.ONLINE);
            CONST.IS_IN_RIDE = false;
            btmStatusImg.setBackgroundResource(R.drawable.online_img);
            btmStatusTxt.setText("Waiting for the new Order");
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            removeFirebaseValue();
            //used to check if the user wish to have which status while selecting the switch button
            sessionManager.setDefaultOnlineStatus(CONST.OFFLINE);
            sessionManager.setOnlineStatus(CONST.OFFLINE);
            CONST.IS_IN_RIDE = false;
            btmStatusImg.setBackgroundResource(R.drawable.offline_img);
            btmStatusTxt.setText("Turn online to start accepting orders");
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    private void removeFirebaseValue() {
        Log.e(TAG, "removeFirebaseValue:available_providers ");
        mDatabaseLatLng = FirebaseDatabase.getInstance().getReference().child(CONST.Params.available_providers).child(userId);
        mDatabaseLatLng.removeValue();
    }

    private void updateFirebase() {
        //start LocationUpdate service
        if (!global.isMyServiceRunning(LocationUpdateService.class, MainActivity.this)) {
            Log.e(TAG, "updateFirebase: service starting");
            startService(new Intent(this, LocationUpdateService.class));
        }

       /* if (switcher.isChecked()) {
            getCurrentBooking();
        }*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.avalability_switch) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawers();
            }
        }, 2000);*/

        drawer.closeDrawer(GravityCompat.START, false);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_order_history:
                startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_payout:
                startActivity(new Intent(MainActivity.this, PayoutsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_earning_hisory:
                startActivity(new Intent(MainActivity.this, EarningActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_faq:
                startActivity(new Intent(MainActivity.this, FAQActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_help:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_about_us:
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.nav_logout:
                Log.e(TAG, "logout:removeValue btn Change ");
                sessionManager.logoutUser(MainActivity.this);
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void bottomSheetVisibilitychange(String bookingStatus) {
        Log.e(TAG, "bottomSheetVisibilitychange:SwitcherStatus " + status);
        setSwitcherVisibility(bookingStatus);

        switch (bookingStatus) {

            case CONST.FOOD_PREPARED:
                btmStatusRelative.setVisibility(View.GONE);
                workOnProcessLinear.setVisibility(View.VISIBLE);
                btmAcceptRejectBtnLinear.setVisibility(View.VISIBLE);
                btmCompleteCard.setVisibility(View.GONE);
                txtDirection.setVisibility(View.GONE);
                Log.e(TAG, "bottomSheetVisibilitychange:setCountDownTimer");
                setCountDownTimer();
                break;
            case DELIVERY_REQUEST_ACCEPTED:
                btmStatusRelative.setVisibility(View.GONE);
                workOnProcessLinear.setVisibility(View.VISIBLE);
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText(CONST.BUTTON_REACHED_RESTAURANT);
                trackLatLng = new LatLng(fromLatLng.latitude, fromLatLng.longitude);
                break;
            case CONST.REACHED_RESTAURANT:
                btmStatusRelative.setVisibility(View.GONE);
                workOnProcessLinear.setVisibility(View.VISIBLE);
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText(CONST.TOWARDS_CUSTOMER);
                trackLatLng = new LatLng(toLatLng.latitude, toLatLng.longitude);
                break;
            case CONST.FOOD_COLLECTED_ONWAY:
                btmStatusRelative.setVisibility(View.GONE);
                workOnProcessLinear.setVisibility(View.VISIBLE);
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText(CONST.DELIVERED_TO_CUSTOMER);
                trackLatLng = new LatLng(toLatLng.latitude, toLatLng.longitude);
                break;
            case CONST.FOOD_DELIVERED:
                btmStatusRelative.setVisibility(View.GONE);
                workOnProcessLinear.setVisibility(View.VISIBLE);
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText(CONST.CASH_RECEIVED);
                trackLatLng = new LatLng(toLatLng.latitude, toLatLng.longitude);
                break;
            default:
                btmStatusRelative.setVisibility(View.VISIBLE);
                workOnProcessLinear.setVisibility(View.GONE);
                txtDirection.setVisibility(View.GONE);
                break;

        }


    }

    private void setCountDownTimer() {
        Log.e("TAG", "setCountDownTimer:timeOutLimit " + timeOutLimit);
        if (timeOutLimit > 2) {
            if (countDownTimer == null && !isCountDownRunning) {
                Log.e(TAG, "setCountDownTimer:Starts");
                startMediaPlayer();
                final int[] counter = {timeOutLimit};
                txtTime.setVisibility(View.VISIBLE);
                txtTime.setText(getString(R.string.request_ends));
                countDownTimer = new CountDownTimer(timeOutLimit * 1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        isCountDownRunning = true;
                        Log.e(TAG, "onTick: " + counter[0]);
                        txtTime.setText(getString(R.string.request_ends) + " " + counter[0]);
                        counter[0]--;
                    }

                    public void onFinish() {
                        if (isCountDownRunning) {
                            isCountDownRunning = false;
                            Log.e(TAG, "onFinish:setCountDownTimer");
                            stopCountDownTimer();
                            txtTime.setVisibility(View.GONE);
                            jsonCancelRequest(requestId);
                        }
                    }
                }.start();
            }
        } else {
            isCountDownRunning = false;
            Log.e(TAG, "Timeout is 0");
            stopCountDownTimer();
            txtTime.setVisibility(View.GONE);
            jsonCancelRequest(requestId);
        }
    }

    private void stopCountDownTimer() {
        if (countDownTimer != null) {
            Log.e(TAG, "stopCountDownTimer: ");
            countDownTimer.cancel();
            countDownTimer = null;
            isCountDownRunning = false;
        }
        stopMediaPlayer();
        txtTime.setVisibility(View.GONE);
    }

    private void startMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isCountDownRunning) {
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                        }
                    } else {
                        stopMediaPlayer();
                    }
                }
            });
            mediaPlayer.start();
            Log.e(TAG, "startMediaPlayer:Started ");
        } catch (Exception e) {
            Log.e(TAG, "startMediaPlayer:Exception onStart MediaPlayer " + e);
        }


    }

    private void stopMediaPlayer() {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            Log.e(TAG, "onResponse:mediaPlayer exception onstop " + e);
        }
    }

    private void setSwitcherVisibility(String status) {
        Log.e("Nive ", "setSwitcherVisibility: status" + status);
        if (status != null) {
            int statusId = Integer.parseInt(status);

            if (statusId > 1 && statusId < 7) {
                Log.e(TAG, "setSwitcherVisibility:OFFLINE ");
                sessionManager.setOnlineStatus(CONST.OFFLINE);
                CONST.IS_IN_RIDE = true;
                if (switcher != null) {
                    Log.e(TAG, "setSwitcherVisibility:OFFLINE switcher");
                    switcher.setChecked(false);
                    switcher.setVisibility(View.GONE);
                }
            }

            Log.e("Nive ", "setSwitcherVisibility: sessionManager" + sessionManager.getStatus());

            //------------------ updated on May 28
            /*if (status.equals("-1")) {
                if (sessionManager.getStatus().equals("7")) {
                    switcher = (SwitchCompat) actionView.findViewById(R.id.switchForActionBar);
                    if (switcher != null) {
                        sessionManager.setOnlineStatus(CONST.ONLINE);
                        Log.e("Nive ", "setSwitcherVisibility: notnull");
//                        updateFirebase();
                        switcher.setChecked(true);
                        switcher.setVisibility(View.VISIBLE);
                        setOnlineSwitch();
                    }
                }
            }*/


            //------------------ updated on May 28
            if (status.equals("-1")) {
                if (sessionManager.getDefaultOnlineStatus().isEmpty() || sessionManager.getDefaultOnlineStatus().equals(CONST.ONLINE)) {
                    switcher = (SwitchCompat) actionView.findViewById(R.id.switchForActionBar);
                    sessionManager.setOnlineStatus(CONST.ONLINE);
                    CONST.IS_IN_RIDE = false;
                    if (switcher != null) {
                        Log.e("Nive ", "setSwitcherVisibility: notnull");
                        Log.e("Nive ", "setSwitcherVisibility: switcher before" + switcher.isChecked());
//                        updateFirebase();
                        switcher.setChecked(true);
                        switcher.setVisibility(View.VISIBLE);
                        Log.e("Nive ", "setSwitcherVisibility: switcher after" + switcher.isChecked());
                        setOnlineSwitch();
                    }
                }
            }

            //------------------------------------
        }
    }


    public void buttonClicked(String buttonName) {

        switch (buttonName) {

            case CONST.BUTTON_ACCEPT:
                removeFirebaseValue();
                switcher.setChecked(false);
                stopCountDownTimer();
                jsonStatusUpdate(requestId, DELIVERY_REQUEST_ACCEPTED);
                break;
            case CONST.BUTTON_REJECT:
                stopCountDownTimer();
                jsonCancelRequest(requestId);
                break;
            case CONST.BUTTON_REACHED_RESTAURANT:
                jsonStatusUpdate(requestId, CONST.REACHED_RESTAURANT);
                break;
            case CONST.TOWARDS_CUSTOMER:
                jsonStatusUpdate(requestId, CONST.FOOD_COLLECTED_ONWAY);

                break;
            case CONST.DELIVERED_TO_CUSTOMER:
                jsonStatusUpdate(requestId, CONST.FOOD_DELIVERED);

                break;
            case CONST.CASH_RECEIVED:
                jsonStatusUpdate(requestId, CONST.ORDER_COMPLETE);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        Log.e("Nive ", "onDestroy: ");

        removeFirebaseValue();
        deliveryStatusListener.removeListener();
        stopCountDownTimer();

        super.onDestroy();
    }

    private void setStatusView() {
        Log.e(TAG, "setStatusView:Sattus " + status);
        Log.e(TAG, "setStatusView:deliverBoyLatLng " + deliverBoyLatLng);
        Log.e(TAG, "setStatusView:fromLatLng " + fromLatLng);
        Log.e(TAG, "setStatusView:toLatLng " + toLatLng);
        //status will be null if no booking are available
        if (status == null) {
            //set Map clear to reset map on cancelling a delivery request
            mGoogleMap.clear();
        } else {
            switch (status) {
                case CONST.FOOD_PREPARED:
                    if (deliverBoyLatLng != null && fromLatLng != null && toLatLng != null) {
                        mGoogleMap.clear();
                        setPickupMarker();
                        setDropMarker();
                        float bearing = (float) Global.bearingBetweenLocations(deliverBoyLatLng, fromLatLng);
                        global.directionJson(MainActivity.this, deliverBoyLatLng, fromLatLng, mGoogleMap, false);
                        global.directionJson(MainActivity.this, fromLatLng, toLatLng, mGoogleMap, true);
                        if (bikeMarker != null) {
                            bikeMarker.setRotation(bearing);
                        }

                    }
                    break;
                case DELIVERY_REQUEST_ACCEPTED:
                    if (deliverBoyLatLng != null && fromLatLng != null) {
                        mGoogleMap.clear();
                        float bearing = (float) Global.bearingBetweenLocations(deliverBoyLatLng, fromLatLng);
                        global.directionJson(MainActivity.this, deliverBoyLatLng, fromLatLng, mGoogleMap, false);
                        if (bikeMarker != null) {
                            bikeMarker.setRotation(bearing);
                        }
                        setPickupMarker();
                    }
                    break;

                case CONST.REACHED_RESTAURANT:
                    if (deliverBoyLatLng != null && toLatLng != null) {
                        mGoogleMap.clear();
                        global.directionJson(MainActivity.this, deliverBoyLatLng, toLatLng, mGoogleMap, false);
                        float bearing = (float) Global.bearingBetweenLocations(deliverBoyLatLng, toLatLng);
                        if (bikeMarker != null) {
                            bikeMarker.setRotation(bearing);
                        }
                        setDropMarker();
                    }
                    break;

                case CONST.FOOD_COLLECTED_ONWAY:
                    if (deliverBoyLatLng != null && toLatLng != null) {
                        mGoogleMap.clear();
                        float bearing = (float) Global.bearingBetweenLocations(deliverBoyLatLng, toLatLng);
                        global.directionJson(MainActivity.this, deliverBoyLatLng, toLatLng, mGoogleMap, false);
                        if (bikeMarker != null) {
                            bikeMarker.setRotation(bearing);
                        }
                        setDropMarker();
                    }
                    break;

                case CONST.FOOD_DELIVERED:
                    mGoogleMap.clear();
                    break;

                case CONST.ORDER_COMPLETE:
                    mGoogleMap.clear();
                    break;

            }
        }
    }

    private void updateFirebaseStatus(String status, String requestId) {
        Log.e(TAG, "updateFirebaseStatus:requestId " + requestId);
        Log.e(TAG, "updateFirebaseStatus:userId " + userId);
        Log.e(TAG, "updateFirebaseStatus:status " + status);
        Log.e(TAG, "updateFirebaseStatus:requestedUserId " + requestedUserId);
        mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.current_request).child(requestId);

        String lat = "" + deliverBoyLatLng.latitude;
        String lng = "" + deliverBoyLatLng.longitude;

        LatLngModel latLngModel = new LatLngModel();
        latLngModel.setLatitude(deliverBoyLatLng.latitude);
        latLngModel.setLongitude(deliverBoyLatLng.longitude);
        CurrentRequestFirebase currentRequestFirebase = new CurrentRequestFirebase();
        currentRequestFirebase.setLatLng(latLngModel);
        currentRequestFirebase.setProvider_id(userId);
        currentRequestFirebase.setRequest_id(requestId);
        currentRequestFirebase.setStatus(Long.parseLong(status));
        currentRequestFirebase.setUser_id(requestedUserId);

        mDatabaseCurrentRequest.setValue(currentRequestFirebase);

        Log.e("Nive ", "updateFirebaseStatus: " + status);


    }

    private void setPickupMarker() {
        if (fromLatLng != null && mGoogleMap != null) {
            if (pickupMarker != null) {
                pickupMarker.remove();
            }
            pickupMarker = mGoogleMap.addMarker(new MarkerOptions().position(this.fromLatLng).title("Restaurant")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_hotel)));
        }
    }

    private void setDropMarker() {
        if (toLatLng != null && mGoogleMap != null) {
            if (dropMarker != null) {
                dropMarker.remove();
            }
            dropMarker = mGoogleMap.addMarker(new MarkerOptions().position(toLatLng).title("User")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_home)));
        }
    }


    @OnClick({R.id.btm_pickup_call_img, R.id.btm_pickup_gps_img, R.id.pickup_relative, R.id.btm_delivery_call_img,
            R.id.btm_delivery_gps_img, R.id.drop_relative, R.id.btm_reject_txt, R.id.btm_accept_txt,
            R.id.btm_status_update_txt, R.id.txt_direction, R.id.tv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btm_pickup_call_img:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + "+" + restaurantDetailPojo.getPhone()));
                startActivity(dialIntent);
                break;

            case R.id.tv_chat:
                // INFO TODO implement Chat

                Intent intentChat = new Intent(this, ChatSelectActivity.class);

                if (requestId != null && requestedUserId != 0) {
                    intentChat.putExtra(Global.ORDER_ID, requestId);
                    intentChat.putExtra(Global.USER_ID, requestedUserId);
//                    intentChat.putExtra(Global.FROM_TYPE, USER);
                }
                startActivity(intentChat);


                break;
            case R.id.btm_pickup_gps_img:
                break;
            case R.id.txt_direction:
                try {
                    Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + trackLatLng.latitude + "," + trackLatLng.longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    CommonFunctions.shortToast(MainActivity.this, e.getMessage());
                }
                break;
            case R.id.pickup_relative:
                if (countDownTimer == null) {
                    Intent intent = new Intent(MainActivity.this, ActivityTaskDetail.class);
                    intent.putExtra(CONST.Params.request_id, requestId);
                    startActivityForResult(intent, REFRESH_ACTIVITY);
                }

                break;
            case R.id.btm_delivery_call_img:
                Intent dialIntentDelivery = new Intent(Intent.ACTION_DIAL);
                dialIntentDelivery.setData(Uri.parse("tel:" + "+" + userPojo.getPhone()));
                startActivity(dialIntentDelivery);
                break;
            case R.id.btm_delivery_gps_img:
                break;
            case R.id.drop_relative:
                if (countDownTimer == null) {
                    Intent intents = new Intent(MainActivity.this, ActivityTaskDetail.class);
                    intents.putExtra(CONST.Params.request_id, requestId);
                    startActivityForResult(intents, REFRESH_ACTIVITY);
                }
                break;
            case R.id.btm_reject_txt:
                buttonClicked(btmRejectTxt.getText().toString());
                break;
            case R.id.btm_accept_txt:
                buttonClicked(btmAcceptTxt.getText().toString());
                break;
            case R.id.btm_status_update_txt:
                buttonClicked(btmStatusUpdateTxt.getText().toString());
                break;
        }
    }


    public void jsonStatusUpdate(String requestId, String status) {
        CommonFunctions.showSimpleProgressDialog(MainActivity.this, "Updating Status", false);
        Log.e("Nive ", "jsonStatusUpdate:status " + status);
        HashMap<String, String> statusMap = new HashMap<String, String>();
        statusMap.put(CONST.Params.request_id, requestId);
        statusMap.put(CONST.Params.status, status);
        Call<UpdateRequestPojo> call = apiInterface.setUpdateRequest(sessionManager.getHeader(), statusMap, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<UpdateRequestPojo>() {
            @Override
            public void onResponse(Call<UpdateRequestPojo> call, Response<UpdateRequestPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        tvChat.setVisibility(View.VISIBLE);

                        if (status.equals("7")) {
                            sessionManager.setStatus("7");
                        }

                        updateFirebaseStatus(status, requestId);
                        Log.e(TAG, "jsonStatusUpdate:getCurrentBooking ");
                        getCurrentBooking();

                    } else {
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                    }

                } else if (response.code() == 401) {
                    sessionManager.logoutUser(MainActivity.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }


            }

            @Override
            public void onFailure(Call<UpdateRequestPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                CommonFunctions.removeProgressDialog();
            }
        });

    }


    public void jsonCancelRequest(String requestId) {
        CommonFunctions.showSimpleProgressDialog(MainActivity.this, "Cancelling the Order.Please wait!", false);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(CONST.Params.request_id, requestId);
        Call<SuccessPojo> call = apiInterface.setCancelRequest(sessionManager.getHeader(), map, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        //firebase update of current status is not needed while cancel
                        Log.e(TAG, "onResponse:jsonCancelRequest getCurrentBooking");
                        getCurrentBooking();
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                } else if (response.code() == 401) {
                    sessionManager.logoutUser(MainActivity.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }


            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {
                CommonFunctions.removeProgressDialog();
                stopCountDownTimer();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationUpdate(ProviderLocation providerLocation) {
        Log.e(TAG, "onLocationUpdate:providerLocation " + providerLocation);
        double lat = Double.parseDouble(providerLocation.getLat());
        double lng = Double.parseDouble(providerLocation.getLng());
        setDeliveryBoyMarker(lat, lng);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnauthorise(LogoutEvent logoutEvent) {
        Log.e(TAG, "onUnauthorise: Event");
        sessionManager.logoutUser(this);
    }

    private void setDeliveryBoyMarker(double lat, double lng) {
        //getCurrentBooking service will be called on first time delivery boy location is fetched in this activity
        if (deliverBoyLatLng == null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15.0f));
            deliverBoyLatLng = new LatLng(lat, lng);
            Log.e(TAG, "onResponse:setDeliveryBoyMarker getCurrentBooking");
            getCurrentBooking();
        } else {
            deliverBoyLatLng = new LatLng(lat, lng);
        }

        if (mGoogleMap != null) {
            if (bikeMarker != null) {
                bikeMarker.remove();
            }
            bikeMarker = mGoogleMap.addMarker(new MarkerOptions().position(deliverBoyLatLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_bike)));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESH_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                CommonFunctions.showSimpleProgressDialog(MainActivity.this, "Updating Status", false);
                Log.e(TAG, "onActivityResult:RefreshActivity ");
                Log.e(TAG, "onActivityResult:getCurrentBooking ");
                getCurrentBooking();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCurrentRequest(CurrentRequestFirebase currentRequestFirebase) {
        String currentRequestId = currentRequestFirebase.getRequest_id();
        Log.e(TAG, "OnNewRequest:EventBus " + currentRequestId);
        if (!active) {
            Intent i = new Intent(this, MainActivity.class);
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(i);
        }
        if (requestId == null) {
            Log.e(TAG, "onResponse:OnCurrentRequest getCurrentBooking");
            getCurrentBooking();
        }


        //setting camera position

         /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/



       /* HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put(CONST.Params.request_id, requestId);
        jsonRequestDetail(requestMap);*/
    }



    /*    public void jsonRequestDetail(HashMap<String, String> map) {

        Log.e(TAG, "jsonRequestDetail: " + map);
        Call<RequestDetailPojo> call = apiInterface.getRequestDetail(sessionManager.getHeader(), map);
        call.enqueue(new Callback<RequestDetailPojo>() {
            @Override
            public void onResponse(Call<RequestDetailPojo> call, Response<RequestDetailPojo> response) {

                Log.e(TAG, "onResponse: " + response.code());

                if (response.code() == 200) {

                    Log.e(TAG, "onResponse: " + response.body().getStatus());

                    if (response.body().getStatus()) {

                        RequestDetailPojo.UserDetail userPojo = response.body().getUserDetail();
                        RequestDetailPojo.RestaurantDetail restaurantDetailPojo = response.body().getRestaurantDetail();
                        RequestDetailPojo.AddressDetail dataPojo = response.body().getAddressDetail().get(0);

                        btmPickupHotelTxt.setText(restaurantDetailPojo.getRestaurantName());
                        btmPickupLocationTxt.setText(restaurantDetailPojo.getAddress());
                        btmDeliveryHotelTxt.setText(userPojo.getName());
                        btmDeliveryLocationTxt.setText(dataPojo.getDAddress());

                        btmOrderIdTxt.setText("Order ID #" + response.body().getOrderId());

                        fromLatLng = new LatLng(dataPojo.getSLat(), dataPojo.getSLng());
                        toLatLng = new LatLng(dataPojo.getDLat(), dataPojo.getDLng());

                        Log.e(TAG, "onResponse: " + fromLatLng + "\t\t" + toLatLng);

                        String date = Global.setDate(response.body().getOrderedTime(), "yyyy-MM-dd HH:mm:ss", "dd MMM , hh:mm a");
                        btmOrderTimeTxt.setText(date);

                        Glide.with(getApplicationContext())
                                .load(restaurantDetailPojo.getImage())
                                .into(btmPickupHotelImg);

                        Glide.with(getApplicationContext())
                                .load(userPojo.getProfileImage())
                                .into(btmDeliveryHotelImg);
//                        mDatabaseCurrentRequest.child(CONST.Params.status).setValue(status);
                        bottomSheetVisibilitychange(status);


                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().toString());

                } else if (response.code() == 401) {
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                    sessionManager.logoutUser();
                } else
                    CommonFunctions.shortToast(getApplicationContext(), response.message());

            }

            @Override
            public void onFailure(Call<RequestDetailPojo> call, Throwable t) {

            }
        });

    }*/

    /*private void setFirebaseListener() {
        Log.e(TAG, "onCreate:mDatabaseNewRequest KEY_USER_ID " + userId);

        mDatabaseNewRequest = FirebaseDatabase.getInstance().getReference().child(CONST.Params.new_request).child(userId);
        mDatabaseNewRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.e(TAG, "onDataChange:mDatabaseNewRequest map " + map);
                if (map != null) {
                    request_id = String.valueOf(map.get(CONST.Params.request_id));


                    mDatabaseCurrentRequest.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            Log.e(TAG, "onDataChange: mDatabaseCurrentRequest map " + map);
                            if (map != null) {
                                request_id = String.valueOf(map.get(CONST.Params.request_id));
                                provider_id = String.valueOf(map.get(CONST.Params.provider_id));
                                status = String.valueOf(map.get(CONST.Params.status));
                                if (status.equalsIgnoreCase("2")) {
                                    isStatusTwoFirst = !isStatusTwoFirst;
                                } else
                                    isStatusTwoFirst = false;
                                Log.e(TAG, "onDataChange: " + request_id + "\t" + provider_id + "\t" + status);
                                HashMap<String, String> requestMap = new HashMap<>();
                                requestMap.put(CONST.Params.request_id, request_id);
                                jsonRequestDetail(requestMap);
                            }
                            if (map == null) {
                                bottomSheetVisibilitychange(String.valueOf(1));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });


                    Log.e(TAG, "onDataChange: " + request_id);
                } else {
                    bottomSheetVisibilitychange(String.valueOf(1));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }*/


    /*@Override
    public void locationChanged(Location location) {

        Log.e(TAG, "locationChanged: " + gps);

        mLastLocation = location;
        //Place current location marker
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());


        if (count < 20) {
            count++;
        } else {

            count = 0;
            //This GPS to disable Tracking if activity is on pause or stop state
            if (gps) {

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        deliverBoyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        //setting bike marker onLocationChange
                        if (bikeMarker != null) {
                            bikeMarker.remove();
                        }
                        if (mGoogleMap != null) {
                            bikeMarker = mGoogleMap.addMarker(new MarkerOptions().position(deliverBoyLatLng).title("Your Location")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_bike)));
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(deliverBoyLatLng));
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
                            setStatusView();
                        }

                        if (isFirst) {
                            if (mGoogleMap != null) {
                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(deliverBoyLatLng));
                                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
                                isFirst = false;
                            }
                        }

                        addressStr = address;

                        CONST.currentAddress = addressStr;

                        if (switcher != null) {
                            int currentStatus = Integer.parseInt(status);
                            Log.e(TAG, "locationChanged:currentStatus " + currentStatus);
                            if (switcher.isChecked() && (currentStatus == 1 || currentStatus >= 7)) {
                                Log.e(TAG, "locationChanged:update btn CHnage ");
                                HashMap<String, String> addressMap = new HashMap<>();
                                mDatabaseAvailableProv.child(CONST.Params.lat).setValue(String.valueOf(latitude));
                                mDatabaseAvailableProv.child(CONST.Params.lng).setValue(String.valueOf(longitude));
                            } else {
                                Log.e(TAG, "locationChanged:removeValue btn CHnage ");
                                mDatabaseAvailableProv.removeValue();
                            }

                            Log.e(TAG, "onLocationChanged: " + addressStr);

                            this.latitude = String.valueOf(latitude);
                            this.longitude = String.valueOf(longitude);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Log.e(TAG, "locationChanged: Count " + count);
                if (!(status.equalsIgnoreCase("0") || status.equalsIgnoreCase("1"))) {
                    mDatabaseCurrentRequest.child(CONST.Params.LatLng).setValue(deliverBoyLatLng);
                    Log.e(TAG, "locationChanged: update" + status);
                } else {
                    Log.e(TAG, "locationChanged: no" + status);
                }
            }
        }
    }

    @Override
    public void displayGPSSettingsDialog() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }*/
}
