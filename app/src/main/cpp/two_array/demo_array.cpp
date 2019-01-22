#include <jni.h>
#include <string.h>
#include <sstream>
#include<android/log.h>
#define TAG "zdbb-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型
//LOGI("测试array：%d",sf);

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_getIntArrayElements(JNIEnv *env, jobject instance,
                                                                 jintArray array_) {
     jint *intArray=env->GetIntArrayElements(array_,NULL);//获取基本类型int数组的元素
    if (intArray==NULL)
        return;
     int len=env->GetArrayLength(array_);
     for(int i=0;i<len;i++){
       intArray[i]=intArray[i]*10;
     }
    env->ReleaseIntArrayElements(array_, intArray, 0);//释放数组把intArray拷贝到array_通知虚拟机回收内存
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_getIntArrayRegion(JNIEnv *env, jobject instance,
                                                            jintArray array_) {

    jint *subArray=new jint[2];//new是堆里要释放
    jint sum=0;
    env->GetIntArrayRegion(array_,0,2,subArray);
    for(int i=0;i<2;i++){
        sum+=subArray[i];
    }
    delete[] subArray;//在visual studio上删除是不能用subArray的内存
    LOGI("测试array：%d",*subArray);//测试在android ndk上可以再用怀疑是通知虚拟机回收内存什么时候释放就不知道
    return sum;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_setIntArrayRegion(JNIEnv *env, jobject instance,
                                                                 jintArray array_) {
    jint *subArray=new jint[3];
    for (int i = 0; i < 3; ++i) {
        *(subArray+i)=i+10;
    }
    //将buffers这个数组中值设置到数组从0开始长度是3的值中
    env->SetIntArrayRegion(array_,0,3,subArray);
    delete[](subArray);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_getObjectArrayElement(JNIEnv *env, jobject instance,
                                                                   jobjectArray array_) {
    int len=env->GetArrayLength(array_);
    for (int i = 0; i < len; ++i) {
        jstring item=  (jstring)env->GetObjectArrayElement(array_,i);
        const char *str=env->GetStringUTFChars(item,NULL); //  GetStringUTFChars函数用来从jstring获取UTF-8 C/C++字符串
        LOGI("遍历数据：%s",str);
        env->ReleaseStringUTFChars(item, str);
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_setObjectArrayElement(JNIEnv *env, jobject instance, jobjectArray array) {

    int len=env->GetArrayLength(array);
    for (int i = 0; i < len; ++i) {
        char* s="我的世界";
        char buf[32];//c语言做法
        sprintf(buf,"我的世界%d",i);

        std::stringstream ss;//c++的做法
        ss<<s<<i;
       std::string zz= ss.str();
        // int slen=strlen(s);
        jstring obj=env->NewStringUTF(zz.c_str());
        env->SetObjectArrayElement(array,i,obj);//SetObjectArrayElement函数用来设置JNI对象数组元素
    }

}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_newDirectByteBuffer(JNIEnv *env, jobject instance) {

    const char *data="hello world";
    int len=strlen(data);
    jobject obj=env->NewDirectByteBuffer((void*)data,len);
    long capicity=env->GetDirectBufferCapacity(obj);
    char *data2=(char*)env->GetDirectBufferAddress(obj);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_getDirectBufferAddress(JNIEnv *env, jobject instance,
                                                                 jobject buffer_, jint len) {
    void* buffer =env->GetDirectBufferAddress(buffer_);
    if (NULL == buffer) {
        LOGI("natilayer->setImageDirectBuffer ->Invalid buffer!");
        return ;
    }
    int  g_bufferLen = static_cast<int>(len);
    unsigned char* str= static_cast<unsigned char *>(buffer);
    // 一个put进去的字符占俩个字节, 所以%s不能直接显示, 所以需要循环显示
    for(int i = 0; i < 20; ++i) {
        LOGI("Buffer里面的数据为: %d", str[i]);
        str[i]=i;
    }
    LOGI("Buffer里面的数据为: %s, 容量为: %d", str, g_bufferLen);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniArrayActivity_setbyteArrayElement(JNIEnv *env, jobject instance,
                                                                 jbyteArray array_) {
    int chars_len = env->GetArrayLength(array_);
    jbyte *ar = env->GetByteArrayElements(array_, NULL);
    char* chars = new char[chars_len + 1];
    memset(chars,0,chars_len + 1);
    memcpy(chars, ar, chars_len);
    chars[chars_len] = 0;
    LOGE("array=%s",ar);

    env->ReleaseByteArrayElements(array_, ar, 0);
}