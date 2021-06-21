package com.codegama.todolistapplication.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.codegama.todolistapplication.model.Staff;

@Database(entities = {Staff.class}, version = 3, exportSchema = false)
public  abstract class StaffDatabase extends RoomDatabase {

    public abstract StaffOnDataBaseAction staffDataBaseAction();
    private static volatile StaffDatabase staffDatabase;

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



