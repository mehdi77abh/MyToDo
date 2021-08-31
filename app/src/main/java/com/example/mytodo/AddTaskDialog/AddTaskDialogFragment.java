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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_NORMAL;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_HIGH;
import static com.example.mytodo.AddTaskDialog.Const.IMPORTANCE_LOW;

import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.List;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class AddTaskDialogFragment extends DialogFragment implements PersianPickerListener {

    private static final String TAG = "AddTaskDialogFragment";
    private TextInputLayout txt_input_task_layout;
    private TextInputEditText txt_input_task;
    private TextInputEditText txt_input_date;
    private TextInputEditText txt_input_time;
    private List<String> importance_list = List.of("اولویت بالا", "اولویت متوسط", "اولویت پایین");
    private ArrayAdapter adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private MaterialTimePicker timePicker;
    private AddTaskDialogViewModel viewModel;


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
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getContext())).get(AddTaskDialogViewModel.class);

        adapter = new ArrayAdapter(getContext(), R.layout.importance_list_item, R.id.importance_item_list, importance_list);

        autoCompleteTextView.setAdapter(adapter);
        txt_input_date.setOnClickListener(v ->
                DatePickerProvider.getDatePicker(getContext())
                        .setListener(AddTaskDialogFragment.this).show()

        );

        txt_input_time.setOnClickListener(v -> {
            timePicker = TimePickerProvider.getTimePicker();
            timePicker.show(getParentFragmentManager(), null);
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                txt_input_time.setText(timePicker.getHour() + ":" + timePicker.getMinute());
                Log.i(TAG, "Time Picker : " + timePicker.getHour() + ":" + timePicker.getMinute());
            });
            timePicker.addOnNegativeButtonClickListener(v1 -> timePicker.dismiss());
        });

        view.findViewById(R.id.add_dialog_btn).setOnClickListener(v -> {
            if (txt_input_task.getText().length() > 0) {
                Task task = new Task();
                task.setTitle(txt_input_task.getText().toString());
                task.setDate(txt_input_date.getText().toString());
                task.setTime(txt_input_time.getText().toString());
                autoCompleteTextView.getText();
                task.setImportance(MyDialogHelper.getImportanceInt(autoCompleteTextView.getText().toString()));
                task.setTime(txt_input_time.getText().toString().isEmpty() ? "بدون ساعت" : txt_input_time.getText().toString());
                task.setDate(txt_input_date.getText().toString().isEmpty() ? "بدون تاریخ" : txt_input_date.getText().toString());
                task.setComplete(false);
                viewModel.saveTask(task);
                dismiss();

            } else txt_input_task_layout.setError("نمیتواند خالی باشد");

        });
        return builder.create();

    }


    @Override
    public void onDateSelected(PersianPickerDate persianPickerDate) {
        txt_input_date.setText(persianPickerDate.getPersianLongDate());
        String s = persianPickerDate.getGregorianDate().toString();
        Log.i(TAG, "onDateSelected: " + s);


    }


    @Override
    public void onDismissed() {

    }


}
