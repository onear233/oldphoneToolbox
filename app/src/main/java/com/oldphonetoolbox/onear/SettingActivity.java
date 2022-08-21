package com.oldphonetoolbox.onear;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    //数据源，tittleArray为标题，descriptionArray为描述

    Constant constant = new Constant();
    private final String[] settingTitleArray = {constant.settingsBackgroundColorTitle,constant.settingsTextColorTitle,constant.settingsPasswordTitle,constant.settingsIpAddressTitle};
    private final String[] settingDescriptionArray = {constant.settingsBackgroundColorDesc,constant.settingsTextColorDesc,constant.settingsPasswordDesc,constant.settingsIpAddressDesc};
    private final String[] helpTitleArray = {constant.helpAndAboutTitle};
    private final String[] helpDescriptionArray = {constant.helpAndAboutDesc};
    private List<Map<String, Object>> list;
    private HashMap<String, Object> item;
    private EditText editText;
    private final String REGEX_COLOR = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
    private final String REGEX_IP = "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showListview(settingTitleArray,settingDescriptionArray,"title","desc",R.id.setting_item_tittle,R.id.setting_item_desc,R.id.lv_settings);
        showListview(helpTitleArray,helpDescriptionArray,"helpTitle","helpDesc",R.id.setting_item_tittle,R.id.setting_item_desc,R.id.lv_help);
    }

    private void showListview(String titleArray[],String descArray[],String title,String description,int idT,int idD,int listViewId) { //idT为条目布局
        list = new ArrayList<>();
        for (int i = 0; i < titleArray.length; i++) {
            item = new HashMap<>();
            item.put(title,titleArray[i]);
            item.put(description,descArray[i]);
            list.add(item);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,R.layout.simple_list_item_tv_only,new String[]{title,description},new int[]{idT,idD});
        if (listViewId == R.id.lv_settings){
            ListView settingListView = findViewById(listViewId);
            settingListView.setAdapter(simpleAdapter);
            settingListView.setOnItemClickListener(this);
        }else if(listViewId == R.id.lv_help){
            ListView helpListView = findViewById(listViewId);
            helpListView.setAdapter(simpleAdapter);
            helpListView.setOnItemClickListener(this);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.lv_help){ //判断是哪个Listview
            Intent intent = new Intent(this,HelpActivity.class);
            startActivity(intent);
        }else if(parent.getId() == R.id.lv_settings){
            switch (position){
                case 0:
                    showDialog("请输入文字颜色的16进制码（仅支持6位）","确定","取消","backgroundColor","此颜色不正确！",REGEX_COLOR);
                    break;
                case 1:
                    showDialog("请输入文字颜色的16进制码（仅支持6位）","确定","取消","textColor","此颜色不正确！",REGEX_COLOR);//显示对话框的方法
                    break;
                case 2:
                    showDialog("请输入密码","确定","取消","pwd","密码为空！","");
                    break;
                case 3:
                    showDialog("请输入电脑的IP地址","确定","取消","ipAddress","请输入正确的ip地址",REGEX_IP);
                    break;
            }
        }
    }

    private void showDialog(String title,String positiveButtonText,String negativeButtonText,String getAndSaveStrKey,String unsuitableToast,String REGEX){
        editText = new EditText(this);
        AlertDialog.Builder iPInputDialog = new AlertDialog.Builder(SettingActivity.this);
        iPInputDialog.setTitle(title).setView(editText);
        iPInputDialog.setPositiveButton(positiveButtonText,(dialog, which) -> {
            if (REGEX.length() != 0){
                String input = editText.getText().toString();
                boolean isLegal = Pattern.matches(REGEX,input);
                if (isLegal){
                    saveStr(getAndSaveStrKey,input);
                }else{
                    Toast.makeText(SettingActivity.this,unsuitableToast,Toast.LENGTH_SHORT).show();
                }
            }else{
                saveStr(getAndSaveStrKey,editText.getText().toString());
            }
        });
        iPInputDialog.setNegativeButton(negativeButtonText, (dialog, which) -> {  });
        AlertDialog dialog = iPInputDialog.create();
        dialog.show();//展示对话框
        getStr(getAndSaveStrKey);
    }

    private void getStr(String key) {
        SharedPreferences getPWD = getSharedPreferences(key, MODE_PRIVATE);
        String getPassword = getPWD.getString(key,"");
        editText.setText(getPassword);
    }

    private void saveStr(String id,String pwd) {
        SharedPreferences password = getSharedPreferences(id, MODE_PRIVATE);
        SharedPreferences.Editor editor = password.edit();
        editor.putString(id,pwd);
        editor.commit();
    }
}