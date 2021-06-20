package com.codegama.todolistapplication.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.codegama.todolistapplication.model.Doctor;

@Database(entities = {Doctor.class}, version = 1, exportSchema = false)
public  abstract class DoctorDatabase extends RoomDatabase {

    public abstract DoctorOnDataBaseAction doctorDataBaseAction();
    private static volatile DoctorDatabase doctorDatabase;

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


