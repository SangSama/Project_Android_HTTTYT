package com.codegama.todolistapplication.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.codegama.todolistapplication.model.Doctor;
import com.codegama.todolistapplication.model.Staff;

import java.util.List;

@Dao
public interface StaffOnDataBaseAction {

    @Query("SELECT * FROM Staff")
    List<Staff> getAllTasksList();

    @Query("DELETE FROM Staff")
    void truncateTheList();

    @Insert
    void insertDataIntoTaskList(Staff staff);

    @Query("DELETE FROM Staff WHERE taskId = :taskId")
    void deleteTaskFromId(int taskId);

    @Query("SELECT * FROM Staff WHERE taskId = :taskId")
    Staff selectDataFromAnId(int taskId);

    @Query("UPDATE Staff SET taskName = :taskName, taskGender = :taskGender," +
            " taskDate = :taskDate,  taskPhone = :taskPhone,  taskWork = :taskWork WHERE taskId = :taskId")
    void updateAnExistingRow(int taskId, String taskName, String taskGender ,
                             String taskDate, String taskPhone, String taskWork );

}


