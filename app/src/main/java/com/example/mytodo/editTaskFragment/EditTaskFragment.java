package com.example.mytodo.editTaskFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.FragmentEditTaskBinding;
import com.example.mytodo.main.MainViewModel;
import com.example.mytodo.other.DatePickerProvider;
import com.example.mytodo.other.TimePickerProvider;
import com.example.mytodo.other.Const;
import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.other.ViewModelFactory;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class EditTaskFragment extends Fragment implements PersianPickerListener {
    private Calendar mCalender = Calendar.getInstance();
    private MainViewModel viewModel;
    private Task task, selectedTask;
    private FragmentEditTaskBinding binding;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        notificationHelper = new NotificationHelper(getContext());
        View view = binding.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.editTaskToolbar);
        setHasOptionsMenu(true);

        selectedTask = getArguments().getParcelable("selectedTask");
        PersianDateImpl persianDate = new PersianDateImpl();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(selectedTask.getDateLong());
        persianDate.setDate(selectedTask.getDateLong());
        binding.dateEditEt.setText(persianDate.getPersianLongDate());
        binding.timeEditEt.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        binding.titleEditEt.setText(selectedTask.getTitle());
        binding.desEditEt.setText(selectedTask.getDescription().equals("بدون توضیحات") ? "" : selectedTask.getDescription());
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
        binding.editTaskBtn.setOnClickListener(v -> {
            if (binding.dateEditEt.getText().length() == 0) {
                binding.dateLayout.setError("لطفا تاریخ را وارد کنید");
            } else if (binding.timeEditEt.getText().length() == 0) {
                binding.timeLayout.setError("لطفا ساعت را وارد کنید");

            } else if (binding.titleEditEt.getText().length() == 0) {
                binding.titleLayout.setError("لطفا متن کار خود را وارد کنید");
            } else {
                task.setId(selectedTask.getId());
                task.setTitle(binding.titleEditEt.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setDescription(binding.desEditEt.getText().length() != 0 ? binding.desEditEt.getText().toString() : "بدون توضیحات");

                task.setComplete(false);
                notificationHelper.deleteNotification(task);
                notificationHelper.deleteAlarm(task);
                notificationHelper.setAlarm(task);
                viewModel.updateTask(task);
                Toast.makeText(getContext(), "کار ویرایش شد", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_EditTaskFragment_to_MainListFragmnet);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_back)
            getActivity().onBackPressed();
        else if (item.getItemId() == R.id.menu_frg2_delete) {
            if (selectedTask.getDateLong() > Calendar.getInstance().getTimeInMillis()) {

                notificationHelper.deleteAlarm(selectedTask);
                notificationHelper.deleteNotification(selectedTask);

            }
            viewModel.deleteTask(selectedTask);
            Toast.makeText(getContext(), "کار مورد نظر پاک شد", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
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
            binding.dateEditEt.setText(persianPickerDate.getPersianLongDate());
            binding.dateLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void onDismissed() {

    }
}