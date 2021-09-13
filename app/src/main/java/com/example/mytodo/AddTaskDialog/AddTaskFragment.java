package com.example.mytodo.AddTaskDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.other.Const;
import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.other.DatePickerProvider;
import com.example.mytodo.other.TimePickerProvider;
import com.example.mytodo.other.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class AddTaskFragment extends Fragment implements PersianPickerListener {

    private static final String TAG = "AddTaskDialogFragment";
    private TextInputEditText titleEt, dateEt, timeEt, desEt;
    private MaterialTimePicker timePicker;
    private AddTaskFragmentViewModel viewModel;
    private Calendar mCalender;
    private TextInputLayout title_layout;
    private TextInputLayout time_layout;
    private TextInputLayout date_layout;
    private RadioGroup importanceRg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_task_fragment, container, false);

        titleEt = view.findViewById(R.id.titleEditEt);
        title_layout = view.findViewById(R.id.title_layout);
        desEt = view.findViewById(R.id.desEditEt);
        dateEt = view.findViewById(R.id.dateEditEt);
        timeEt = view.findViewById(R.id.timeEditEt);
        time_layout = view.findViewById(R.id.time_layout);
        date_layout = view.findViewById(R.id.date_layout);
        importanceRg = view.findViewById(R.id.importance_rg);
        Toolbar toolbar = view.findViewById(R.id.addTaskToolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mCalender = Calendar.getInstance();
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getContext())).get(AddTaskFragmentViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateEt.setOnClickListener(v -> {
            DatePickerProvider.getDatePicker(getContext())
                    .setListener(AddTaskFragment.this).show();

        });

        timeEt.setOnClickListener(v -> {
            timePicker = TimePickerProvider.getTimePicker();
            timePicker.show(getParentFragmentManager(), null);
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                timeEt.setText(timePicker.getHour() + ":" + timePicker.getMinute());
                mCalender.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                mCalender.set(Calendar.MINUTE, timePicker.getMinute());
            });
            timePicker.addOnNegativeButtonClickListener(v1 -> timePicker.dismiss());
        });

        view.findViewById(R.id.edit_task_btn).setOnClickListener(v -> {
            if (dateEt.getText().length() == 0) {
                date_layout.setError("لطفا تاریخ را وارد کنید");
            } else if (timeEt.getText().length() == 0) {
                time_layout.setError("لطفا ساعت را وارد کنید");

            } else if (titleEt.getText().length() == 0) {
                title_layout.setError("لطفا متن کار خود را وارد کنید");
            } else {
                Task task = new Task();
                task.setTitle(titleEt.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setDescription(desEt.getText().length() != 0 ? desEt.getText().toString() : "بدون توضیحات");
                importanceRg.setOnCheckedChangeListener((group, checkedId) -> {
                    switch (checkedId) {
                        case R.id.importanceHigh:
                            task.setImportance(Const.IMPORTANCE_HIGH);
                            break;
                        case R.id.importanceNormal:
                            task.setImportance(Const.IMPORTANCE_NORMAL);

                            break;
                        case R.id.importanceLow:
                            task.setImportance(Const.IMPORTANCE_LOW);
                            break;
                        default:
                            task.setImportance(Const.IMPORTANCE_NORMAL);

                    }
                });
                task.setComplete(false);
                viewModel.saveTask(task);
                getActivity().onBackPressed();
            }
        });

    }


    @Override
    public void onDateSelected(PersianPickerDate persianPickerDate) {
        mCalender.set(Calendar.YEAR, persianPickerDate.getGregorianYear());
        mCalender.set(Calendar.MONTH, persianPickerDate.getGregorianMonth() - 1);
        mCalender.set(Calendar.DAY_OF_MONTH, persianPickerDate.getGregorianDay());
        mCalender.set(Calendar.MINUTE,Calendar.getInstance().get(Calendar.MINUTE)+1);
        mCalender.set(Calendar.HOUR_OF_DAY,Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        long check = mCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (check <= 0) {
            date_layout.setError("لطفا تاریخ را به درستی انتخاب کنید");
        } else {
            dateEt.setText(persianPickerDate.getPersianLongDate());
            date_layout.setErrorEnabled(false);
        }

    }


    @Override
    public void onDismissed() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_task_toolbar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.menu_back == item.getItemId()){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}
