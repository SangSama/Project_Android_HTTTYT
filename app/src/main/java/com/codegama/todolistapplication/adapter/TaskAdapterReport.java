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
import com.codegama.todolistapplication.activity.ReportActivity;
import com.codegama.todolistapplication.bottomSheetFragment.CreateReportBottomSheetFragment;
import com.codegama.todolistapplication.bottomSheetFragment.CreateTodolistBottomSheetFragment;
import com.codegama.todolistapplication.database.ReportDatabaseClient;
import com.codegama.todolistapplication.database.TodolistDatabaseClient;
import com.codegama.todolistapplication.model.Report;
import com.codegama.todolistapplication.model.Todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapterReport extends RecyclerView.Adapter<TaskAdapterReport.TaskViewHolder> {

    private ReportActivity context;
    private LayoutInflater inflater;
    private List<Report> reportList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    Date date = null;
    String outputDateString = null;
    CreateReportBottomSheetFragment.setRefreshListener setRefreshListener;

    public TaskAdapterReport(ReportActivity context, List<Report> reportList, CreateReportBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.reportList = reportList;
        this.setRefreshListener = setRefreshListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskAdapterReport.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_report, viewGroup, false);
        return new TaskAdapterReport.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.title.setText(report.getTaskTitle());
        holder.description.setText(report.getTaskDescrption());
        holder.time.setText(report.getLastAlarm());
        holder.status.setText(report.isComplete() ? "COMPLETED" : "UPCOMING");
        holder.options.setOnClickListener(view -> showPopUpMenu(view, position));

        try {
            date = inputDateFormat.parse(report.getDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.day.setText(day);
            holder.date.setText(dd);
            holder.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopUpMenu(View view, int position) {
        final Report report = reportList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                            setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteTaskFromId(report.getTaskId(), position);
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.menuUpdate:
                    CreateReportBottomSheetFragment createReportBottomSheetFragment = new CreateReportBottomSheetFragment();
                    createReportBottomSheetFragment.setReportId(report.getTaskId(), true, context, context);
                    createReportBottomSheetFragment.show(context.getSupportFragmentManager(), createReportBottomSheetFragment.getTag());
                    break;
                case R.id.menuComplete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(report.getTaskId(), position))
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
        class GetSavedTasks extends AsyncTask<Void, Void, List<Report>> {
            @Override
            protected List<Report> doInBackground(Void... voids) {
                ReportDatabaseClient.getInstance(context)
                        .getReportDatabase()
                        .reportDataBaseAction()
                        .deleteTaskFromId(taskId);

                return reportList;
            }

            @Override
            protected void onPostExecute(List<Report> reports) {
                super.onPostExecute(reports);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    private void removeAtPosition(int position) {
        reportList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,reportList.size());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.day)
        TextView day;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.month)
        TextView month;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.options)
        ImageView options;
        @BindView(R.id.time)
        TextView time;

        TaskViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

