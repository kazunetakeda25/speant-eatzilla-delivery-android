package com.speant.delivery.Common;

/**
 * Created by senthil on 23-Feb-18.
 */

public class CONST {

    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final int SUCCESS_CODE = 200;
    public static final String SERVICE_FAILED = "Service Failed";
    public static final String BUTTON_ACCEPT = "Accept";
    public static final String BUTTON_REJECT = "Reject";
    public static final String HELP_URL = "http://demo.speant.info/help.html";
//    public static final String HELP_URL = "http://3.18.202.172/eatzilla/help";
    public static final String REQUEST_ID = "request_id" ;
    public static final String PAST_ORDER_DETAIL = "past_order_detail" ;
    public static final String PAST_ORDER_ITEMS = "past_order_items" ;
    public static final String VEG = "1" ;
    public static final int DATE_PICKER_REQUEST = 0 ;

    public static final String ONLINE_STATUS = "1";
    public static final String OFFLINE_STATUS = "0";
    public static final String ONRIDE_STATUS = "2";
    public static final int REFRESH_ACTIVITY = 1232;


    //    http://18.217.220.83/superior-cleaning-app-web/api/check_postal_code/{postal_code}
//    public static String BASE_URL = "http://54.218.62.130/eatzilla/api/providerApi/";
//    public static String BASE_URL = "http://138.197.14.246/foodie/api/providerApi/";
//    public static String BASE_URL = " http://18.223.116.216/boxfood/api/providerApi/";
//    public static String BASE_URL = " http://3.18.202.172/eatzilla/api/providerApi/";
    public static String BASE_URL = "http://167.71.153.176/eatzilla/api/providerApi/";

    //    public static String APIKEY = "AIzaSyCfNs_K3AD2HQ4ycRdC6zxZvnM0bAukT48";
//    public static String APIKEY = "AIzaSyDlwxHz0Y2zatWPImYziSlEdmf0g-wgJWo";
//    public static String APIKEY = "AIzaSyDjz8dCm4MMlLTACIFkd3B8k0F-roEQjAI";
//    public static String APIKEY = "AIzaSyCnLj1p0yZPfsu906_5pMoLGzyqQhWwTdE";
    public static String APIKEY = "AIzaSyCYGarwIcVlcdiYuRioZuffuEuRd5n4L1U";


    public static String FIREBASE_URL = "https://cleaning-f2437.firebaseio.com/chat_message/";
    public static String STRIPE_KEY = "pk_test_SX2qwU595p4aBsbX4uCAmSj7";

    public static String currentAddress = "";

    public static final String ORDER_CREATED = "0";
    public static final String RESTAURANT_ACCEPTED = "1";
    public static final String FOOD_PREPARED = "2";
    public static final String DELIVERY_REQUEST_ACCEPTED = "3";
    public static final String REACHED_RESTAURANT = "4";
    public static final String FOOD_COLLECTED_ONWAY = "5";
    public static final String FOOD_DELIVERED = "6";
    public static final String ORDER_COMPLETE = "7";
    public static final String ORDER_CANCELLED = "10";
    public static final String NO_ORDER = "-1";

    public static final String BUTTON_REACHED_RESTAURANT = "Reached Restaurant";
    public static final String TOWARDS_CUSTOMER = "Started towards Customer";
    public static final String DELIVERED_TO_CUSTOMER = "Food delivered";
    public static final String CASH_RECEIVED = "Cash received";
    public static final String MOBILE_NUMBER ="mobile_number";
    public static boolean IS_IN_RIDE;

    public static class Params {

        public static String phone = "phone";
        public static String device_token = "device_token";
        public static String password = "password";
        public static String email = "email";

        public static String available_providers = "available_providers";
        public static String providers_status = "providers_status";

        public static String lat = "lat";
        public static String lng = "lng";
        public static String new_request = "new_request";
        public static String current_request = "current_request";
        public static String request_id = "request_id";
        public static String provider_id = "provider_id";
        public static String status = "status";
        public static String LatLng = "LatLng";
        public static String prov_location = "prov_location";
        public static String dev_token = "log_device_token";
        public static String device_type = "device_type";
        public static String login_type = "login_type";
    }

    public static class Keywords {

        //these keywords used in login page to make different visibility of linear inside card
        public static final String sign_in = "signIn";//To display Signin page
        public static final String sign_up = "signUp";//To display Register page
        public static final String verification = "verification";//To show otp verification
        public static final String forgot_pswd = "forgotPswd";// To show forgot password geting phone number screen
        public static final String new_pswd = "newPswd";//To show reset password page


    }
}
