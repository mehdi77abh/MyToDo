package com.example.mytodo.FirstFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class FirstFragmentViewModel extends ViewModel {
    private MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private TaskDao taskDao ;

    public FirstFragmentViewModel(TaskDao taskDao){
        this.taskDao = taskDao;

    }
    public LiveData<List<Task>> getTasks() {
        return taskDao.getNotCompleteTasks();
    }

}
