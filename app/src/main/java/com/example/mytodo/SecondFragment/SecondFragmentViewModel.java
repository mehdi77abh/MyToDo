package com.example.mytodo.SecondFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class SecondFragmentViewModel extends ViewModel {
    private MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private TaskDao taskDao;
    public SecondFragmentViewModel(TaskDao taskDao){

        this.taskDao = taskDao;
    }

    public LiveData<List<Task>> getTasks() {
        return taskDao.getCompleteTasks();
    }


    
}
