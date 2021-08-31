package com.example.mytodo.MainFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class MainFragmentViewModel extends ViewModel {
    private TaskDao taskDao ;

    public MainFragmentViewModel(TaskDao taskDao){
        this.taskDao = taskDao;

    }

    public void updateTask(Task task){
        taskDao.updateTask(task);

    }
    public void clearTasks(){
        taskDao.clearAllFromMain();
    }

    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }
    public LiveData<List<Task>> getNotCompleteTasks(){
        return taskDao.getNotCompleteTasks();
    }
    //search

}
