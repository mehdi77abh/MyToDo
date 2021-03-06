package com.example.mytodo.taskListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.FragmentEditTaskBinding;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.model.Group;
import com.example.mytodo.myApp.DatePickerProvider;
import com.example.mytodo.myApp.TimePickerProvider;
import com.example.mytodo.myApp.Const;
import com.example.mytodo.model.Task;
import com.example.mytodo.R;
import com.example.mytodo.myApp.ViewModelFactory;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class EditTaskFragment extends Fragment implements PersianPickerListener {
    private Calendar mCalender ;
    private MainViewModel viewModel;
    private Task task, selectedTask;
    private FragmentEditTaskBinding binding;
    private NotificationHelper notificationHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        notificationHelper = new NotificationHelper(getContext());
        mCalender = Calendar.getInstance();
        View view = binding.getRoot();

        selectedTask = getArguments().getParcelable("selectedTask");
        PersianDateImpl persianDate = new PersianDateImpl();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedTask.getDateLong());
        persianDate.setDate(selectedTask.getDateLong());
        binding.dateEditEt.setText(persianDate.getPersianLongDate());
        binding.timeEditEt.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        binding.titleEditEt.setText(selectedTask.getTitle());
        binding.desEditEt.setText(selectedTask.getDescription().equals("???????? ??????????????") ? "" : selectedTask.getDescription());
        switch (selectedTask.getImportance()) {
            case Const.IMPORTANCE_HIGH:
                binding.importanceRg.check(R.id.edit_importance_high);
                break;
            case Const.IMPORTANCE_NORMAL:
                binding.importanceRg.check(R.id.edit_importance_normal);

                break;
            case Const.IMPORTANCE_LOW:
                binding.importanceRg.check(R.id.edit_importance_low);

                break;
            default:
                binding.importanceRg.check(R.id.edit_importance_normal);


        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext())).get(MainViewModel.class);
        task = new Task();
        binding.timeEditEt.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = TimePickerProvider.getTimePicker();
            materialTimePicker.show(getParentFragmentManager(), null);

            materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
                mCalender.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                mCalender.set(Calendar.MINUTE, materialTimePicker.getMinute());
                mCalender.set(Calendar.SECOND, 00);
                binding.timeEditEt.setText(materialTimePicker.getHour() + ":" + materialTimePicker.getMinute());

            });
            materialTimePicker.addOnNegativeButtonClickListener(v1 -> materialTimePicker.dismiss());
        });
        binding.dateEditEt.setOnClickListener(v -> {
            DatePickerProvider.getDatePicker(getContext()).setListener(this).show();
        });
        binding.importanceRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.edit_importance_high:
                    task.setImportance(Const.IMPORTANCE_HIGH);
                    break;
                case R.id.edit_importance_normal:
                    task.setImportance(Const.IMPORTANCE_NORMAL);

                    break;
                case R.id.edit_importance_low:
                    task.setImportance(Const.IMPORTANCE_LOW);
                    break;
                default:
                    task.setImportance(Const.IMPORTANCE_NORMAL);

            }
        });
        binding.imgEditBack.setOnClickListener(v -> getActivity().onBackPressed());
        binding.imgEditDelete.setOnClickListener(v -> {
            if (selectedTask.getDateLong() > Calendar.getInstance().getTimeInMillis()) {
                notificationHelper.deleteAlarm(selectedTask);
                notificationHelper.deleteNotification(selectedTask);

            }

            viewModel.deleteTask(selectedTask);
            Toast.makeText(getContext(), "?????? ???????? ?????? ?????? ????", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        });

        binding.editTaskBtn.setOnClickListener(v -> {
            if (binding.dateEditEt.getText().length() == 0) {
                binding.dateLayout.setError("???????? ?????????? ???? ???????? ????????");
            } else if (binding.timeEditEt.getText().length() == 0) {
                binding.timeLayout.setError("???????? ???????? ???? ???????? ????????");

            } else if (binding.titleEditEt.getText().length() == 0) {
                binding.titleLayout.setError("???????? ?????? ?????? ?????? ???? ???????? ????????");
            } else {
                
                task.setId(selectedTask.getId());
                task.setTitle(binding.titleEditEt.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setGroupId(selectedTask.getGroupId());
                task.setDescription(binding.desEditEt.getText().length() != 0 ? binding.desEditEt.getText().toString() : "???????? ??????????????");
                mCalender.set(Calendar.MINUTE,00);
                mCalender.set(Calendar.HOUR_OF_DAY,00);
                mCalender.set(Calendar.SECOND,00);
                task.setDate(mCalender.getTime().toString());
                task.setComplete(false);
                notificationHelper.deleteNotification(selectedTask);
                notificationHelper.deleteAlarm(selectedTask);
                notificationHelper.setAlarm(task);
                viewModel.updateTask(task);
                Toast.makeText(getContext(), "?????? ???????????? ????", Toast.LENGTH_SHORT).show();
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
            binding.dateLayout.setError("???????? ?????????? ???? ???? ?????????? ???????????? ????????");
        } else {
            binding.dateEditEt.setText(persianPickerDate.getPersianLongDate());
            binding.dateLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void onDismissed() {

    }
}