package com.codegama.todolistapplication.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.codegama.todolistapplication.model.Todolist;

@Database(entities = {Todolist.class}, version = 3, exportSchema = false)
public  abstract class TodolistDatabase extends RoomDatabase{

    public abstract TodolistOnDataBaseAction todolistDataBaseAction();

    private static volatile TodolistDatabase todolistDatabase;

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
