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
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("SELECT * FROM tbl_tasks")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM tbl_tasks WHERE title LIKE :q")
    LiveData<List<Task>> searchTasks(String q);
    //TODO Check Search After

    @Query("DELETE FROM tbl_tasks Where is_complete =0")
    void clearAllFromMain();

    @Query("DELETE FROM tbl_tasks Where is_complete =1")
    void clearAllFromHistory();

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0")
    LiveData<List<Task>> getNotCompleteTasks();

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1")
    LiveData<List<Task>> getCompleteTasks();


}
