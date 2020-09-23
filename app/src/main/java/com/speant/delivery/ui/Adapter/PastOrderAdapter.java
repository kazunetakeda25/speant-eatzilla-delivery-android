package com.speant.delivery.ui.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.delivery.Models.ItemList;
import com.speant.delivery.Models.PastOrders;
import com.bumptech.glide.Glide;
import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.global.Global;
import com.speant.delivery.R;
import com.speant.delivery.ui.CompletedOrderActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.Viewholder> {
    Activity activity;
    List<PastOrders> pastOrders;
    List<ItemList> itemLists;
    private String ordersMenu;

    public PastOrderAdapter(Activity activity, List<PastOrders> pastOrders) {
        Log.e("Giri ", "PastOrderAdapter:pastOrders "+pastOrders.size() );
        this.activity = activity;
        this.pastOrders = pastOrders;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_history_list, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Log.e("Giri ", "onBindViewHolder: "+position );
        itemLists = pastOrders.get(position).getItem_list();
        Glide.with(activity)
                .load(pastOrders.get(position).getRestaurant_image())
                .into(holder.imgFood);
        holder.hotelName.setText(pastOrders.get(position).getRestaurant_name());
        holder.txtAmount.setText(pastOrders.get(position).getBill_amount());
        String time = Global.getDateFromString(pastOrders.get(position).getOrdered_on(), "yyyy-MM-dd HH:mm", "EEEE MMM, d hh:mm a");
        holder.txtOrderno.setText(time);
        for (int i = 0; i < itemLists.size(); i++) {
            String itemName = itemLists.get(i).getFood_quantity() + " x " + itemLists.get(i).getFood_name() + ",";
            ordersMenu = new StringBuilder().append(itemName).toString();
        }
        holder.txtItems.setText(ordersMenu);
    }

    @Override
    public int getItemCount() {
        return pastOrders.size();
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
                    Log.e("Giri ", "onClick:itemView "+pastOrders.get(getAdapterPosition()).getItem_list().size() );
                    Intent intent = new Intent(activity, CompletedOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CONST.PAST_ORDER_DETAIL, pastOrders.get(getAdapterPosition()));
                    bundle.putParcelableArrayList(CONST.PAST_ORDER_ITEMS, (ArrayList<? extends Parcelable>) pastOrders.get(getAdapterPosition()).getItem_list());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
