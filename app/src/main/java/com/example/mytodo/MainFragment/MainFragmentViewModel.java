package com.example.mytodo.MainFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class MainFragmentViewModel extends ViewModel {
    private TaskDao taskDao;
    private MutableLiveData<List<Task>> searchedTasks = new MutableLiveData<>();
    public MainFragmentViewModel(TaskDao taskDao) {
        this.taskDao = taskDao;

    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);

    }

    public void clearTasks() {
        taskDao.clearAllFromMain();
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public LiveData<List<Task>> getNotCompleteTasks() {
        return taskDao.getNotCompleteTasksList();
    }
    //search
    public LiveData<List<Task>> searchTasksLive(String q){
        return taskDao.searchTasksHistory(q);
    }


}
