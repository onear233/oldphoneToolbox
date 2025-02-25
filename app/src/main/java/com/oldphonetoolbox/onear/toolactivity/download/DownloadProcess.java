package com.oldphonetoolbox.onear.toolactivity.download;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oldphonetoolbox.onear.R;
import com.oldphonetoolbox.onear.toolactivity.OPTBActivityCompat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DownloadProcess extends OPTBActivityCompat {
    public volatile static boolean[] isDownloading = new boolean[]{false,false,false};
    public volatile static int[] progress = new int[]{0,0,0};
    public volatile static String[] name = new String[]{"","",""};

    public ProgressBar downloadProgress1;
    public ProgressBar downloadProgress2;
    public ProgressBar downloadProgress3;

    public TextView downloadName1;
    public TextView downloadName2;
    public TextView downloadName3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_process);
        Log.i("DOWNLOAD", "进入进度显示页面");
        //获取控件
        downloadProgress1 = findViewById(R.id.pb_download1);
        downloadProgress2 = findViewById(R.id.pb_download2);
        downloadProgress3 = findViewById(R.id.pb_download3);
        downloadName1 = findViewById(R.id.process_download1);
        downloadName2 = findViewById(R.id.process_download2);
        downloadName3 = findViewById(R.id.process_download3);
        //每秒刷新一次控件
        new Thread(()-> new PageThread(this).flush()).start();
    }


    @Override
    protected RelativeLayout getMainLayout() {
        return findViewById(R.id.download_process_main);
    }

    @Override
    protected List<TextView> getTextView() {
        List<TextView> list = new LinkedList<>();
        list.add(findViewById(R.id.process_download1));
        list.add(findViewById(R.id.process_download2));
        list.add(findViewById(R.id.process_download3));
        return list;
    }

    @Override
    public void close() {
        PageThread.isRunning = false;
    }
    private static class PageThread{
        private final DownloadProcess activity;
        public static volatile boolean isRunning = true;
        public PageThread(DownloadProcess activity){
            this.activity = activity;
        }

        public void flush() {
            Log.i("DOWNLOAD", "开始刷新控件");
            isRunning = true;
            while (isRunning){
                activity.runOnUiThread(()-> {
                    activity.downloadProgress1.setProgress(progress[0]);
                    activity.downloadProgress2.setProgress(progress[1]);
                    activity.downloadProgress3.setProgress(progress[2]);
                    activity.downloadName1.setText(name[0]);
                    activity.downloadName2.setText(name[1]);
                    activity.downloadName3.setText(name[2]);
                });
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    
                }
            }
        }
    }
}