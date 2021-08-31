package com.example.mytodo.Database;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao()
public interface TaskDao {
    @Insert
    long insertTask(Task task);

    @Delete
    int deleteTask(Task task);

    @Update
    int updateTask(Task task);

    @Query("SELECT * FROM tbl_tasks")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE :q")
    List<Task> searchTasks(String q);
    //TODO Check Search After

    @Query("DELETE FROM tbl_tasks")
    void clearAll();

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0")
    LiveData<List<Task>> getNotCompleteTasks();

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1")
    LiveData<List<Task>> getCompleteTasks();


}
