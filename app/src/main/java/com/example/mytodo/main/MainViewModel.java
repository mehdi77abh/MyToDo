package com.example.mytodo.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.Database.Group;
import com.example.mytodo.Database.GroupDao;
import com.example.mytodo.Database.Task;
import com.example.mytodo.Database.TaskDao;

import java.util.List;

public class MainViewModel extends ViewModel {

    private TaskDao taskDao;
    private GroupDao groupDao;

    public MainViewModel(TaskDao taskDao, GroupDao groupDao){

        this.taskDao = taskDao;
        this.groupDao = groupDao;
    }
    public void saveTask(Task task){
        taskDao.insertTask(task);
    }

    public LiveData<List<Task>> getNotCompleteTasks(int groupId) {
        return taskDao.getNotCompleteTasksList(groupId);
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

    ///////Group Dao Methods////////////

    public LiveData<List<Group>> getAllGroups(){
        return groupDao.getAllGroups();

    }

    public void addGroup(Group group){
        groupDao.addGroup(group);
    }
    public void deleteGroup(Group group){
        groupDao.deleteGroup(group);

    }
    public void updateGroup(Group group){
        groupDao.updateGroup(group);
    }
}
