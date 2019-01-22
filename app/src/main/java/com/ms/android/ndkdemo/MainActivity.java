package com.ms.android.ndkdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ms.android.ndkdemo.adapter.BaseViewHolder;
import com.ms.android.ndkdemo.adapter.QuickRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //参考原理：https://blog.csdn.net/kgdwbb/article/details/72810251

   private QuickRecyclerAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycler= (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new QuickRecyclerAdapter<String>(R.layout.main_item_view) {
            @Override
            protected void onBindView(BaseViewHolder holder, int position, final String s) {
                holder.setText(R.id.btn,s);
                holder.getView().findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=null;
                        switch (s){
                            case "jni第一节string":
                                intent=new Intent(getApplicationContext(),JniOneStrActivity.class);
                                startActivity(intent);
                                break;
                            case "jni第二节array":
                                intent=new Intent(getApplicationContext(),JniArrayActivity.class);
                                startActivity(intent);
                                break;
                            case "jni第三节JNI访问Java类":
                                intent=new Intent(getApplicationContext(),JniObjectActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                });
            }
        };
        recycler.setAdapter(adapter);

        List<String>list=new ArrayList<>();
        list.add("jni第一节string");
        list.add("jni第二节array");
        list.add("jni第三节JNI访问Java类");
        adapter.setDataList(list);
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();





}
