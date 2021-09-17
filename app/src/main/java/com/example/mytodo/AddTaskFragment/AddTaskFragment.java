package com.example.mytodo.AddTaskFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.AddTaskFragmentBinding;
import com.example.mytodo.other.Const;
import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.other.DatePickerProvider;
import com.example.mytodo.other.TimePickerProvider;
import com.example.mytodo.other.ViewModelFactory;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class AddTaskFragment extends Fragment implements PersianPickerListener {

    private static final String TAG = "AddTaskDialogFragment";
    private MaterialTimePicker timePicker;
    private AddTaskFragmentViewModel viewModel;
    private Calendar mCalender;
    private NotificationHelper notificationHelper;
    private Task task;
    private AddTaskFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddTaskFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        notificationHelper = new NotificationHelper(getContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.addTaskToolbar);
        setHasOptionsMenu(true);

        mCalender = Calendar.getInstance();
        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext())).get(AddTaskFragmentViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        task = new Task();

        binding.dateAddEt.setOnClickListener(v -> DatePickerProvider.getDatePicker(getContext())
                .setListener(AddTaskFragment.this).show());

        binding.timeAddEt.setOnClickListener(v -> {
            timePicker = TimePickerProvider.getTimePicker();
            timePicker.show(getParentFragmentManager(), null);
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                binding.timeAddEt.setText(timePicker.getHour() + ":" + timePicker.getMinute());
                mCalender.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                mCalender.set(Calendar.MINUTE, timePicker.getMinute());
                mCalender.set(Calendar.SECOND,00);
            });
            timePicker.addOnNegativeButtonClickListener(v1 -> timePicker.dismiss());
        });
        binding.importanceRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.add_importance_high:
                    task.setImportance(Const.IMPORTANCE_HIGH);
                    break;
                case R.id.add_importance_normal:
                    task.setImportance(Const.IMPORTANCE_NORMAL);

                    break;
                case R.id.add_importance_low:
                    task.setImportance(Const.IMPORTANCE_LOW);
                    break;
                default:
                    task.setImportance(Const.IMPORTANCE_NORMAL);

            }
        });
        view.findViewById(R.id.edit_task_btn).setOnClickListener(v -> {

            if (binding.titleEditEt.getText().length() == 0) {
                binding.titleLayout.setError("لطفا متن کار خود را وارد کنید");

            } else if (binding.timeAddEt.getText().length() == 0) {
                binding.timeLayout.setError("لطفا ساعت را وارد کنید");

            } else if (binding.timeAddEt.getText().length() == 0) {
                binding.dateLayout.setError("لطفا تاریخ را وارد کنید");

            } else {
                task.setTitle(binding.titleEditEt.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setDescription(binding.desEditEt.getText().length() != 0 ? binding.desEditEt.getText().toString() : "بدون توضیحات");

                task.setComplete(false);
                task.setNotificationId(task.getId());
                notificationHelper.setAlarm(task);
                Toast.makeText(getContext(), "اضافه شد.", Toast.LENGTH_SHORT).show();

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
        mCalender.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE) + 1);
        mCalender.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        mCalender.set(Calendar.SECOND,00);
        long check = mCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (check <= 0) {
            binding.dateLayout.setError("لطفا تاریخ را به درستی انتخاب کنید");
        } else {
            binding.dateAddEt.setText(persianPickerDate.getPersianLongDate());
            binding.dateLayout.setErrorEnabled(false);
        }

    }


    @Override
    public void onDismissed() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_task_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.menu_back == item.getItemId()) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
}
