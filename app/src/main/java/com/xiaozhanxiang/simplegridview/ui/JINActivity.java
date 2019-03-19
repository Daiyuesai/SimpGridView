package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.bean.ReflexBean;
import com.xiaozhanxiang.simplegridview.utils.JNIUtils;

public class JINActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native");
    }

    private TextView mTvJin;
    private TextView mTvInt;
    private TextView mTvArray;
    private TextView mTvVoidCall;
    private JNIUtils mJniUtils;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context,JINActivity.class);

        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jin);
        mJniUtils = new JNIUtils();
        mTvJin = findViewById(R.id.tv_jin);
        mTvInt = findViewById(R.id.tv_int);
        mTvArray = findViewById(R.id.tv_array);
        mTvVoidCall = findViewById(R.id.tv_voidCall);
        TextView tvStringCall = findViewById(R.id.tv_StringCall);
        TextView tvArrayCall = findViewById(R.id.tv_ArrayCall);

        mTvJin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mTvJin.setText(getStringForJin());

                mTvJin.setText(mJniUtils.getNativeFormCCC("hhhhh"));
            }
        });

        mTvInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = mJniUtils.nativeInt(3,6);
                mTvInt.setText("3+6=" + result);
            }
        });

        mTvArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int [] array = {1,2,3,5,6};
               boolean isCopy = mJniUtils.nativeArrayJava(array);
                String s = "";
                for (int i = 0; i < array.length; i++) {
                    s += "  ["+(i + 1)+"] =  " + array[i];
                }
                s = s+ isCopy;
                mTvArray.setText(s);
            }
        });

        mTvVoidCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJniUtils.callJavaVoid();
            }
        });

        tvStringCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJniUtils.callJavaString("哈哈哈哈啊");
            }
        });

        tvArrayCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int [] array = {1,2,3,5,6};
                mJniUtils.callJavaArray(array);
            }
        });
    }


    public native String getStringForJin();


}
