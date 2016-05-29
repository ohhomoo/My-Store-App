package com.myezgenius.mystoreapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by moo on 5/29/2016 AD.
 */
public class MyManage {

    //Explict
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MyManage(Context context) {

        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();

    }   //Constructor

}   // MyManage Class
