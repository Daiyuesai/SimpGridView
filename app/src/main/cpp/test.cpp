//
// Created by daiyuesai on 2019/3/12.
//

#include "test.h"

#include <unistd.h>
#include <android/native_window_jni.h>
//#ifdef __cplusplus
extern "C" {

//#endif
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libavfilter/avfilter.h"
#include "android_debug.h"
//像素处理
#include "libswscale/swscale.h"

//#ifdef __cplusplus
}
//#endif

test::test(int a, int b) {
    w =a ;
    h = b;
}

int test::getWidth() {
    return w;
}

int test::getHeight() {
    return h;
}

int test::getArea() {
    return w*h;
}

test::~test() {


}


 jstring  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_getNativeFormCCC
        (JNIEnv *env, jobject thzz, jstring s){
    char info[40000]={0};

    av_register_all();
    struct URLProtocol *pup = NULL;
    //input
    struct URLProtocol **p_temp = &pup;
    avio_enum_protocols((void **)p_temp, 0);
    while ((*p_temp) != NULL){
        sprintf(info, "%s[in ][%10s]\n", info, avio_enum_protocols((void **)p_temp, 0));
    }
    pup = NULL;
    //output
    avio_enum_protocols((void **)p_temp,1);
    while ((*p_temp) != NULL){
        sprintf(info, "%s[out][%10s]\n", info, avio_enum_protocols((void **)p_temp, 1));
    }
    LOGE("%s", info);
    return env->NewStringUTF(info);
}


JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_view_MyVideoView_render
        (JNIEnv * env, jobject thiz, jstring inputStr_,jobject surface){

    const char * inputPath = env ->GetStringUTFChars(inputStr_, NULL);
    //第一步 注册
    av_register_all();
    // 2 获取视频内容
    AVFormatContext * avFormatContext = avformat_alloc_context(); //获取上下文
    int error;
    char buff[] = "";
    //打开一个输入流
    if (error =  avformat_open_input(&avFormatContext,inputPath,NULL,NULL) < 0){
        av_strerror(error,buff,1024);
        LOGE("Couldn't open file %s: %d(%s)", inputPath, error, buff);
        // LOGE("%d",error)
        LOGE("打开视频失败");
        return;
    }
    //获取文件信息
    avformat_find_stream_info(avFormatContext,NULL);

    //我们应当找出相对应的视频流
    int video_index = -1;
    // unsigned int nb_streams：视音频流的个数
    // AVStream **streams：视音频流
    for (int i = 0; i < avFormatContext ->nb_streams; ++i) {
        if(avFormatContext ->streams[i] ->codec->codec_type == AVMEDIA_TYPE_VIDEO){
            //找到视频 标记一下
            video_index = i;
             break;  //讲道理可以break
        }
    }
    //获取解码器上下文
    AVCodecContext *avCodecContext =  avFormatContext ->streams[video_index]->codec;
    //获取解码器
    AVCodec *avCodec = avcodec_find_decoder(avCodecContext->codec_id);
    //打开解码器
    if(avcodec_open2(avCodecContext,avCodec,NULL) < 0) {
        LOGE("打开解码器失败");
        return;
    }

    //b.申请AVPacket和AVFrame，其中AVPacket的作用是：
    // 保存解码之前的数据和一些附加信息，如显示时间戳（pts）、
    // 解码时间戳（dts）、数据时长，所在媒体流的索引等；
    // AVFrame的作用是：存放解码过后的数据。

    //申请AVPacket
    AVPacket *packet = (AVPacket *)(av_malloc(sizeof(AVPacket)));
    av_init_packet(packet);
    //申请AVFrame
    AVFrame *frame = av_frame_alloc(); //分配一个AVFrame结构体,AVFrame结构体一般用于存储原始数据，指向解码后的原始帧
    AVFrame *rgb_frame = av_frame_alloc();//分配一个AVFrame结构体，指向存放转换成rgb后的帧
//     //缓存区
//     uint8_t  *out_buffer= (uint8_t *)av_malloc(avpicture_get_size(AV_PIX_FMT_RGBA,
//                                                                   avCodecContext->width,avCodecContext->height));
    //缓存区
    uint8_t *out_buffer = (uint8_t *)av_malloc(avpicture_get_size(AV_PIX_FMT_RGBA,
            avCodecContext->width, avCodecContext ->height));
    //与缓存区相关联，设置rgb_frame缓存区
    avpicture_fill((AVPicture *)(rgb_frame), out_buffer, AV_PIX_FMT_RGBA,
            avCodecContext->width, avCodecContext->height);

    SwsContext* swsContext = sws_getContext(avCodecContext->width,avCodecContext->height,avCodecContext->pix_fmt,
                                            avCodecContext->width,avCodecContext->height,AV_PIX_FMT_RGBA,
                                            SWS_BICUBIC,NULL,NULL,NULL);

    //取到ANativeWindow
    ANativeWindow * nativeWindow = ANativeWindow_fromSurface(env,surface);
    if(nativeWindow==0){
        LOGE("nativewindow取到失败");
        return;
    }
    //视频缓冲区
    ANativeWindow_Buffer native_outBuffer;


    int frameCount;
    int h =0;
    LOGE("解码 ");

    while (av_read_frame(avFormatContext,packet) >= 0){
        LOGE("解码 %d",packet->stream_index);
        LOGE("VINDEX %d",video_index);
        if(packet->stream_index == video_index) {
            LOGE("解码 hhhhh");
            //如果是视频流
            //解码
            avcodec_decode_video2(avCodecContext,frame,&frameCount,packet);
            LOGE("解码中....  %d",frameCount);
            if(frameCount) {
                LOGE("转换并绘制");
                //说明有内容
                //绘制之前配置nativewindow
                ANativeWindow_setBuffersGeometry(nativeWindow,avCodecContext->width,avCodecContext->height
                        ,WINDOW_FORMAT_RGBA_8888);
                LOGE("测试1");
                //上锁`
                ANativeWindow_lock(nativeWindow, &native_outBuffer, NULL);
                LOGE("测试2");
                //转换为rgb格式
                sws_scale(swsContext,(const uint8_t *const *)frame->data,frame->linesize,0,
                          frame->height,rgb_frame->data,
                          rgb_frame->linesize);

                LOGE("测试3");
                //  rgb_frame是有画面数据
                uint8_t *dst= (uint8_t *) native_outBuffer.bits;
                LOGE("测试4");
                //            拿到一行有多少个字节 RGBA
                int destStride=native_outBuffer.stride*4;

                //像素数据的首地址
                uint8_t * src=  rgb_frame->data[0];
//            实际内存一行数量
                int srcStride = rgb_frame->linesize[0];
                //int i=0;
                for (int i = 0; i < avCodecContext->height; ++i) {

//                memcpy(void *dest, const void *src, size_t n)
                    //将rgb_frame中每一行的数据复制给nativewindow
                    memcpy(dst + i * destStride,  src + i * srcStride, srcStride);
                }
//解锁

                ANativeWindow_unlockAndPost(nativeWindow);
                usleep(1000 * 16);
            }
        }
        av_free_packet(packet);
    }
    //释放
    ANativeWindow_release(nativeWindow);
    av_frame_free(&frame);
    av_frame_free(&rgb_frame);
    avcodec_close(avCodecContext);
    avformat_free_context(avFormatContext);
    env->ReleaseStringUTFChars(inputStr_, inputPath);
}


JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_view_MyVideoView_renderRe
        (JNIEnv * env, jobject thiz, jstring inputStr_,jobject surface){

    const char *inputPath = env->GetStringUTFChars(inputStr_, NULL);
    //注册各大组件
    av_register_all();
    LOGE("注册成功");
    AVFormatContext *avFormatContext = avformat_alloc_context();//获取上下文

    int error;
    char buf[] = "";
    //打开视频地址并获取里面的内容(解封装)
    if (error = avformat_open_input(&avFormatContext, inputPath, NULL, NULL) < 0) {
        av_strerror(error, buf, 1024);
        // LOGE("%s" ,inputPath)
        LOGE("Couldn't open file %s: %d(%s)", inputPath, error, buf);
        // LOGE("%d",error)
        LOGE("打开视频失败");
        return;
    }
    if (avformat_find_stream_info(avFormatContext, NULL) < 0) {
        LOGE("获取内容失败");
        return;
    }
    //获取到整个内容过后找到里面的视频流
    int video_index=-1;
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {
        if (avFormatContext->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            //如果是视频流,标记一哈
            video_index = i;
        }
    }
    LOGE("成功找到视频流");

    //对视频流进行解码
    //获取解码器上下文
    AVCodecContext *avCodecContext = avFormatContext->streams[video_index]->codec;
    //获取解码器
    AVCodec *avCodec = avcodec_find_decoder(avCodecContext->codec_id);
    //打开解码器
    if (avcodec_open2(avCodecContext, avCodec, NULL) < 0) {
        LOGE("打开失败");
        return;
    }

    //申请AVPacket
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    av_init_packet(packet);
    //申请AVFrame
    AVFrame *frame = av_frame_alloc();//分配一个AVFrame结构体,AVFrame结构体一般用于存储原始数据，指向解码后的原始帧
    AVFrame *rgb_frame = av_frame_alloc();//分配一个AVFrame结构体，指向存放转换成rgb后的帧
    //输出文件
    //FILE *fp = fopen(outputPath,"wb");


    //缓存区
    uint8_t  *out_buffer= (uint8_t *)av_malloc(avpicture_get_size(AV_PIX_FMT_RGBA,
                                                                  avCodecContext->width,avCodecContext->height));
    //与缓存区相关联，设置rgb_frame缓存区
    avpicture_fill((AVPicture *)rgb_frame,out_buffer,AV_PIX_FMT_RGBA,avCodecContext->width,avCodecContext->height);


    SwsContext* swsContext = sws_getContext(avCodecContext->width,avCodecContext->height,avCodecContext->pix_fmt,
                                            avCodecContext->width,avCodecContext->height,AV_PIX_FMT_RGBA,
                                            SWS_BICUBIC,NULL,NULL,NULL);

    //取到nativewindow
    ANativeWindow *nativeWindow=ANativeWindow_fromSurface(env,surface);
    if(nativeWindow==0){
        LOGE("nativewindow取到失败");
        return;
    }
    //视频缓冲区
    ANativeWindow_Buffer native_outBuffer;


    int frameCount;
    int h =0;
    LOGE("解码 ");
    while (av_read_frame(avFormatContext, packet) >= 0) {
        LOGE("解码 %d",packet->stream_index);
        LOGE("VINDEX %d",video_index);
        if(packet->stream_index==video_index){
            LOGE("解码 hhhhh");
            //如果是视频流
            //解码
            avcodec_decode_video2(avCodecContext, frame, &frameCount, packet);
            LOGE("解码中....  %d",frameCount);
            if (frameCount) {
                LOGE("转换并绘制");
                //说明有内容
                //绘制之前配置nativewindow
                ANativeWindow_setBuffersGeometry(nativeWindow,avCodecContext->width,avCodecContext->height
                        ,WINDOW_FORMAT_RGBA_8888);
                //上锁
                ANativeWindow_lock(nativeWindow, &native_outBuffer, NULL);
                //转换为rgb格式
                sws_scale(swsContext,(const uint8_t *const *)frame->data,frame->linesize,0,
                          frame->height,rgb_frame->data,
                          rgb_frame->linesize);
                //  rgb_frame是有画面数据
                uint8_t *dst= (uint8_t *) native_outBuffer.bits;
//            拿到一行有多少个字节 RGBA
                int destStride=native_outBuffer.stride* sizeof(int32_t);
                //像素数据的首地址
                uint8_t * src=  rgb_frame->data[0];
//            实际内存一行数量
                int srcStride = rgb_frame->linesize[0];
                //int i=0;
                for (int i = 0; i < avCodecContext->height; ++i) {
//                memcpy(void *dest, const void *src, size_t n)
                    //将rgb_frame中每一行的数据复制给nativewindow
                    memcpy(dst + i * destStride,  src + i * srcStride, srcStride);
                }
//解锁
                ANativeWindow_unlockAndPost(nativeWindow);
                usleep(1000 * 16);

            }
        }
        av_free_packet(packet);
    }
    //释放
    ANativeWindow_release(nativeWindow);
    av_frame_free(&frame);
    av_frame_free(&rgb_frame);
    avcodec_close(avCodecContext);
    avformat_free_context(avFormatContext);
    env->ReleaseStringUTFChars(inputStr_, inputPath);

}