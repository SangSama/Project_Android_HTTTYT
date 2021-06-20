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
import com.codegama.todolistapplication.adapter.TaskAdapterTodolist;
import com.codegama.todolistapplication.bottomSheetFragment.CreateTodolistBottomSheetFragment;
import com.codegama.todolistapplication.bottomSheetFragment.TodolistShowCalendarViewBottomSheet;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.TodolistDatabaseClient;
import com.codegama.todolistapplication.model.Todolist;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TodolistActivity extends AppCompatActivity implements CreateTodolistBottomSheetFragment.setRefreshListener {

    Button btBackhome;
    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapterTodolist taskAdapterTodolist;
    List<Todolist> todolists = new ArrayList<>();
    @BindView(R.id.calendar)
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        btBackhome = findViewById(R.id.bt_backhome);
        btBackhome.setOnClickListener(this::onClick);

        ButterKnife.bind(this);
        setUpAdapterTodolist();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        addTask.setOnClickListener(view -> {
            CreateTodolistBottomSheetFragment createTodolistBottomSheetFragment = new CreateTodolistBottomSheetFragment();
            createTodolistBottomSheetFragment.setTodolistId(0, false, this,
                    TodolistActivity.this);
            createTodolistBottomSheetFragment.show(getSupportFragmentManager(), createTodolistBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            TodolistShowCalendarViewBottomSheet todolistShowCalendarViewBottomSheet = new TodolistShowCalendarViewBottomSheet();
            todolistShowCalendarViewBottomSheet.show(getSupportFragmentManager(), todolistShowCalendarViewBottomSheet.getTag());
        });
    }

    private void onClick(View v) {
        int id = v.getId();
        if ( id == btBackhome.getId() ) {
            Intent intent = new Intent(TodolistActivity.this, HomeActivity.class);
            startActivity(intent);
        }

    }

    public void setUpAdapterTodolist() {
        taskAdapterTodolist = new TaskAdapterTodolist(this, todolists, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapterTodolist);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Todolist>>{
            @Override
            protected List<Todolist> doInBackground(Void... voids) {
                todolists = TodolistDatabaseClient
                        .getInstance(getApplicationContext())
                        .getTodolistDatabase()
                        .todolistDataBaseAction()
                        .getAllTasksList();
                return todolists;
            }

            @Override
            protected void onPostExecute(List<Todolist> todolists) {
                super.onPostExecute(todolists);
                setUpAdapterTodolist();
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
