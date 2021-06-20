package com.codegama.todolistapplication.bottomSheetFragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.codegama.todolistapplication.R;
import com.codegama.todolistapplication.activity.DoctorActivity;
import com.codegama.todolistapplication.broadcastReceiver.AlarmBroadcastReceiver;
import com.codegama.todolistapplication.database.DoctorDatabaseClient;
import com.codegama.todolistapplication.model.Doctor;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zubair.alarmmanager.builder.AlarmBuilder;
import com.zubair.alarmmanager.enums.AlarmType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.ALARM_SERVICE;

public class CreateDoctorBottomSheetFragment extends BottomSheetDialogFragment {

    Unbinder unbinder;
    @BindView(R.id.addTaskName)
    EditText addTaskName;
    @BindView(R.id.addTaskGender)
    EditText addTaskGender;
    @BindView(R.id.addTaskPhone)
    EditText addTaskPhone;
    @BindView(R.id.addTaskDate)
    EditText addTaskDate;
    @BindView(R.id.addTaskWork)
    EditText addTaskWork;
    @BindView(R.id.addTask)
    Button addTask;
    int taskId;
    boolean isEdit;
    Doctor doctor;
//    int mYear, mMonth, mDay;
//    int mHour, mMinute;
    setRefreshListener setRefreshListener;
//    AlarmManager alarmManager;
//    TimePickerDialog timePickerDialog;
//    DatePickerDialog datePickerDialog;
    DoctorActivity activity;
    public static int count = 0;

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

    public void setDoctorId(int taskId, boolean isEdit, setRefreshListener setRefreshListener, DoctorActivity activity) {
        this.taskId = taskId;
        this.isEdit = isEdit;
        this.activity = activity;
        this.setRefreshListener = setRefreshListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_create_doctor, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
//        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        addTask.setOnClickListener(view -> {
            if(validateFields())
                createDoctor();
        });
        if (isEdit) {
            showTaskFromId();
        }
    }

    public boolean validateFields() {
        if(addTaskName.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid fullname", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskGender.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskPhone.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addTaskWork.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a work calendar", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createDoctor() {
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                Doctor createDoctor = new Doctor();
                createDoctor.setTaskName(addTaskName.getText().toString());
                createDoctor.setTaskGender(addTaskGender.getText().toString());
                createDoctor.setTaskDate(addTaskDate.getText().toString());
                createDoctor.setTaskPhone(addTaskPhone.getText().toString());
                createDoctor.setTaskWork(addTaskWork.getText().toString());

                if (!isEdit)
                    DoctorDatabaseClient.getInstance(getActivity()).getDoctorDatabase()
                            .doctorDataBaseAction()
                            .insertDataIntoTaskList(createDoctor);
                else
                    DoctorDatabaseClient.getInstance(getActivity()).getDoctorDatabase()
                            .doctorDataBaseAction()
                            .updateAnExistingRow(taskId, addTaskName.getText().toString(),
                                    addTaskGender.getText().toString(), addTaskDate.getText().toString(),
                                    addTaskPhone.getText().toString(), addTaskWork.getText().toString() ) ;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    createAnAlarm();
                }
                setRefreshListener.refresh();
                Toast.makeText(getActivity(), "Your event is been added", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void createAnAlarm() {
//        try {
//            String[] items1 = taskDate.getText().toString().split("-");
//            String dd = items1[0];
//            String month = items1[1];
//            String year = items1[2];
//
//            String[] itemTime = taskTime.getText().toString().split(":");
//            String hour = itemTime[0];
//            String min = itemTime[1];
//
//            Calendar cur_cal = new GregorianCalendar();
//            cur_cal.setTimeInMillis(System.currentTimeMillis());
//
//            Calendar cal = new GregorianCalendar();
//            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
//            cal.set(Calendar.MINUTE, Integer.parseInt(min));
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            cal.set(Calendar.DATE, Integer.parseInt(dd));
//
//            Intent alarmIntent = new Intent(activity, AlarmBroadcastReceiver.class);
//            alarmIntent.putExtra("TITLE", addTaskTitle.getText().toString());
//            alarmIntent.putExtra("DESC", addTaskDescription.getText().toString());
//            alarmIntent.putExtra("DATE", taskDate.getText().toString());
//            alarmIntent.putExtra("TIME", taskTime.getText().toString());
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//                } else {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//                }
//                count ++;
//
//                PendingIntent intent = PendingIntent.getBroadcast(activity, count, alarmIntent, 0);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
//                    } else {
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 600000, intent);
//                    }
//                }
//                count ++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void showTaskFromId() {
        class showTaskFromId extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                doctor = DoctorDatabaseClient.getInstance(getActivity()).getDoctorDatabase()
                        .doctorDataBaseAction().selectDataFromAnId(taskId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setDataInUI();
            }
        }
        showTaskFromId st = new showTaskFromId();
        st.execute();
    }

    private void setDataInUI() {
        addTaskName.setText(doctor.getTaskName());
        addTaskGender.setText(doctor.getTaskGender());
        addTaskDate.setText(doctor.getTaskDate());
        addTaskPhone.setText(doctor.getTaskPhone());
        addTaskWork.setText(doctor.getTaskWork());

    }

    public interface setRefreshListener {
        void refresh();
    }
}

