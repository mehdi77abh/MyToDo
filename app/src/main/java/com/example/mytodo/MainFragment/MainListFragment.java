package com.example.mytodo.MainFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mytodo.Database.Task;
import com.example.mytodo.alarm.NotificationHelper;
import com.example.mytodo.databinding.MainListFragmentBinding;
import com.example.mytodo.other.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.other.ViewModelFactory;

import java.util.List;

public class MainListFragment extends Fragment implements MainTaskAdapter.EventListener {
    private static final String TAG = "FirstTabFragment";
    private MainTaskAdapter adapter;
    private MainFragmentViewModel viewModel;
    private List<Task> taskList;
    private NotificationHelper notificationHelper;
    private MainListFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = MainListFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        notificationHelper = new NotificationHelper(getContext());
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mainToolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()
                , new ViewModelFactory(getContext()))
                .get(MainFragmentViewModel.class);

        binding.recyclerViewFirstFragment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        viewModel.getNotCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskList = tasks;
            adapter = new MainTaskAdapter(tasks,this);
            binding.recyclerViewFirstFragment.setAdapter(adapter);


        });

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
                        Log.i(TAG, "onViewCreated: " + tasks);
                        adapter = new MainTaskAdapter(tasks,MainListFragment.this);
                        binding.recyclerViewFirstFragment.setAdapter(adapter);

                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnAddNewTaskMain.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_firstTabFragment_to_addTaskDialogFragment));

        ListTouchHelper.getTouchHelper(getContext(), pos -> {
            notificationHelper.deleteAlarm(adapter.getTask(pos));
            notificationHelper.deleteNotification(adapter.getTask(pos));
            Toast.makeText(getContext(), "کار پاک شد", Toast.LENGTH_SHORT).show();
            viewModel.deleteTask(adapter.getTask(pos));

        }).attachToRecyclerView(binding.recyclerViewFirstFragment);


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Toast.makeText(getContext(), "Setting...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_deleteAll:
                if (taskList.size() > 0) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("پاک کردن")

                            .setMessage(R.string.delete_all_items_dialog)
                            .setPositiveButton("آره", (dialog, which) -> {
                                viewModel.clearTasks();
                                Toast.makeText(getContext(), "کل لیست پاک شد", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            })
                            .setNegativeButton("نه", (dialog, which) -> {
                                dialog.dismiss();
                            }).create().show();

                } else
                    Toast.makeText(getContext(), "لیست شما خالی است", Toast.LENGTH_SHORT).show();


                break;
            case R.id.menu_history:
                Navigation.findNavController(getView()).navigate(R.id.action_firstTabFragment_to_secondTabFragment);

                break;


        }
        return true;
    }

    @Override
    public void ItemClickListener(Task task) {
        //Edit And Update task
        //last step
        Log.i(TAG, "ItemClickListener: " + task.getTitle());
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedTask", task);
        Toast.makeText(getContext(), "ویرایش کار", Toast.LENGTH_SHORT).show();

        Navigation.findNavController(getView()).navigate(R.id.action_firstTabFragment_to_editTaskFragment, bundle);


    }

    @Override
    public void ImgClickListener(Task task) {
        Log.i(TAG, "ItemClickListener: " + task.getTitle());

        Toast.makeText(getContext(), "انجام شد", Toast.LENGTH_SHORT).show();
        notificationHelper.deleteNotification(task);
        task.setComplete(!task.isComplete());
        viewModel.updateTask(task);
    }

}