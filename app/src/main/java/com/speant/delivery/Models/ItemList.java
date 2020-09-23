package com.speant.delivery.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemList implements Parcelable {

    private String food_name;

    private String item_price;

    private String is_veg;

    private String food_quantity;

    protected ItemList(Parcel in) {
        food_name = in.readString();
        item_price = in.readString();
        is_veg = in.readString();
        food_quantity = in.readString();
    }

    public static final Creator<ItemList> CREATOR = new Creator<ItemList>() {
        @Override
        public ItemList createFromParcel(Parcel in) {
            return new ItemList(in);
        }

        @Override
        public ItemList[] newArray(int size) {
            return new ItemList[size];
        }
    };

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getIs_veg() {
        return is_veg;
    }

    public void setIs_veg(String is_veg) {
        this.is_veg = is_veg;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(String food_quantity) {
        this.food_quantity = food_quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(food_name);
        dest.writeString(item_price);
        dest.writeString(is_veg);
        dest.writeString(food_quantity);
    }
}
