package com.xiaozhanxiang.simplegridview.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Process;

import java.io.DataOutputStream;

/**
 * author: dai
 * date:2019/3/20
 */
public class MusicPlay {

    static {
        System.loadLibrary("native");
        System.loadLibrary("avcodec");
        System.loadLibrary("avdevice");
    }

    private AudioTrack audioTrack;

    public native void playSound(String input);

    public native void testWhile();



    //    这个方法  是C进行调用
    public void createTrack(int sampleRateInHz,int nb_channals) {

        int channaleConfig;//通道数
        if (nb_channals == 1) {
            channaleConfig = AudioFormat.CHANNEL_OUT_MONO;
        } else if (nb_channals == 2) {
            channaleConfig = AudioFormat.CHANNEL_OUT_STEREO;
        }else {
            channaleConfig = AudioFormat.CHANNEL_OUT_MONO;
        }
        int buffersize= AudioTrack.getMinBufferSize(sampleRateInHz,
                channaleConfig, AudioFormat.ENCODING_PCM_16BIT);

        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRateInHz,channaleConfig,
                    AudioFormat.ENCODING_PCM_16BIT,buffersize,AudioTrack.MODE_STREAM);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        audioTrack.play();
    }

    //C传入音频数据
    public void playTrack(byte[] buffer, int lenth) {
        if (audioTrack != null && audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack.write(buffer, 0, lenth);
        }
    }
}
