package com.example.mytodo.myApp;

import android.content.Context;
import android.graphics.Color;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;

public class DatePickerProvider {

    public static PersianDatePickerDialog getDatePicker(Context context) {
        PersianDatePickerDialog picker = new PersianDatePickerDialog(context)
                .setPositiveButtonString("تایید")
                .setNegativeButton("لغو")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)
                .setShowInBottomSheet(false)
                .setMaxYear(1410)
                .setActionTextColor(Color.BLACK)
                .setActionTextSize(16)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR);

        return picker;
    }


}
