package com.codegama.todolistapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codegama.todolistapplication.model.Report;

import java.util.List;

@Dao
public interface ReportOnDataBaseAction {

    @Query("SELECT * FROM Report")
    List<Report> getAllTasksList();

    @Query("DELETE FROM Report")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Report report);

    @Query("DELETE FROM Report WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Report WHERE taskId = :taskId")
    Report selectDataFromAnId(int taskId);

    @Query("UPDATE Report SET taskTitle = :taskTitle, taskDescription = :taskDescription, date = :taskDate, " +
            "lastAlarm = :taskTime, event = :taskEvent WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskTitle, String taskDescription , String taskDate, String taskTime,
                             String taskEvent);

}

