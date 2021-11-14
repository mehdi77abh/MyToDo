package com.example.mytodo.calendarFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mytodo.model.Task;
import com.example.mytodo.R;
import com.example.mytodo.databinding.FragmentCalenderBinding;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.taskListFragment.MainTaskAdapter;
import com.example.mytodo.myApp.ViewModelFactory;
import com.mohamadian.persianhorizontalexpcalendar.PersianHorizontalExpCalendar;
import com.mohamadian.persianhorizontalexpcalendar.enums.PersianCustomMarks;
import com.mohamadian.persianhorizontalexpcalendar.enums.PersianViewPagerType;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.PersianChronologyKhayyam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class CalendarFragment extends Fragment implements MainTaskAdapter.EventListener {
    private static final String TAG = "CalendarFragment";
    private FragmentCalenderBinding binding;
    private MainViewModel mainViewModel;
    private MainTaskAdapter adapter;
    private Calendar calendar = Calendar.getInstance();
    private Chronology perChr = PersianChronologyKhayyam.getInstance(DateTimeZone.forID("Asia/Tehran"));
    private DateTime nowTime;
    private PersianDateImpl persianDate;
    private DateTime changedTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalenderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getContext())).get(MainViewModel.class);
        binding.emptyStateContainer.setVisibility(View.GONE);
        binding.listOnDay.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        persianDate = new PersianDateImpl();
        Glide.with(getContext()).load(R.drawable.empty_state).into(binding.emptyStateImg);
        nowTime = new DateTime(perChr);
        changedTime = new DateTime();
        setEvents();
        setList(nowTime);
        String yearName = PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianYear()));

        binding.txtMonth.setText(persianDate.getPersianMonthName() + " " + yearName);

        binding.todayTv.setText(PersianHelper.toPersianNumber(String.valueOf(nowTime.getDayOfMonth())));
        binding.todayTv.setOnClickListener(v -> scrollToToday());

        binding.persianCalendar.setPersianHorizontalExpCalListener(new PersianHorizontalExpCalendar.PersianHorizontalExpCalListener() {
            @Override
            public void onCalendarScroll(DateTime dateTime) {
                Log.i(TAG, "onCalendarScroll: "+dateTime.toDate().toString());
                changedTime = dateTime;
                persianDate.setDate(dateTime.toDate());
                String yearName = PersianHelper.toPersianNumber(String.valueOf(persianDate.getPersianYear()));
                binding.txtMonth.setText(persianDate.getPersianMonthName() + " " + yearName);

            }

            @Override
            public void onDateSelected(DateTime dateTime) {
                calendar.setTime(dateTime.toDate());
                setList(dateTime);
            }

            @Override
            public void onChangeViewPager(PersianViewPagerType persianViewPagerType) {
                Log.i(TAG, "onChangeViewPager: "+persianViewPagerType.name());
            }
        });
        binding.addTaskBtn.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            SelectGroupDialog selectGroupDialog = new SelectGroupDialog();
            this.calendar.set(Calendar.MINUTE, 00);
            this.calendar.set(Calendar.HOUR_OF_DAY, 00);
            this.calendar.set(Calendar.SECOND, 00);
            Calendar newCal = Calendar.getInstance();
            newCal.set(Calendar.MINUTE,00);
            newCal.set(Calendar.HOUR_OF_DAY,00);
            newCal.set(Calendar.SECOND,00);
            long check = calendar.getTimeInMillis() - newCal.getTimeInMillis();
            if (check<=0){
                Toast.makeText(getContext(), "روز را درست انتخاب کنید", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "انتخاب گروه", Toast.LENGTH_SHORT).show();
                bundle.putLong("selectedLongDate", this.calendar.getTimeInMillis());
                selectGroupDialog.setArguments(bundle);
                selectGroupDialog.show(getParentFragmentManager(), null);

            }

          });
        binding.imgNext.setOnClickListener(v -> {
            scrollToPervMonth();
        });
        binding.imgPrv.setOnClickListener(v -> {
            scrollToNextMonth();
        });
    }

    private Calendar getPersianCal(DateTime date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.SECOND, 00);
        return calendar;

    }

    private void setList(DateTime selectedDate) {
        Calendar calendar = getPersianCal(selectedDate);
        mainViewModel.getNotCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            List<Task> taskList = new ArrayList<>();
            for (Task task : tasks) {
                if (task.getDate().equals(calendar.getTime().toString())) {
                    taskList.add(task);
                }

            }

            if (taskList.size() == 0) {
                binding.emptyStateContainer.setVisibility(View.VISIBLE);
            } else
                binding.emptyStateContainer.setVisibility(View.GONE);

            adapter = new MainTaskAdapter(taskList, CalendarFragment.this);
            binding.listOnDay.setAdapter(adapter);


        });


    }

    private void setEvents() {

        mainViewModel.getNotCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            PersianDateImpl persianDate = new PersianDateImpl();
            for (Task task : tasks) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(task.getDateLong());
                persianDate.setDate(task.getDateLong());

                DateTime dateTime = new DateTime(persianDate.getPersianYear()
                        ,persianDate.getPersianMonth(),persianDate.getPersianDay(),0,0);
                binding.persianCalendar.markDate(dateTime, PersianCustomMarks.SmallOval_Bottom, Color.RED);
            }
        });
    }

    @Override
    public void ItemClickListener(Task task) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedTask", task);
        Toast.makeText(getContext(), "ویرایش کار", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(getView()).navigate(R.id.action_calenderFragment_to_editTaskFragment2, bundle);

    }

    @Override
    public void ImgClickListener(Task task) {
        task.setComplete(!task.isComplete());
        mainViewModel.updateTask(task);

    }

    public void scrollToToday() {
        binding.persianCalendar
                .scrollToDate(nowTime);
    }

    public void scrollToNextMonth() {
        binding.persianCalendar
                .scrollToDate(changedTime.plusMonths(1).plusDays(0));
    }

    public void scrollToPervMonth() {
        binding.persianCalendar.scrollToDate(changedTime.minusMonths(1).minusDays(0));
    }
}