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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytodo.model.Group;
import com.example.mytodo.model.Task;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.FragmentHistoryListBinding;
import com.example.mytodo.groupListFragment.AddGroupDialogFragment;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.myApp.ViewModelFactory;

import java.util.Calendar;
import java.util.List;

public class HistoryListFragment extends Fragment implements HistoryListAdapter.EventListener {
    private HistoryListAdapter adapter;
    private MainViewModel viewModel;
    private FragmentHistoryListBinding binding;
    private NotificationHelper notificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryListBinding.inflate(inflater, container, false);
        notificationHelper = new NotificationHelper(getContext());
        View view = binding.getRoot();
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.historyToolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext())).get(MainViewModel.class);

        binding.recyclerViewSecondFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.historyDismissTv.setVisibility(View.GONE);
        binding.historySearch.setVisibility(View.GONE);
        Glide.with(getContext()).load(R.drawable.empty_box).into(binding.historyEmptyStateImg);
        viewModel.getCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks.size() > 0)
                binding.historyEmptyStateContainer.setVisibility(View.GONE);
            else
                binding.historyEmptyStateContainer.setVisibility(View.VISIBLE);

            adapter = new HistoryListAdapter(tasks, this);
            binding.recyclerViewSecondFragment.setAdapter(adapter);

        });
        ListTouchHelper.getTouchHelper(getContext(), pos -> {
            Task task =adapter.getTask(pos);
            viewModel.deleteTask(task);
            Toast.makeText(getContext(), "پاک شد", Toast.LENGTH_SHORT).show();
        }).attachToRecyclerView(binding.recyclerViewSecondFragment);
        binding.historySearchImg.setOnClickListener(v -> {
            binding.historySearch.setVisibility(View.VISIBLE);
            binding.historySearchImg.setVisibility(View.GONE);
            binding.historyDismissTv.setVisibility(View.VISIBLE);
            binding.historyTitleTv.setVisibility(View.GONE);

        });
        binding.historyDismissTv.setOnClickListener(v -> {
            binding.historySearchImg.setVisibility(View.VISIBLE);
            binding.historyDismissTv.setVisibility(View.GONE);
            binding.historySearch.setVisibility(View.GONE);
            binding.historyTitleTv.setVisibility(View.VISIBLE);
            AddGroupDialogFragment.hideKeyboardFrom(getContext(),binding.getRoot().getRootView());
            binding.historySearch.setText("");

        });
        binding.historySearch.addTextChangedListener(new TextWatcher() {
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
        binding.historyClear.setOnClickListener(v -> {
            if (binding.historyEmptyStateContainer.getVisibility()==View.GONE) {
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

        });
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
        if (task.getDateLong() < Calendar.getInstance().getTimeInMillis()) {
            notificationHelper.deleteAlarm(task);
            notificationHelper.deleteNotification(task);

        }

        task.setComplete(false);
        viewModel.updateTask(task);
        Toast.makeText(getContext(), "به لیست اضافه شد", Toast.LENGTH_SHORT).show();


    }
}
