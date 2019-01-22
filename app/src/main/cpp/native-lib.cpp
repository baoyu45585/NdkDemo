#include <jni.h>
#include <string>


//extern “C”]JNIEXPORT 函数返回值 JNICALL 完整的函数声明(JNIENV *env, jobject thiz, …)
//其中extern “C”根据需要动态添加，如果是C++代码，则必须要添加extern “C”声明，如果是C代码，则不用添加

//现在这段JNI函数声明代码采用的是C++语言写的，所以需要添加extern "C"声明，如果源代码是C语言，则不需要添加这个声明。
//
//JNIEXPORT 这个关键字说明这个函数是一个可导出函数，学过C/C++的朋友都知道，C/C++ 库里面的函数有些可以直接被外部调用，
//有些不可以，原因就是每一个C/C++库都有一个导出函数列表，只有在这个列表里面的函数才可以被外部直接调用，类似Java的public函数和private函数的区别。
//
//JNICALL 说明这个函数是一个JNI函数，用来和普通的C/C++函数进行区别，实际发现不加这个关键字，Java也是可以调用这个JNI函数的。
//Void 说明这个函数的返回值是void，如果需要返回值，则把这个关键字替换成要返回的类型即可。
//
//Java_com_ms_android_ndkdemo_MainActivity_stringFromJNI(JNIEnv*env, jobject thiz,jstring msg)这是完整的JNI函数声明，JNI函数名的原型如下：
//Java_ + JNI方法所在的完整的类名，把类名里面的”.”替换成”_” + 真实的JNI方法名，这个方法名要和Java代码里面声明的JNI方法名一样+
//JNI函数必须的默认参数(JNIEnv* env, jobjectthiz)
//
//env参数是一个指向JNIEnv函数表的指针，
//thiz参数代表的就是声明这个JNI方法的Java类的引用
//msg参数就是和Java声明的JNI函数的msg参数对于的JNI函数参数



extern "C" JNIEXPORT jstring
JNICALL
Java_com_ms_android_ndkdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
