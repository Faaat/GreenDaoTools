package com.felix.testapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class BannerViewActivity extends BaseActivity {

    private Button mBtnClose;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
        mThread.start();
    }

    @Override
    public void onBackPressed() {

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mBtnClose.setText((CharSequence) msg.obj);
                    break;
                case 1:
                    mBtnClose.setText((CharSequence) msg.obj);
                    mBtnClose.setEnabled(true);
            }
            return false;

        }
    });

    private Thread mThread = new Thread() {
        int count = 5;

        @Override
        public void run() {
            super.run();
            while (count > 0) {
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = count + " s 后关闭";
                count--;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = "关闭";
            mHandler.sendMessage(msg);
        }
    };

    private void initView() {
        mBtnClose = findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
