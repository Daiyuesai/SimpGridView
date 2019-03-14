#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include "android_debug.h"

//命名规则 Java_包名(文件中间用 _ 符号隔开)_类名_方法名(方法名中如果包涵'_'符号则用‘_1’表示)
jstring Java_com_xiaozhanxiang_simplegridview_ui_JINActivity_getStringForJin(
        JNIEnv *env,
        jobject  thiz ) {
    char*  str = "Hello from C";
    LOGI("WOEIFJWE ");
    return (*env)->NewStringUTF(env,str);
}

