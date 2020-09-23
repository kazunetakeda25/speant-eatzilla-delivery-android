package com.speant.delivery.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.PayoutsResponse;
import com.speant.delivery.R;
import com.speant.delivery.ui.Adapter.PayoutsAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

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

public class PayoutsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_payout)
    AppCompatTextView txtPayout;
    @BindView(R.id.payouts_rv)
    ShimmerRecyclerView payoutsRv;
    @BindView(R.id.txt_no_transaction)
    AppCompatTextView txtNoTransaction;
    private Activity activity;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private LinearLayoutManager linearLayoutManager;
    private PayoutsAdapter payoutsRvAdapter;
    private List<PayoutsResponse.PayoutTransactionHistory> payoutsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);
        ButterKnife.bind(this);
        toolbar.setTitle("Payout Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity = PayoutsActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);

        linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        payoutsRv.setLayoutManager(linearLayoutManager);
        payoutsRvAdapter = new PayoutsAdapter(activity, payoutsList);
        payoutsRv.setAdapter(payoutsRvAdapter);
        payoutsRv.showShimmerAdapter();

        getPayoutsList();
    }

    private void getPayoutsList() {
        Call<PayoutsResponse> call = apiInterface.getPayouts(sessionManager.getHeader(),sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<PayoutsResponse>() {
            @Override
            public void onResponse(Call<PayoutsResponse> call, Response<PayoutsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        txtPayout.setText(sessionManager.getCurrency() + " " + response.body().getPending_payout());
                        payoutsList.addAll(response.body().getPayout_history());
                        if (payoutsList.size() > 0) {
                            txtNoTransaction.setVisibility(View.GONE);
                            payoutsRv.getAdapter().notifyDataSetChanged();
                        } else {
                            txtNoTransaction.setVisibility(View.VISIBLE);
                            CommonFunctions.shortToast(activity, "No Transactions Found");
                        }
                    }
                }

                payoutsRv.hideShimmerAdapter();
            }

            @Override
            public void onFailure(Call<PayoutsResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        Log.e("tag", "onUnauthorise: Event" );
        sessionManager.logoutUser(this);
    }
}
