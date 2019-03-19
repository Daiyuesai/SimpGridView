//
// Created by daiyuesai on 2019/3/12.
//

#ifndef SIMPLEGRIDVIEW_TEST_H
#define SIMPLEGRIDVIEW_TEST_H

#include <jni.h>

#ifndef _Included_com_xiaozhanxiang_simplegridview_utils_JNIUtils
#define _Included_com_xiaozhanxiang_simplegridview_utils_JNIUtils
#ifdef __cplusplus
extern "C" {
#endif

class test {
 private:
    int w;
    int h;
 public:
     test(int a , int b);  //构造方法
     ~test();   //结构方法
     int getWidth();
     int getHeight();
     int getArea();
};

JNIEXPORT jstring JNICALL Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_getNativeFormCCC
        (JNIEnv *, jobject, jstring);

JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_view_MyVideoView_render
        (JNIEnv *, jobject, jstring,jobject);

/*
 * Class:     com_xiaozhanxiang_simplegridview_view_MyVideoView
 * Method:    renderRe
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_com_xiaozhanxiang_simplegridview_view_MyVideoView_renderRe
        (JNIEnv *, jobject, jstring,jobject);


#ifdef __cplusplus
}
#endif
#endif
#endif //SIMPLEGRIDVIEW_TEST_H
