package com.speant.delivery.Common;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.speant.delivery.R;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    public Calendar myCalendar;
    private OnDateCompleteListener mListener;
    String datePicked;
    DatePickerDialog datePickerDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        Log.e("", "onCreateDialog: " + getActivity());
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DateDialogTheme, this, year, month, day);

        //used to disable past dates
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the chosen date
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, day);
        mListener.onDateComplete(myCalendar.getTime());
    }

    public void onCancel(DialogInterface dialog) {
        // Send a message to confirm cancel button click
//        Toast.makeText(getActivity(), "Date Picker Canceled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //use getTargetFragment if the picker is called  from fragment
            this.mListener = (OnDateCompleteListener) getTargetFragment();
        } catch (final ClassCastException e) {
            throw new ClassCastException(" must implement OnCompleteListener" + e);
        }
    }

    public interface OnDateCompleteListener {
        void onDateComplete(Date date);
    }


}