package com.speant.delivery.ui.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speant.delivery.Models.RequestDetailPojo;
import com.speant.delivery.ui.ActivityTaskDetail;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    List<RequestDetailPojo.FoodDetail> foodList = new ArrayList<>();
    private Context mContext;
    SessionManager sessionManager;

    public FoodListAdapter(ActivityTaskDetail activityTaskDetail, List<RequestDetailPojo.FoodDetail> foodDetailList) {
        this.foodList = foodDetailList;
        mContext = activityTaskDetail;
        sessionManager = new SessionManager(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_food, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestDetailPojo.FoodDetail pojo = foodList.get(position);

        holder.itemNameTxt.setText(pojo.getQuantity()+ " X "+ pojo.getName());
        holder.listCartItemAmt.setText(sessionManager.getCurrency()+""+pojo.getPrice());

        Drawable non_veg = mContext.getResources().getDrawable(R.drawable.ic_non_veg);
        Drawable veg = mContext.getResources().getDrawable(R.drawable.ic_veg);

        if (pojo.getIsVeg() == 1) {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(veg, null, null, null);
        } else {
            holder.itemNameTxt.setCompoundDrawablesWithIntrinsicBounds(non_veg, null, null, null);
        }
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_linear)
        LinearLayout addLinear;
        @BindView(R.id.item_name_txt)
        TextView itemNameTxt;
        @BindView(R.id.list_cart_item_amt)
        TextView listCartItemAmt;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
