package com.example.mytodo.historyListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.bumptech.glide.Glide;
import com.example.mytodo.Database.Task;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.FragmentHistoryListBinding;
import com.example.mytodo.main.MainViewModel;
import com.example.mytodo.other.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.other.ViewModelFactory;

import java.util.Calendar;
import java.util.List;

public class HistoryListFragment extends Fragment implements HistoryListAdapter.EventListener {
    private HistoryListAdapter adapter;
    private MainViewModel viewModel;
    private List<Task> taskList;
    private FragmentHistoryListBinding binding;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryListBinding.inflate(inflater, container, false);
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
                , new ViewModelFactory(getContext())).get(MainViewModel.class);
        binding.recyclerViewSecondFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Glide.with(getContext()).load(R.drawable.empty_box).into(binding.historyEmptyStateImg);
        viewModel.getCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            this.taskList = tasks;
            if (tasks.size() > 0)
                binding.historyEmptyStateContainer.setVisibility(View.GONE);
            else
                binding.historyEmptyStateContainer.setVisibility(View.VISIBLE);
            adapter = new HistoryListAdapter(tasks, this);
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
                    viewModel.searchTasksHistory(s.toString()).observe(getViewLifecycleOwner(), tasks -> {
                        Log.i("TAG", "onViewCreated: " + tasks);
                        adapter = new HistoryListAdapter(tasks, HistoryListFragment.this);
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
                            viewModel.clearTasksHistory();
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
        Navigation.findNavController(getView()).navigate(R.id.action_historyListFragment_to_editTaskFragment, bundle);

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
