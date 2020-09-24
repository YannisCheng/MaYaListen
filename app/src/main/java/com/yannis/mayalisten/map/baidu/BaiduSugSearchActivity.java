package com.yannis.mayalisten.map.baidu;

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

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yannis.maplib.R;

import java.util.List;

import static com.yannis.mayalisten.map.baidu.BaiduMapMultiTaskActivity.SEARCH_SUG_REQUEST_CODE;


/**
 * BaiduSugSearchActivity  百度地图 地点检索输入提示检索（Sug检索）
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-29
 */
public class BaiduSugSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView = null;
    private EditText hotKeyWord = null;
    private ImageView searchCancel = null;
    private ImageView closeBtn = null;
    private SuggestionSearch suggestionSearch = null;
    private SuggestionSearchOption searchOption = null;
    private BaseQuickAdapter<SuggestionResult.SuggestionInfo, BaseViewHolder> adapter;
    /**
     * 创建Sug检索监听器
     */
    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {

        // Sug检索默认不限制在city内，检索结果优先展示city内结果。
        // 通过设置SuggestionSearchOption对象cityLimit字段为true限制Sug检索区域在city内。
        @Override
        public void onGetSuggestionResult(SuggestionResult suggestionResult) {
            if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                adapter.getData().clear();
                adapter.notifyDataSetChanged();
                return;
            }
            //处理sug检索结果
            List<SuggestionResult.SuggestionInfo> infoList = suggestionResult.getAllSuggestions();
            if (infoList.get(0).pt == null) {
                infoList.remove(0);
            }
            adapter.setNewData(infoList);
            adapter.notifyDataSetChanged();
        }
    };
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
            sugSearchSetting(s.toString());
        }
    };

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
        String searchCity = getIntent().getStringExtra("search_city");
        if ("".equals(searchCity)) {
            setResult(SEARCH_SUG_REQUEST_CODE, null);
            finish();
        }
        suggestionSearch = SuggestionSearch.newInstance();
        searchOption = new SuggestionSearchOption();
        // city为必填项
        searchOption.city(searchCity);
    }

    private void onClick() {
        // 取消搜索
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(SEARCH_SUG_REQUEST_CODE, null);
                finish();
            }
        });

        // 清除输入的搜索文字
        searchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotKeyWord.setText("");
                sugSearchSetting("");
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            // 返回 搜索的结果
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("search_data", ((SuggestionResult.SuggestionInfo) adapter.getData().get(position)));
                setResult(SEARCH_SUG_REQUEST_CODE, intent);
                finish();
            }
        });
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
        adapter = new BaseQuickAdapter<SuggestionResult.SuggestionInfo, BaseViewHolder>(R.layout.baidu_search_sug_item_layout) {
            @Override
            protected void convert(BaseViewHolder helper, SuggestionResult.SuggestionInfo item) {
                helper.setText(R.id.tv_title_baidu, item.key);
                helper.setText(R.id.tv_address_baidu, item.city + "，" + item.district);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private void initToolBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("搜索地址");
        findViewById(R.id.tv_cancel).setVisibility(View.GONE);
        findViewById(R.id.tv_confirm).setVisibility(View.GONE);
        closeBtn = (ImageView) findViewById(R.id.iv_close);
        closeBtn.setVisibility(View.VISIBLE);
    }

    private void sugSearchSetting(String s) {
        // 设置Sug检索监听器
        suggestionSearch.setOnGetSuggestionResultListener(listener);
        // 搜索参数
        searchOption.keyword(s);
        suggestionSearch.requestSuggestion(searchOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView = null;
        }
        if (suggestionSearch != null) {
            suggestionSearch.destroy();
            suggestionSearch = null;
        }

        if (searchOption != null) {
            searchOption = null;
        }

        if (textWatcher != null) {
            textWatcher = null;
        }

        if (adapter != null) {
            adapter = null;
        }
    }
}
