package com.xiaozhanxiang.simplegridview.service;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.bean.event.MediaPlayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/8/22
 */
public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String CHANNEL_ID = "com.xiaozhanxiang.massageapp.arrivalTime";
    private static final int NOTIFICATION_ID = 2;
    private MediaPlayer mMediaPlayer;
    //   /storage/emulated/0/bluetooth/白小白 - 最美情侣.mp3
    //    /storage/emulated/0/bluetooth/阿肆 郭采洁 - 世界上的另一个我.mp3
    private int currIndex = 0;

    List<String> paths = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForegroundService();
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        paths.add("/storage/emulated/0/bluetooth/白小白 - 最美情侣.mp3");
        paths.add("/storage/emulated/0/bluetooth/阿肆 郭采洁 - 世界上的另一个我.mp3");
        mMediaPlayer = new MediaPlayer();
        //  mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);

    }


    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        if (path == null) return false;
        try {
            if (player.isPlaying()) player.stop();

            player.reset();
            if (path.startsWith("content://")) {
                player.setDataSource(this, Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.setOnBufferingUpdateListener(this);
            player.setOnErrorListener(this);
            player.setOnCompletionListener(this);
        } catch (Exception todo) {
            return false;
        }
        return true;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 通过通知启动服务
     */
    @TargetApi(Build.VERSION_CODES.O)
    public void setForegroundService() {
        //设定的通知渠道名称
        String channelName = "关键通知";
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
        channel.setDescription("描述一下");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setContentTitle("正在运行")//设置通知标题
                .setContentText("")//设置通知内容
                .addAction(R.drawable.ic_skip_next, "", retrievePlaybackAction("hhhhhh"))
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, builder.build());
    }


    private PendingIntent retrievePlaybackAction(final String action) {
        Intent intent = new Intent(action);
        intent.setComponent(new ComponentName(this, MediaPlayerService.class));
        return PendingIntent.getService(this, 0, intent, 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mediaPlayMessage(MediaPlayEvent event) {
        switch (event.code) {
            case 1:  //播放
                next();
                break;
            case 2:  //暂停
                pause();
                break;
        }

    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }


    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
        mMediaPlayer.release();

        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        currIndex++;
        //自动播放下一首
        next();

    }


    private void next() {
        int indext = currIndex % 2;
        setDataSourceImpl(mMediaPlayer, paths.get(indext));
    }
}
