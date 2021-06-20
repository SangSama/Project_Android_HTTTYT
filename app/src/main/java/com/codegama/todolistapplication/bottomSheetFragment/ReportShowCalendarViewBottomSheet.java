package com.codegama.todolistapplication.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.codegama.todolistapplication.R;
import com.codegama.todolistapplication.activity.ReportActivity;
import com.codegama.todolistapplication.activity.TodolistActivity;
import com.codegama.todolistapplication.database.ReportDatabaseClient;
import com.codegama.todolistapplication.database.TodolistDatabaseClient;
import com.codegama.todolistapplication.model.Report;
import com.codegama.todolistapplication.model.Todolist;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.drawable.DrawableUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ReportShowCalendarViewBottomSheet extends BottomSheetDialogFragment {

    Unbinder unbinder;
    ReportActivity activity;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    List<Report> reports = new ArrayList<>();


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_calendar_view, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
        calendarView.setHeaderColor(R.color.colorAccent);
        getSavedTasks();
        back.setOnClickListener(view -> dialog.dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Report>> {
            @Override
            protected List<Report> doInBackground(Void... voids) {
                reports = ReportDatabaseClient
                        .getInstance(getActivity())
                        .getReportDatabase()
                        .reportDataBaseAction()
                        .getAllTasksList();
                return reports;
            }

            @Override
            protected void onPostExecute(List<Report> reports) {
                super.onPostExecute(reports);
                calendarView.setEvents(getHighlitedDays());
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    public List<EventDay> getHighlitedDays() {
        List<EventDay> events = new ArrayList<>();

        for(int i = 0; i < reports.size(); i++) {
            Calendar calendar = Calendar.getInstance();
            String[] items1 = reports.get(i).getDate().split("-");
            String dd = items1[0];
            String month = items1[1];
            String year = items1[2];

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(year));
            events.add(new EventDay(calendar, R.drawable.dot));
        }
        return events;
    }

}


