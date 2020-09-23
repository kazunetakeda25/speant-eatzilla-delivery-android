package com.speant.delivery.ui.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.Models.PayoutsResponse;
import com.speant.delivery.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayoutsAdapter extends RecyclerView.Adapter<PayoutsAdapter.ViewHolder> {
    Activity activity;
    List<PayoutsResponse.PayoutTransactionHistory> payoutsList;
    private SessionManager sessionManager;

    public PayoutsAdapter(Activity activity, List<PayoutsResponse.PayoutTransactionHistory> payoutsList) {
        this.activity = activity;
        this.payoutsList = payoutsList;
        sessionManager = new SessionManager(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_payout_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = Global.getDateFromString(payoutsList.get(position).getCreated_at(), "yyyy-MM-dd HH:mm:ss", "EEEE MMM , d hh:mm a");
        holder.txtCash.setText(sessionManager.getCurrency() + " " + payoutsList.get(position).getPayout_amount());
        holder.txtDate.setText(time);
        holder.txtTransId.setText("Transaction Id : "+payoutsList.get(position).getTransaction_id());
    }


    @Override
    public int getItemCount() {
        return payoutsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_trans_id)
        AppCompatTextView txtTransId;
        @BindView(R.id.txt_date)
        AppCompatTextView txtDate;
        @BindView(R.id.txt_cash)
        AppCompatTextView txtCash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
