package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xiaozhanxiang.simplegridview.R;

import java.util.TooManyListenersException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author: dai
 * date:2019/5/14
 */
public class TestActivity extends BaseActivity {

    private Handler mHandler;
    private static final String TAG = "TestActivity";
    public static void getInstance(Context context) {
        Intent intent = new Intent(context,TestActivity.class);

        context.startActivity(intent);
    }

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        findViewById(R.id.tv_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacksAndMessages(null);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e(TAG, "run: 我有来了" );
//                    }
//                });
            }
        });

//        new Thread(){
//            @Override
//            public void run() {
//                Looper.prepare();
//                mHandler = new Handler();
//
//                Looper.loop();
//                Log.e(TAG, "run: 我已经循环完了" );
//            }
//        }.start();

        test(1);
    }


    public void test(final int count) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                if (count > 0) {
                    test(count - 1);
                }else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestActivity.this,"运行完了啊",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }
}
