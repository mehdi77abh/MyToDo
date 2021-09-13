package com.example.mytodo.EditTaskFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mytodo.other.DatePickerProvider;
import com.example.mytodo.other.TimePickerProvider;
import com.example.mytodo.other.Const;
import com.example.mytodo.Database.Task;
import com.example.mytodo.R;
import com.example.mytodo.other.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.hamsaa.persiandatepicker.date.PersianDateImpl;

public class EditTaskFragment extends Fragment implements PersianPickerListener {
    private TextInputEditText title, date, des, time;
    private TextInputLayout titleLayout,dateLayout,desLayout,timeLayout;
    private RadioGroup importanceRg;
    private Task selectedTask;
    private Calendar mCalender= Calendar.getInstance();
    private EditTaskViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
        Toolbar toolbar = view.findViewById(R.id.editTaskToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        selectedTask = getArguments().getParcelable("selectedTask");
        time = view.findViewById(R.id.timeEditEt);
        date = view.findViewById(R.id.dateEditEt);
        title = view.findViewById(R.id.titleEditEt);
        des = view.findViewById(R.id.desEditEt);
        titleLayout = view.findViewById(R.id.title_layout);
        dateLayout = view.findViewById(R.id.date_layout);
        timeLayout = view.findViewById(R.id.time_layout);
        importanceRg = view.findViewById(R.id.importance_rg);
        PersianDateImpl persianDate = new PersianDateImpl();
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTimeInMillis(selectedTask.getDateLong());
        persianDate.setDate(selectedTask.getDateLong());
        date.setText(persianDate.getPersianLongDate());
        time.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        title.setText(selectedTask.getTitle());
        des.setText(selectedTask.getDescription().equals("بدون توضیحات") ? "" : selectedTask.getDescription());
        switch (selectedTask.getImportance()) {
            case Const.IMPORTANCE_HIGH:
                importanceRg.check(R.id.importanceHigh);

                break;
            case Const.IMPORTANCE_NORMAL:
                importanceRg.check(R.id.importanceNormal);

                break;
            case Const.IMPORTANCE_LOW:
                importanceRg.check(R.id.importanceLow);

                break;
            default:
                importanceRg.check(R.id.importanceNormal);


        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         viewModel = new ViewModelProvider(requireActivity()
                ,new ViewModelFactory(getContext())).get(EditTaskViewModel.class);

        time.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = TimePickerProvider.getTimePicker();
            materialTimePicker.show(getParentFragmentManager(),null);
            materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
                mCalender.set(Calendar.HOUR_OF_DAY,materialTimePicker.getHour());
                mCalender.set(Calendar.MINUTE,materialTimePicker.getMinute());
                time.setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute());

            });
            materialTimePicker.addOnNegativeButtonClickListener(v1 -> materialTimePicker.dismiss());
        });
        date.setOnClickListener(v -> {
            DatePickerProvider.getDatePicker(getContext()).setListener(this).show();
        });

        view.findViewById(R.id.edit_task_btn).setOnClickListener(v -> {
            if (date.getText().length() == 0) {
                dateLayout.setError("لطفا تاریخ را وارد کنید");
            } else if (time.getText().length() == 0) {
                timeLayout.setError("لطفا ساعت را وارد کنید");

            } else if (title.getText().length() == 0) {
                titleLayout.setError("لطفا متن کار خود را وارد کنید");
            } else {
                Task task = new Task();
                task.setId(selectedTask.getId());
                task.setTitle(title.getText().toString());
                task.setDateLong(mCalender.getTimeInMillis());
                task.setDescription(des.getText().length() != 0 ? des.getText().toString() : "بدون توضیحات");
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
                viewModel.editTask(task);
                Toast.makeText(getContext(), "کار ویرایش شد و به لیست اصلی اضاف شد", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigate(R.id.action_EditTaskFragment_to_MainListFragmnet);
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.history_toolbar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_back)
            getActivity().onBackPressed();
        else if(item.getItemId() == R.id.menu_frg2_delete){
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
        mCalender.set(Calendar.MINUTE,Calendar.getInstance().get(Calendar.MINUTE)+1);
        mCalender.set(Calendar.HOUR_OF_DAY,Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        long check = mCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (check <= 0) {
            dateLayout.setError("لطفا تاریخ را به درستی انتخاب کنید");
        } else {
            date.setText(persianPickerDate.getPersianLongDate());
            dateLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void onDismissed() {

    }
}