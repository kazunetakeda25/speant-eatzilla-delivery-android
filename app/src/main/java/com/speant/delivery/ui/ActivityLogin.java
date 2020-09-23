package com.speant.delivery.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Models.LoginPojo;
import com.speant.delivery.Models.TimeoutSuccessPojo;
import com.speant.delivery.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 108;
    private static final String TAG = "Login";
    @BindView(R.id.login_welcon_txt)
    TextView loginWelconTxt;
    @BindView(R.id.login_mail_edt)
    EditText loginMailEdt;
    @BindView(R.id.login_pswd_edt)
    EditText loginPswdEdt;
    @BindView(R.id.login_txt)
    TextView loginTxt;
    @BindView(R.id.login_forget_pswd_txt)
    TextView loginForgetPswdTxt;
    @BindView(R.id.login_no_acc_txt)
    TextView loginNoAccTxt;
    @BindView(R.id.ccp)
    CountryCodePicker ccp;
    private AlertDialog alertDialog;

    APIInterface apiInterface;
    APIClient apiClient;
    SessionManager sessionManager;
    private String refreshedToken;
    private DatabaseReference mDatabaseDeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);
        mDatabaseDeviceToken = FirebaseDatabase.getInstance().getReference().child(CONST.Params.dev_token);
        getTimeoutLimit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,},
                        MY_REQUEST_CODE);
            }
        }

        if (sessionManager.getDeviceToken() == null) {
            sessionManager.saveDeviceToken(FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "onCreate: " + FirebaseInstanceId.getInstance().getToken());
            Log.e(TAG, "onCreate: " + sessionManager.getDeviceToken());
        }

       /* loginMailEdt.setText("919600771099");
        loginPswdEdt.setText("12345678");*/

    }

    @OnClick({R.id.login_txt, R.id.login_forget_pswd_txt, R.id.login_no_acc_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_txt:

                if (loginMailEdt.getText().length() < 10) {
                    CommonFunctions.shortToast(ActivityLogin.this, "Enter Valid Mobile Number");
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.phone, ccp.getSelectedCountryCode()+loginMailEdt.getText().toString().trim());
//                    map.put(CONST.Params.phone, loginMailEdt.getText().toString().trim());
                    map.put(CONST.Params.password, loginPswdEdt.getText().toString());
                    map.put(CONST.Params.device_token, sessionManager.getDeviceToken());
                    map.put(CONST.Params.device_type, "android");
                    jsonLogin("login", map,sessionManager.getDeviceToken());
                    Log.e(TAG, "onViewClicked: " + map);
                }

                break;
            case R.id.login_forget_pswd_txt:
                if (loginMailEdt.getText().length() < 10) {
                    CommonFunctions.shortToast(ActivityLogin.this, "Enter Valid Mobile Number");
                } else {
                    Intent intent = new Intent(ActivityLogin.this, VerifyMobileActivity.class);
                    intent.putExtra(CONST.MOBILE_NUMBER, ccp.getSelectedCountryCode()+loginMailEdt.getText().toString());
//                    intent.putExtra(CONST.MOBILE_NUMBER, loginMailEdt.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.login_no_acc_txt:
                alertSignup();
                break;
        }
    }

    public void alertSignup() {

        // Create a alert dialog builder.
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLogin.this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.dialogue_signup, null);
        // Set above view in alert dialog.
        builder.setView(view);

        ImageView signup_close_img = view.findViewById(R.id.signup_close_img);
        EditText signup_phone_edt = view.findViewById(R.id.signup_phone_edt);
        EditText signup_mail_edt = view.findViewById(R.id.signup_mail_edt);
        EditText signup_pswd_edt = view.findViewById(R.id.signup_pswd_edt);
        TextView signup_submit_txt = view.findViewById(R.id.signup_submit_txt);

        signup_close_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        signup_submit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void getTimeoutLimit() {
        Call<TimeoutSuccessPojo> call = apiInterface.getProviderTimeout();
        call.enqueue(new Callback<TimeoutSuccessPojo>() {
            @Override
            public void onResponse(Call<TimeoutSuccessPojo> call, Response<TimeoutSuccessPojo> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        sessionManager.setDefaultTimeOut(Integer.parseInt(response.body().getProvider_timeout()));
                    }
                }
            }

            @Override
            public void onFailure(Call<TimeoutSuccessPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void jsonLogin(String url, HashMap<String, String> map, String deviceToken) {

        Call<LoginPojo> call = apiInterface.login(url, map, sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        LoginPojo pojo = response.body();
                        sessionManager.createLoginSession(pojo.getAuthid(), pojo.getAuthtoken(),
                                pojo.getUserName(), pojo.getEmail(), pojo.getPhone(), pojo.getProfileImage(), pojo.getPartner_id(),deviceToken);
                        mDatabaseDeviceToken.child(""+pojo.getAuthid()).setValue(deviceToken);
                        Intent login = new Intent(ActivityLogin.this, MainActivity.class);
                        startActivity(login);
                        finishAffinity();

                    } else
                        CommonFunctions.shortToast(getApplicationContext(), response.body().getMessage());

                } else {
                    CommonFunctions.shortToast(getApplicationContext(), response.message());
                }

            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

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
