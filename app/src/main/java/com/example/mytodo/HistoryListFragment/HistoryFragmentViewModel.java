package com.example.mytodo.HistoryListFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class HistoryFragmentViewModel extends ViewModel {
    private TaskDao taskDao ;

    public HistoryFragmentViewModel(TaskDao taskDao){
        this.taskDao = taskDao;

    }
    public LiveData<List<Task>> getCompleteTasks(){
        return taskDao.getCompleteTasks();
    }

    public void updateTask(Task task){
        taskDao.updateTask(task);

    }
    public void clearAll(){
        taskDao.clearAllFromHistory();
    }

    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }
    public LiveData<List<Task>> searchTasksLive(String q){
        return taskDao.searchTasksMain(q);
    }

    
}
