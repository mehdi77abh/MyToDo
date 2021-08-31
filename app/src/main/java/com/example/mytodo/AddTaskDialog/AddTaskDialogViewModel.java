package com.example.mytodo.AddTaskDialog;

import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

public class AddTaskDialogViewModel extends ViewModel {
    private TaskDao taskDao ;

    public AddTaskDialogViewModel(TaskDao taskDao){
        this.taskDao = taskDao;

    }
    public void saveTask(Task task){
        taskDao.insertTask(task);
    }

}
