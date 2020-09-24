package com.yannis.mayalisten.map.gaode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yannis.maplib.R;

import java.util.Iterator;
import java.util.List;

import static com.yannis.mayalisten.map.gaode.GaoDe2DMapActivity.LOCATION_RESULT_CODE;

/**
 * Gaode2dSugSearchActivity  高德地图地点搜索
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-08-01
 */
public class Gaode2dSugSearchActivity extends AppCompatActivity implements Inputtips.InputtipsListener {

    private RecyclerView recyclerView = null;
    private EditText hotKeyWord = null;
    private ImageView searchCancel = null;
    private ImageView closeBtn = null;
    private String searchCity = null;
    /**
     * 搜索文字输入监听器
     */
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                searchCancel.setVisibility(View.VISIBLE);
            } else {
                searchCancel.setVisibility(View.GONE);
            }
            inputTips(s.toString());
        }
    };
    private BaseQuickAdapter<Tip, BaseViewHolder> gaode2dSugSearchAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_map_sug_search_stroke_layout);

        initBasicInfo();
        initToolBar();
        initSugView();
        initRecyclerWithAdapter();
        onClick();
    }

    private void initBasicInfo() {
        searchCity = getIntent().getStringExtra("search_city");
        if ("".equals(searchCity)) {
            setResult(LOCATION_RESULT_CODE, null);
            finish();
        }
    }

    private void initToolBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("搜索地址");
        findViewById(R.id.tv_cancel).setVisibility(View.GONE);
        findViewById(R.id.tv_confirm).setVisibility(View.GONE);
        closeBtn = (ImageView) findViewById(R.id.iv_close);
        closeBtn.setVisibility(View.VISIBLE);
    }

    private void initSugView() {
        searchCancel = (ImageView) findViewById(R.id.iv_cancel_search_input_public);
        recyclerView = (RecyclerView) findViewById(R.id.rv_public);
        hotKeyWord = (EditText) findViewById(R.id.ed_input_public);
        hotKeyWord.addTextChangedListener(textWatcher);
    }

    private void initRecyclerWithAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        gaode2dSugSearchAdapter = new BaseQuickAdapter<Tip, BaseViewHolder>(R.layout.gaode_2d_search_sug_item_layout) {
            @Override
            protected void convert(BaseViewHolder helper, Tip item) {
                helper.setText(R.id.tv_title_gaode_2d, item.getName());
                helper.setText(R.id.tv_address_gaode_2d, item.getAddress());
            }
        };
        recyclerView.setAdapter(gaode2dSugSearchAdapter);
    }

    private void onClick() {
        // 取消搜索
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(LOCATION_RESULT_CODE, null);
                finish();
            }
        });

        // 清除输入的搜索文字
        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotKeyWord.setText("");
                inputTips("");
            }
        });

        gaode2dSugSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            // 返回 搜索的结果
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("search_data", ((Tip) adapter.getData().get(position)));
                setResult(LOCATION_RESULT_CODE, intent);
                finish();
            }
        });
    }

    private void inputTips(String key) {
        //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
        InputtipsQuery inputQuery = new InputtipsQuery(key, searchCity);
        //限制在当前城市
        inputQuery.setCityLimit(false);
        Inputtips inputTips = new Inputtips(this, inputQuery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {

        if (gaode2dSugSearchAdapter.getData().size() != 0) {
            gaode2dSugSearchAdapter.getData().clear();
            gaode2dSugSearchAdapter.notifyDataSetChanged();
        }

        if (rCode == 1000) {
            if (tipList != null && tipList.size() != 0) {
                // 通过遍历去除 经纬度 为null 的情况
                Iterator<Tip> iterator = tipList.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getPoint() == null) {
                        iterator.remove();
                    }
                }
                gaode2dSugSearchAdapter.setNewData(tipList);
                gaode2dSugSearchAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView = null;
        }

        if (textWatcher != null) {
            textWatcher = null;
        }

        if (gaode2dSugSearchAdapter != null) {
            gaode2dSugSearchAdapter = null;
        }
    }
}
