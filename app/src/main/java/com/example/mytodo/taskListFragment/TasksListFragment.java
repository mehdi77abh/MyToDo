package com.example.mytodo.taskListFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytodo.databinding.FragmentTasksListBinding;
import com.example.mytodo.model.Group;
import com.example.mytodo.model.Task;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.groupListFragment.AddGroupDialogFragment;
import com.example.mytodo.mainActivity.MainViewModel;
import com.example.mytodo.myApp.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.myApp.ViewModelFactory;

public class TasksListFragment extends Fragment implements MainTaskAdapter.EventListener {
    private static final String TAG = "TaskListFragment";
    private MainTaskAdapter adapter;
    private MainViewModel viewModel;
    private NotificationHelper notificationHelper;

    private FragmentTasksListBinding binding;
    private Group selectedGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTasksListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        notificationHelper = new NotificationHelper(getContext());
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedGroup = getArguments().getParcelable("group");

        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext()))
                .get(MainViewModel.class);

        //Set Initial Visibility
        binding.dismissTv.setVisibility(View.GONE);
        binding.searchTasksEt.setVisibility(View.GONE);
        binding.emptyStateContainer.setVisibility(View.INVISIBLE);

        binding.titleTasksTv.setText(selectedGroup.getGroupTitle());
        Glide.with(getContext()).load(R.drawable.empty_state).into(binding.emptyStateImg);
        binding.recyclerViewFirstFragment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        viewModel.getNotCompleteTasks(selectedGroup.getGroupId()).observe(getActivity(), tasks -> {
            if (tasks.size() == 0) {
                binding.emptyStateContainer.setVisibility(View.VISIBLE);

            } else
                binding.emptyStateContainer.setVisibility(View.INVISIBLE);

            adapter = new MainTaskAdapter(tasks, this);
            binding.recyclerViewFirstFragment.setAdapter(adapter);

            //Update Group Count
            selectedGroup.setCounter(tasks.size());
            viewModel.updateGroup(selectedGroup);
        });

        binding.imgBack.setOnClickListener(v -> getActivity().onBackPressed());

        binding.searchTasksEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchTasksMain(s.toString(), selectedGroup.getGroupId()).observe(getViewLifecycleOwner(), tasks -> {
                        Log.i(TAG, "Search : " + tasks);
                        adapter = new MainTaskAdapter(tasks, TasksListFragment.this);
                        binding.recyclerViewFirstFragment.setAdapter(adapter);
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnAddNewTaskMain.setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putParcelable("selectedGroup", selectedGroup);
            Navigation.findNavController(v)
                    .navigate(R.id.action_tasksListFragment_to_addTaskFragment, bundle);

        });

        ListTouchHelper.getTouchHelper(getContext(), pos -> {
            notificationHelper.deleteAlarm(adapter.getTask(pos));
            notificationHelper.deleteNotification(adapter.getTask(pos));
            Toast.makeText(getContext(), "کار پاک شد", Toast.LENGTH_SHORT).show();
            viewModel.deleteTask(adapter.getTask(pos));
        }).attachToRecyclerView(binding.recyclerViewFirstFragment);

        binding.imgSearch.setOnClickListener(v -> {
            binding.titleTasksTv.setVisibility(View.GONE);
            binding.imgSearch.setVisibility(View.GONE);
            binding.dismissTv.setVisibility(View.VISIBLE);
            binding.searchTasksEt.setVisibility(View.VISIBLE);
        });
        binding.dismissTv.setOnClickListener(v -> {
            binding.titleTasksTv.setVisibility(View.VISIBLE);
            binding.searchTasksEt.setVisibility(View.GONE);
            binding.dismissTv.setVisibility(View.GONE);
            binding.imgSearch.setVisibility(View.VISIBLE);
            binding.searchTasksEt.setText("");
            AddGroupDialogFragment.hideKeyboardFrom(getContext(), binding.getRoot().getRootView());
        });
        binding.tasksToolbarMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(),binding.tasksToolbarMenu);
            popupMenu.getMenuInflater().inflate(R.menu.main_toolbar_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_deleteAll) {
                    if (binding.emptyStateContainer.getVisibility()==View.INVISIBLE) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("پاک کردن")
                                .setMessage(R.string.delete_all_items_dialog)
                                .setPositiveButton("آره", (dialog, which) -> {
                                    viewModel.clearTasksMain(selectedGroup.getGroupId());
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
            });
            popupMenu.show();
        });
    }
    @Override
    public void ItemClickListener(Task task) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedTask", task);
        Toast.makeText(getContext(), "ویرایش کار", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).navigate(R.id.action_tasksListFragment_to_editTaskFragment, bundle);
    }

    @Override
    public void ImgClickListener(Task task) {
        Toast.makeText(getContext(), "انجام شد", Toast.LENGTH_SHORT).show();
        notificationHelper.deleteNotification(task);
        task.setComplete(!task.isComplete());
        viewModel.updateTask(task);
    }


}