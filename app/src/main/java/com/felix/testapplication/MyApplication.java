package com.felix.testapplication;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.felix.testapplication.utils.DaoManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
    }

    private void initGreenDao() {
        DaoManager daoManager = DaoManager.getInstance();
        daoManager.init(this);
    }
}
