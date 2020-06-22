package com.dgg.multidialog;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showDialog(View view) {
        if (isLoaded && jsonBean!=null&&jsonBean.size()>0){
            showProvinceDialog();
        }else {
            isLoaded=false;
            mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        }
    }

    private List<ProvinceData> currentData;
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private static boolean isLoaded = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    showProvinceDialog();
                    break;

                case MSG_LOAD_FAILED:
                    break;
            }
        }
    };
    private void showProvinceDialog() {
        TextView textView=new TextView(this);
        textView.setText("数据加载中...");
        textView.setTextColor(Color.RED);
        final DggMultistageDialog multistageDialog =new DggMultistageDialog.Builder(this)
                .setTitle("选择所在地区")
                .setCurrentColor(0xff525BDF)
                .setLoadingView(textView)
                .build();
        multistageDialog.setListener(new DggMultistageDialog.OnChooseItemListener<ProvinceData>() {
            @Override
            public void onChoose(final ProvinceData data,List<ProvinceData> dataList) {
                if (data!=null&&data.getChildren()!=null&&data.getChildren().size()>0){
                    currentData=data.getChildren();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            multistageDialog.addData(data.getChildren());
                        }
                    },2000);
                }else {
                    multistageDialog.dismiss();
                    String value="";
                    for (ProvinceData provinceData:dataList){
                        value=value+provinceData.getName()+"-";
                    }
                    Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
                }
            }
        });
        multistageDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                multistageDialog.addData(jsonBean);
            }
        },2000);
    }

    private ArrayList<MultistageData> jsonBean;
    private void initJsonData() {//解析数据

        String JsonData = new GetJsonDataUtil().getJson(this, "data.json");//获取assets目录下的json文件数据

        jsonBean = parseData(JsonData);//用Gson 转成实体

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    private ArrayList<MultistageData> parseData(String jsonData) {
        ArrayList<MultistageData> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                MultistageData entity = gson.fromJson(data.optJSONObject(i).toString(), ProvinceData.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
