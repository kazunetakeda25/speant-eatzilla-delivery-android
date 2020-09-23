package com.speant.delivery.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.speant.delivery.EventModels.LogoutEvent;
import com.speant.delivery.Models.SendOtpResponse;
import com.chaos.view.PinView;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyMobileActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_verify)
    TextView txtVerify;
    @BindView(R.id.password_field)
    PinView passwordField;
    @BindView(R.id.txt_resend)
    TextView txtResend;
    @BindView(R.id.txt_next)
    TextView txtNext;
    private APIInterface apiInterface;
    String mobileNumber;
    String otpNumber;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        getIntentValues();
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

    private void getIntentValues() {
        try {
            mobileNumber = getIntent().getStringExtra(CONST.MOBILE_NUMBER);
            String verifyText = Global.setMultipleColorText("Please enter the verification code sent to ", mobileNumber);
            txtVerify.setText((Html.fromHtml(verifyText)));
            sendOtp();

        } catch (Exception e) {
            Log.e("Giri ", "getIntentValues: Exception" + e);
        }
    }

    private void sendOtp() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CONST.Params.phone, mobileNumber);
        Call<SendOtpResponse> call = apiInterface.sendOtp(map,sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<SendOtpResponse>() {
            @Override
            public void onResponse(Call<SendOtpResponse> call, Response<SendOtpResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        otpNumber = response.body().getOtp();
//                        passwordField.setText(otpNumber);
                        CommonFunctions.shortToast(VerifyMobileActivity.this, "OTP Sent Successfully");
                    } else {
                        CommonFunctions.shortToast(VerifyMobileActivity.this, response.body().getMessage());
                    }
                }else if(response.code() == 401){
                    sessionManager.logoutUser(VerifyMobileActivity.this);
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

            }

            @Override
            public void onFailure(Call<SendOtpResponse> call, Throwable t) {
                CommonFunctions.shortToast(VerifyMobileActivity.this, t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @OnClick({R.id.txt_resend, R.id.txt_next, R.id.password_field})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_resend:
                sendOtp();
                break;
            case R.id.txt_next:
                VerifyOtp();
                break;
            case R.id.password_field:
                passwordField.setCursorVisible(true);
                break;
        }
    }

    private void VerifyOtp() {
        if (passwordField.getText().toString().isEmpty()) {
            CommonFunctions.shortToast(VerifyMobileActivity.this, "Enter OTP Number");
        } else if (passwordField.getText().length() < 5) {
            CommonFunctions.shortToast(VerifyMobileActivity.this, "Enter Valid OTP Number");
        } else if (!passwordField.getText().toString().equals(otpNumber)) {
            CommonFunctions.shortToast(VerifyMobileActivity.this, "Invalid OTP");
        } else {
            CommonFunctions.shortToast(VerifyMobileActivity.this, "OTP Verified Successfully");
            Intent intent = new Intent(VerifyMobileActivity.this, ActivityForgot.class);
            intent.putExtra(CONST.MOBILE_NUMBER,mobileNumber);
            startActivity(intent);
            finish();
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
        Log.e("tag", "onUnauthorise: Event" );
        sessionManager.logoutUser(this);
    }
}
