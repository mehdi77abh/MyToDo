package com.example.mytodo.model;

import androidx.lifecycle.LiveData;
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

    //Clear Tasks From Main
    @Query("DELETE FROM tbl_tasks Where is_complete =0 AND groupId = :groupId")
    void clearAllFromMain(int groupId);

    //Clear Tasks From History
    @Query("DELETE FROM tbl_tasks Where is_complete =1")
    void clearAllFromHistory();

    //Search For Main Tasks
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0 AND groupId = :groupId AND title LIKE '%' || :q || '%' ORDER BY dateLong")
    LiveData<List<Task>> searchTasksMain(String q,int groupId);

    //Search For History Tasks
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1 AND title LIKE '%' || :q || '%' ORDER BY dateLong")
    LiveData<List<Task>> searchTasksHistory(String q);


    //get List For History
    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 1 ORDER BY dateLong")
    LiveData<List<Task>> getCompleteTasks();

    //get List For Main List
    @Query("SELECT * FROM tbl_tasks WHERE groupId = :groupId AND is_complete = 0  ORDER BY dateLong")
    LiveData<List<Task>> getNotCompleteTasksList(int groupId);

    @Query("SELECT * FROM tbl_tasks WHERE is_complete = 0 ORDER BY dateLong")
    LiveData<List<Task>> getNotCompleteTasksList();

    @Query("DELETE  FROM tbl_tasks WHERE groupId = :groupId")
    void deleteTasksByGroupId(int groupId);

}
