package com.speant.delivery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.ItemList;
import com.speant.delivery.Models.PastOrders;
import com.speant.delivery.R;
import com.speant.delivery.ui.Adapter.PastOrderItemAdapter;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedOrderActivity extends AppCompatActivity {
    PastOrders pastOrders;
    List<ItemList> itemListList;
    @BindView(R.id.recycler_item_list)
    RecyclerView recyclerItemList;
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
    @BindView(R.id.hotel_img)
    ImageView hotelImg;
    @BindView(R.id.hotel_name_txt)
    TextView hotelNameTxt;
    @BindView(R.id.hotel_place_txt)
    TextView hotelPlaceTxt;
    @BindView(R.id.hotel_delivery_txt)
    TextView hotelDeliveryTxt;
    @BindView(R.id.address_txt)
    TextView addressTxt;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.item_total_discount_txt)
    TextView itemTotalDiscountTxt;
    private PastOrderItemAdapter pastOrderItemAdapter;
    private SessionManager sessionManager;
    private String currencyStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CompletedOrderActivity.this);
        currencyStr = sessionManager.getCurrency();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        pastOrders = bundle.getParcelable(CONST.PAST_ORDER_DETAIL);
        itemListList = bundle.getParcelableArrayList(CONST.PAST_ORDER_ITEMS);
        Log.e("Giri ", "onCreate:pastOrders " + pastOrders.getRequest_id());
        Log.e("Giri ", "onCreate:pastOrders " + itemListList.size());
        setAdapter();
        setDetails();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setAdapter() {
        //set Toolbar
        String time = Global.getDateFromString(pastOrders.getOrdered_on(), "yyyy-MM-dd HH:mm", "hh:mm a");
        toolbar.setTitle(pastOrders.getOrder_id());
        toolbar.setSubtitle(time + " | " + itemListList.size() + " Item" + " | " + currencyStr + pastOrders.getBill_amount());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerItemList.setLayoutManager(linearLayoutManager);
        recyclerItemList.setNestedScrollingEnabled(false);
        pastOrderItemAdapter = new PastOrderItemAdapter(CompletedOrderActivity.this, itemListList, currencyStr);
        recyclerItemList.setAdapter(pastOrderItemAdapter);

    }

    private void setDetails() {
        String dateTime = Global.getDateFromString(pastOrders.getOrdered_on(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");

        Log.e("Giri ", "setDetails:setTitle " + pastOrders.getOrder_id());
        Picasso.get().load(pastOrders.getRestaurant_image()).into(hotelImg);
        hotelNameTxt.setText(pastOrders.getRestaurant_name());
        hotelPlaceTxt.setText(pastOrders.getRestaurant_address());
        hotelDeliveryTxt.setText(dateTime);
        addressTxt.setText(pastOrders.getDelivery_address());

        itemTotalAmountTxt.setText(currencyStr + pastOrders.getItem_total());

        offerDiscountAmountTxt.setText(currencyStr + pastOrders.getOffer_discount());
        packagingChargeAmountTxt.setText(currencyStr + pastOrders.getRestaurant_packaging_charge());
        gstAmountTxt.setText(currencyStr + pastOrders.getTax());
        deliveryChargeAmountTxt.setText(currencyStr + pastOrders.getDelivery_charge());
        totalToPayAmountTxt.setText(currencyStr + pastOrders.getBill_amount());
        itemTotalDiscountTxt.setText(currencyStr + pastOrders.getRestaurant_discount());

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
        Log.e("TAG", "onUnauthorise: Event" );
        sessionManager.logoutUser(this);
    }
}
