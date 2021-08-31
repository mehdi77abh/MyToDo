package com.example.mytodo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.AddTaskDialog.AddTaskDialogViewModel;
import com.example.mytodo.Database.TaskDao;
import com.example.mytodo.Database.TaskDatabaseProvider;
import com.example.mytodo.MainFragment.MainFragmentViewModel;
import com.example.mytodo.HistoryListFragment.HistoryFragmentViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private TaskDao taskDao;

    public ViewModelFactory(Context context)
    {
        this.taskDao = TaskDatabaseProvider.getDatabase(context).getTaskDao();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainFragmentViewModel.class))
            return (T) new MainFragmentViewModel(taskDao);
        else if (modelClass.isAssignableFrom(HistoryFragmentViewModel.class))
            return (T) new HistoryFragmentViewModel(taskDao);
        else if (modelClass.isAssignableFrom(AddTaskDialogViewModel.class))
            return (T) new AddTaskDialogViewModel(taskDao);


            throw new IllegalArgumentException("ViewModel Is Not valid ");

    }
}
