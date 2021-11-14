package com.example.mytodo.myApp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.model.GroupDao;
import com.example.mytodo.model.TaskDao;
import com.example.mytodo.model.TaskDatabaseProvider;
import com.example.mytodo.mainActivity.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private TaskDao taskDao;
    private GroupDao groupDao;

    public ViewModelFactory(Context context) {
        this.taskDao = TaskDatabaseProvider.getDatabase(context).getTaskDao();
        this.groupDao = TaskDatabaseProvider.getDatabase(context).getGroupDao();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(taskDao, groupDao);


    }
}
