package com.speant.delivery.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestDetailPojo {


    @Expose
    @SerializedName("food_detail")
    private List<FoodDetail> foodDetail;
    @Expose
    @SerializedName("bill_detail")
    private List<BillDetail> billDetail;
    @Expose
    @SerializedName("address_detail")
    private List<AddressDetail> addressDetail;
    @Expose
    @SerializedName("user_detail")
    private UserDetail userDetail;
    @Expose
    @SerializedName("restaurant_detail")
    private RestaurantDetail restaurantDetail;
    @Expose
    @SerializedName("order_id")
    private String orderId;
    @Expose
    @SerializedName("ordered_time")
    private String orderedTime;
    @Expose
    @SerializedName("request_id")
    private String requestId;
    @Expose
    @SerializedName("message")
    private String message;
    @Expose
    @SerializedName("status")
    private boolean status;
    @Expose
    @SerializedName("request_status")
    private String request_status;
    @Expose
    @SerializedName("assigned_time")
    private String assigned_time;
    @Expose
    @SerializedName("notification_time")
    private int notification_time;

    public String getAssigned_time() {
        return assigned_time;
    }

    public void setAssigned_time(String assigned_time) {
        this.assigned_time = assigned_time;
    }

    public int getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(int notification_time) {
        this.notification_time = notification_time;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public List<FoodDetail> getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(List<FoodDetail> foodDetail) {
        this.foodDetail = foodDetail;
    }

    public List<BillDetail> getBillDetail() {
        return billDetail;
    }

    public void setBillDetail(List<BillDetail> billDetail) {
        this.billDetail = billDetail;
    }

    public List<AddressDetail> getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(List<AddressDetail> addressDetail) {
        this.addressDetail = addressDetail;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public RestaurantDetail getRestaurantDetail() {
        return restaurantDetail;
    }

    public void setRestaurantDetail(RestaurantDetail restaurantDetail) {
        this.restaurantDetail = restaurantDetail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public static class FoodDetail {
        @Expose
        @SerializedName("is_veg")
        private int isVeg;
        @Expose
        @SerializedName("price")
        private int price;
        @Expose
        @SerializedName("quantity")
        private int quantity;
        @Expose
        @SerializedName("name")
        private String name;

        public int getIsVeg() {
            return isVeg;
        }

        public void setIsVeg(int isVeg) {
            this.isVeg = isVeg;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class BillDetail {
        @Expose
        @SerializedName("bill_amount")
        private double billAmount;
        @Expose
        @SerializedName("delivery_charge")
        private int deliveryCharge;
        @Expose
        @SerializedName("tax")
        private double tax;
        @Expose
        @SerializedName("packaging_charge")
        private int packagingCharge;
        @Expose
        @SerializedName("offer_discount")
        private double offerDiscount;
        @Expose
        @SerializedName("item_total")
        private double itemTotal;
        @Expose
        @SerializedName("restaurant_discount")
        private double restaurant_discount ;
        @Expose
        @SerializedName("paid_type")
        private int paid_type ;

        public int getPaid_type() {
            return paid_type;
        }

        public void setPaid_type(int paid_type) {
            this.paid_type = paid_type;
        }

        public double getRestaurant_discount() {
            return restaurant_discount;
        }

        public void setRestaurant_discount(double restaurant_discount) {
            this.restaurant_discount = restaurant_discount;
        }

        public double getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(double billAmount) {
            this.billAmount = billAmount;
        }

        public int getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(int deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public int getPackagingCharge() {
            return packagingCharge;
        }

        public void setPackagingCharge(int packagingCharge) {
            this.packagingCharge = packagingCharge;
        }

        public double getOfferDiscount() {
            return offerDiscount;
        }

        public void setOfferDiscount(double offerDiscount) {
            this.offerDiscount = offerDiscount;
        }

        public double getItemTotal() {
            return itemTotal;
        }

        public void setItemTotal(double itemTotal) {
            this.itemTotal = itemTotal;
        }
    }

    public static class AddressDetail {
        @Expose
        @SerializedName("s_lng")
        private double sLng;
        @Expose
        @SerializedName("s_lat")
        private double sLat;
        @Expose
        @SerializedName("d_lng")
        private double dLng;
        @Expose
        @SerializedName("d_lat")
        private double dLat;
        @Expose
        @SerializedName("s_address")
        private String sAddress;
        @Expose
        @SerializedName("d_address")
        private String dAddress;

        public double getSLng() {
            return sLng;
        }

        public void setSLng(double sLng) {
            this.sLng = sLng;
        }

        public double getSLat() {
            return sLat;
        }

        public void setSLat(double sLat) {
            this.sLat = sLat;
        }

        public double getDLng() {
            return dLng;
        }

        public void setDLng(double dLng) {
            this.dLng = dLng;
        }

        public double getDLat() {
            return dLat;
        }

        public void setDLat(double dLat) {
            this.dLat = dLat;
        }

        public String getSAddress() {
            return sAddress;
        }

        public void setSAddress(String sAddress) {
            this.sAddress = sAddress;
        }

        public String getDAddress() {
            return dAddress;
        }

        public void setDAddress(String dAddress) {
            this.dAddress = dAddress;
        }
    }

    public static class UserDetail {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("login_type")
        private int loginType;
        @Expose
        @SerializedName("referral_amount")
        private int referralAmount;
        @Expose
        @SerializedName("referral_code")
        private String referralCode;
        @Expose
        @SerializedName("profile_image")
        private String profileImage;
        @Expose
        @SerializedName("otp")
        private int otp;
        @Expose
        @SerializedName("device_token")
        private String deviceToken;
        @Expose
        @SerializedName("authToken")
        private String authtoken;
        @Expose
        @SerializedName("password")
        private String password;
        @Expose
        @SerializedName("email")
        private String email;
        @Expose
        @SerializedName("phone")
        private String phone;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private int id;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getLoginType() {
            return loginType;
        }

        public void setLoginType(int loginType) {
            this.loginType = loginType;
        }

        public int getReferralAmount() {
            return referralAmount;
        }

        public void setReferralAmount(int referralAmount) {
            this.referralAmount = referralAmount;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public int getOtp() {
            return otp;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getAuthtoken() {
            return authtoken;
        }

        public void setAuthtoken(String authtoken) {
            this.authtoken = authtoken;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class RestaurantDetail {
        @Expose
        @SerializedName("updated_at")
        private String updatedAt;
        @Expose
        @SerializedName("created_at")
        private String createdAt;
        @Expose
        @SerializedName("status")
        private int status;
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;
        @Expose
        @SerializedName("address")
        private String address;
        @Expose
        @SerializedName("packaging_charge")
        private int packagingCharge;
        @Expose
        @SerializedName("estimated_delivery_time")
        private String estimatedDeliveryTime;
        @Expose
        @SerializedName("is_open")
        private int isOpen;
        @Expose
        @SerializedName("rating")
        private double rating;
        @Expose
        @SerializedName("discount")
        private String discount;
        @Expose
        @SerializedName("phone")
        private String phone;
        @Expose
        @SerializedName("email")
        private String email;
        @Expose
        @SerializedName("image")
        private String image;
        @Expose
        @SerializedName("restaurant_name")
        private String restaurantName;
        @Expose
        @SerializedName("id")
        private int id;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getPackagingCharge() {
            return packagingCharge;
        }

        public void setPackagingCharge(int packagingCharge) {
            this.packagingCharge = packagingCharge;
        }

        public String getEstimatedDeliveryTime() {
            return estimatedDeliveryTime;
        }

        public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
            this.estimatedDeliveryTime = estimatedDeliveryTime;
        }

        public int getIsOpen() {
            return isOpen;
        }

        public void setIsOpen(int isOpen) {
            this.isOpen = isOpen;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
