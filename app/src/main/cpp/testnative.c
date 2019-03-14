#include <stdlib.h>
#include <stdio.h>
#include <jni.h>
#include <string.h>
#include "android_debug.h"
#include <stdarg.h>

#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libavfilter/avfilter.h"




//#define  const 定义常量
//struct 结构体
// union 联合体
//extern 声明函数或变量 在其它地方定义
//register
// 指针   指针初始化赋值为 NULL Null也是一个整数0 , 一般的操作系统都不会允许操作改地址，同时也方便判读指针是否已经初始化 if(p) true 已经初始化  false 没有初始化
// C 中用0和null 表示false 其它的都为true
// 方法指针
//

 int a = 3;
 int b = 5;
 extern int sum();
 extern int callTest();



jstring  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_getNativeValue
(JNIEnv * env, jobject thiz, jstring sss){
   // char*  str = "Hello from C";

    jboolean isCopy= JNI_FALSE ;
    char* s = (char *) (*env) ->GetStringUTFChars(env, sss, &isCopy);
    int sume = sum(); //这里是测试 extern 关键字
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


   // int test = callTest();
   // LOGE("从cpp 里来的代码：%d",test);
    LOGE("求和的值 = %d",sume);
    strcat(s,"我是测试啊");
    int test = callTest();
    LOGE("我是cpp %d",test);
    return (*env)->NewStringUTF(env,info);
}



jint  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_nativeInt
        (JNIEnv * env, jobject thiz, jint i, jint j){
    return i +j;
}
 jboolean  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_nativeArrayJava
        (JNIEnv* env, jobject thiz, jintArray array){
        int len = (*env)->GetArrayLength(env,array);
        jboolean isCopy= JNI_FALSE ;
        //第三个参数，系统是否复制了一份内存
        int* p = (*env)->GetIntArrayElements(env, array,  &isCopy);
        int i ;
        for(i = 0;i<len;i++){
            *(p+i) += 10;
        }
        LOGI("我单位甲方违反");
     //这句话一定要加  不然没有效果
    // void        (*ReleaseIntArrayElements)(JNIEnv*, jintArray,jint*, jint);
      // jint mode 取值 0   JNI_COMMIT JNI_ABORT
     // 0：复制内容并释放elems缓冲区
     //JNI_COMMIT：复制内容但不释放elems缓冲区
     //JNI_ABORT：释放缓冲区而不复制可能的更改
     (*env) ->ReleaseIntArrayElements(env,array,p,0);

     return isCopy;
}
 void  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_callJavaVoid
        (JNIEnv * env, jobject thiz){
      //找到字节码
      //找到方法
      //创建对象
      //调用方法
     jclass clazz = (*env) ->FindClass(env,"com/xiaozhanxiang/simplegridview/utils/JNIUtils");
     jmethodID jmethodID = (*env)->GetMethodID(env,clazz,"nativeCallJavaVoid","()V");
     (*env)->CallVoidMethod(env,thiz,jmethodID);
  }

  void  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_callJavaString
        (JNIEnv * env, jobject thiz, jstring str){
      jboolean isCopy= JNI_FALSE ;
      char* s = (char *) (*env) ->GetStringUTFChars(env, str, &isCopy);
      strcat(s,"我是测试啊");
      jclass clazz = (*env) ->FindClass(env,"com/xiaozhanxiang/simplegridview/utils/JNIUtils");
      jmethodID jmethodID = (*env)->GetMethodID(env,clazz,"nativeCallJavaString","(Ljava/lang/String;)V");
      (*env)->CallVoidMethod(env,thiz,jmethodID,(*env)->NewStringUTF(env,s));
  }


 void  Java_com_xiaozhanxiang_simplegridview_utils_JNIUtils_callJavaArray
        (JNIEnv * env, jobject thiz, jintArray array){
      int arr[]= {1,2,3,4,56,7,8};
      int length = sizeof(arr)/ sizeof(int);
     jintArray reArray = (*env)->NewIntArray(env,length);
     // (*SetIntArrayRegion)(JNIEnv*, jintArray,jsize, jsize, const jint*);  //第三个参数 从那里开始复制  第四个参数 复制的长度
     (*env)->SetIntArrayRegion(env,reArray,0,length,arr);
     jclass clazz = (*env) ->FindClass(env,"com/xiaozhanxiang/simplegridview/utils/JNIUtils");
     jmethodID jmethodID = (*env)->GetMethodID(env,clazz,"nativeCallJavaArray","([I)V");
     (*env)->CallVoidMethod(env,thiz,jmethodID,reArray);



  }
