package com.example.nagoor.sqlitetodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nagoor.sqlitetodo.adapter.TaskAdapter;
import com.example.nagoor.sqlitetodo.database.DatabaseHelper;
import com.example.nagoor.sqlitetodo.modelclass.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mCreateTask_iv;

    private LinearLayout mAddTask_ll;

    private RecyclerView mRecyclerView;

    private TaskAdapter mAdapter;

    private List<Task> mLists = new ArrayList<>();

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        mCreateTask_iv = (ImageView) findViewById(R.id.addtask);
        mAddTask_ll = (LinearLayout) findViewById(R.id.addtask2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new TaskAdapter(this, databaseHelper, mLists);

        mRecyclerView.setAdapter(mAdapter);

        getALLTasks();

        mCreateTask_iv.setOnClickListener(this);
        mAddTask_ll.setOnClickListener(this);

    }

    public void getALLTasks(){

        Thread newThred =  new Thread(new Runnable() {
            @Override
            public void run() {

                mLists.clear();
                mLists = databaseHelper.getAllTasks();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!mLists.isEmpty()){

                            Log.e("size response", mLists.size()+"");
                            mAddTask_ll.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mAdapter.refreshData(mLists);
                        }else {

                            mAddTask_ll.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }


                    }
                });
            }
        });

        newThred.start();

    }

    @Override
    public void onClick(View view) {

        Bundle bundle = new Bundle();
        bundle.putBoolean("createtask", true);

        CreateTaskDialog createTaskDialog = new CreateTaskDialog();
        createTaskDialog.setArguments(bundle);
        createTaskDialog.setListener(new CreateTaskDialog.DialogListener() {
            @Override
            public void onSuccess(String title, String description) {

                createTask(title, description);

            }
        });
        createTaskDialog.show(getSupportFragmentManager(), "createtask");

    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }

    private void createTask(final String title, final String description){

        Thread newthread = new Thread(new Runnable() {
            @Override
            public void run() {

                long i = databaseHelper.createTask(title, description);

                if (i!=-1){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(TodoActivity.this, "Task Created", Toast.LENGTH_LONG).show();
                            getALLTasks();

                        }
                    });

                }else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(TodoActivity.this, "Create task failed.", Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }
        });

        newthread.start();


    }

}
