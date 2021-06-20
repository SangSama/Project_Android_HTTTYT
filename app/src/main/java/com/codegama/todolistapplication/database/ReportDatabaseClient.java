package com.codegama.todolistapplication.database;

import android.content.Context;

import androidx.room.Room;

public class ReportDatabaseClient {
    private Context mCtx;
    private static ReportDatabaseClient mInstance;

    //our app database object
    private ReportDatabase reportDatabase;

    private ReportDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        reportDatabase = Room.databaseBuilder(mCtx, ReportDatabase.class, "Report.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized ReportDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new ReportDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public ReportDatabase getReportDatabase() {
        return reportDatabase;
    }
}
