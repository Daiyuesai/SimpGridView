package com.xiaozhanxiang.simplegridview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.BuildConfig;
import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.bean.event.MediaPlayEvent;
import com.xiaozhanxiang.simplegridview.service.MediaPlayerService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: dai
 * date:2019/8/27
 */
public class ForegroundServiceActivity extends BaseActivity {


    @BindView(R.id.tv_start_service)
    TextView tvStartService;
    @BindView(R.id.tv_paly)
    TextView tvPaly;
    @BindView(R.id.tv_pause)
    TextView tvPause;
    private Intent mIntent;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, ForegroundServiceActivity.class);

        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foreground_service);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_start_service, R.id.tv_paly, R.id.tv_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_service:
                mIntent = new Intent(this, MediaPlayerService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    startForegroundService(mIntent);
                }else {
                    startService(mIntent);
                }
                break;
            case R.id.tv_paly:
                EventBus.getDefault().post(new MediaPlayEvent(1));
                break;
            case R.id.tv_pause:
                EventBus.getDefault().post(new MediaPlayEvent(2));
                break;
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mIntent!= null) {
//            stopService(mIntent);
//        }

    }
}
