package com.codegama.todolistapplication.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codegama.todolistapplication.R;
import com.codegama.todolistapplication.activity.DoctorActivity;
import com.codegama.todolistapplication.activity.TodolistActivity;
import com.codegama.todolistapplication.bottomSheetFragment.CreateDoctorBottomSheetFragment;
import com.codegama.todolistapplication.bottomSheetFragment.CreateTodolistBottomSheetFragment;
import com.codegama.todolistapplication.database.DoctorDatabaseClient;
import com.codegama.todolistapplication.database.TodolistDatabaseClient;
import com.codegama.todolistapplication.model.Doctor;
import com.codegama.todolistapplication.model.Todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapterDoctor extends RecyclerView.Adapter<TaskAdapterDoctor.TaskViewHolder> {

    private DoctorActivity context;
    private LayoutInflater inflater;
    private List<Doctor> doctorList;
//    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
//    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
//    Date date = null;
//    String outputDateString = null;
    CreateDoctorBottomSheetFragment.setRefreshListener setRefreshListener;

    public TaskAdapterDoctor(DoctorActivity context, List<Doctor> doctorList, CreateDoctorBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.doctorList = doctorList;
        this.setRefreshListener = setRefreshListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskAdapterDoctor.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_doctor, viewGroup, false);
        return new TaskAdapterDoctor.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.name.setText(doctor.getTaskName());
        holder.gender.setText(doctor.getTaskGender());
        holder.status.setText(doctor.isComplete() ? "COMPLETED" : "UPCOMING");
        holder.options.setOnClickListener(view -> showPopUpMenu(view, position));

//        try {
//            date = inputDateFormat.parse(todolist.getDate());
//            outputDateString = dateFormat.format(date);
//
//            String[] items1 = outputDateString.split(" ");
//            String day = items1[0];
//            String dd = items1[1];
//            String month = items1[2];
//
//            holder.day.setText(day);
//            holder.date.setText(dd);
//            holder.month.setText(month);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void showPopUpMenu(View view, int position) {
        final Doctor doctor = doctorList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                            setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteTaskFromId(doctor.getTaskId(), position);
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.menuUpdate:
                    CreateDoctorBottomSheetFragment createDoctorBottomSheetFragment = new CreateDoctorBottomSheetFragment();
                    createDoctorBottomSheetFragment.setDoctorId(doctor.getTaskId(), true, context, context);
                    createDoctorBottomSheetFragment.show(context.getSupportFragmentManager(), createDoctorBottomSheetFragment.getTag());
                    break;
                case R.id.menuComplete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(doctor.getTaskId(), position))
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    public void showCompleteDialog(int taskId, int position) {
        Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed_theme);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view -> {
            deleteTaskFromId(taskId, position);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    private void deleteTaskFromId(int taskId, int position) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Doctor>> {
            @Override
            protected List<Doctor> doInBackground(Void... voids) {
                DoctorDatabaseClient.getInstance(context)
                        .getDoctorDatabase()
                        .doctorDataBaseAction()
                        .deleteTaskFromId(taskId);

                return doctorList;
            }

            @Override
            protected void onPostExecute(List<Doctor> doctors) {
                super.onPostExecute(doctors);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    private void removeAtPosition(int position) {
        doctorList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,doctorList.size());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.gender)
        TextView gender;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.work)
        TextView work;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.options)
        ImageView options;
        @BindView(R.id.avatar)
        ImageView avatar;

        TaskViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

