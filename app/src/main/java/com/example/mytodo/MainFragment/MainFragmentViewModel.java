package com.example.mytodo.MainFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class MainFragmentViewModel extends ViewModel {
    private TaskDao taskDao;
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
        return taskDao.getNotCompleteTasks();
    }
    //search

/*
    //updateTasksStatus
    public void updateAllTasksStatus() {
       List<Task> tasks = taskDao.getNotCompleteTasksList();
        if (tasks.size()>0){
            taskDao.insertAllTasks(MyFunctions.updateTasksStatus(tasks));
            Log.i("TAG", "updateAllTasks: "+tasks);

        }else
            Log.i("TAG", "Null: ");

    }*/

}
