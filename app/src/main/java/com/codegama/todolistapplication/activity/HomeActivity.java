package com.codegama.todolistapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codegama.todolistapplication.R;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btAppointment;
    Button btDoctor;
    Button btStaff;
    Button btTodolist;
    Button btReport;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btAppointment = findViewById(R.id.bt_appointment);
        btDoctor = findViewById(R.id.bt_doctor);
        btStaff = findViewById(R.id.bt_staff);
        btTodolist = findViewById(R.id.bt_todolist);
        btReport = findViewById(R.id.bt_report);

        btAppointment.setOnClickListener(this::onClick);
        btDoctor.setOnClickListener(this::onClick);
        btStaff.setOnClickListener(this::onClick);
        btTodolist.setOnClickListener(this::onClick);
        btReport.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        if ( btAppointment.getText().equals("Appointment") ) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else if ( btDoctor.getText().equals("Doctor") ) {
            Intent intent = new Intent(HomeActivity.this, DoctorActivity.class);
            startActivity(intent);
        }else if ( btStaff.getText().equals("Staff") ) {
            Intent intent = new Intent(HomeActivity.this, StaffActivity.class);
            startActivity(intent);
        }else if ( btTodolist.getText().equals("Todo List") ) {
            Intent intent = new Intent(HomeActivity.this, TodolistActivity.class);
            startActivity(intent);
        } else if ( btReport.getText().equals("Report") ) {
            Intent intent = new Intent(HomeActivity.this, ReportActivity.class);
            startActivity(intent);
        }
    }
}
