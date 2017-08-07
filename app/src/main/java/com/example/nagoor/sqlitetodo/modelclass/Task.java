package com.example.nagoor.sqlitetodo.modelclass;

/**
 * Created by nagoor on 04/08/17.
 */

public class Task {

    private long mId;

    private String mTitle;
    private String mDescription;

    public Task(){}

    public void setId(long id){

        this.mId = id;
    }
    public void setTitle(String title){
        this.mTitle = title;
    }

    public void setDescription(String description){
        this.mDescription = description;
    }

    public long getId(){return this.mId;}

    public String getTitle(){return this.mTitle;}

    public String getDescription(){return this.mDescription;}
}
