package com.vkei.okhttpdemo;

import android.app.Activity;
import android.os.Bundle;

import com.vkei.okhttpdemo.http.HttpChannel;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new HttpChannel(MainActivity.this);
            }
        }).start();
    }
}
