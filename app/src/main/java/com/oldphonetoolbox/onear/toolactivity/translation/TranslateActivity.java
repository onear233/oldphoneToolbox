package com.oldphonetoolbox.onear.toolactivity.translation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.oldphonetoolbox.onear.R;
import com.oldphonetoolbox.onear.toolactivity.OPTBActivityCompat;

public class TranslateActivity extends OPTBActivityCompat {

    private TextView chn_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String source = getIntent().getStringExtra("english");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_translate);
        TextView eng_view = findViewById(R.id.ts_en);
        chn_view = findViewById(R.id.ts_ch);
        eng_view.setText(source);
        Log.i(TranslateActivity.class.getName(), "进入翻译界面");
        //执行翻译程序
        new Thread(new TranslatorThread(this,source)).start();
    }

    @Override
    public void close() {
    }

    public void setText(String text){
        this.chn_view.setText(text);
    }
}