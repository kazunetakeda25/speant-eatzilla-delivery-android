package com.speant.delivery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Models.LoginPojo;
import com.speant.delivery.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityForgot extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 108;
    private static final String TAG = "ActivityForgot";
    @BindView(R.id.login_welcon_txt)
    TextView loginWelconTxt;
    @BindView(R.id.forgot_phone_edt)
    EditText forgotPhoneEdt;
    @BindView(R.id.forgot_mbl_submit_txt)
    TextView forgotMblSubmitTxt;
    @BindView(R.id.forgot_mbl_linear)
    LinearLayout forgotMblLinear;
    @BindView(R.id.forgot_otp_head_txt)
    TextView forgotOtpHeadTxt;
    @BindView(R.id.otp_mbl_num_txt)
    TextView otpMblNumTxt;
    @BindView(R.id.forgot_otp1_edt)
    EditText forgotOtp1Edt;
    @BindView(R.id.forgot_otp2_edt)
    EditText forgotOtp2Edt;
    @BindView(R.id.forgot_otp3_edt)
    EditText forgotOtp3Edt;
    @BindView(R.id.forgot_otp4_edt)
    EditText forgotOtp4Edt;
    @BindView(R.id.forgot_otp5_edt)
    EditText forgotOtp5Edt;
    @BindView(R.id.forgot_otp6_edt)
    EditText forgotOtp6Edt;
    @BindView(R.id.forgot_otp_submit_txt)
    TextView forgotOtpSubmitTxt;
    @BindView(R.id.forgot_otp_linear)
    LinearLayout forgotOtpLinear;
    @BindView(R.id.forgot_new_pswd_edt)
    EditText forgotNewPswdEdt;
    @BindView(R.id.forgot_conf_pswd_edt)
    EditText forgotConfPswdEdt;
    @BindView(R.id.forgot_pswd_submit_txt)
    TextView forgotPswdSubmitTxt;
    @BindView(R.id.forgot_reset_pswd_linear)
    LinearLayout forgotResetPswdLinear;

    APIInterface apiInterface;
    SessionManager sessionManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String otpStr;
    private String phoneNumStr;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        forgotMblLinear.setVisibility(View.GONE);
        forgotOtpLinear.setVisibility(View.GONE);
        forgotResetPswdLinear.setVisibility(View.VISIBLE);
        getIntentValues();
        /*forgotOtp1Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp1Edt));
        forgotOtp2Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp2Edt));
        forgotOtp3Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp3Edt));
        forgotOtp4Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp4Edt));
        forgotOtp5Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp5Edt));
        forgotOtp6Edt.addTextChangedListener(new GenericTextWatcher(forgotOtp6Edt));
        forgotOtp6Edt.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,},
                        MY_REQUEST_CODE);
            }
        }*/
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }

    private void getIntentValues() {
        try {
            mobileNumber = getIntent().getStringExtra(CONST.MOBILE_NUMBER);
        } catch (Exception e) {
            Log.e("Giri ", "getIntentValues: Exception" + e);
        }
    }

    public Boolean backCheckVisibility() {

        if (forgotMblLinear.getVisibility() == View.VISIBLE) {
            return true;
        }

        if (forgotOtpLinear.getVisibility() == View.VISIBLE) {
            forgotMblLinear.setVisibility(View.VISIBLE);
            forgotOtpLinear.setVisibility(View.GONE);
            forgotResetPswdLinear.setVisibility(View.GONE);
            return false;
        }

        if (forgotResetPswdLinear.getVisibility() == View.VISIBLE) {
            forgotMblLinear.setVisibility(View.GONE);
            forgotOtpLinear.setVisibility(View.VISIBLE);
            forgotResetPswdLinear.setVisibility(View.GONE);
            return false;
        }

        return true;

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");

                    String[] parts = message.split("is"); // escape .
                    String OTP = parts[1].replace(" ", "").trim();

                    String[] value = OTP.split("");
                    forgotOtp1Edt.setText(value[1]);
                    forgotOtp2Edt.setText(value[2]);
                    forgotOtp3Edt.setText(value[3]);
                    forgotOtp4Edt.setText(value[4]);
                    forgotOtp5Edt.setText(value[5]);

                }
            } catch (Exception e) {
                System.out.println(TAG + e.getMessage());
            }

        }
    };


    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();

            switch (view.getId()) {

                case R.id.forgot_otp1_edt:
                    if (text.length() == 1)
                        forgotOtp2Edt.requestFocus();
                    break;
                case R.id.forgot_otp2_edt:
                    if (text.length() == 1)
                        forgotOtp3Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp1Edt.requestFocus();
                    break;
                case R.id.forgot_otp3_edt:
                    if (text.length() == 1)
                        forgotOtp4Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp2Edt.requestFocus();
                    break;
                case R.id.forgot_otp4_edt:
                    if (text.length() == 1)
                        forgotOtp5Edt.requestFocus();
                    else if (text.length() == 0)
                        forgotOtp3Edt.requestFocus();
                    break;
                case R.id.forgot_otp5_edt:
                    if (text.length() == 0)
                        forgotOtp4Edt.requestFocus();
                    else if (text.length() == 1)
                        confirmOtp();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    public void confirmOtp() {

        String otp1, otp2, otp3, otp4, otp5, otp6;
        otp1 = forgotOtp1Edt.getText().toString().trim();
        otp2 = forgotOtp2Edt.getText().toString().trim();
        otp3 = forgotOtp3Edt.getText().toString().trim();
        otp4 = forgotOtp4Edt.getText().toString().trim();
        otp5 = forgotOtp5Edt.getText().toString().trim();
        otp6 = forgotOtp6Edt.getText().toString().trim();

        String otp_str = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
        otp_str.trim();

        if (otpStr.equalsIgnoreCase(otp_str)) {
            forgotMblLinear.setVisibility(View.GONE);
            forgotOtpLinear.setVisibility(View.GONE);
            forgotResetPswdLinear.setVisibility(View.VISIBLE);
        } else
            CommonFunctions.shortToast(getApplicationContext(), "Enter Valid OTP");


    }

    public void jsonForgotReset(final String url, HashMap<String, String> map) {
        Log.e(TAG, "onViewClicked:reset_password "+url);
        Call<LoginPojo> call = apiInterface.login(url, map,sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                if (response.code() == 200) {
                    if (response.body().getStatus()) {

                        switch (url) {
                            case "forgot_password":
                                otpMblNumTxt.setText(phoneNumStr);
                                otpStr = String.valueOf(response.body().getOtp());
                                break;
                            case "reset_password":
                                /*Intent intent = new Intent(ActivityForgot.this, ActivityLogin.class);
                                startActivity(intent);*/
                                CommonFunctions.shortToast(ActivityForgot.this,"Password Reset Successfully");
                                finish();
                                break;
                        }

                    }
                } else
                    CommonFunctions.shortToast(getApplicationContext(), response.message());

            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @OnClick({R.id.forgot_mbl_submit_txt, R.id.forgot_otp_submit_txt, R.id.forgot_pswd_submit_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot_mbl_submit_txt:
                forgotMblLinear.setVisibility(View.GONE);
                forgotOtpLinear.setVisibility(View.VISIBLE);
                forgotResetPswdLinear.setVisibility(View.GONE);
                if (forgotPhoneEdt.getText().length() < 10) {
                    forgotPhoneEdt.setError("Enter Valid Mobile Number");
                } else {
                    phoneNumStr = forgotPhoneEdt.getText().toString();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, forgotPhoneEdt.getText().toString());
                    jsonForgotReset("forgot_password", map);
                }
                break;
            case R.id.forgot_otp_submit_txt:
                confirmOtp();
                break;
            case R.id.forgot_pswd_submit_txt:
                if (forgotNewPswdEdt.getText().length() < 4) {
                    forgotNewPswdEdt.setError("Enter Valid Password");
                } else if (!forgotNewPswdEdt.getText().toString().equalsIgnoreCase(forgotConfPswdEdt.getText().toString())) {
                    forgotConfPswdEdt.setError("Password Mismatch");
                } else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, mobileNumber);
                    map.put(CONST.Params.password, forgotNewPswdEdt.getText().toString());
                    jsonForgotReset("reset_password", map);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
