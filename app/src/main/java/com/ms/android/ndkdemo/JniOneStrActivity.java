package com.ms.android.ndkdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author shenjianbin@ms.com
 * @since 2017/8/29
 */
public class JniOneStrActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni_one_str);
        tvText= (TextView) findViewById(R.id.tvText);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn_str).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                tvText.setText(newStringUTF());
                break;
            case R.id.btn_str:
                tvText.setText(newString("设置类型newString"));
                break;
            case R.id.btn_2:
                getStringChars("设置类型newString");
                break;
            case R.id.btn_3:
                getStringUTFChars("设置类型newString");
                break;
            case R.id.btn_4:
                tvText.setText(getStringRegion("设置类型newString"));
                break;
            case R.id.btn_5:
                tvText.setText(getStringUTFRegion("设置类型newString"));
                break;
            case R.id.btn_6:
                tvText.setText(getStringLength("设置类型newString")+"");
                break;
            case R.id.btn_7:
                tvText.setText(getStringUTFLength("设置类型newString")+"");
                break;
        }

    }

    static {
        System.loadLibrary("one-str");
    }

    public native String stringHello();

    //NewStringUTF函数用来生成UTF-8 JNI字符串
    public native String newStringUTF();

    //NewString函数用来生成Unicode JNI字符串
    public native String newString(String str);

   // GetStringChars函数用来从jstring获取Unicode C/C++字符串
   public native void getStringChars(String str);

  //  GetStringUTFChars函数用来从jstring获取UTF-8 C/C++字符串
  public native void getStringUTFChars(String str);


    //  GetStringRegion函数用来截取Unicode JNI字符串
    public native String getStringRegion(String str);
    //  GetStringUTFRegion函数用来截取UTF-8 JNI字符串
    public native String getStringUTFRegion(String str);

   // GetStringLength用来获取Unicode JNI字符串的长度
    public native int getStringLength(String str);
   // GetStringUTFLength函数用来获取UTF-8 JNI字符串的长度
    public native int getStringUTFLength(String str);
}
