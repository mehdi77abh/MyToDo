package com.example.mytodo.AddTaskDialog;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import io.reactivex.Single;

public class AddTaskDialogViewModel extends ViewModel {
    private TaskDao dao;

    public AddTaskDialogViewModel(TaskDao dao){

        this.dao = dao;
    }
    public void saveTask(Task task){
        dao.insertTask(task);
    }

}
