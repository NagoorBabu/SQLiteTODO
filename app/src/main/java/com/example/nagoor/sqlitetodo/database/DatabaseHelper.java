package com.example.nagoor.sqlitetodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nagoor.sqlitetodo.modelclass.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksManager";

    private static final String CREATE_TABLE_TASK =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT)";

    private static final String DELETE_TABLE_TASK =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required table
        db.execSQL(CREATE_TABLE_TASK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older table
        db.execSQL(DELETE_TABLE_TASK);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "task" table methods ----------------//

    /*
     * Creating a todo
     */
    public long createTask(String title, String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, title);
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, description);

        return db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);

    }

    /*
     * get single todo
     */
    public Task getTask(long task_id) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TaskContract.TaskEntry.TABLE_NAME + " WHERE "
                + TaskContract.TaskEntry._ID + " = " + task_id;

        Log.e(LOG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        try {


            cursor.moveToFirst();
            Task task = new Task();
            task.setId(cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry._ID)));
            task.setTitle((cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE))));
            task.setDescription(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)));

            return task;


        } finally {

            if (cursor != null)
                cursor.close();
        }


    }

    public int updateTask(Task task) {

        Log.e("alltask response", task.getId()+"");
        Log.e("alltask response", task.getTitle());
        Log.e("alltask response", task.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());

        return db.update(TaskContract.TaskEntry.TABLE_NAME, values, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    public int deleteTask(long task_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TaskContract.TaskEntry.TABLE_NAME, TaskContract.TaskEntry._ID + " = ?",
                new String[] { String.valueOf(task_id) });
    }


    /**
     * getting all todos
     */
    public List<Task> getAllTasks() {

        List<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TaskContract.TaskEntry.TABLE_NAME;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        try {

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setId(cursor.getInt((cursor.getColumnIndex(TaskContract.TaskEntry._ID))));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
                    task.setDescription(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)));

                    // adding to todo list
                    tasks.add(task);

                    Log.e("alltask response", cursor.getInt((cursor.getColumnIndex(TaskContract.TaskEntry._ID)))+"");
                    Log.e("alltask response", cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
                    Log.e("alltask response", cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)));

                } while (cursor.moveToNext());
            }

        } finally {

            cursor.close();
        }


        return tasks;
    }

}
