package com.speant.delivery.ui.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.speant.delivery.Common.CommonFunctions;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Models.GraphData;
import com.speant.delivery.Models.WeeklyEarningsResponse;
import com.speant.delivery.R;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.fabric.sdk.android.Fabric.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthlyEarningFragment extends Fragment {


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
    private ArrayList<Float> totalOrderList = new ArrayList<>();
    private ArrayList<Float> totalAmountList = new ArrayList<>();
    private ArrayList<String> dayList = new ArrayList<>();

    public MonthlyEarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_earning, container, false);
        ButterKnife.bind(this, view);
        layDate.setVisibility(View.GONE);
        activity = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(activity);
        currencyStr = sessionManager.getCurrency();
        setDefaultText();
        return view;
    }

    private void setDefaultText() {
        txtIncentivesDesc.setText("Incentives of the Month");
        txtTotalCollectDesc.setText("Total earnings of the Month");
        Date date = Global.getCurrentDate();
        setDate(date);
    }

    private void setDate(Date date) {
        String dateString = Global.setDateFormat(date, "dd-MMM-yyyy");
        txtDate.setText(dateString);
        Log.e("dateString", "onDateComplete: " + dateString);
        String serviceDateString = Global.setDateFormat(date, "yyyy-MM-dd");
        getEarningDetails(serviceDateString);
    }

    private void getEarningDetails(String serviceDateString) {
        Call<WeeklyEarningsResponse> call = apiInterface.getMonthlyEarning(sessionManager.getHeader(), serviceDateString,sessionManager.getCurrentLanguage());
        call.enqueue(new Callback<WeeklyEarningsResponse>() {
            @Override
            public void onResponse(Call<WeeklyEarningsResponse> call, Response<WeeklyEarningsResponse> response) {
                if (response.code() == 200) {
                    if (response.body().isStatus()) {
                        setDetails(response.body());

                        if(response.body().getGraph_data().size()>0) {
                            totalOrderList.clear();
                            totalAmountList.clear();
                            dayList.clear();

                            for (GraphData graphData : response.body().getGraph_data()) {
                                totalOrderList.add(Float.valueOf(graphData.getTotal_orders()));
                                totalAmountList.add(Float.valueOf(graphData.getTotal_amount()));
                                dayList.add(graphData.getDay());
                            }

                            Log.e(TAG, "onResponse:totalOrderList Month " + totalOrderList.size());
                            Log.e(TAG, "onResponse:totalAmountList Month " + totalAmountList.size());
                            Log.e(TAG, "onResponse:dayList Month" + dayList.size());

                            setMultiBarGraph();
                        }
                    }
                } else if (response.code() == 401) {
                    sessionManager.logoutUser(activity);
                    CommonFunctions.shortToast(activity, response.message());
                }
            }

            @Override
            public void onFailure(Call<WeeklyEarningsResponse> call, Throwable t) {

            }
        });
    }

    private void setDetails(WeeklyEarningsResponse body) {
        txtIncentive.setText(currencyStr + " " + body.getMonthly_incentives());
        txtTotalCollected.setText(currencyStr + " " + body.getMonthly_earnings());
    }

    private void setMultiBarGraph() {
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        chart.setMaxVisibleValueCount(6);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(dayList.size());
        xAxis.setLabelRotationAngle(290f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return dayList.get((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);
        chart.animateXY(2000, 2000);
        float barWidth = 0.5f; // x4 DataSet

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();


        for (int i = 0; i < totalAmountList.size(); i++) {
            values1.add(new BarEntry(i, (float) totalAmountList.get(i)));

        }
        for (int i = 0; i < totalOrderList.size(); i++) {
            values2.add(new BarEntry(i, (float) 40));
        }

        BarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);

            set1.setValues(values1);
            set2.setValues(values2);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "Total Earnings");
            set1.setColor(Color.rgb(104, 250, 175));
            set2 = new BarDataSet(values2, "No of Orders");
            set2.setColor(Color.rgb(242, 247, 158));

            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());

            chart.setData(data);
        }


        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);
        chart.getBarData().setDrawValues(true);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.setVisibleXRangeMaximum(7);
        chart.zoom(0.1f,0.1f,10,10);
        chart.invalidate();



    }



    /*private void setGraph() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(dayList.size());
        xAxis.setLabelRotationAngle(290f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMaximum(dayList.size() - 1);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dayList.get((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);
        chart.setData(generateDataLine(1));
        chart.invalidate();
        chart.animateX(3000);

    }

    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(0, 0));
        for (int i = 1; i < totalAmountList.size()+1; i++) {
            e1.add(new Entry(i, totalAmountList.get(i-1)));
        }
        LineDataSet d1 = new LineDataSet(e1, "Earnings");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(5f);
        d1.setHighlightEnabled(false);
        d1.setDrawValues(true);
        d1.setColor(Color.GREEN);
        d1.setCircleColor(Color.RED);
        d1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<Entry> e2 = new ArrayList<Entry>();
        e2.add(new Entry(0,0));
        for (int i = 1; i < totalOrderList.size()+1; i++) {
            e2.add(new Entry(i, totalOrderList.get(i-1)));
        }

        LineDataSet d2 = new LineDataSet(e2, "Orders");
        d2.setLineWidth(2.5f);
        d2.setDrawValues(false);
        d2.setHighlightEnabled(false);
        d2.setCircleRadius(5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setColor(Color.RED);
        d2.setCircleColor(Color.RED);
        d2.setCircleColorHole(Color.RED);
        d2.setDrawValues(true);
        d2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }*/

    /*private void setGraph() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setPinchZoom(true);

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Fri,Mar 9");
        xLabel.add("Sat,Aug 12");
        xLabel.add("Mon,Sep 08");
        xLabel.add("Sun,Nov 02");
        xLabel.add("Thu,Aug 20");
        xLabel.add("Sat,Nov 30");


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(xLabel.size());
        xAxis.setLabelRotationAngle(290f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMaximum(xLabel.size() - 1);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(6, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);
        chart.setData(generateDataLine(1));
        chart.invalidate();
        chart.animateX(2000);

    }

    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> e1 = new ArrayList<Entry>();
        e1.add(new Entry(0, 200));
        for (int i = 1; i < 10; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 650) + 456));
        }
        LineDataSet d1 = new LineDataSet(e1, "Money Received");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(2.5f);
        d1.setHighlightEnabled(false);
        d1.setDrawValues(true);
        d1.setColor(Color.RED);
        d1.setCircleColor(Color.RED);
        d1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 10; i++) {
            e2.add(new Entry(i, (int) (Math.random() * 200) + 56));
        }

        LineDataSet d2 = new LineDataSet(e2, "Money Sent");
        d2.setLineWidth(2.0f);
        d2.setDrawValues(false);
        d2.setHighlightEnabled(false);
        d2.setCircleRadius(2.5f);
        *//*d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);*//*
        d2.setColor(Color.RED);
        d2.setCircleColor(Color.RED);
        d2.setDrawValues(true);
        d2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
//        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }*/

}
