package com.speant.delivery.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;

import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.ProfileDetailsResponse;
import com.bumptech.glide.Glide;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.RoundedImageView;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_profile)
    RoundedImageView imgProfile;
    @BindView(R.id.txt_username)
    AppCompatTextView txtUsername;
    @BindView(R.id.txt_address)
    AppCompatTextView txtAddress;
    @BindView(R.id.txt_city)
    AppCompatTextView txtCity;
    @BindView(R.id.txt_rating)
    AppCompatTextView txtRating;
    @BindView(R.id.txt_phone)
    AppCompatTextView txtPhone;
    @BindView(R.id.txt_license_no)
    AppCompatTextView txtLicenseNo;
    @BindView(R.id.txt_join_date)
    AppCompatTextView txtJoinDate;
    @BindView(R.id.txt_serv_zone)
    AppCompatTextView txtServZone;
    @BindView(R.id.txt_bank_name)
    AppCompatTextView txtBankName;
    @BindView(R.id.txt_acc_no)
    AppCompatTextView txtAccNo;
    @BindView(R.id.txt_ifsc)
    AppCompatTextView txtIfsc;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private HashMap<String, String> userDetatils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        userDetatils = sessionManager.getUserDetails();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileDetails();
    }

    private void getProfileDetails() {
        Call<ProfileDetailsResponse> call = apiInterface.getProfile(sessionManager.getHeader(),sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(Call<ProfileDetailsResponse> call, Response<ProfileDetailsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        setDetails(response.body());
                    }
                }else if(response.code() == 401){
                    sessionManager.logoutUser(ProfileActivity.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

            }

            @Override
            public void onFailure(Call<ProfileDetailsResponse> call, Throwable t) {

            }
        });
    }

    private void setDetails(ProfileDetailsResponse profileResponse) {
        Glide.with(getApplicationContext())
                .load(profileResponse.getProfile_pic())
                .into(imgProfile);
        txtUsername.setText(profileResponse.getName()+"("+profileResponse.getPartner_id()+")");
        txtAddress.setText("Locality : "+profileResponse.getAddress());
        txtCity.setText("City : "+profileResponse.getCity());
        txtRating.setText(profileResponse.getRating());
        txtPhone.setText(profileResponse.getPhone());
        txtLicenseNo.setText(profileResponse.getDriving_license_no());
        txtJoinDate.setText(profileResponse.getJoining_date());
        txtServZone.setText(profileResponse.getService_zone());
        txtBankName.setText(profileResponse.getBank_name());
        txtAccNo.setText(profileResponse.getAcc_no());
        txtIfsc.setText(profileResponse.getIfsc_code());

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
