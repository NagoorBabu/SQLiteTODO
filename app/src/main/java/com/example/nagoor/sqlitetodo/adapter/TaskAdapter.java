package com.example.nagoor.sqlitetodo.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nagoor.sqlitetodo.CreateTaskDialog;
import com.example.nagoor.sqlitetodo.R;
import com.example.nagoor.sqlitetodo.TodoActivity;
import com.example.nagoor.sqlitetodo.database.DatabaseHelper;
import com.example.nagoor.sqlitetodo.modelclass.Task;

import java.util.List;

/**
 * Created by nagoor on 04/08/17.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private AppCompatActivity activity;
    private List<Task> taskList;
    private DatabaseHelper databaseHelper;

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView title, description;
        private Button edit, complete;


        private TaskViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);

            edit = (Button) view.findViewById(R.id.edit);
            complete = (Button) view.findViewById(R.id.complete);

        }
    }


    public TaskAdapter(AppCompatActivity activity, DatabaseHelper databaseHelper, List<Task> taskList) {

        this.activity = activity;
        this.databaseHelper = databaseHelper;
        this.taskList = taskList;
    }

    public void refreshData(List<Task> list){

        taskList.clear();
        taskList.addAll(list);

        notifyDataSetChanged();

    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_task, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        final Task task = taskList.get(position);

        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());

        holder.edit.setOnClickListener(new OnItemClickListener(position) {
            @Override
            public void itemClick(View view, final int i) {

                Bundle bundle = new Bundle();
                bundle.putBoolean("createtask", false);
                bundle.putString("title", taskList.get(i).getTitle());
                bundle.putString("description", taskList.get(i).getDescription());

                CreateTaskDialog createTaskDialog = new CreateTaskDialog();
                createTaskDialog.setArguments(bundle);
                createTaskDialog.setListener(new CreateTaskDialog.DialogListener() {
                    @Override
                    public void onSuccess(String title, String description) {

                        Task task1 = new Task();
                        task1.setId(taskList.get(i).getId());
                        task1.setTitle(title);
                        task1.setDescription(description);

                        updateTask(task1);
                    }
                });
                createTaskDialog.show(activity.getSupportFragmentManager(), "edittask");

            }
        });

        holder.complete.setOnClickListener(new OnItemClickListener(position) {
            @Override
            public void itemClick(View view, final int i) {

                Thread newthread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int j = databaseHelper.deleteTask(taskList.get(i).getId());

                        if (j == 0){

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(activity, "Not found the record", Toast.LENGTH_LONG).show();

                                }
                            });
                        }else {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(activity, "Delete Success", Toast.LENGTH_LONG).show();
                                    ((TodoActivity)activity).getALLTasks();

                                }
                            });
                        }

                    }
                });

                newthread.start();

            }
        });

    }

    private void updateTask(final Task task){

        Thread newthread = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = databaseHelper.updateTask(task);
                Log.e("update response", i+"");

                if (i==0){

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity, "same values updated", Toast.LENGTH_LONG).show();

                        }
                    });
                }else {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity, "Update Success.", Toast.LENGTH_LONG).show();
                            ((TodoActivity)activity).getALLTasks();

                        }
                    });
                }

            }
        });

        newthread.start();


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public abstract class OnItemClickListener implements View.OnClickListener{

        private int i;

        public OnItemClickListener(int i){

            this.i = i;
        }

        @Override
        public void onClick(View view) {

            itemClick(view, i);
        }

        public abstract void itemClick(View view, int i);
    }
}