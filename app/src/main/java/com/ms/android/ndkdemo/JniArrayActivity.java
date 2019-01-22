package com.ms.android.ndkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

/**
 * @author shenjb@ms.com
 * @since 2017/8/29
 */
public class JniArrayActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_array);
        tvText= (TextView) findViewById(R.id.tvText);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
    }

    @Override
    public void onClick(View v) {
        int sum=0;
        switch (v.getId()){
            case R.id.btn:
                int[]array={1,2,3};
                getIntArrayElements(array);
                for (int i = 0; i <array.length ; i++) {
                    sum+=array[i];
                }
                tvText.setText(sum+"");
                break;
            case R.id.btn_1:
                int[]arr={1,2,3};
                tvText.setText(getIntArrayRegion(arr)+"");
                break;
            case R.id.btn_2:
                int[]arrs ={1,2,3};
                setIntArrayRegion(arrs);
                for (int i = 0; i <arrs.length ; i++) {
                    sum+=arrs[i];
                }
                tvText.setText(sum+"");
                break;
            case R.id.btn_3:
                String[]arrw ={"dfdf","dsffg","SDgggg"};
                getObjectArrayElement(arrw);
                break;

            case R.id.btn_4:
                String str="";
                String[]arrd ={"dfdf","dsffg","SDgggg"};
                setObjectArrayElement(arrd);
                for (int i = 0; i <arrd.length ; i++) {
                    str+=arrd[i];
                }
                tvText.setText(str);
                break;
            case R.id.btn_5:


             newDirectByteBuffer();

                // 首先分配, 然后调用进行设置
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                if (buffer == null) {
                    Log.d("JniDemo", "分配Direct Buffer失败!");
                    return ;
                }
                buffer.putChar('a');
                buffer.putChar('c');
                buffer.putChar('d');
                buffer.putChar('d');
                buffer.putChar('w');
                getDirectBufferAddress(buffer, buffer.capacity());
                for (int i = 0; i <20 ; i++) {
                    Log.i("测试===","测试："+buffer.get(i));
                }
                break;
            case R.id.btn_6:
                byte[]arrayByte={'a','b','c'};
                setbyteArrayElement(arrayByte);
                break;
        }
    }

    static {
        System.loadLibrary("one-array");
    }

 //   Get<Type>ArrayElements函数用来获取基本类型JNI数组的元素，这里面的<Type>需要被替换成实际的类型，比如GetIntArrayElements，GetLongArrayElements
    public native void getIntArrayElements(int[] array);

 //  Get<Type>ArrayRegion函数用来获取JNI数组的子数组，这里面的<Type>需要被替换成实际的类型，比如GetIntArrayRegion，GetLongArrayRegion等
  public native int getIntArrayRegion(int[] array);

    //  Set<Type>ArrayRegion函数用来获取JNI基本类型数组的子数组，这里面的<Type>需要被替换成实际的类型，比如SetIntArrayRegion，SetLongArrayRegion等
    public native void  setIntArrayRegion(int[] array);

   // GetObjectArrayElement函数用来获取JNI对象数组元素
   public native void  getObjectArrayElement(Object[] array);

    // SetObjectArrayElement函数用来设置JNI对象数组元素
    public native void  setObjectArrayElement(Object[] array);

    public native void  setbyteArrayElement(byte[] array);


//    使用NIO缓冲区可以在Java和JNI代码中共享大数据，性能比传递数组要快很多，当Java和JNI需要传递大数据时，推荐使用NIO缓冲区的方式来传递。
//    NewDirectByteBuffer函数用来创建NIO缓冲区
//    GetDirectBufferAddress函数用来获取NIO缓冲区的内容
//    GetDirectBufferCapacity函数用来获取NIO缓冲区的大小

  public native void  newDirectByteBuffer();

  public native void  getDirectBufferAddress(ByteBuffer buffer,int len);



}
