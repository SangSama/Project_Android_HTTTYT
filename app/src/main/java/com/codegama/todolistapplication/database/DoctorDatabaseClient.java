package com.codegama.todolistapplication.database;

import android.content.Context;

import androidx.room.Room;

public class DoctorDatabaseClient {
    private Context mCtx;
    private static DoctorDatabaseClient mInstance;

    //our app database object
    private DoctorDatabase doctorDatabase;

    private DoctorDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        doctorDatabase = Room.databaseBuilder(mCtx, DoctorDatabase.class, "Doctor.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DoctorDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DoctorDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public DoctorDatabase getDoctorDatabase() {
        return doctorDatabase;
    }
}

