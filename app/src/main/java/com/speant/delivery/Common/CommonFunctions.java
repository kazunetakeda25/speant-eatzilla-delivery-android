package com.speant.delivery.Common;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.speant.delivery.R;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonFunctions {

    public static Dialog mDialog, load_dialog;
    public static Dialog mProgressDialog;
    private static Activity activity;

    public static void showSimpleProgressDialog(Activity context, String msg, boolean isCancelable) {
        if (context != null) {
            activity = context;
            mProgressDialog = new Dialog(context, R.style.DialogSlideAnim_leftright);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.animation_loading);
            TextView tv_title = (TextView) mProgressDialog.findViewById(R.id.tv_title);
            tv_title.setText(msg);

            if(!((Activity) context).isFinishing())
            {
                //show dialog
                mProgressDialog.show();
            }

        }
    }

    public static void removeProgressDialog() {
        if (null != mProgressDialog && !((Activity) activity).isFinishing())
            mProgressDialog.dismiss();
    }

    //Short Toast
    public static void shortToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    public static boolean checkLocationPermission(Context context)
    {
        String coarse = Manifest.permission.ACCESS_COARSE_LOCATION;
        String fine = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = context.checkCallingOrSelfPermission(coarse);
        int res1 = context.checkCallingOrSelfPermission(fine);
        return (res == PackageManager.PERMISSION_GRANTED && res1 == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean isGPSEnabled(Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
