package com.speant.delivery.ui.Adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Models.ItemList;
import com.speant.delivery.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PastOrderItemAdapter extends RecyclerView.Adapter<PastOrderItemAdapter.ViewHolder> {

    private final String currencyStr;
    Activity activity;
    List<ItemList> item_list ;


    public PastOrderItemAdapter(Activity activity, List<ItemList> item_list, String currencyStr) {
        this.activity = activity;
        this.item_list = item_list;
        this.currencyStr = currencyStr;
        Log.e("Giri ", "PastOrderItemAdapter:currencyStr "+ this.currencyStr);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_past_order_itemlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(item_list.get(position).getIs_veg().equals(CONST.VEG)){
            Picasso.get().load(R.drawable.ic_agmark_veg).into(holder.itemAgmark);
        }else{
            Picasso.get().load(R.drawable.ic_agmark_non_veg).into(holder.itemAgmark);
        }
        holder.itemName.setText(item_list.get(position).getFood_quantity()+" X "+item_list.get(position).getFood_name());
        Log.e("Giri ", "onBindViewHolder: "+currencyStr+item_list.get(position).getItem_price() );
        holder.itemAmt.setText(currencyStr+item_list.get(position).getItem_price());
    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_agmark)
        AppCompatImageView itemAgmark;
        @BindView(R.id.item_name)
        AppCompatTextView itemName;
        @BindView(R.id.item_amt)
        AppCompatTextView itemAmt;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
