package com.example.mytodo.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDao {
    @Query("SELECT * FROM group_tbl")
    LiveData<List<Group>> getAllGroups();
    @Insert
    void addGroup(Group group);

    @Delete
    void deleteGroup(Group group);

    @Update
    void updateGroup(Group group);


}
