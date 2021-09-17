package com.example.mytodo.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao()
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTasks(List<Task> tasks);

    @Delete
    void deleteTask(Task task);

    @Update
    void updateTask(Task task);


    @Query("DELETE FROM tbl_tasks Where is_complete =0")
    void clearAllFromMain();

    @Query("DELETE FROM tbl_tasks Where is_complete =1")
    void clearAllFromHistory();

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0 AND title LIKE '%' || :q || '%' ORDER BY dateLong")
    LiveData<List<Task>> searchTasksMain(String q);

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1 AND title LIKE '%' || :q || '%' ORDER BY dateLong")
    LiveData<List<Task>> searchTasksHistory(String q);

    //TODO Check Search After

    //get List For Main List
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0 ORDER BY dateLong")
    LiveData<List<Task>> getNotCompleteTasks();

    //get List For History
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1")
    LiveData<List<Task>> getCompleteTasks();

    //get List For Status Check
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0")
    LiveData<List<Task>> getNotCompleteTasksList();


}
