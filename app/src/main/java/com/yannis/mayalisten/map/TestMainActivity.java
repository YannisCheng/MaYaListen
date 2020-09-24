package com.yannis.mayalisten.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yannis.mayalisten.map.baidu.BaiduMapMultiTaskActivity;
import com.yannis.mayalisten.map.baidu.BaiduTransferBean;

import static com.yannis.mayalisten.map.baidu.BaiduMapMultiTaskActivity.LOCATION_RESULT_CODE;
import static com.yannis.mayalisten.map.utils.ThirdMapConstants.TASK_TYPE_CHOOSE_POINT;

/**
 * @author yannischeng
 */
public class TestMainActivity extends AppCompatActivity {

    private static final String TAG = "TestMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.yannis.maplib.R.layout.activity_main);

        // 百度地图
        /*FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl, BaiduMapMultiTaskFragment.getInstance(TASK_TYPE_CHOOSE_POINT,null,false));
        transaction.commit();*/

        // 百度地图
        //BaiduMapMultiTaskActivity.start(TestMainActivity.this, TASK_TYPE_CHOOSE_POINT, null, false);

        // 高德地图
        findViewById(com.yannis.maplib.R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduMapMultiTaskActivity.start(TestMainActivity.this, TASK_TYPE_CHOOSE_POINT, null, false);
                //GaoDe2DMapActivity.start(TestMainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LOCATION_RESULT_CODE && data != null) {
            BaiduTransferBean transferContent = (BaiduTransferBean) data.getSerializableExtra("content");
            TextView show = findViewById(com.yannis.maplib.R.id.tv_show);
            show.setText(transferContent.toString());
        }
    }
}
