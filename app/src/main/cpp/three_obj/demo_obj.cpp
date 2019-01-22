#include <jni.h>
#include <string>
#include <sstream>
#include<android/log.h>
#define TAG "zdbb-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型
//LOGI("测试array：%d",sf);


//Java类型签名映射表 JNI获取Java类的方法ID和字段ID，都需要一个很重要的参数，就是Java类的方法和字段的签名，这个签名需要通过下面的表来获取，这个表很重要，建议大家一定要记住。
//Java类型 签名
//Boolean Z
//Byte B
//Char C
//Short S
//Integer I
//Long J
//Float F
//Double D
//Void V
//任何Java类的全名
//L任何Java类的全名;
//比如Java String类对应的签名是Ljava/lang/String;
//type[]
//type[
//这个就是Java数组的签名，比如Java int[]的签名是[I，Java long[]的签名就是[J，Java String[]的签名是 [Ljava/lang/String;
//方法类型
//（参数类型）返回值 类型，
//比如Java方法void hello(String msg,String msg2)对应的签名就是(Ljava/lang/String; Ljava/lang/String;)V
//再比如Java方法String getNewName(String name)对应的签名是（Ljava/lang/String;) Ljava/lang/String;
//再比如Java方法long add(int a,int b)对应的签名是(II)J


extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_getObjectClass(JNIEnv *env, jobject instance) {
    jclass clazz=env->GetObjectClass(instance);
    if(clazz==NULL) return;

    //调用JniObjectActivity的 public void helloWorld(String msg)
    //GetMethodID(jclass clazz /*对应调用类对象*/, const char* name/*对应调用类的方法名*/, const char* sig/*参数和还回值的签名*/)
    jmethodID methodID=env->GetMethodID(clazz,"helloWorld","(Ljava/lang/String;)V");
    if(methodID==NULL) return;
    const char *msg="hello world";
    jstring jmsg=env->NewStringUTF(msg);
    env->CallVoidMethod(instance,methodID,jmsg);

    //调用JniObjectActivity的 String helloWorld()
    jmethodID method=env->GetMethodID(clazz,"helloWorld","()Ljava/lang/String;");
    if(method==NULL) return;
    jstring jstring1= static_cast<jstring>(env->CallObjectMethod(instance, method));
    const char* ch=env->GetStringUTFChars(jstring1,NULL);
    LOGI("测试还回string：%s",ch);
    env->ReleaseStringUTFChars(jstring1,ch);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_getMethodID(JNIEnv *env, jobject instance,
                                                          jobject person) {
    jclass clazz=env->GetObjectClass(instance);
    if(clazz==NULL) return;
    jmethodID methodID=env->GetMethodID(clazz,"helloWorld","(Lcom/ms/android/ndkdemo/entity/Person;)V");
    if(methodID==NULL) return;
    env->CallVoidMethod(instance,methodID,person);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_getStaticMethodID(JNIEnv *env, jobject instance) {
    jclass clazz=env->GetObjectClass(instance);
    if(clazz==NULL) return;
    //调用JniObjectActivity的 public void helloWorld(String msg)
    //GetMethodID(jclass clazz /*对应调用类对象*/, const char* name/*对应调用类的方法名*/, const char* sig/*参数和还回值的签名*/)
    jmethodID methodID=env->GetStaticMethodID(clazz,"statichelloWorld","(Ljava/lang/String;)V");
    if(methodID==NULL) return;
    const char *msg="hello world";
    jstring jmsg=env->NewStringUTF(msg);
    env->CallStaticVoidMethod(clazz,methodID,jmsg);

}



extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_getJavaObjectField(JNIEnv *env, jobject instance,
                                                                 jobject person) {
   jclass clazz=env->GetObjectClass(person);
    //GetFieldID(jclass clazz/*调用类名对象*/, const char* name/*类对象的属性名*/, const char* sig/*属性名对应类型签名*/)
   jfieldID name_fieldID=env->GetFieldID(clazz,"name","Ljava/lang/String;");
   jstring name=(jstring) env->GetObjectField(person,name_fieldID);//获取属性的值

   jfieldID age_fieldID=env->GetFieldID(clazz,"age","I");
   jint age=env->GetIntField(person,age_fieldID);

    const char* str=env->GetStringUTFChars(name,NULL);
    LOGI("测试获取类的属性-年龄:%d;名字：%s",age,str);
    env->ReleaseStringUTFChars(name,str);

    jfieldID name_static_fieldID=env->GetStaticFieldID(clazz,"name_static","Ljava/lang/String;");
   jstring name_static=(jstring) env->GetStaticObjectField(clazz,name_static_fieldID);

   jfieldID age_static_fieldID=env->GetStaticFieldID(clazz,"age_static","I");
   jint age_static=env->GetStaticIntField(clazz,age_static_fieldID);

    const char* strSet=env->GetStringUTFChars(name_static,NULL);
    LOGI("测试获取类静态的属性-年龄:%d;名字：%s",age_static,strSet);
    env->ReleaseStringUTFChars(name_static,strSet);

    const char *msg="hello world";
    jstring jmsg=env->NewStringUTF(msg);
    env->SetObjectField(person,name_fieldID,jmsg);//修改属性的值

}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_jniLock(JNIEnv *env, jobject instance, jobject obj) {
    env->MonitorEnter(obj);
    //do something
    env->MonitorExit(obj);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_exceptionOccurred(JNIEnv *env, jobject instance) {
    jclass clazz=env->GetObjectClass(instance);
   if(clazz==NULL) return;
   jmethodID methodID=env->GetMethodID(clazz,"intHelloWorld","()V");
   if(methodID==NULL) return;
   env->CallVoidMethod(instance,methodID);
   if(env->ExceptionOccurred()!=NULL){//ExceptionOccurred函数用来判断JNI函数调用的Java方法是否出现异常
        env->ExceptionClear();// ExceptionClear函数用来清除JNI函数调用的Java方法出现的异常
        LOGE("hello=%s","program end with java exception");
        return;
       }
    LOGE("hello=%s","program end normallly");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_throwNew(JNIEnv *env, jobject instance) {
    // FindClass(const char* name/*name这里就是类包名和类名和对应类型签名不一样*/)
    jclass clazz=env->FindClass("java/lang/NullPointerException");
    if(clazz==NULL) return;
    env->ThrowNew(clazz,"null pointer exception occurred");
}



extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_deleteLocalRef(JNIEnv *env, jobject instance) {
    jclass clazz=env->GetObjectClass(instance);
    if(clazz==NULL) return;
    jmethodID methodID=env->GetMethodID(clazz,"setHelloWorld","()V");
    if(methodID==NULL) {
        if(env->ExceptionOccurred()!=NULL){//ExceptionOccurred函数用来判断JNI函数调用的Java方法是否出现异常
            env->ExceptionClear();// ExceptionClear函数用来清除JNI函数调用的Java方法出现的异常
            LOGE("hello=%s","program end with java exception");
        }
        return;
    }
    env->CallVoidMethod(instance,methodID);
    env->DeleteLocalRef(clazz);
}

jobject thiz;
extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_newGlobalRef(JNIEnv *env, jobject instance) {
    //thiz=instance;//不能这样给全局JNI对象赋值，要采用下面这种方式
    thiz=env->NewGlobalRef(instance);//生成全局的JNI对象引用，这样生成的全局的JNI对象才可以在其它函数中使用

    env->DeleteGlobalRef(thiz);//假如我们不需要gThiz这个全局的JNI对象引用，我们可以把它删除掉

}

jobject gThiz;
extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_newWeakGlobalRef(JNIEnv *env, jobject instance) {
//gThiz=instance;//不能这样给全局JNI对象赋值，要采用下面这种方式
    gThiz=env->NewWeakGlobalRef(instance);//生成全局的JNI对象引用，这样生成的全局的JNI对象才可以在其它函数中使用

    if(env->IsSameObject(gThiz,NULL)){
        //弱全局引用已经被Java的垃圾回收器回收
        }
    env->DeleteWeakGlobalRef(gThiz);//假如我们不需要gThiz这个全局的JNI对象引用，我们可以把它删除掉
}


//区别普通的JNI方法对应的JNI函数的第二个参数是jobject类型，而静态的JNI方法对应的JNI函数的第二个参数是jclass类型
extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_showStatic(JNIEnv *env, jclass type) {
    const char *msg="hello world";
    jstring jmsg=env->NewStringUTF(msg);
}




//声明一个静态变量
static JavaVM *JVM;
static jobject JniObjectActivity;
static jmethodID methodCallMeBaby;

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_init(JNIEnv *env, jobject instance) {
    //获取Java虚拟机，赋值给静态变量
    env->GetJavaVM(&JVM);
    //获取Java对象并做static强引用
    JniObjectActivity = env->NewGlobalRef(instance);
    //获取该对象的Java类
    jclass clazz = env->GetObjectClass(JniObjectActivity);
    methodCallMeBaby = env->GetMethodID(clazz, "callMeBaby", "(Ljava/lang/String;)V");
}

//通过Java虚拟机获取到当前线程的JNIEnv
JNIEnv *getCurrentJNIEnv() {
    if (JVM != NULL) {
        JNIEnv *env_new;
        JVM->AttachCurrentThread(&env_new, NULL);
        return env_new;
    } else {
        return NULL;
    }
}


void *callingJava(void *arg) {
    JNIEnv *jniEnv = getCurrentJNIEnv();
    if (jniEnv != NULL&&methodCallMeBaby!=NULL) {
        std::string msg = "I'm fucking love u!";
        jniEnv->CallVoidMethod(JniObjectActivity, methodCallMeBaby,
                               jniEnv->NewStringUTF(msg.c_str()));
//        jniEnv->DeleteGlobalRef(JniObjectActivity);//假如我们不需要gThiz这个全局的JNI对象引用，我们可以把它删除掉
    }
    return NULL;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_ms_android_ndkdemo_JniObjectActivity_startCThread(JNIEnv *env, jobject instance) {
//创建一个C语言的线程,执行上面的callingJava方法
    pthread_t pthread;
    pthread_create(&pthread, NULL, callingJava, NULL);
}