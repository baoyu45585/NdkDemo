package com.ms.android.ndkdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.android.ndkdemo.entity.Person;

/**
 * @author shenjianbin@ms.com
 * @since 2017/8/29
 */
public class JniObjectActivity  extends AppCompatActivity implements View.OnClickListener{

    TextView tvText;
    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_obj);
        context=this;
        tvText= (TextView) findViewById(R.id.tvText);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_10).setOnClickListener(this);
        findViewById(R.id.btn_11).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                getObjectClass();
                break;
            case R.id.btn_1:
                getStaticMethodID();
                break;
            case R.id.btn_2:
                Person.name_static="wubb";
                Person.age_static=20;
                Person person=new Person();
                person.name="wubb";
                person.age=20;
                getJavaObjectField(person);
                Toast.makeText(context,"Jni调查成功修改名字~"+person.name,Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_3:
                Person pn=new Person();
                pn.name="wubb";
                pn.age=20;
                getMethodID(pn);

                break;
            case R.id.btn_4:
                jniLock(new Object());
                break;
            case R.id.btn_5:
                exceptionOccurred();
                break;
            case R.id.btn_6:
                try {
                    throwNew();
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                }
                break;
            case R.id.btn_7:
                deleteLocalRef();
                break;
            case R.id.btn_8:
                newGlobalRef();
                break;
            case R.id.btn_9:
                newWeakGlobalRef();
                showStatic();
                break;
            case R.id.btn_10:
                showStatic();
                break;
            case R.id.btn_11:
                init();
                startCThread();
                break;
        }
    }

    static {
        System.loadLibrary("three-obj");
    }


//    GetObjectClass函数用来获取Java对象对应的类类型
//    GetMethodID函数用来获取Java类实例方法的方法ID
//   Call<Type>Method函数用来调用Java类实例特定返回值的方法，比如CallVoidMethod，调用java没有返回值的方法，CallLongMethod用来调用Java返回值为Long的方法，等等

   public native void getObjectClass();

    //使用jni调用这个方法
    public void helloWorld(String msg){
        tvText.setText(msg);
        Toast.makeText(context,"Jni调查成功~"+msg,Toast.LENGTH_LONG).show();
    }
    public native void getMethodID(Person person);

    public void helloWorld(Person person){
        Toast.makeText(context,"Jni调查成功~"+person.name,Toast.LENGTH_LONG).show();
    }

    public String helloWorld(){
        return "我的hello";
    }

//    GetStaticMethodID函数用来获取Java类静态方法的方法ID
//    CallStatic<Type>Method函数用来调用Java类特定返回值的静态方法，比如CallStaticVoidMethod，调用java没有返回值的静态方法，CallStaticLongMethod用来调用Java返回值为Long的静态方法，等等
    public native void getStaticMethodID();

    public static void statichelloWorld(String msg){
        Log.e("测试静态的方法调用","调用statichelloWorld成功="+msg);
    }

//    GetFieldID函数用来获取Java字段的字段ID
//    Get<Type>Field用来获取Java类字段的值，比如用GetIntField函数获取Java int型字段的值，用GetLongField函数获取Java long字段的值，用GetObjectField
// 函数获取Java引用类型字段的值
    public native void getJavaObjectField(Person person);

//    JNI可以使用Java对象进行线程同步 MonitorEnter函数用来锁定Java对象 MonitorExit函数用来释放Java对象锁
   public native void jniLock(Object obj);


//    当JNI函数调用的Java方法出现异常的时候，并不会影响JNI方法的执行，但是我们并不推荐JNI函数忽略Java方法出现的异常继续执行，
//    这样可能会带来更多的问题。我们推荐的方法是，当JNI函数调用的Java方法出现异常的时候，JNI函数应该合理的停止执行代码。
//    ExceptionOccurred函数用来判断JNI函数调用的Java方法是否出现异常
//    ExceptionClear函数用来清除JNI函数调用的Java方法出现的异常
    public native void  exceptionOccurred();

    public void intHelloWorld(){
        throw new NullPointerException("null pointer occurred");
        //Log.i("hello","hello world");
    }

    //JNI通过ThrowNew函数抛出Java类型的异常
    public native void throwNew();

//    JNI对象的局部引用
//    在JNI接口函数中引用JNI对象的局部变量，都是对JNI对象的局部引用，一旦JNI接口函数返回，所有这些JNI对象都会被自动释放。
//    不过我们也可以采用JNI代码提供的DeleteLocalRef函数来删除一个局部JNI对象引用
    public native void deleteLocalRef();

    public void setHelloWorld(){
        Toast.makeText(context,"hello world~",Toast.LENGTH_LONG).show();
    }

//    对于JNI对象，绝对不能简单的声明一个全局变量，在JNI接口函数里面给这个全局变量赋值这么简单，一定要使用JNI代码提供的管理
// JNI对象的函数，否则代码可能会出现预想不到的问题。JNI对象的全局引用分为两种，一种是强全局引用，这种引用会阻止Java的垃圾回
// 收器回收JNI代码引用的Java对象，另一种是弱全局引用，这种全局引用则不会阻止垃圾回收器回收JNI代码引用的Java对象。

//    强全局引用
//    NewGlobalRef用来创建强全局引用的JNI对象
//    DeleteGlobalRef用来删除强全局引用的JNI对象
    public native void newGlobalRef();

//    弱全局引用
//    NewWeakGlobalRef用来创建弱全局引用的JNI对象
//    DeleteWeakGlobalRef用来删除弱全局引用的JNI对象
//    IsSameObject用来判断两个JNI对象是否相同
    public native void newWeakGlobalRef();

//    静态JNI方法和实例JNI方法区别
    public native static void showStatic();


//    线程A不能使用线程B的 JNIEnv，就好像特朗普的翻译官不会给普京用一个道理，因此，我们需要一个方法来获取当前线程的JNIEnv
//    如何使用C语言线程调用java方法

    //提供给C语言回调的方法
    public void callMeBaby(final String msg) {

        //在主线程运行
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvText.setText(msg);
            }
        });
    }
    //初始化JNI库
    public native void init();
    //实行线程
    public native  void startCThread();

}
