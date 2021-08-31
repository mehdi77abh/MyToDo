package com.example.mytodo.AddTaskDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class AddTaskDialogFragment extends DialogFragment implements PersianPickerListener {
    public static final int IMPORTANCE_HIGH = 1;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_NORMAL = 3;
    private static final String TAG = "AddTaskDialogFragment";
    private TextInputLayout txt_input_task_layout;
    private TextInputEditText txt_input_task;
    private TextInputEditText txt_input_date;
    private TextInputEditText txt_input_time;
    private MaterialButton btn_add;
    private List<String> importance_list = List.of("اولویت بالا", "اولویت متوسط", "اولویت پایین");
    private ArrayAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private MaterialTimePicker timePicker;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_task_dialog_fragment, null, false);
        builder.setView(view);
        autoCompleteTextView = view.findViewById(R.id.importance_tv);
        txt_input_task = view.findViewById(R.id.txt_input_task);
        txt_input_time = view.findViewById(R.id.txt_input_time);
        txt_input_task_layout = view.findViewById(R.id.txt_input_task_layout);
        txt_input_date = view.findViewById(R.id.txt_input_date);
        btn_add = view.findViewById(R.id.add_dialog_btn);
        timePicker = new MaterialTimePicker();
        adapter = new ArrayAdapter(getContext(), R.layout.importance_list_item, R.id.importance_item_list, importance_list);
        autoCompleteTextView.setAdapter(adapter);
        DateTimeZone zone = DateTimeZone.forID("Asia/Tehran");
        DateTime time = new DateTime(zone);
        txt_input_date.setOnClickListener(v ->
                DatePickerProvider.getDatePicker(getContext())
                        .setListener(AddTaskDialogFragment.this).show()

        );

        txt_input_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("انتخاب ساعت")
                        .setMinute(time.getMinuteOfHour())
                        .setHour(time.getHourOfDay())
                        .build();
                timePicker.show(getParentFragmentManager(),null);
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_input_time.setText(timePicker.getHour()+":"+timePicker.getMinute());
                        Log.i(TAG, "Time Picker : "+timePicker.getHour()+":"+timePicker.getMinute());

                    }
                });
            }
        });

        timePicker.addOnPositiveButtonClickListener(v -> {
           });
        timePicker.addOnNegativeButtonClickListener(v -> {

        });


        btn_add.setOnClickListener(v -> {
            if (txt_input_task.getText().length() > 0) {
                Task task = new Task();
                task.setTitle(txt_input_task.getText().toString());
                task.setDate(txt_input_date.getText().toString());
                task.setTime(txt_input_time.getText().toString());
                autoCompleteTextView.getText();
                task.setImportance(getImportanceInt(autoCompleteTextView.getText().toString()));
                task.setTime(txt_input_time.getText().toString().isEmpty() ? "بدون ساعت" : txt_input_time.getText().toString());
                task.setDate(txt_input_date.getText().toString().isEmpty() ? "بدون تاریخ" : txt_input_date.getText().toString());
                task.setComplete(false);
                dismiss();
                //Say to View Model To Add Task To DB

            } else txt_input_task_layout.setError("نمیتواند خالی باشد");

        });
        return builder.create();

    }


    @Override
    public void onDateSelected(PersianPickerDate persianPickerDate) {
        txt_input_date.setText(persianPickerDate.getPersianLongDate());
        String s = persianPickerDate.getGregorianDate().toString();
        Log.i(TAG, "onDateSelected: "+s);


    }


    @Override
    public void onDismissed() {

    }

    private int getImportanceInt(String importanceText) {
        int importance = IMPORTANCE_NORMAL;
        switch (importanceText) {

            case "اولویت بالا":
                importance = IMPORTANCE_HIGH;
                break;
            case "اولویت متوسط":
                importance = IMPORTANCE_NORMAL;

                break;
            case "اولویت پایین":
                importance = IMPORTANCE_LOW;

                break;

        }
        return importance;
    }
}
