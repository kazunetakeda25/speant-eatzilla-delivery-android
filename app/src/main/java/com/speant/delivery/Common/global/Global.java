package com.speant.delivery.Common.global;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.speant.delivery.Common.CONST;
import com.speant.delivery.Common.webService.APIClient;
import com.speant.delivery.Common.webService.APIInterface;
import com.speant.delivery.Models.DirectionResults;
import com.speant.delivery.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Global {

    private APIInterface apiService;
    Polyline line;
    List<DirectionResults.Routes> routesDetails = new ArrayList<>();
    List<LatLng> route = new ArrayList<>();
    private ArrayList<DirectionResults.Legs> Legs = new ArrayList<>();
    private ArrayList<DirectionResults.Steps> Steps = new ArrayList<>();


    public static final String SOCKET_URL = "ws://167.71.153.176:3000";
    public static final String USER_ID = "DELIVERYBOY_ID";
    public static final String FROM_TYPE = "from_type";
    public static final String USER = "User";
    public static final String ADMIN = "Admin";
    public static final String ORDER_ID = "ORDER_ID";


    public static double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public static Date getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        return date;
    }
    public static String setDateFormat(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String datePicked = sdf.format(date.getTime());
        return datePicked;
    }

    public static String getDateFromString(String dateString, String perviousformat, String newFormat) {
        DateFormat oldDateFormat = new SimpleDateFormat(perviousformat, Locale.ENGLISH);
        DateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        Date olddate = null;
        String newDate = null;
        try {
            olddate = oldDateFormat.parse(dateString);
            newDate = newDateFormat.format(olddate);
        } catch (ParseException e) {
            Log.e("Global", "getDateFromString:ParseException " + e);
        }

        return newDate;
    }

    public static String getCurrentTime(String dateformat) {

        SimpleDateFormat df1 = new SimpleDateFormat(dateformat);

        String dateString = df1.format(new Date(System.currentTimeMillis()));

        Log.e("TAG", "getCurrentTime:dateString "+dateString );

        return dateString;
    }

    public static int getTimeOutSeconds(String assigned_time, int notification_time) {

        //getting time difference between assigned time and current time
        //getting timeout seconds from their difference
        int finalTime;
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        Date endDate = null;
        try {
           startDate = outputFormat.parse(assigned_time);
           endDate = outputFormat.parse(getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffInMs = endDate.getTime() - startDate.getTime();

        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

        if((notification_time - diffInSec) < 0){
            finalTime = 0;
        }else{
            finalTime = (int) (notification_time - diffInSec);
        }

        /*long diffMs = endDate.getTime() - startDate.getTime();
        long diffSec = diffMs / 1000;
        long min = diffSec / 60;
        long sec = diffSec % 60;
        Log.e("TAG", "getTimeOutSeconds:finalTime "+"The difference is "+min+" minutes and "+sec+" seconds.");*/

        Log.e("TAG", "getTimeOutSeconds:finalTime "+finalTime );
        Log.e("TAG", "getTimeOutSeconds:notification_time "+notification_time );
        Log.e("TAG", "getTimeOutSeconds:diffInSec "+diffInSec );
        Log.e("TAG", "getTimeOutSeconds:startDate "+startDate );
        Log.e("TAG", "getTimeOutSeconds:endDate "+endDate );
        Log.e("TAG", "getTimeOutSeconds:endDate getTime"+endDate.getTime() );
        Log.e("TAG", "getTimeOutSeconds:startDate getTime "+startDate.getTime() );
        return finalTime;

    }

    public void directionJson(Context context, LatLng pickup, LatLng drop, GoogleMap mMap, boolean addAdditionalPolyline) {
        apiService = APIClient.getPlacesClient().create(APIInterface.class);
        if (pickup != null && drop != null) {
            final String origin = "" + pickup.latitude + "," + pickup.longitude;
            String destination = "" + drop.latitude + "," + drop.longitude;
            Call<DirectionResults> call = apiService.getRoute(CONST.APIKEY,origin, destination);
            call.enqueue(new Callback<DirectionResults>() {
                @Override
                public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("OK")) {
                            Log.e("latlng", "getDirections: origin" + origin);
                            if (line != null && !addAdditionalPolyline) {
                                line.remove();
                            }

                            routesDetails.clear();
                            route.clear();
                            routesDetails.addAll(response.body().getRoutes());

                            //get values using legs Value
                            parseData();

                            line = mMap.addPolyline(new PolylineOptions()
                                    .width(6)
                                    .color(context.getResources().getColor(R.color.green)));
                            line.setPoints(route);

                            //Zoom camera
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(pickup).include(drop);
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 20);
                            mMap.animateCamera(cu);

                        }
                    }
                }

                @Override
                public void onFailure(Call<DirectionResults> call, Throwable t) {
                    // Log error here since request failed
                    Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("Retro Error" + t.getMessage().toString());
                    call.cancel();
                }
            });

        }
    }

    private void parseData() {
        /** Traversing all routes */
        for (int i = 0; i < routesDetails.size(); i++) {
            Legs = routesDetails.get(i).getLegs();
            List path = new ArrayList<HashMap<String, String>>();

            /** Traversing all legs */
            for (int j = 0; j < Legs.size(); j++) {
                Steps = Legs.get(j).getSteps();

                /** Traversing all steps */
                for (int k = 0; k < Steps.size(); k++) {
                    String polyline = "";
                    polyline = Steps.get(k).getPolyline().getPoints();
                    List list = decodePoly(polyline);

                    /** Traversing all points */
                    for (int l = 0; l < list.size(); l++) {
                        Double lat = ((LatLng) list.get(l)).latitude;
                        Double lng = ((LatLng) list.get(l)).longitude;
                        LatLng latLng = new LatLng(lat, lng);
                        route.add(latLng);
                    }
                }
//                    route.add(path);
            }
        }
    }

    private List decodePoly(String encoded) {

        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static String setDate(String oldDate, String oldFormat, String newFormat) {
        DateFormat outputFormat = new SimpleDateFormat(newFormat, java.util.Locale.getDefault());
        DateFormat inputFormat = new SimpleDateFormat(oldFormat, Locale.US);
        String finalDate = null;
        Date date = null;
        try {
            String input = oldDate;
            date = inputFormat.parse(input);
            finalDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDate;
    }


    public boolean isMyServiceRunning(Class<?> serviceClass, Context context) {

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service already", "running");
                return true;
            }
        }
        Log.i("Service not", "running");
        return false;
    }

    //set Black and grey color in same string
    public static String setMultipleColorText(String firstText, String secondText) {
        String text = "<font color=#c9adad>" + firstText +" "+"</font> <font color=#000000 ><b>" + secondText + "</b></font>";
        return text;
    }

}
