package com.example.hasee.snow;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;


/**
 * 作者：Limerence 2018/12/28 2045:06
 * 邮箱：gl1450030827@163.com
 */
public class MainActivity extends Activity {
    private SnowView snow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snow = (SnowView) findViewById(R.id.snow2);
        // 获取当前屏幕的高和宽
        snow.SetView(getWindowManager());//获取的像素值
    }
    /**
     * 请别忘了释放内存
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        snow.stop();
    }
}
