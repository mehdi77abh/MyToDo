package com.example.mytodo;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.Database.TaskDao;
import com.example.mytodo.Database.TaskDatabaseProvider;
import com.example.mytodo.FirstFragment.FirstFragmentViewModel;
import com.example.mytodo.SecondFragment.SecondFragmentViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private TaskDao dao;

    public ViewModelFactory(Context context) {
        this.dao = TaskDatabaseProvider.getDatabase(context).getTaskDao();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FirstFragmentViewModel.class))
            return (T) new FirstFragmentViewModel(dao);
        else if (modelClass.isAssignableFrom(SecondFragmentViewModel.class))
            return (T) new SecondFragmentViewModel(dao);
        else

            throw new IllegalArgumentException("ViewModel Is Not valid ");

    }
}
