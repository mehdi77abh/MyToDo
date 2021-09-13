package com.example.mytodo.EditTaskFragment;

import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

public class EditTaskViewModel extends ViewModel {
    private TaskDao taskDao;

    public EditTaskViewModel(TaskDao taskDao) {

        this.taskDao = taskDao;
    }


    public void editTask(Task task) {
        taskDao.updateTask(task);
    }
    public void deleteTask(Task task){
        taskDao.deleteTask(task);
    }
}
