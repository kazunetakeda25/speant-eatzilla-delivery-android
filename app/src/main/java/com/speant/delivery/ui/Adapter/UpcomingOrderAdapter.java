package com.speant.delivery.ui.Adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.delivery.Models.ItemList;
import com.speant.delivery.Models.UpcomingOrders;
import com.bumptech.glide.Glide;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.R;
import com.speant.delivery.ui.ActivityTaskDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpcomingOrderAdapter extends RecyclerView.Adapter<UpcomingOrderAdapter.Viewholder> {
    Activity activity;
    List<UpcomingOrders> upcomingOrders;
    List<ItemList> itemLists;
    String ordersMenu;
    StringBuilder stringBuilder;

    public UpcomingOrderAdapter(Activity activity, List<UpcomingOrders> upcomingOrders) {
        Log.e("Giri ", "PastOrderAdapter:pastOrders "+upcomingOrders.size() );
        this.activity = activity;
        this.upcomingOrders = upcomingOrders;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_history_list, parent, false);
        stringBuilder = new StringBuilder();
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        itemLists = upcomingOrders.get(position).getItem_list();
        Glide.with(activity)
                .load(upcomingOrders.get(position).getRestaurant_image())
                .into(holder.imgFood);
        holder.hotelName.setText(upcomingOrders.get(position).getRestaurant_name());
        holder.txtAmount.setText(upcomingOrders.get(position).getBill_amount());
        String time = Global.getDateFromString(upcomingOrders.get(position).getOrdered_on(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");
        holder.txtOrderno.setText(time);
        for (int i = 0; i < itemLists.size(); i++) {
            String itemName = itemLists.get(i).getFood_quantity() + " x " + itemLists.get(i).getFood_name() + ",";
            ordersMenu = stringBuilder.append(itemName).toString();
            Log.e("Giri ", "onBindViewHolder: ordersMenu"+ordersMenu );
        }
        holder.txtItems.setText(ordersMenu);

    }

    @Override
    public int getItemCount() {
        return upcomingOrders.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_food)
        AppCompatImageView imgFood;
        @BindView(R.id.hotel_name)
        AppCompatTextView hotelName;
        @BindView(R.id.hotel_loc)
        AppCompatTextView hotelLoc;
        @BindView(R.id.txt_items)
        AppCompatTextView txtItems;
        @BindView(R.id.txt_orderno)
        AppCompatTextView txtOrderno;
        @BindView(R.id.txt_amount)
        AppCompatTextView txtAmount;

        public Viewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intents = new Intent(activity, ActivityTaskDetail.class);
                    intents.putExtra(CONST.Params.request_id, upcomingOrders.get(getAdapterPosition()).getRequest_id());
                    activity.startActivity(intents);
                }
            });
        }
    }
}
