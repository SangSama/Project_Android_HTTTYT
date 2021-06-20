package com.codegama.todolistapplication.database;

import android.content.Context;

import androidx.room.Room;

public class TodolistDatabaseClient {
    private Context mCtx;
    private static TodolistDatabaseClient mInstance;

    //our app database object
    private TodolistDatabase todolistDatabase;

    private TodolistDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        todolistDatabase = Room.databaseBuilder(mCtx, TodolistDatabase.class, "Todolist.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized TodolistDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new TodolistDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public TodolistDatabase getTodolistDatabase() {
        return todolistDatabase;
    }
}

