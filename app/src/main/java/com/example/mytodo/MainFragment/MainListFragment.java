package com.example.mytodo.MainFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytodo.Database.Task;
import com.example.mytodo.ListTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.ViewModelFactory;

import java.util.List;

public class MainListFragment extends Fragment implements MainListAdapter.EventListener {
    private static final String TAG = "FirstTabFragment";
    private RecyclerView notDoneList;
    private MainListAdapter adapter;
    private View btn_add;
    private Toolbar toolbar;
    private EditText searchEt;
    private MainFragmentViewModel viewModel;
    private List<Task> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        setHasOptionsMenu(true);
        notDoneList = view.findViewById(R.id.recyclerView_first_fragment);
        searchEt = view.findViewById(R.id.searchEt);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_add = view.findViewById(R.id.btn_add_new_task_main);
        notDoneList = view.findViewById(R.id.recyclerView_first_fragment);

        viewModel = new ViewModelProvider(this
                , new ViewModelFactory(getContext()))
                .get(MainFragmentViewModel.class);

        notDoneList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        viewModel.getNotCompleteTasks().observe(getViewLifecycleOwner(), tasks -> {
            taskList = tasks;
            Log.i(TAG, "onViewCreated: " + tasks);
            adapter = new MainListAdapter(tasks, this);
            notDoneList.setAdapter(adapter);

        });


        btn_add.setOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_firstTabFragment_to_addTaskDialogFragment));

        ListTouchHelper.getTouchHelper(pos -> {
            viewModel.deleteTask(adapter.getTask(pos));

        }).attachToRecyclerView(notDoneList);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if for s that it empty or not
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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


    }

    @Override
    public void ImgClickListener(Task task) {
        task.setComplete(!task.isComplete());
        viewModel.updateTask(task);
    }
}