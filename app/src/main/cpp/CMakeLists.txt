# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(lib_src_DIR ${PROJECT_SOURCE_DIR}/${ANDROID_ABI})

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.



add_library(avcodec SHARED IMPORTED)
add_library(avdevice SHARED IMPORTED)
add_library(avfilter SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(postproc SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)

set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libavcodec.so)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libavdevice.so)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libavfilter.so)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libavformat.so)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libavutil.so)
set_target_properties(postproc PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libpostproc.so)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libswresample.so)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/libswscale.so)

include_directories(${lib_src_DIR}/include)

##添加一个库
add_library( # Sets the name of the library. 设置库名字
        native
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).  添加源文件，编译成一个库
        CallTest.cpp
        testnative.c
        externtest.c
        test.cpp
        native.c)
#
#add_library(
#        ffmpegtest
#        SHARED
#       )




# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
#找到log库
find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
##给目标库添加依赖库
target_link_libraries( # Specifies the target library.
        native  #目标库
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        postproc
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        # 依赖库，可以写多个
        ${log-lib}
        android)

####给目标库添加依赖库
#target_link_libraries( # Specifies the target library.
#        ffmpegtest  #目标库
#        avcodec
#        avdevice
#        avfilter
#        avformat
#        avutil
#        postproc
#        swresample
#        swscale
#        # Links the target library to the log library
#        # included in the NDK.
#        # 依赖库，可以写多个
#        ${log-lib}
#        android)

