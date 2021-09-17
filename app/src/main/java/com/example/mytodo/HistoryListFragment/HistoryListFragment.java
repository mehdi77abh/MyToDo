package com.example.mytodo.HistoryListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytodo.Database.Task;
import com.example.mytodo.MainFragment.MainListFragment;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.HistoryListFragmentBinding;
import com.example.mytodo.other.ListTouchHelper;
import com.example.mytodo.MainFragment.TaskListAdapter;
import com.example.mytodo.R;
import com.example.mytodo.other.ViewModelFactory;

import java.util.Calendar;
import java.util.List;

import ir.hamsaa.persiandatepicker.date.PersianDateImpl;
import ir.hamsaa.persiandatepicker.util.PersianHelper;

public class HistoryListFragment extends Fragment implements TaskListAdapter.EventListener {
    private TaskListAdapter adapter;
    private HistoryFragmentViewModel viewModel;
    private List<Task> taskList;
    private HistoryListFragmentBinding binding;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HistoryListFragmentBinding.inflate(inflater, container, false);
        notificationHelper = new NotificationHelper(getContext());
        View view = binding.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.historyToolbar);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext())).get(HistoryFragmentViewModel.class);
        binding.recyclerViewSecondFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewModel.getCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            this.taskList = tasks;
            adapter = new TaskListAdapter(tasks, false, this);
            binding.recyclerViewSecondFragment.setAdapter(adapter);

        });
        ListTouchHelper.getTouchHelper(getContext(), pos -> {
            viewModel.deleteTask(adapter.getTask(pos));

        }).attachToRecyclerView(binding.recyclerViewSecondFragment);

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null)
                    return;
                else {
                    viewModel.searchTasksLive(s.toString()).observe(getViewLifecycleOwner(), tasks -> {
                        Log.i("TAG", "onViewCreated: " + tasks);
                        adapter = new TaskListAdapter(tasks, true, HistoryListFragment.this);
                        binding.recyclerViewSecondFragment.setAdapter(adapter);

                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_toolbar_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_back)
            getActivity().onBackPressed();
        else if (item.getItemId() == R.id.menu_frg2_delete) {
            if (taskList.size() > 0) {
                new AlertDialog.Builder(getContext())
                        .setTitle("پاک کردن")

                        .setMessage(R.string.delete_all_items_dialog)
                        .setPositiveButton("آره", (dialog, which) -> {
                            viewModel.clearAll();
                            Toast.makeText(getContext(), "کل لیست پاک شد", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setNegativeButton("نه", (dialog, which) -> {
                            dialog.dismiss();
                        }).create().show();

            } else
                Toast.makeText(getContext(), "لیست شما خالی است", Toast.LENGTH_SHORT).show();


        }
        return true;
    }

    @Override
    public void ItemClickListener(Task task) {
        Toast.makeText(getContext(), "رفتن به ویرایش", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedTask", task);
        Navigation.findNavController(getView()).navigate(R.id.action_HistoryListFragment_to_EditTaskFragment, bundle);

    }

    @Override
    public void ImgClickListener(Task task) {
        if (task.getDateLong() > Calendar.getInstance().getTimeInMillis()) {

            notificationHelper.deleteAlarm(task);
            notificationHelper.deleteNotification(task);

        }
        task.setComplete(false);
        viewModel.updateTask(task);
        Toast.makeText(getContext(), "به لیست اضافه شد", Toast.LENGTH_SHORT).show();


    }
}
