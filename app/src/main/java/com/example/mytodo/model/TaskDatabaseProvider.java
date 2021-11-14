package com.example.mytodo.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class,Group.class}, version = 1,exportSchema = false)
public abstract class TaskDatabaseProvider extends RoomDatabase {
    private static TaskDatabaseProvider database;

    public static TaskDatabaseProvider getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, TaskDatabaseProvider.class, "myTasks")
                    .allowMainThreadQueries()
                    .build();
        }
            return database;
    }

    public abstract TaskDao getTaskDao();
    public abstract GroupDao getGroupDao();

}
