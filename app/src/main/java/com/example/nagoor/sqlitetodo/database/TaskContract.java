package com.example.nagoor.sqlitetodo.database;

import android.provider.BaseColumns;

/**
 * Created by nagoor on 04/08/17.
 */

public class TaskContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TaskContract() {}

    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }

}
