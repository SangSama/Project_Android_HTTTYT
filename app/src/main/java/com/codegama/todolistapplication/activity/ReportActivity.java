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
import com.codegama.todolistapplication.adapter.TaskAdapterReport;
import com.codegama.todolistapplication.bottomSheetFragment.CreateReportBottomSheetFragment;
import com.codegama.todolistapplication.bottomSheetFragment.ReportShowCalendarViewBottomSheet;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.ReportDatabaseClient;
import com.codegama.todolistapplication.model.Report;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity implements CreateReportBottomSheetFragment.setRefreshListener {

    Button btBackhome;
    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapterReport taskAdapterReport;
    List<Report> reports = new ArrayList<>();
    @BindView(R.id.calendar)
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btBackhome = findViewById(R.id.bt_backhome);
        btBackhome.setOnClickListener(this::onClick);

        ButterKnife.bind(this);
        setUpAdapterReport();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        addTask.setOnClickListener(view -> {
            CreateReportBottomSheetFragment createReportBottomSheetFragment = new CreateReportBottomSheetFragment();
            createReportBottomSheetFragment.setReportId(0, false, this,
                    ReportActivity.this);
            createReportBottomSheetFragment.show(getSupportFragmentManager(), createReportBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            ReportShowCalendarViewBottomSheet reportShowCalendarViewBottomSheet = new ReportShowCalendarViewBottomSheet();
            reportShowCalendarViewBottomSheet.show(getSupportFragmentManager(), reportShowCalendarViewBottomSheet.getTag());
        });
    }

    private void onClick(View v) {
        int id = v.getId();
        if ( id == btBackhome.getId() ) {
            Intent intent = new Intent(ReportActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    public void setUpAdapterReport() {
        taskAdapterReport = new TaskAdapterReport(this, reports, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapterReport);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Report>>{
            @Override
            protected List<Report> doInBackground(Void... voids) {
                reports = ReportDatabaseClient
                        .getInstance(getApplicationContext())
                        .getReportDatabase()
                        .reportDataBaseAction()
                        .getAllTasksList();
                return reports;
            }

            @Override
            protected void onPostExecute(List<Report> reports) {
                super.onPostExecute(reports);
                setUpAdapterReport();
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
