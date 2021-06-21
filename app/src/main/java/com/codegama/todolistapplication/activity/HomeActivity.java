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
    Button btLogout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btAppointment = findViewById(R.id.bt_appointment);
        btDoctor = findViewById(R.id.bt_doctor);
        btStaff = findViewById(R.id.bt_staff);
        btTodolist = findViewById(R.id.bt_todolist);
        btReport = findViewById(R.id.bt_report);
        btLogout = findViewById(R.id.bt_logout);

        btAppointment.setOnClickListener(this::onClick);
        btDoctor.setOnClickListener(this::onClick);
        btStaff.setOnClickListener(this::onClick);
        btTodolist.setOnClickListener(this::onClick);
        btReport.setOnClickListener(this::onClick);
        btLogout.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        int id=v.getId();
        if (id==btAppointment.getId()){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        } else if ( id == btDoctor.getId() ) {
            Intent intent = new Intent(this, DoctorActivity.class);
            startActivity(intent);
        } else if ( id == btStaff.getId() ) {
            Intent intent = new Intent(this, StaffActivity.class);
            startActivity(intent);
        } else if ( id == btTodolist.getId() ) {
            Intent intent = new Intent(this, TodolistActivity.class);
            startActivity(intent);
        } else if ( id == btReport.getId() ) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        } else if ( id == btLogout.getId() ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
