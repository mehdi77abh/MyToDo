package com.example.mytodo.AddTaskDialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.annotation.ColorRes;

import com.example.mytodo.R;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;

public class DatePickerProvider {

    public static PersianDatePickerDialog getDatePicker(Context context) {
        PersianDatePickerDialog picker = new PersianDatePickerDialog(context)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1399)
                .setShowInBottomSheet(false)
                .setMaxYear(1410)
                .setActionTextColor(Color.BLACK)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR);

        return picker;
    }
}
