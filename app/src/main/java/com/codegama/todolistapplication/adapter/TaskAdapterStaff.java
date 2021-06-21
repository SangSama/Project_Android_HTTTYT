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
import com.codegama.todolistapplication.activity.StaffActivity;
import com.codegama.todolistapplication.bottomSheetFragment.CreateStaffBottomSheetFragment;
import com.codegama.todolistapplication.database.StaffDatabaseClient;
import com.codegama.todolistapplication.model.Staff;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapterStaff extends RecyclerView.Adapter<TaskAdapterStaff.TaskViewHolder> {

    private StaffActivity context;
    private LayoutInflater inflater;
    private List<Staff> staffList;
    //    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
//    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
//    Date date = null;
//    String outputDateString = null;
    CreateStaffBottomSheetFragment.setRefreshListener setRefreshListener;

    public TaskAdapterStaff(StaffActivity context, List<Staff> staffList, CreateStaffBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.staffList = staffList;
        this.setRefreshListener = setRefreshListener;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskAdapterStaff.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.item_staff, viewGroup, false);
        return new TaskAdapterStaff.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.name.setText(staff.getTaskName());
        holder.gender.setText(staff.getTaskGender());
        holder.date.setText(staff.getTaskDate());
        holder.phone.setText(staff.getTaskPhone());
        holder.work.setText(staff.getTaskWork());
        holder.status.setText(staff.isComplete() ? "COMPLETED" : "UPCOMING");
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
        final Staff staff = staffList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                            setPositiveButton(R.string.yes, (dialog, which) -> {
                                deleteTaskFromId(staff.getTaskId(), position);
                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;
                case R.id.menuUpdate:
                    CreateStaffBottomSheetFragment createStaffBottomSheetFragment = new CreateStaffBottomSheetFragment();
                    createStaffBottomSheetFragment.setStaffId(staff.getTaskId(), true, context, context);
                    createStaffBottomSheetFragment.show(context.getSupportFragmentManager(), createStaffBottomSheetFragment.getTag());
                    break;
                case R.id.menuComplete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(staff.getTaskId(), position))
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
        class GetSavedTasks extends AsyncTask<Void, Void, List<Staff>> {
            @Override
            protected List<Staff> doInBackground(Void... voids) {
                StaffDatabaseClient.getInstance(context)
                        .getStaffDatabase()
                        .staffDataBaseAction()
                        .deleteTaskFromId(taskId);

                return staffList;
            }

            @Override
            protected void onPostExecute(List<Staff> staffs) {
                super.onPostExecute(staffs);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    private void removeAtPosition(int position) {
        staffList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,staffList.size());
    }

    @Override
    public int getItemCount() {
        return staffList.size();
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


