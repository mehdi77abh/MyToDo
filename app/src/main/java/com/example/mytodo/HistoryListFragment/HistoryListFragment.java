package com.example.mytodo.HistoryListFragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mytodo.Database.Task;
import com.example.mytodo.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.ViewModelFactory;

import java.util.List;

public class HistoryListFragment extends Fragment implements HistoryListAdapter.DoneListEventListener {
    private RecyclerView secondRecyclerList;
    private HistoryListAdapter adapter;
    private HistoryFragmentViewModel viewModel;
    private Toolbar toolbar;
    private List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list_fragment, container, false);
        toolbar = view.findViewById(R.id.history_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this
                , new ViewModelFactory(getContext())).get(HistoryFragmentViewModel.class);
        secondRecyclerList = view.findViewById(R.id.recyclerView_second_fragment);
        secondRecyclerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewModel.getCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            this.taskList = tasks;
            adapter = new HistoryListAdapter(tasks, this);
            secondRecyclerList.setAdapter(adapter);

        });
        ListTouchHelper.getTouchHelper(pos -> {
            viewModel.deleteTask(adapter.getTask(pos));

        }).attachToRecyclerView(secondRecyclerList);

    }


    @Override
    public void onImgClickListener(Task selectedTask) {
        //observe for UnTick Task
        selectedTask.setComplete(false);
        viewModel.updateTask(selectedTask);

    }

    @Override
    public void onItemClickListener(Task selectedTask) {
        //observe for Edit Task
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
}
