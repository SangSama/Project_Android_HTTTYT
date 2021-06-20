package com.codegama.todolistapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codegama.todolistapplication.model.Todolist;

import java.util.List;

@Dao
public interface TodolistOnDataBaseAction {

    @Query("SELECT * FROM Todolist")
    List<Todolist> getAllTasksList();

    @Query("DELETE FROM Todolist")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Todolist todolist);

    @Query("DELETE FROM Todolist WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Todolist WHERE taskId = :taskId")
    Todolist selectDataFromAnId(int taskId);

    @Query("UPDATE Todolist SET taskTitle = :taskTitle, taskDescription = :taskDescription, date = :taskDate, " +
            "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskTitle, String taskDescription , String taskDate, String taskTime,
                             String taskEvent);

}
