package com.codegama.todolistapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codegama.todolistapplication.model.Doctor;

import java.util.List;

@Dao
public interface DoctorOnDataBaseAction {

    @Query("SELECT * FROM Doctor")
    List<Doctor> getAllTasksList();

    @Query("DELETE FROM Doctor")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Doctor doctor);

    @Query("DELETE FROM Doctor WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Doctor WHERE taskId = :taskId")
    Doctor selectDataFromAnId(int taskId);

    @Query("UPDATE Doctor SET taskName = :taskName, taskGender = :taskGender," +
            " taskDate = :taskDate,  taskPhone = :taskPhone,  taskWork = :taskWork WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskName, String taskGender ,
                             String taskDate, String taskPhone, String taskWork );

}

