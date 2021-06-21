package com.codegama.todolistapplication.database;

import android.content.Context;

import androidx.room.Room;

public class StaffDatabaseClient {
    private Context mCtx;
    private static StaffDatabaseClient mInstance;

    //our app database object
    private StaffDatabase staffDatabase;

    private StaffDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        staffDatabase = Room.databaseBuilder(mCtx, StaffDatabase.class, "Staff.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized StaffDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new StaffDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public StaffDatabase getStaffDatabase() {
        return staffDatabase;
    }
}


