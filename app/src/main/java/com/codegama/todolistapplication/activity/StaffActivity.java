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
import com.codegama.todolistapplication.adapter.TaskAdapterStaff;
import com.codegama.todolistapplication.bottomSheetFragment.CreateStaffBottomSheetFragment;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.StaffDatabaseClient;
import com.codegama.todolistapplication.model.Staff;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StaffActivity extends AppCompatActivity implements CreateStaffBottomSheetFragment.setRefreshListener {

    Button btBackhome;
    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapterStaff taskAdapterStaff;
    List<Staff> staffs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        btBackhome = findViewById(R.id.bt_backhome);
        btBackhome.setOnClickListener(this::onClick);

        ButterKnife.bind(this);
        setUpAdapterStaff();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);

        addTask.setOnClickListener(view -> {
            CreateStaffBottomSheetFragment createStaffBottomSheetFragment = new CreateStaffBottomSheetFragment();
            createStaffBottomSheetFragment.setStaffId(0, false, this,
                    StaffActivity.this);
            createStaffBottomSheetFragment.show(getSupportFragmentManager(), createStaffBottomSheetFragment.getTag());
        });

        getSavedTasks();

    }

    private void onClick(View v) {
        int id = v.getId();
        if ( id == btBackhome.getId() ) {
            Intent intent = new Intent(StaffActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    public void setUpAdapterStaff() {
        taskAdapterStaff = new TaskAdapterStaff(this, staffs, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapterStaff);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Staff>>{
            @Override
            protected List<Staff> doInBackground(Void... voids) {
                staffs = StaffDatabaseClient
                        .getInstance(getApplicationContext())
                        .getStaffDatabase()
                        .staffDataBaseAction()
                        .getAllTasksList();
                return staffs;
            }

            @Override
            protected void onPostExecute(List<Staff> staffs) {
                super.onPostExecute(staffs);
                setUpAdapterStaff();
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


