//
// Created by daiyuesai on 2019/3/20.
//
#include "musicplay.h"
#include <cstdlib>



JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_utils_MusicPlay_playSound
        (JNIEnv * env, jobject thzz, jstring path){
   const char* vodioPath = env ->GetStringUTFChars(path,NULL);

   //注册全部组件
   av_register_all();

   //获取 AVFormatContext
   AVFormatContext * formatContext = avformat_alloc_context();
   //打开文件
   if(avformat_open_input(&formatContext,vodioPath,NULL,NULL) != 0) {
       //打开失败
        LOGE("打开文件呢失败");
       return;
   }

    if (avformat_find_stream_info(formatContext,NULL) < 0){
       LOGE("获取文件信息失败");
       return;
   }
   int audio_stream_idx=-1;
   int i = 0;
   for (i = 0; i < formatContext->nb_streams; ++i) {
        if (formatContext ->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO){
            //音频文件 ，记录下位置
            audio_stream_idx = i;
            break;
        }
   }
   if(audio_stream_idx == -1){
       LOGE("没有找到音频文件");
       return;
   }
   AVCodecContext * codecContext = formatContext->streams[audio_stream_idx]->codec;
   //找到解码器
   AVCodec *avCodec = avcodec_find_decoder(codecContext ->codec_id);
    //打开解码器
    if (avcodec_open2(codecContext,avCodec,NULL) < 0){
        LOGE("打开解码器失败");
        return;
    }
    //申请avpakcet，装解码前的数据
    AVPacket * avPacket = (AVPacket *)(av_malloc(sizeof(AVPacket)));
    //av_init_packet(avPacket);
    // //申请avframe，装解码后的数据
    AVFrame *avFrame = av_frame_alloc();
    //得到SwrContext ，进行重采样，具体参考http://blog.csdn.net/jammg/article/details/52688506
    SwrContext *swrContext = swr_alloc();
    //声道类型，AV_CH_LAYOUT_STEREO 表示左右双声道
    uint64_t out_ch_layout = AV_CH_LAYOUT_STEREO;
    //缓存区
    uint8_t *out_buffer = (uint8_t *)av_malloc(44100 * 2);
    //采样精度
    enum AVSampleFormat out_formart = AV_SAMPLE_FMT_S16;
    //输出的采样率必须与输入相同
    int out_sample_rate = codecContext->sample_rate;
    //swr_alloc_set_opts将PCM源文件的采样格式转换为自己希望的采样格式
    swr_alloc_set_opts(swrContext, out_ch_layout, out_formart, out_sample_rate,
                       codecContext->channel_layout, codecContext->sample_fmt, codecContext->sample_rate, 0,
                       NULL);
    swr_init(swrContext);
    //    获取通道数  2
    int out_channer_nb = av_get_channel_layout_nb_channels(out_ch_layout);
    //    反射得到Class类型
    jclass  david_player = env ->GetObjectClass(thzz);
    //    反射得到createAudio方法
    jmethodID createAudio = env ->GetMethodID(david_player,"createTrack","(II)V");
    // 反射调用createAudio
    env ->CallVoidMethod(thzz,createAudio,44100,out_channer_nb);
    //反射得到playTrack方法
    jmethodID  playTrack = env->GetMethodID(david_player,"playTrack","([BI)V");

    int got_frame;
    while (av_read_frame(formatContext, avPacket) >= 0) {
        if (avPacket->stream_index == audio_stream_idx) {
//            解码  mp3   编码格式frame----pcm   frame
            avcodec_decode_audio4(codecContext, avFrame, &got_frame, avPacket);
            if (got_frame) {
                LOGE("解码");
                swr_convert(swrContext, &out_buffer, 44100 * 2, (const uint8_t **) avFrame->data, avFrame->nb_samples);
//                缓冲区的大小
                int size = av_samples_get_buffer_size(NULL, out_channer_nb, avFrame->nb_samples,
                                                      AV_SAMPLE_FMT_S16, 1);
                jbyteArray audio_sample_array = env->NewByteArray(size);
                env->SetByteArrayRegion(audio_sample_array, 0, size, (const jbyte *) out_buffer);
                env->CallVoidMethod(thzz, playTrack, audio_sample_array, size);
                env->DeleteLocalRef(audio_sample_array);
            }
        }
    }

    av_frame_free(&avFrame);
    swr_free(&swrContext);
    avcodec_close(codecContext);
    avformat_close_input(&formatContext);
    env->ReleaseStringUTFChars(path, vodioPath);
}


JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_utils_MusicPlay_testWhile
        (JNIEnv * env , jobject thzz){


}

