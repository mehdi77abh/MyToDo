package com.example.mytodo.other;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.Database.GroupDao;
import com.example.mytodo.Database.TaskDao;
import com.example.mytodo.Database.TaskDatabaseProvider;
import com.example.mytodo.main.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private TaskDao taskDao;
    private GroupDao groupDao;
    public ViewModelFactory(Context context) {
        this.taskDao = TaskDatabaseProvider.getDatabase(context).getTaskDao();
        this.groupDao =TaskDatabaseProvider.getDatabase(context).getGroupDao();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new MainViewModel(taskDao,groupDao);


    }
}
