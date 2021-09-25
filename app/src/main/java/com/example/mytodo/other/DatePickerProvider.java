package com.example.mytodo.other;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.ColorRes;

import com.example.mytodo.R;

import java.util.Calendar;

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
