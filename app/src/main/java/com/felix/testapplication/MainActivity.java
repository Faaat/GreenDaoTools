package com.felix.testapplication;

import android.os.Bundle;
import android.util.Log;

import com.felix.testapplication.entity.User;

import java.util.Arrays;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
//    private UserDaoUtils instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, BannerViewActivity.class));
//        instance = UserDaoUtils.getInstance(this);
////        instance.insert(new User(1004, "zhangsan", 23));
//        Log.d(TAG, "onCreate: " + Arrays.toString(instance.queryAll().toArray()));
    }
}
