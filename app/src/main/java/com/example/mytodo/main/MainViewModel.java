package com.example.mytodo.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class MainViewModel extends ViewModel {
    private TaskDao taskDao;

    public MainViewModel(TaskDao taskDao){

        this.taskDao = taskDao;
    }
    public void saveTask(Task task){
        taskDao.insertTask(task);
    }

    public LiveData<List<Task>> getNotCompleteTasks() {
        return taskDao.getNotCompleteTasksList();
    }

    public LiveData<List<Task>> getCompleteTasks(){
        return taskDao.getCompleteTasks();
    }

    public void updateTask(Task task){
        taskDao.updateTask(task);

    }
    public void clearTasksHistory(){
        taskDao.clearAllFromHistory();
    }

    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }


    public void clearTasksMain() {
        taskDao.clearAllFromMain();
    }

    //search
    public LiveData<List<Task>> searchTasksHistory(String q){
        return taskDao.searchTasksHistory(q);
    }
    public LiveData<List<Task>> searchTasksMain(String q){
        return taskDao.searchTasksMain(q);
    }
}
