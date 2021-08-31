package com.example.mytodo.SecondFragment;

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

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;
import com.example.mytodo.Database.TaskDatabaseProvider;
import com.example.mytodo.R;
import com.example.mytodo.ViewModelFactory;

public class SecondTabFragment extends Fragment implements DoneRecyclerListAdapter.DoneListEventListener {
    private RecyclerView secondRecyclerList;
    private DoneRecyclerListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SecondFragmentViewModel fragmentViewModel = new ViewModelProvider(this
                , new ViewModelFactory(getContext())).get(SecondFragmentViewModel.class);
        secondRecyclerList = view.findViewById(R.id.recyclerView_second_fragment);
        secondRecyclerList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        fragmentViewModel.getTasks().observe(getViewLifecycleOwner(),tasks -> {
            adapter = new DoneRecyclerListAdapter(tasks,this);
            secondRecyclerList.setAdapter(adapter);

        });

    }


    @Override
    public void onImgClickListener(Task selectedTask) {
        //observe for UnTick Task

    }

    @Override
    public void onItemClickListener(Task selectedTask) {
        //observe for Edit Task
    }
}
