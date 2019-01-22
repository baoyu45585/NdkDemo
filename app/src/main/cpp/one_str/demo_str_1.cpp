#include <jni.h>
#include <string>


extern "C" JNIEXPORT jstring
JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_stringHello(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "sdfsdfsfsd";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring
JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_newStringUTF(
        JNIEnv *env,
        jobject /* this */) {
    char *str="helloboy";
    jstring jstr2=env->NewStringUTF(str);//NewStringUTF函数用来生成UTF-8 JNI字符串
    return jstr2;

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_newString(JNIEnv *env, jobject instance,
                                                        jstring jstr) {

    const jchar *jchar2=env->GetStringChars(jstr,NULL);// GetStringChars函数用来从jstring获取Unicode C/C++字符串
    size_t len=env->GetStringLength(jstr);
    jstring jstr3=env->NewString(jchar2,len);//NewString函数用来生成Unicode JNI字符串
    env->ReleaseStringChars(jstr,jchar2);//ReleaseStringChars函数用来释放Unicode C/C++字符串
    return jstr3;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringChars(JNIEnv *env, jobject instance,jstring str_) {
    const jchar *jchar2=env->GetStringChars(str_,NULL);// GetStringChars函数用来从jstring获取Unicode C/C++字符串
    env->ReleaseStringChars(str_,jchar2);//ReleaseStringChars函数用来释放Unicode C/C++字符串
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringUTFChars(JNIEnv *env, jobject instance, jstring str_) {
    const char *str=env->GetStringUTFChars(str_,NULL); //  GetStringUTFChars函数用来从jstring获取UTF-8 C/C++字符串
    env->ReleaseStringUTFChars(str_,str);//ReleaseStringUTFChars函数用来释放UTF-8 C/C++字符串
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringRegion(JNIEnv *env, jobject instance,
                                                              jstring jstr) {
     const char *str=env->GetStringUTFChars(jstr,NULL);
     char *subStr=new char[3];
     env->GetStringUTFRegion(jstr,0,3,subStr);
     jstring str2=env->NewStringUTF(subStr);
     env->ReleaseStringUTFChars(jstr,str);
    delete[](subStr);
    return str2;
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringUTFRegion(JNIEnv *env, jobject instance,
                                                                 jstring jstr) {
    const jchar *jchar2=env->GetStringChars(jstr,NULL);
    jchar *subJstr=new jchar[3];
    env->GetStringRegion(jstr,0,3,subJstr);
   jstring str3=env->NewString(subJstr,3);
    env->ReleaseStringChars(jstr,jchar2);

    return str3;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringLength(JNIEnv *env, jobject instance,
                                                              jstring str_) {
    jint len=env->GetStringLength(str_);
    return  len ;

}

extern "C"
JNIEXPORT jint JNICALL
Java_com_ms_android_ndkdemo_JniOneStrActivity_getStringUTFLength(JNIEnv *env, jobject instance,
                                                                 jstring str_) {
    return   env->GetStringUTFLength(str_);
}
