package com.example.mytodo.FirstFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodo.AddTaskDialog.AddTaskDialogFragment;
import com.example.mytodo.Database.TaskDao;
import com.example.mytodo.Database.TaskDatabaseProvider;
import com.example.mytodo.R;
import com.example.mytodo.ViewModelFactory;

public class FirstTabFragment extends Fragment  {
    private static final String TAG = "FirstTabFragment";
    private TaskDao taskDao;
    private RecyclerView notDoneList;
    private DoRecyclerListAdapter adapter;
    private View btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
        taskDao = TaskDatabaseProvider.getDatabase(getContext()).getTaskDao();
        notDoneList = view.findViewById(R.id.recyclerView_first_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_add = view.findViewById(R.id.btn_add_new_task_main);
        notDoneList = view.findViewById(R.id.recyclerView_first_fragment);

        FirstFragmentViewModel viewModel = new ViewModelProvider(this,new ViewModelFactory(getContext())).get(FirstFragmentViewModel.class);
        notDoneList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        viewModel.getTasks().observe(getViewLifecycleOwner(),tasks -> {
            adapter = new DoRecyclerListAdapter(tasks);
            notDoneList.setAdapter(adapter);

        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskDialogFragment taskDialogFragment = new AddTaskDialogFragment();
                taskDialogFragment.show(getChildFragmentManager(), null);

            }
        });
    }







}