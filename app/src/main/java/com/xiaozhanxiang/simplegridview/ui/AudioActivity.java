package com.xiaozhanxiang.simplegridview.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.callback.PermissionResultListener;
import com.xiaozhanxiang.simplegridview.utils.DateUtils;
import com.xiaozhanxiang.simplegridview.utils.MusicPlay;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * author: dai
 * date:2019/3/21
 */
public class AudioActivity extends BaseActivity {

    private static String [] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO};

    private static final String TAG = "AudioActivity";

    private AudioRecord mAudioRecord;
    private boolean isRecording = false;
    private int mBufferSize;
    private MusicPlay mMusicPlay;
    private TextView mTvTest;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context,AudioActivity.class);

        context.startActivity(intent);
    }
    //录制音频的来源 为麦克风
    private static final int AUDIO_RESOURCE = MediaRecorder.AudioSource.MIC;
    //设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置
    private final static int AUDIO_SAMPLE_RATE = 44100;
    //设置声道声道数量为双声道
    private final static int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;
    //设置采样精度，将采样的数据以PCM进行编码，每次采集的数据位宽为16bit。
    private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        mMusicPlay = new MusicPlay();
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestPermissionsd(permissions, 100, new PermissionResultListener() {
                    @Override
                    public void permissionResult(HashMap<String, Boolean> permission, boolean isAllAgree) {
                        startRecord();
                    }
                });

            }
        });

        findViewById(R.id.tv_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });

        findViewById(R.id.tv_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionsd(permissions, 100, new PermissionResultListener() {
                    @Override
                    public void permissionResult(HashMap<String, Boolean> permission, boolean isAllAgree) {
                        if (isAllAgree){
                            selectFile();
                        }
                    }
                });
            }
        });

        mTvTest = findViewById(R.id.tv_test);
        mTvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }


    private void initRecorder(){
        mBufferSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        if (AudioRecord.ERROR_BAD_VALUE == mBufferSize || AudioRecord.ERROR == mBufferSize){
            Log.e(TAG, "initRecorder: 设备不支持");
        }
        mAudioRecord = new AudioRecord(AUDIO_RESOURCE,AUDIO_SAMPLE_RATE,CHANNEL_CONFIG,AUDIO_FORMAT, mBufferSize);
    }
    private void startRecord(){
        if (mAudioRecord == null){
            initRecorder();
        }

        if (mAudioRecord != null){
            //已经准备完成
            if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                mAudioRecord.startRecording();//开始录制
                //开启子线程写入数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //设置线程优先级
                        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
                        //开始写数据
                        //创建一个流，存放从AudioRecord读取的数据

                        File saveFile = new File(getDir(), UUID.randomUUID().toString() + "audio-record.pcm");

                        DataOutputStream dataOutputStream = null;

                        try {
                            dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)));
                            isRecording = true;
                            byte[] data = new byte[mBufferSize];
                            while (isRecording && mAudioRecord.getRecordingState() ==AudioRecord.RECORDSTATE_RECORDING ){
                                //正在录制中
                               int read =  mAudioRecord.read(data,0,mBufferSize);
                               if (read != AudioRecord.ERROR_INVALID_OPERATION && read != AudioRecord.ERROR_BAD_VALUE
                                       && read != AudioRecord.ERROR&& read != AudioRecord.ERROR_DEAD_OBJECT){
                                   dataOutputStream.write(data,0,read);
                               }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }finally {
                            try {
                                dataOutputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
    }



    public void stopRecord() {
        isRecording = false;

        if (mAudioRecord != null){
            if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
                //正在录制  释放
                mAudioRecord.stop();
            }

            if (mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                mAudioRecord.release();
            }
        }
    }

    private File getDir(){
        File saveFile = new File(Environment.getExternalStorageDirectory(), "SimpleView");
        if (!saveFile.exists()){
            saveFile.mkdirs();
        }
        return saveFile;
    }
    @Override
    protected void onDestroy() {
        stopRecord();
        super.onDestroy();
    }


    private int time = 15*60;
    private void play() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    time --;
                    if (time <= 0) {
                        break;
                    }
                    String s = DateUtils.timeStamp2Date(time * 1000, "mm:ss");
                    mTvTest.setText(s);
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }




    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                final String path = getPath(this, uri);
                new Thread(){
                    @Override
                    public void run() {
                        Log.e(TAG, "run: " + path);
                        mMusicPlay.playSound(path);
                    }
                }.start();

            }
        }
    }




    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }



    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file url.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) { final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);}
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



}
