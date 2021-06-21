package com.codegama.todolistapplication.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Staff implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int taskId;
    @ColumnInfo(name = "taskName")
    String taskName;
    @ColumnInfo(name = "taskGender")
    String taskGender;
    @ColumnInfo(name = "taskPhone")
    String taskPhone;
    @ColumnInfo(name = "taskDate")
    String taskDate;
    @ColumnInfo(name = "taskWork")
    String taskWork;
    @ColumnInfo(name = "isComplete")
    boolean isComplete;

    public Staff() {

    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGender() {
        return taskGender;
    }

    public void setTaskGender(String taskGender) {
        this.taskGender = taskGender;
    }

    public String getTaskPhone() {
        return taskPhone;
    }

    public void setTaskPhone(String taskPhone) { this.taskPhone = taskPhone; }

    public String getTaskWork() { return taskWork; }

    public void setTaskWork(String taskWork) {
        this.taskWork = taskWork;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) { this.taskDate = taskDate; }

}

