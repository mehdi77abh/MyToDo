package com.example.mytodo.AddTaskDialog;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimePickerProvider {
    private static MaterialTimePicker timePicker;

    public static MaterialTimePicker getTimePicker() {
        DateTimeZone zone = DateTimeZone.forID("Asia/Tehran");
        DateTime time = new DateTime(zone);
        timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("انتخاب ساعت")
                .setMinute(time.getMinuteOfHour())
                .setHour(time.getHourOfDay())
                .build();

        return timePicker;
    }
}
