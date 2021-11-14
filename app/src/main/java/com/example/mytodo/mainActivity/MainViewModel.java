package com.example.mytodo.mainActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mytodo.model.Group;
import com.example.mytodo.model.GroupDao;
import com.example.mytodo.model.Task;
import com.example.mytodo.model.TaskDao;
import com.example.mytodo.myApp.Const;

import java.util.List;

public class MainViewModel extends ViewModel {

    private TaskDao taskDao;

    private GroupDao groupDao;

    public MainViewModel(TaskDao taskDao, GroupDao groupDao) {
        this.taskDao = taskDao;
        this.groupDao = groupDao;

    }



    //TaskDao Methods////
    public void saveTask(Task task) {
        taskDao.insertTask(task);
    }

    public LiveData<List<Task>> getNotCompleteTasks(int groupId) {

        return taskDao.getNotCompleteTasksList(groupId);

    }


    public LiveData<List<Task>> getNotCompleteTasks() {
        return taskDao.getNotCompleteTasksList();
    }


    public LiveData<List<Task>> getCompleteTasks() {
        return taskDao.getCompleteTasks();
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);

    }

    public void clearTasksHistory() {
        taskDao.clearAllFromHistory();
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public void deleteTasksByGroupId(int groupId) {
        taskDao.deleteTasksByGroupId(groupId);
    }


    public void clearTasksMain(int groupId) {
        taskDao.clearAllFromMain(groupId);
    }

    //search Tasks
    public LiveData<List<Task>> searchTasksHistory(String q) {
        return taskDao.searchTasksHistory(q);
    }

    public LiveData<List<Task>> searchTasksMain(String q, int groupId) {
        return taskDao.searchTasksMain(q, groupId);
    }


    ///////Group Dao Methods////////////

    public LiveData<List<Group>> getAllGroups() {
        return groupDao.getAllGroups();

    }

    public void addGroup(Group group) {
        groupDao.addGroup(group);

    }

    public void deleteGroup(Group group) {
        groupDao.deleteGroup(group);

    }

    public void updateGroup(Group group) {
        groupDao.updateGroup(group);
    }

}