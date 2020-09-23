package com.speant.delivery.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.HistoryResponse;
import com.speant.delivery.Models.PastOrders;
import com.speant.delivery.Models.UpcomingOrders;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.R;
import com.speant.delivery.ui.Adapter.PastOrderAdapter;
import com.speant.delivery.ui.Adapter.UpcomingOrderAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyler_upcoming)
    ShimmerRecyclerView recylerUpcoming;
    @BindView(R.id.recycler_past)
    ShimmerRecyclerView recyclerPast;
    @BindView(R.id.upcoming_lay)
    LinearLayout upcomingLay;
    @BindView(R.id.past_lay)
    LinearLayout pastLay;
    private LinearLayoutManager MyLayoutManager;
    private APIInterface apiInterface;
    private LinearLayoutManager pastLinearLayoutManager;
    private LinearLayoutManager upcomingLinearLayoutManager;
    private SessionManager sessionManager;
    private PastOrderAdapter pastOrderAdapter;
    private UpcomingOrderAdapter upcomingOrderAdapter;
    private List<PastOrders> pastOrders = new ArrayList<>();
    private List<UpcomingOrders> upcomingOrders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        toolbar.setTitle("Your Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pastLinearLayoutManager = new LinearLayoutManager(OrderHistoryActivity.this, RecyclerView.VERTICAL, false);
        upcomingLinearLayoutManager = new LinearLayoutManager(OrderHistoryActivity.this, RecyclerView.VERTICAL, false);
        recylerUpcoming.setLayoutManager(upcomingLinearLayoutManager);
        recyclerPast.setLayoutManager(pastLinearLayoutManager);
        recyclerPast.setNestedScrollingEnabled(false);
        recylerUpcoming.setNestedScrollingEnabled(false);
        setPastAdapter();
        recyclerPast.showShimmerAdapter();
        setUpcomingAdapter();
        recylerUpcoming.showShimmerAdapter();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        getOrderList();
    }

    private void getOrderList() {

        Call<HistoryResponse> call = apiInterface.getOrderHistory(sessionManager.getHeader(),sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equalsIgnoreCase("true")) {

                        pastOrders = response.body().getPast_orders();
                        upcomingOrders = response.body().getUpcoming_orders();

                        Log.e("Giri ", "onResponse:upcomingOrders " + upcomingOrders.size());
                        Log.e("Giri ", "onResponse:pastOrders " + pastOrders.size());

                        if (!pastOrders.isEmpty()) {
                            pastLay.setVisibility(View.VISIBLE);
                            setPastAdapter();

                            recyclerPast.hideShimmerAdapter();
                        } else {
                            pastLay.setVisibility(View.GONE);
                        }

                        if (!upcomingOrders.isEmpty()) {
//                            upcomingLay.setVisibility(View.VISIBLE);
                            setUpcomingAdapter();

                            recylerUpcoming.hideShimmerAdapter();
                        } else {
                            upcomingLay.setVisibility(View.GONE);
                        }
                    } else {
                        pastLay.setVisibility(View.GONE);
                        upcomingLay.setVisibility(View.GONE);
                        CommonFunctions.shortToast(OrderHistoryActivity.this, "No order History found");
                    }

                }else if(response.code() == 401){
                    sessionManager.logoutUser(OrderHistoryActivity.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
            }
        });

    }

    private void setUpcomingAdapter() {
        upcomingOrderAdapter = new UpcomingOrderAdapter(OrderHistoryActivity.this, upcomingOrders);
        recylerUpcoming.setAdapter(upcomingOrderAdapter);

    }
    private void setPastAdapter() {
        pastOrderAdapter = new PastOrderAdapter(OrderHistoryActivity.this, pastOrders);
        recyclerPast.setAdapter(pastOrderAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
