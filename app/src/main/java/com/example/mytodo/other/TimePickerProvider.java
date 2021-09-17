package com.example.mytodo.other;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import java.util.Calendar;

public class TimePickerProvider {
    private static MaterialTimePicker timePicker;

    public static MaterialTimePicker getTimePicker() {
        Calendar calendar = Calendar.getInstance();
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("انتخاب ساعت")
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE)+1)
                .build();

        return timePicker;
    }
}
