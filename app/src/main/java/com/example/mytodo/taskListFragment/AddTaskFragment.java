package com.example.mytodo.taskListFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.databinding.FragmentAddTaskBinding;
import com.example.mytodo.model.Group;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.Const;
import com.example.mytodo.model.Task;
import com.example.mytodo.R;
import com.example.mytodo.myApp.DatePickerProvider;
import com.example.mytodo.myApp.TimePickerProvider;
import com.example.mytodo.myApp.ViewModelFactory;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class AddTaskFragment extends Fragment implements PersianPickerListener {

    private static final String TAG = "AddTaskDialogFragment";
    private MaterialTimePicker timePicker;
    private MainViewModel viewModel;
    private Calendar mCalender;
    private NotificationHelper notificationHelper;
    private Task task;
    private FragmentAddTaskBinding binding;
    private Group selectedGroup;
    private Long selectedLongDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);

        binding = FragmentAddTaskBinding.inflate(inflater, container, false);

        View view = binding.getRoot();
        notificationHelper = new NotificationHelper(getContext());
        mCalender = Calendar.getInstance();
        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext())).get(MainViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        task = new Task();
        selectedGroup = getArguments().getParcelable("selectedGroup");
        selectedLongDate = getArguments().getLong("selectedLongDate");
        if (selectedLongDate > 0) {
            PersianDateImpl persianDate = new PersianDateImpl();
            persianDate.setDate(selectedLongDate);
            mCalender.setTimeInMillis(selectedLongDate);
        }


        binding.dateAddEt.setOnClickListener(v -> DatePickerProvider.getDatePicker(getContext())
                .setListener(AddTaskFragment.this).show());
        Log.i("TaskListFragment", selectedGroup.getGroupTitle());

        binding.addTaskTitle.setText("آیتم جدید به گروه " + selectedGroup.getGroupTitle());
        binding.timeAddEt.setOnClickListener(v -> {
            timePicker = TimePickerProvider.getTimePicker();
            timePicker.show(getParentFragmentManager(), null);
            timePicker.addOnPositiveButtonClickListener(view1 -> {
                binding.timeAddEt.setText(timePicker.getHour() + ":" + timePicker.getMinute());
                mCalender.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                mCalender.set(Calendar.MINUTE, timePicker.getMinute());
                mCalender.set(Calendar.SECOND, 00);
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
        binding.imgBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.addTaskBtn.setOnClickListener(v -> {
            Log.i(TAG, "Click On add : ");

            if (binding.titleEditEt.getText().toString().isEmpty()) {
                binding.titleLayout.setError("لطفا متن کار خود را وارد کنید");

            } else if (binding.timeAddEt.getText().toString().isEmpty()) {
                binding.timeLayout.setError("لطفا ساعت را وارد کنید");

            } else if (binding.dateAddEt.getText().toString().isEmpty()) {
                binding.dateLayout.setError("لطفا تاریخ را وارد کنید");

            } else {
                binding.titleLayout.setErrorEnabled(false);
                binding.timeLayout.setErrorEnabled(false);
                binding.dateLayout.setErrorEnabled(false);
                task.setTitle(binding.titleEditEt.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setDescription(binding.desEditEt.getText().length() != 0 ? binding.desEditEt.getText().toString() : "بدون توضیحات");
                task.setComplete(false);
                task.setNotificationId(task.getId());
                notificationHelper.setAlarm(task);
                Toast.makeText(getContext(), "اضافه شد.", Toast.LENGTH_SHORT).show();
                task.setGroupId(selectedGroup.getGroupId());

                mCalender.set(Calendar.MINUTE, 00);
                mCalender.set(Calendar.HOUR_OF_DAY, 00);
                mCalender.set(Calendar.SECOND, 00);
                Log.i("TAG", "onViewCreated: " + task);
                task.setDate(mCalender.getTime().toString());
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
        mCalender.set(Calendar.SECOND, 00);
        long check = mCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (check <= 0) {
            binding.dateLayout.setError("لطفا تاریخ را به درستی انتخاب کنید");
        } else {
            binding.dateLayout.setErrorEnabled(false);
            binding.dateAddEt.setText(persianPickerDate.getPersianLongDate());
        }

    }


    @Override
    public void onDismissed() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);

    }
}
