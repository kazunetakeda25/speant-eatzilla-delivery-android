package com.speant.delivery.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.ViewPagerAdapter;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.R;
import com.speant.delivery.ui.fragment.DailyEarningFragment;
import com.speant.delivery.ui.fragment.MonthlyEarningFragment;
import com.speant.delivery.ui.fragment.WeeklyEarningFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.titleTabs)
    TabLayout titleTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private SessionManager sessionManager;
    private String currencyStr;
    private APIInterface apiInterface;
    private ViewPagerAdapter viewPagerAdapter;
    private DailyEarningFragment dailyEarningFragment;
    private WeeklyEarningFragment weeklyEarningFragment;
    private MonthlyEarningFragment monthlyEarningFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_earning);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(EarningActivity.this);
        currencyStr = sessionManager.getCurrency();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        titleTabs.setupWithViewPager(viewPager);
        initViewPager();
    }

    private void initViewPager() {
        if (viewPager != null) {
            Log.e("viewPager", "initViewPager:null ");
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

            dailyEarningFragment = new DailyEarningFragment();
            weeklyEarningFragment = new WeeklyEarningFragment();
            monthlyEarningFragment = new MonthlyEarningFragment();

            viewPagerAdapter.addFragment(dailyEarningFragment, "Daily");
            viewPagerAdapter.addFragment(weeklyEarningFragment, "Weekly");
            viewPagerAdapter.addFragment(monthlyEarningFragment, "Monthly");
            viewPager.setAdapter(viewPagerAdapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
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
