package com.speant.delivery.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UpcomingOrders implements Parcelable {

    private List<ItemList> item_list;

    private String delivery_address;

    private String restaurant_address;

    private String offer_discount;

    private String ordered_on;

    private String request_id;

    private String order_id;

    private String delivery_charge;

    private String restaurant_packaging_charge;

    private String tax;

    private String restaurant_image;

    private String bill_amount;

    private String restaurant_name;

    private String item_total;

    private String restaurant_discount ;

    protected UpcomingOrders(Parcel in) {
        item_list = in.createTypedArrayList(ItemList.CREATOR);
        delivery_address = in.readString();
        restaurant_address = in.readString();
        offer_discount = in.readString();
        ordered_on = in.readString();
        request_id = in.readString();
        order_id = in.readString();
        delivery_charge = in.readString();
        restaurant_packaging_charge = in.readString();
        tax = in.readString();
        restaurant_image = in.readString();
        bill_amount = in.readString();
        restaurant_name = in.readString();
        item_total = in.readString();
        restaurant_discount = in.readString();
    }

    public static final Creator<UpcomingOrders> CREATOR = new Creator<UpcomingOrders>() {
        @Override
        public UpcomingOrders createFromParcel(Parcel in) {
            return new UpcomingOrders(in);
        }

        @Override
        public UpcomingOrders[] newArray(int size) {
            return new UpcomingOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(item_list);
        dest.writeString(delivery_address);
        dest.writeString(restaurant_address);
        dest.writeString(offer_discount);
        dest.writeString(ordered_on);
        dest.writeString(request_id);
        dest.writeString(order_id);
        dest.writeString(delivery_charge);
        dest.writeString(restaurant_packaging_charge);
        dest.writeString(tax);
        dest.writeString(restaurant_image);
        dest.writeString(bill_amount);
        dest.writeString(restaurant_name);
        dest.writeString(item_total);
        dest.writeString(restaurant_discount);
    }

    public List<ItemList> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemList> item_list) {
        this.item_list = item_list;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public String getOffer_discount() {
        return offer_discount;
    }

    public void setOffer_discount(String offer_discount) {
        this.offer_discount = offer_discount;
    }

    public String getOrdered_on() {
        return ordered_on;
    }

    public void setOrdered_on(String ordered_on) {
        this.ordered_on = ordered_on;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getRestaurant_packaging_charge() {
        return restaurant_packaging_charge;
    }

    public void setRestaurant_packaging_charge(String restaurant_packaging_charge) {
        this.restaurant_packaging_charge = restaurant_packaging_charge;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getRestaurant_image() {
        return restaurant_image;
    }

    public void setRestaurant_image(String restaurant_image) {
        this.restaurant_image = restaurant_image;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public String getRestaurant_discount() {
        return restaurant_discount;
    }

    public void setRestaurant_discount(String restaurant_discount) {
        this.restaurant_discount = restaurant_discount;
    }
}
