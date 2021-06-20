package com.codegama.todolistapplication.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codegama.todolistapplication.R;
import com.codegama.todolistapplication.adapter.TaskAdapterDoctor;
import com.codegama.todolistapplication.bottomSheetFragment.CreateDoctorBottomSheetFragment;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.DoctorDatabaseClient;
import com.codegama.todolistapplication.model.Doctor;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoctorActivity extends AppCompatActivity implements CreateDoctorBottomSheetFragment.setRefreshListener {

    Button btBackhome;
    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapterDoctor taskAdapterDoctor;
    List<Doctor> doctors = new ArrayList<>();
//    @BindView(R.id.calendar)
//    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        btBackhome = findViewById(R.id.bt_backhome);
        btBackhome.setOnClickListener(this::onClick);

        ButterKnife.bind(this);
        setUpAdapterDoctor();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        addTask.setOnClickListener(view -> {
            CreateDoctorBottomSheetFragment createDoctorBottomSheetFragment = new CreateDoctorBottomSheetFragment();
            createDoctorBottomSheetFragment.setDoctorId(0, false, this,
                    DoctorActivity.this);
            createDoctorBottomSheetFragment.show(getSupportFragmentManager(), createDoctorBottomSheetFragment.getTag());
        });

        getSavedTasks();

//        calendar.setOnClickListener(view -> {
//            TodolistShowCalendarViewBottomSheet todolistShowCalendarViewBottomSheet = new TodolistShowCalendarViewBottomSheet();
//            todolistShowCalendarViewBottomSheet.show(getSupportFragmentManager(), todolistShowCalendarViewBottomSheet.getTag());
//        });
    }

    private void onClick(View v) {
        int id = v.getId();
        if ( id == btBackhome.getId() ) {
            Intent intent = new Intent(DoctorActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    public void setUpAdapterDoctor() {
        taskAdapterDoctor = new TaskAdapterDoctor(this, doctors, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapterDoctor);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Doctor>>{
            @Override
            protected List<Doctor> doInBackground(Void... voids) {
                doctors = DoctorDatabaseClient
                        .getInstance(getApplicationContext())
                        .getDoctorDatabase()
                        .doctorDataBaseAction()
                        .getAllTasksList();
                return doctors;
            }

            @Override
            protected void onPostExecute(List<Doctor> doctors) {
                super.onPostExecute(doctors);
                setUpAdapterDoctor();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }
}

