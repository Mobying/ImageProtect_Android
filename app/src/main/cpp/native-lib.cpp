#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>

extern "C"
jstring
Java_bying_imageprotect_activity_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
