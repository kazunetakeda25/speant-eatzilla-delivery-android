package com.speant.delivery.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.DatePickerFragment;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Models.DailyEarningsResponse;
import com.speant.delivery.R;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyEarningFragment extends Fragment implements DatePickerFragment.OnDateCompleteListener {

    @BindView(R.id.btn_change_date)
    AppCompatTextView btnChangeDate;
    @BindView(R.id.txt_date)
    AppCompatTextView txtDate;
    @BindView(R.id.lay_date)
    RelativeLayout layDate;
    @BindView(R.id.txt_total_collected)
    AppCompatTextView txtTotalCollected;
    @BindView(R.id.txt_total_collect_desc)
    AppCompatTextView txtTotalCollectDesc;
    @BindView(R.id.txt_incentives_desc)
    AppCompatTextView txtIncentivesDesc;
    @BindView(R.id.txt_incentive)
    AppCompatTextView txtIncentive;
    @BindView(R.id.lay_chart)
    LinearLayout layChart;
    Unbinder unbinder;
    @BindView(R.id.chart)
    BarChart chart;

    private SessionManager sessionManager;
    private String currencyStr;
    private APIInterface apiInterface;
    private Activity activity;

    public DailyEarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        currencyStr = sessionManager.getCurrency();
        layChart.setVisibility(View.GONE);
        setDefaultText();
        return view;
    }

    private void setDefaultText() {
        txtIncentivesDesc.setText("Incentives of the day");
        txtTotalCollectDesc.setText("Total earnings of the day");
        Date date = Global.getCurrentDate();
        setDate(date);
    }


    @OnClick({R.id.btn_change_date, R.id.txt_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change_date:
                //Call Date picker fragment using setTargetFragment if it is called from a fragment
                DialogFragment dateDialogFragment = new DatePickerFragment();
                dateDialogFragment.setTargetFragment(this, CONST.DATE_PICKER_REQUEST);
                dateDialogFragment.show(this.getFragmentManager(), "Date Picker");
                break;
            case R.id.txt_date:

                break;
        }
    }

    private void getEarningDetails(String dateString) {
        Call<DailyEarningsResponse> call = apiInterface.getEarning(sessionManager.getHeader(), dateString,sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<DailyEarningsResponse>() {
            @Override
            public void onResponse(Call<DailyEarningsResponse> call, Response<DailyEarningsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        setDetails(response.body());
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser(activity);
                    CommonFunctions.shortToast(activity, response.message());
                }
            }

            @Override
            public void onFailure(Call<DailyEarningsResponse> call, Throwable t) {

            }
        });
    }

    private void setDetails(DailyEarningsResponse body) {
        txtIncentive.setText(currencyStr + " " + body.getToday_incentives());
        txtTotalCollected.setText(currencyStr + " " + body.getToday_earnings());
    }

    @Override
    public void onDateComplete(Date date) {
        setDate(date);
    }

    private void setDate(Date date) {
        String dateString = Global.setDateFormat(date, "dd-MMM-yyyy");
        txtDate.setText(dateString);
        Log.e("dateString", "onDateComplete: " + dateString);
        String serviceDateString = Global.setDateFormat(date, "yyyy-MM-dd");
        getEarningDetails(serviceDateString);
    }
}
