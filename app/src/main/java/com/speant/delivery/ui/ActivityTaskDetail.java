package com.speant.delivery.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.GPSService;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.RequestDetailPojo;
import com.speant.delivery.Models.SuccessPojo;
import com.speant.delivery.Models.UpdateRequestPojo;
import com.speant.delivery.R;
import com.speant.delivery.ui.Adapter.FoodListAdapter;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.speant.delivery.Common.CONST.Params.provider_id;

public class ActivityTaskDetail extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ActivityTaskDetail";
    private static final int REQUEST_CHECK_SETTINGS = 108;
    private static final int MY_REQUEST_CODE = 106;
    Global global;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_lay)
    AppBarLayout appbarLay;
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
    @BindView(R.id.item_total_amount_txt)
    TextView itemTotalAmountTxt;
    @BindView(R.id.offer_discount_amount_txt)
    TextView offerDiscountAmountTxt;
    @BindView(R.id.packaging_charge_amount_txt)
    TextView packagingChargeAmountTxt;
    @BindView(R.id.gst_amount_txt)
    TextView gstAmountTxt;
    @BindView(R.id.delivery_charge_amount_txt)
    TextView deliveryChargeAmountTxt;
    @BindView(R.id.total_to_pay_amount_txt)
    TextView totalToPayAmountTxt;
    @BindView(R.id.track_scootor_img)
    ImageView trackScootorImg;
    @BindView(R.id.track_hotel_img)
    ImageView trackHotelImg;
    @BindView(R.id.track_cooking_img)
    ImageView trackCookingImg;
    @BindView(R.id.track_packing_img)
    ImageView trackPackingImg;
    @BindView(R.id.track_wallet_img)
    ImageView trackWalletImg;
    @BindView(R.id.btm_accept_reject_btn_linear)
    LinearLayout btmAcceptRejectBtnLinear;
    @BindView(R.id.pickup_relative)
    RelativeLayout pickupRelative;
    @BindView(R.id.drop_relative)
    RelativeLayout dropRelative;
    @BindView(R.id.task_reject_txt)
    AppCompatButton taskRejectTxt;
    @BindView(R.id.task_accept_txt)
    AppCompatButton taskAcceptTxt;
    @BindView(R.id.btm_status_update_txt)
    AppCompatTextView btmStatusUpdateTxt;
    @BindView(R.id.btm_complete_card)
    CardView btmCompleteCard;
    @BindView(R.id.task_food_list_rv)
    ShimmerRecyclerView taskFoodListRv;
    @BindView(R.id.txt_direction)
    TextView txtDirection;
    @BindView(R.id.item_total_discount_txt)
    TextView itemTotalDiscountTxt;
    @BindView(R.id.btm_paid_by)
    AppCompatTextView btmPaidBy;

    private GoogleMap mGoogleMap;
    RequestDetailPojo.RestaurantDetail restaurantDetailPojo;
    RequestDetailPojo.UserDetail userPojo;
    Marker marker;
    Marker markerDirection;
    MarkerOptions markerOptions;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Polyline polyline = null;

    //      Array
    ArrayList<Integer> durationList = new ArrayList<Integer>();
    ArrayList<LatLng> routelist = new ArrayList<LatLng>();
    ArrayList<LatLng> decodelist = new ArrayList<>();

    APIInterface apiInterface;
    APIInterface apiService;
    SessionManager sessionManager;
    private LatLng fromLatLng;
    private LatLng toLatLng;
    private LatLng trackLatLng;


    DatabaseReference mDatabaseNewRequest;
    DatabaseReference mDatabaseCurrentRequest;

    private double fromLat = 11.005566134831653, fromLng = 76.95446994155645;
    private double toLat = 10.98856, toLng = 76.961738;
    private String status = "0";
    private TextView action_bar_title, action_bar_time_txt, action_bar_item_txt,
            action_bar_amt_txt, action_bar_date_txt;
    private LinearLayoutManager MyLayoutManager;
    List<RequestDetailPojo.FoodDetail> foodDetailList = new ArrayList<>();
    private FoodListAdapter foodListAdapter;
    String request_id = "0";
    private LocationRequest mLocationRequest;
    private LatLng deliverBoyLatLng;
    private LocationRequest locationRequest;

    Boolean gpsListener = false;
    private GPSService gpsLocationClass;

    boolean isFirst = true;
    int count = 20;
    boolean isStatusTwoFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);
        actionBar();
        global = new Global();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        request_id = getIntent().getStringExtra(CONST.Params.request_id);
        Log.e(TAG, "onCreate: " + request_id);




       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,},
                        MY_REQUEST_CODE);
            } else
                locationrequest();
        }

        //To make map view move
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarLay.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(AppBarLayout appBarLayout) {
                return false;
            }
        });
        params.setBehavior(behavior);*/

        HashMap<String, String> userDetatils = sessionManager.getUserDetails();

        MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(RecyclerView.VERTICAL);
        taskFoodListRv.setLayoutManager(MyLayoutManager);
        taskFoodListRv.setNestedScrollingEnabled(false);
        foodListAdapter = new FoodListAdapter(this, foodDetailList);
        taskFoodListRv.setAdapter(foodListAdapter);
        taskFoodListRv.showShimmerAdapter();

        mDatabaseCurrentRequest = FirebaseDatabase.getInstance().getReference().
                child(CONST.Params.current_request).child(String.valueOf(request_id));

        mDatabaseCurrentRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.e(TAG, "onDataChange: map mDatabaseCurrentRequest " + map);
                if (map != null) {
                    request_id = String.valueOf(map.get(CONST.Params.request_id));
                    provider_id = String.valueOf(map.get(provider_id));
                    status = String.valueOf(map.get(CONST.Params.status));
                    if (status.equalsIgnoreCase("2")) {
                        isStatusTwoFirst = !isStatusTwoFirst;
                    } else {
                        isStatusTwoFirst = false;
                    }
                    bottomSheetVisibilitychange(status);
                    Log.e(TAG, "onDataChange: " + request_id + "\t" + provider_id + "\t" + status);
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put(CONST.Params.request_id, request_id);
                    jsonRequestDetail(requestMap, String.valueOf(status));
                } else {
                    CommonFunctions.removeProgressDialog();
                    setResult(RESULT_OK, new Intent());
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        Log.e(TAG, "onDataChange: " + request_id);
         /*  mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();*/


//        directionJson(fromLatLng, toLatLng);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_task_detail_toolbar);
        View view = getSupportActionBar().getCustomView();

        ImageView back_arrow_toolbar = (ImageView) view.findViewById(R.id.back_arrow_toolbar);
        action_bar_title = (TextView) view.findViewById(R.id.action_bar_title);
        action_bar_time_txt = (TextView) view.findViewById(R.id.action_bar_time_txt);
        action_bar_item_txt = (TextView) view.findViewById(R.id.action_bar_item_txt);
        action_bar_amt_txt = (TextView) view.findViewById(R.id.action_bar_amt_txt);
        action_bar_date_txt = (TextView) view.findViewById(R.id.action_bar_date_txt);

        back_arrow_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent());
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void jsonRequestDetail(HashMap<String, String> map, String status) {

        Log.e(TAG, "jsonRequestDetail: " + map);

        Call<RequestDetailPojo> call = apiInterface.getRequestDetail(sessionManager.getHeader(), map, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<RequestDetailPojo>() {
            @Override
            public void onResponse(Call<RequestDetailPojo> call, Response<RequestDetailPojo> response) {

                Log.e(TAG, "onResponse: " + response.code());

                if (response.code() == 200) {

                    Log.e(TAG, "onResponse: " + response.body().getStatus());

                    if (response.body().getStatus()) {

                        userPojo = response.body().getUserDetail();
                        restaurantDetailPojo = response.body().getRestaurantDetail();
                        RequestDetailPojo.AddressDetail dataPojo = response.body().getAddressDetail().get(0);
                        RequestDetailPojo.BillDetail billDetailPojo = response.body().getBillDetail().get(0);

                        btmPickupHotelTxt.setText(restaurantDetailPojo.getRestaurantName());
                        btmPickupLocationTxt.setText(restaurantDetailPojo.getAddress());
                        btmDeliveryHotelTxt.setText(userPojo.getName());
                        btmDeliveryLocationTxt.setText(dataPojo.getDAddress());
                        if (response.body().getBillDetail().get(0).getPaid_type() == 1) {
                            btmPaidBy.setText("Paid by Cash");
                        } else {
                            btmPaidBy.setText("Paid by Card");
                        }
                        gstAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getTax());
                        itemTotalAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getItemTotal());
                        offerDiscountAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getOfferDiscount());
                        packagingChargeAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getPackagingCharge());
                        deliveryChargeAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getDeliveryCharge());
                        totalToPayAmountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getBillAmount());
                        itemTotalDiscountTxt.setText(sessionManager.getCurrency() + " " + billDetailPojo.getRestaurant_discount());

                        orderIdRelative.setVisibility(View.GONE);

                        fromLatLng = new LatLng(dataPojo.getSLat(), dataPojo.getSLng());
                        toLatLng = new LatLng(dataPojo.getDLat(), dataPojo.getDLng());
                        setLatLng(status);

                        Log.e(TAG, "onResponse: " + fromLatLng + "\t\t" + toLatLng);
                        Log.e(TAG, "onResponse: " + response.body().getRequest_status());

                        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        DateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                        Date date = null;
                        try {
                            String inputText = response.body().getOrderedTime();
                            date = inputFormat.parse(inputText);
                            btmOrderIdTxt.setText("Order #" + response.body().getOrderId());
                            btmOrderTimeTxt.setText(dateFormat.format(date));
                            action_bar_date_txt.setText(dateFormat.format(date));
                            action_bar_time_txt.setText(timeFormat.format(date));
                            action_bar_title.setText("Order #" + response.body().getOrderId());
                            action_bar_item_txt.setText("" + response.body().getFoodDetail().size() + " " + "Item");
                            action_bar_amt_txt.setText(sessionManager.getCurrency() + " " +
                                    response.body().getBillDetail().get(0).getBillAmount());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Picasso.get()
                                .load(restaurantDetailPojo.getImage())
                                .placeholder(R.drawable.ic_user)
                                .error(R.drawable.ic_user)
                                .into(btmPickupHotelImg);

                        Picasso.get()
                                .load(userPojo.getProfileImage())
                                .placeholder(R.drawable.ic_user)
                                .error(R.drawable.ic_user)
                                .into(btmDeliveryHotelImg);

                       /* Glide.with(getApplicationContext())
                                .load(restaurantDetailPojo.getImage())
                                .into(btmPickupHotelImg);

                        Glide.with(getApplicationContext())
                                .load(userPojo.getProfileImage())
                                .into(btmDeliveryHotelImg);*/

                        foodDetailList.clear();
                        foodDetailList.addAll(response.body().getFoodDetail());
                        taskFoodListRv.hideShimmerAdapter();
                        Log.e("Giri ", "onResponse:getFoodDetail foodDetailList " + foodDetailList.size());
                        foodListAdapter.notifyDataChanged();


                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().toString());

                } else if (response.code() == 401) {
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                    sessionManager.logoutUser(ActivityTaskDetail.this);
                } else
                    CommonFunctions.shortToast(getApplicationContext(), response.message());

                CommonFunctions.removeProgressDialog();

            }

            @Override
            public void onFailure(Call<RequestDetailPojo> call, Throwable t) {
                CommonFunctions.removeProgressDialog();
            }
        });

    }

    private void setLatLng(String currentStatus) {
        int stat = Integer.parseInt(currentStatus);
        if (stat < 4) {
            if (fromLatLng != null) {
                trackLatLng = new LatLng(fromLatLng.latitude, fromLatLng.longitude);
                txtDirection.setVisibility(View.VISIBLE);
            } else {
                txtDirection.setVisibility(View.GONE);
            }

        } else {
            if (toLatLng != null) {
                trackLatLng = new LatLng(toLatLng.latitude, toLatLng.longitude);
                txtDirection.setVisibility(View.VISIBLE);
            } else {
                txtDirection.setVisibility(View.GONE);
            }
        }

    }

    public void bottomSheetVisibilitychange(String status) {

        Log.e("Giri ", "bottomSheetVisibilitychange: " + status);
        switch (status) {

            case "1":


                btmAcceptRejectBtnLinear.setVisibility(View.VISIBLE);
                btmCompleteCard.setVisibility(View.GONE);
                txtDirection.setVisibility(View.GONE);
                trackScootorImg.setImageResource(R.drawable.ic_scootor_white);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_white);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_white);
                trackPackingImg.setImageResource(R.drawable.ic_packing_white);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "2":
                btmAcceptRejectBtnLinear.setVisibility(View.VISIBLE);
                btmCompleteCard.setVisibility(View.GONE);
                txtDirection.setVisibility(View.VISIBLE);
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_white);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_white);
                trackPackingImg.setImageResource(R.drawable.ic_packing_white);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "3":
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText("Reached Restaurant");
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_green);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_white);
                trackPackingImg.setImageResource(R.drawable.ic_packing_white);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "4":
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText("Started towards Customer");
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_green);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_green);
                trackPackingImg.setImageResource(R.drawable.ic_packing_white);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "5":
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText("Food delivered");
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_green);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_green);
                trackPackingImg.setImageResource(R.drawable.ic_packing_green);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "6":
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText("Cash received");
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_green);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_green);
                trackPackingImg.setImageResource(R.drawable.ic_packing_green);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_white);
                setLatLng(status);
                break;
            case "7":
                btmAcceptRejectBtnLinear.setVisibility(View.GONE);
                btmCompleteCard.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setVisibility(View.GONE);
                txtDirection.setVisibility(View.VISIBLE);
                btmStatusUpdateTxt.setText("Package Delivered");
                trackScootorImg.setImageResource(R.drawable.ic_scootor_green);
                trackHotelImg.setImageResource(R.drawable.ic_hotel_green);
                trackCookingImg.setImageResource(R.drawable.ic_cooking_green);
                trackPackingImg.setImageResource(R.drawable.ic_packing_green);
                trackWalletImg.setImageResource(R.drawable.ic_wallet_green);
                setLatLng(status);
                break;

        }


    }

    public void directionJson(final LatLng fromLatLng, final LatLng toLatLng, boolean polyline) {
        global.directionJson(ActivityTaskDetail.this, fromLatLng, toLatLng, mGoogleMap, polyline);
    }

    @OnClick({R.id.task_reject_txt, R.id.task_accept_txt, R.id.btm_status_update_txt, R.id.btm_pickup_call_img,
            R.id.btm_delivery_call_img, R.id.txt_direction, R.id.btm_complete_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.task_reject_txt:
                buttonClicked(taskRejectTxt.getText().toString());
                break;

            case R.id.task_accept_txt:
                buttonClicked(taskAcceptTxt.getText().toString());
                break;
            case R.id.btm_complete_card:
                buttonClicked(btmStatusUpdateTxt.getText().toString());
                break;
            case R.id.btm_status_update_txt:
                buttonClicked(btmStatusUpdateTxt.getText().toString());
                break;
            case R.id.btm_pickup_call_img:
                Intent pickCallIntent = new Intent(Intent.ACTION_DIAL);
                pickCallIntent.setData(Uri.parse("tel:" + "+" + restaurantDetailPojo.getPhone()));
                startActivity(pickCallIntent);
                break;
            case R.id.btm_delivery_call_img:
                Intent deliveryCallIntent = new Intent(Intent.ACTION_DIAL);
                deliveryCallIntent.setData(Uri.parse("tel:" + "+" + userPojo.getPhone()));
                startActivity(deliveryCallIntent);
                break;
            case R.id.txt_direction:
                try {
                    Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + trackLatLng.latitude + "," + trackLatLng.longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    CommonFunctions.shortToast(ActivityTaskDetail.this, e.getMessage());
                }

                break;
        }
    }

    public void jsonStatusUpdate(HashMap<String, String> map) {
        CommonFunctions.showSimpleProgressDialog(this, "Updating", false);
        Call<UpdateRequestPojo> call = apiInterface.setUpdateRequest(sessionManager.getHeader(), map, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<UpdateRequestPojo>() {
            @Override
            public void onResponse(Call<UpdateRequestPojo> call, Response<UpdateRequestPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

//                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                        if (map.get(CONST.Params.status).equals("7")) {
                            bottomSheetVisibilitychange(String.valueOf(7));
                            mDatabaseCurrentRequest.removeValue();
                            request_id = "0";
                        }

                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                } else if (response.code() == 401) {
                    sessionManager.logoutUser(ActivityTaskDetail.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

            }

            @Override
            public void onFailure(Call<UpdateRequestPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void locationrequest() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(ActivityTaskDetail.this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            builder.setNeedBle(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            getCurrentlocation();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(ActivityTaskDetail.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(getApplicationContext(), "Please Enable Location", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

    }

    public void getCurrentlocation() {
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
            }
        } else {
            buildGoogleApiClient();

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(ActivityTaskDetail.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.e(TAG, "onConnected: " + "Location");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        /*if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this::onLocationChanged);
        }*/

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void buttonClicked(String buttonName) {
        Log.e(TAG, "buttonClicked:buttonName " + buttonName);
        switch (buttonName) {

            case "Accept":
                HashMap<String, String> statusMap = new HashMap<String, String>();
                statusMap.put(CONST.Params.request_id, request_id);
                statusMap.put(CONST.Params.status, "3");
                jsonStatusUpdate(statusMap);
                mDatabaseCurrentRequest.child(CONST.Params.status).setValue(3);
                break;
            case "Reject":
                HashMap<String, String> rjMap = new HashMap<String, String>();
                rjMap.put(CONST.Params.request_id, request_id);
                jsonCancelRequest(rjMap);
                mDatabaseCurrentRequest.child(CONST.Params.status).setValue(1);
                break;
            case "Reached Restaurant":
                HashMap<String, String> rMap = new HashMap<String, String>();
                rMap.put(CONST.Params.request_id, request_id);
                rMap.put(CONST.Params.status, "4");
                jsonStatusUpdate(rMap);
                mDatabaseCurrentRequest.child(CONST.Params.status).setValue(4);
                break;
            case "Started towards Customer":
                HashMap<String, String> sMap = new HashMap<String, String>();
                sMap.put(CONST.Params.request_id, request_id);
                sMap.put(CONST.Params.status, "5");
                jsonStatusUpdate(sMap);
                mDatabaseCurrentRequest.child(CONST.Params.status).setValue(5);
                break;
            case "Food delivered":
                HashMap<String, String> dmap = new HashMap<String, String>();
                dmap.put(CONST.Params.request_id, request_id);
                dmap.put(CONST.Params.status, "6");
                jsonStatusUpdate(dmap);
                mDatabaseCurrentRequest.child(CONST.Params.status).setValue(6);
                break;
            case "Cash received":
                HashMap<String, String> cash = new HashMap<String, String>();
                cash.put(CONST.Params.request_id, request_id);
                cash.put(CONST.Params.status, "7");
                jsonStatusUpdate(cash);
                break;
            case "Package Delivered":
                setResult(RESULT_OK, new Intent());
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        super.onBackPressed();

    }

    private void jsonCancelRequest(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.setCancelRequest(sessionManager.getHeader(), map, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());
                } else if (response.code() == 401) {
                    sessionManager.logoutUser(ActivityTaskDetail.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

            }
        });

    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnauthorise(LogoutEvent logoutEvent) {
        Log.e(TAG, "onUnauthorise: Event");
        sessionManager.logoutUser(this);
    }
}
