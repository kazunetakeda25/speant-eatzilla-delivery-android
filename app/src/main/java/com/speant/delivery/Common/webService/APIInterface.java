package com.speant.delivery.Common.webService;


import com.speant.delivery.Models.ChatHistory;
import com.speant.delivery.Models.DailyEarningsResponse;
import com.speant.delivery.Models.DirectionResults;
import com.speant.delivery.Models.HistoryResponse;
import com.speant.delivery.Models.LoginPojo;
import com.speant.delivery.Models.PayoutsResponse;
import com.speant.delivery.Models.ProfileDetailsResponse;
import com.speant.delivery.Models.RequestDetailPojo;
import com.speant.delivery.Models.SendOtpResponse;
import com.speant.delivery.Models.SuccessPojo;
import com.speant.delivery.Models.TimeoutSuccessPojo;
import com.speant.delivery.Models.UpdateRequestPojo;
import com.speant.delivery.Models.WeeklyEarningsResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("{url}")
//Its for both login and signup
    Call<LoginPojo> login(@Path(value = "url", encoded = true) String type, @FieldMap Map<String, String> fields, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("get_address_detail")
//Its for getting address Details
    Call<RequestDetailPojo> getRequestDetail(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("update_request")
//Its for updating request
    Call<UpdateRequestPojo> setUpdateRequest(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("cancel_request")
//Its for both login and signup
    Call<SuccessPojo> setCancelRequest(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields, @Query(value = "lang", encoded = true) String currentLanguage);


    @GET("directions/json?")
    Call<DirectionResults> polyLines(@Query(value = "key") String key,
                                     @Query(value = "origin") String origin,
                                     @Query(value = "destination") String destination);


    @GET("directions/json?sensor=false&language=pt&mode=driving")
    Call<DirectionResults> getRoute(@Query("key") String apiKey, @Query("origin") String origin, @Query("destination") String dest);

    @GET("get_order_status")
    Call<RequestDetailPojo> getCurrentBooking(@HeaderMap HashMap<String, String> header, @Query(value = "lang", encoded = true) String currentLanguage);

    @GET("get_profile")
    Call<ProfileDetailsResponse> getProfile(@HeaderMap HashMap<String, String> header, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<SendOtpResponse> sendOtp(@FieldMap HashMap<String, String> map, @Query(value = "lang", encoded = true) String currentLanguage);

    @GET("order_history")
    Call<HistoryResponse> getOrderHistory(@HeaderMap HashMap<String, String> header, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("today_earnings")
    Call<DailyEarningsResponse> getEarning(@HeaderMap HashMap<String, String> header, @Field("filter_date") String dateString, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("weekly_earnings")
    Call<WeeklyEarningsResponse> getWeeklyEarning(@HeaderMap HashMap<String, String> header, @Field("filter_date") String serviceDateString, @Query(value = "lang", encoded = true) String currentLanguage);

    @FormUrlEncoded
    @POST("monthly_earnings")
    Call<WeeklyEarningsResponse> getMonthlyEarning(@HeaderMap HashMap<String, String> header, @Field("filter_date") String serviceDateString, @Query(value = "lang", encoded = true) String currentLanguage);

    @GET("payout_details")
    Call<PayoutsResponse> getPayouts(@HeaderMap HashMap<String, String> header, @Query(value = "lang", encoded = true) String currentLanguage);

    @GET("get_provider_timeout")
    Call<TimeoutSuccessPojo> getProviderTimeout();

    //chat

    @GET("get_chat_history/{request_id}/{type}")
    Call<ChatHistory> getChat(@HeaderMap HashMap<String, String> header, @Path("request_id") String req_id, @Path("type") String type);

    /*@FormUrlEncoded
    @POST("{url}")//Its for both login and signup
    Call<LoginPojo> loginorSignup(@Path(value = "url", encoded = true) String type, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("{url}") //It user for Send OTP and ResetPassword
    Call<SuccessPojo> sendOtp(@Path(value = "url", encoded = true) String type, @FieldMap Map<String, String> fields);

    @GET("{url}") //It user for Send OTP and ResetPassword
    Call<SuccessPojo> logout(@Path(value = "url", encoded = true) String type, @HeaderMap Map<String, String> fields);

    @GET("get_filter_list/{type}")
    Call<FilterPojo> getFilter(@Path(value = "type", encoded = true) String type, @HeaderMap Map<String, String> header);

    @GET("get_nearby_restaurant?")
    Call<NearRestarentPojo> getNearRestarent(@HeaderMap Map<String, String> header, @QueryMap Map<String, String> LatLang);

    @GET("get_banners")
    Call<BannerPojo> getBanners(@HeaderMap Map<String, String> header);

    @GET("get_popular_brands")
    Call<PopularBrandsPojo> getPopularBrands(@HeaderMap Map<String, String> header);

    @FormUrlEncoded
    @POST("update_favourite")
    Call<SuccessPojo> homeLike(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("get_menu")
    Call<MenuPojo> menuList(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("get_relevance_restaurant")
    Call<RelevanceHotelPojo> getRelevance(@HeaderMap Map<String, String> header,
                                          @FieldMap Map<String, String> fields, @Field("cuisines[]") ArrayList<String> cuisines,
                                          @Field("relevance[]") ArrayList<String> relevance);

    @FormUrlEncoded
    @POST("single_restaurant")
    Call<HotelDetailPojo> hotelDetail(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("add_to_cart")
    Call<SuccessPojo> addToCart(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("reduce_from_cart")
    Call<SuccessPojo> reduceFromCart(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @GET("check_cart")
    Call<CheckCartPojo> checkCart(@HeaderMap Map<String, String> header);

    @FormUrlEncoded
    @POST("checkout")
    Call<CheckoutPojo> checkout(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @GET("get_category/{restaurant_id}")
    Call<CategoryPojo> getCategory(@HeaderMap Map<String, String> header, @Path(value = "restaurant_id", encoded = true) String bid);

    @FormUrlEncoded
    @POST("get_category_wise_food_list")
    Call<CategoryWiseFoodPojo> getCategoryWiseFoodList(@HeaderMap Map<String, String> header, @FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("paynow")
    Call<SuccessPojo> finalPayment(@HeaderMap Map<String, String> header,
                                   @FieldMap Map<String, String> fields, @Field("food_id[]") ArrayList<String> food_id,
                                   @Field("food_qty[]") ArrayList<String> food_qty);*/
}