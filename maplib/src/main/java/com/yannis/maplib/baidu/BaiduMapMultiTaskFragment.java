package com.yannis.maplib.baidu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yannis.maplib.R;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.yannis.maplib.utils.ThirdMapConstants.TASK_TYPE_CHOOSE_POINT;
import static com.yannis.maplib.utils.ThirdMapConstants.TASK_TYPE_DRAW_LINE;
import static com.yannis.maplib.utils.ThirdMapConstants.TASK_TYPE_MARKER_POINT;

/**
 * BaiduMapMultiTaskFragment 百度地图
 * 基本功能：定位；
 * 可选功能：选点（TASK_TYPE_CHOOSE_POINT）、描点（TASK_TYPE_MARKER_POINT）、画线（TASK_TYPE_DRAW_LINE）
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-29
 */
public class BaiduMapMultiTaskFragment extends Fragment {

    public static final int SEARCH_SUG_REQUEST_CODE = 11;
    public static final int LOCATION_RESULT_CODE = 41;
    private static final String TAG = "MuleBaiduMapMultiTaskFr";
    /**
     * 国家
     */
    public String getCountry = "";
    /**
     * 省
     */
    public String getProvince = "";
    /**
     * 市
     */
    public String getCity = "";
    /**
     * 区
     */
    public String getDistrict = "";
    /**
     * 地址建筑物名称
     */
    public String getAddressName = "";
    /**
     * 地址门牌号
     */
    public String getAddressDetail = "";
    /**
     * 纬度-返回给用户
     */
    public double latitude = -1;
    /**
     * 经度-返回给用户
     */
    public double longitude = -1;
    /**
     * 实现单选，保存当前选中的position
     */
    private int mSelectedPos = 0;
    private MapView mMapView = null;
    private MapView mMapViewLarge = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private Marker centerMarker = null;
    private TextView backBtn = null;
    private TextView confirmBtn = null;
    private TextView toSearchActivity = null;
    private ImageView myLocation = null;
    private RecyclerView mRecyclerView = null;
    private BaiduMarkerPointAdapter mMarkerPointAdapter = null;
    private List<PoiInfoWrap> poiOfMarkerPointList = new ArrayList<>();
    /**
     * 地理编码检索监听器
     */
    OnGetGeoCoderResultListener geoCoderListener = new OnGetGeoCoderResultListener() {

        // 地址转坐标
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        // 坐标转地址
        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult != null || reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                // 设置信息：在地里编码后再获取 "省、市" 更加准确。
                getBasicAddressInfo(reverseGeoCodeResult);

                if (reverseGeoCodeResult.getPoiList() != null && reverseGeoCodeResult.getPoiList().size() != 0) {
                    List<PoiInfo> infoPoiList = reverseGeoCodeResult.getPoiList();
                    if (poiOfMarkerPointList.size() != 0) {
                        poiOfMarkerPointList.clear();
                        mMarkerPointAdapter.notifyDataSetChanged();
                    }

                    for (int i = 0; i < infoPoiList.size(); i++) {
                        if (i == 0) {
                            poiOfMarkerPointList.add(new PoiInfoWrap(infoPoiList.get(i), true));
                        } else {
                            poiOfMarkerPointList.add(new PoiInfoWrap(infoPoiList.get(i), false));
                        }
                    }
                    mMarkerPointAdapter.setNewData(poiOfMarkerPointList);
                    mMarkerPointAdapter.notifyDataSetChanged();
                    getItemChooseData(0);
                    // 将第一个 经纬度 移动至地图中心位置,体验不好
                    // mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(infoPoiList.get(0).getLocation(), mapZoom));
                } else {
                    poiOfMarkerPointList.clear();
                }

            }
        }
    };
    private Activity mActivity = null;
    private View rootView = null;
    /**
     * 地理信息
     */
    private GeoCoder mCoder = null;
    /**
     * 功能类型：基础功能定位，选点、描点、画线
     */
    private int getTaskType = 0;
    /**
     * 选中条目事件的标识
     */
    private boolean isSelectList = false;
    /**
     * 地图状态改变监听
     */
    BaiduMap.OnMapStatusChangeListener statusChangeListener = new BaiduMap.OnMapStatusChangeListener() {

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
            if (centerMarker != null) {
                centerMarker.setPosition(mapStatus.target);
            }
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            // mapStatus.target 表示：地图操作的中心点。
            // 地图中心点坐标位置 绘制 marker
            if (centerMarker != null) {
                centerMarker.setPosition(mapStatus.target);
            }

            if (isSelectList) {
                isSelectList = false;
            } else {
                dealGeoCode(mapStatus.target);
            }
        }
    };
    /**
     * 地图缩放等级
     */
    private float mapZoom = 18.0f;
    /**
     * 当前定位的经纬度
     */
    private LatLng currentLatLng = null;
    private ArrayList<MapMarkersData> getLl = new ArrayList<>();
    /**
     * 定位监听
     */
    BDAbstractLocationListener locationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location != null || (getTaskType == TASK_TYPE_CHOOSE_POINT ? mMapView : mMapViewLarge) != null) {
                getCountry = location.getCountry();
                getProvince = location.getProvince();
                getCity = location.getCity();
                getDistrict = location.getDistrict();
                if (location.getLocType() != BDLocation.TypeServerError) {
                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    isSelectList = false;
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(location.getDirection())
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude())
                            .build();
                    mBaiduMap.setMyLocationData(locData);
                    // 清除地图上的所有覆盖物
                    mBaiduMap.clear();
                    // 切换功能选项
                    switchTask(location);
                }
            }
        }
    };
    private boolean isFromDetail = false;

    /**
     * 单例
     *
     * @param taskType    任务类型
     * @param latLonBeans 描点坐标数组
     */
    public static BaiduMapMultiTaskFragment getInstance(int taskType, ArrayList<MapMarkersData> latLonBeans, boolean isFromDetail) {
        BaiduMapMultiTaskFragment muleBaiduMapMultiTaskFragment = new BaiduMapMultiTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("task_type", taskType);
        bundle.putSerializable("latlng", latLonBeans);
        bundle.putBoolean("detail", isFromDetail);
        muleBaiduMapMultiTaskFragment.setArguments(bundle);
        return muleBaiduMapMultiTaskFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            getTaskType = getArguments().getInt("task_type", -1);
            getLl = (ArrayList<MapMarkersData>) getArguments().getSerializable("latlng");
            isFromDetail = getArguments().getBoolean("detail");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.baidu_map_fragment_layout, container, false);
        initView();
        allClick();
        return rootView;
    }

    private void allClick() {
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
            }
        });
    }

    private void initView() {
        myLocation = rootView.findViewById(R.id.iv_my_location);
        if (getTaskType == TASK_TYPE_CHOOSE_POINT) {
            // 选点操作
            TextView title = rootView.findViewById(R.id.tv_title);
            title.setText("地点选择");
            mRecyclerView = rootView.findViewById(R.id.rv__baidu_fragment);
            mMapView = rootView.findViewById(R.id.map_baidu_fragment);
            mBaiduMap = mMapView.getMap();
        } else {
            // 非选点操作
            LinearLayout linearLayout = rootView.findViewById(R.id.ll_layout_fragment);
            linearLayout.setVisibility(View.GONE);
            mMapViewLarge = rootView.findViewById(R.id.map_baidu_fragment_large);
            mMapViewLarge.setVisibility(View.VISIBLE);
            mBaiduMap = mMapViewLarge.getMap();
            if (isFromDetail) {
                mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
                    @Override
                    public void onTouch(MotionEvent motionEvent) {
                        /*NestedScrollView nestedScrollView = mActivity.findViewById(R.id.nsv);
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        } else {
                            nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        }*/
                    }
                });
            }
        }
        initLocation();
    }

    /**
     * 定位操作
     */
    private void initLocation() {
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocationClient = new LocationClient(mActivity);
        // 通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        // 打开gps
        option.setOpenGps(true);
        // 设置坐标类型
        option.setCoorType("gcj02");
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(0);
        // 可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        // 设置locationClientOption
        mLocationClient.setLocOption(option);
        // 注册LocationListener监听器
        mLocationClient.registerLocationListener(locationListener);
        // 开启地图定位图层
        mLocationClient.start();
    }

    private void switchTask(BDLocation location) {
        if (getTaskType == TASK_TYPE_CHOOSE_POINT) {
            taskChoosePoint(location);
        } else if (getTaskType == TASK_TYPE_MARKER_POINT) {
            taskMarkerPoint();
        } else if (getTaskType == TASK_TYPE_DRAW_LINE) {
            taskDrawLine();
        }
    }

    private void taskDrawLine() {
        taskDrawLineMethod();
    }

    private void taskMarkerPoint() {
        markerBatchPointToView();
    }

    private void taskChoosePoint(BDLocation location) {
        backBtn = rootView.findViewById(R.id.tv_cancel);
        confirmBtn = rootView.findViewById(R.id.tv_confirm);
        toSearchActivity = rootView.findViewById(R.id.ed_input_baidu_fragment);
        dealChoosePointClick();
        initRecyclerWithAdapter();
        // 添加 - 地图状态改变-监听器
        mBaiduMap.setOnMapStatusChangeListener(statusChangeListener);
        // 地理编码检索 - 创建实例
        mCoder = GeoCoder.newInstance();
        // 添加 - 地理编码检索-监听器
        mCoder.setOnGetGeoCodeResultListener(geoCoderListener);
        // 将当前位置设置中心点并进行缩放
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, mapZoom));
        // 定位成功 marker 中心位置
        initCenterMarker(latLng);
        // 地理编码
        dealGeoCode(latLng);
    }

    /**
     * 选点相关操作点击事件
     */
    private void dealChoosePointClick() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                mActivity.setResult(LOCATION_RESULT_CODE, null);
                mActivity.finish();
                // mActivity.overridePendingTransition(R.anim.push_top_in, R.anim.push_bottom_out);
            }
        });

        toSearchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(getCity)) {
                    Intent data = new Intent(mActivity, BaiduSugSearchActivity.class);
                    data.putExtra("search_city", getCity);
                    startActivityForResult(data, SEARCH_SUG_REQUEST_CODE);
                } else {
                    // ToastUtil.showToast(mActivity, R.string.md_city_get_error);
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaiduTransferBean transferBean = new BaiduTransferBean(
                        getCountry,
                        getProvince,
                        getCity,
                        getDistrict,
                        getAddressName,
                        getAddressDetail,
                        latitude,
                        longitude
                );
                Intent intent = new Intent();
                intent.putExtra("content", transferBean);
                mActivity.setResult(LOCATION_RESULT_CODE, intent);
                mActivity.finish();
            }
        });
    }

    /**
     * 初始化 RecyclerView
     */
    private void initRecyclerWithAdapter() {
        mMarkerPointAdapter = new BaiduMarkerPointAdapter(R.layout.baidu_marker_point_list_item_layout, poiOfMarkerPointList);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mMarkerPointAdapter);
        doAdapterItemClick();
    }

    private void doAdapterItemClick() {
        mMarkerPointAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dealAdapterClick(position);
            }
        });

        mMarkerPointAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rb_baidu_item_tag) {
                    dealAdapterClick(position);
                }
            }
        });
    }

    private void dealAdapterClick(int position) {
        isSelectList = true;
        poiOfMarkerPointList.get(mSelectedPos).setSelected(false);
        mSelectedPos = position;
        poiOfMarkerPointList.get(mSelectedPos).setSelected(true);
        mMarkerPointAdapter.notifyDataSetChanged();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(poiOfMarkerPointList.get(mSelectedPos).getPoiInfo().location, 19.0f));
        getItemChooseData(mSelectedPos);
    }

    /**
     * 执行 地理编码
     *
     * @param latLng 经纬度
     */
    private void dealGeoCode(LatLng latLng) {
        mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng)
                // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                .radius(1000));
    }

    /**
     * 绘制屏幕中心的Marker点
     *
     * @param latLng 经纬度
     */
    private void initCenterMarker(LatLng latLng) {
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = setOverlayOption(latLng, null, R.drawable.wechatview_icon_fixed_location);
        //在地图上添加Marker，并显示
        centerMarker = (Marker) mBaiduMap.addOverlay(option);
    }

    private void getBasicAddressInfo(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult.getAddressDetail() != null) {
            getCountry = reverseGeoCodeResult.getAddressDetail().countryName;
            getProvince = reverseGeoCodeResult.getAddressDetail().province;
            getCity = reverseGeoCodeResult.getAddressDetail().city;
            getDistrict = reverseGeoCodeResult.getAddressDetail().district;
        }
    }


    private void getItemChooseData(int i) {
        getAddressName = poiOfMarkerPointList.get(i).getPoiInfo().getName();
        getAddressDetail = poiOfMarkerPointList.get(i).getPoiInfo().getAddress();
        latitude = poiOfMarkerPointList.get(i).getPoiInfo().getLocation().latitude;
        longitude = poiOfMarkerPointList.get(i).getPoiInfo().getLocation().longitude;
    }

    /**
     * 获取 用户查询的sug后返回的 信息
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_SUG_REQUEST_CODE && data != null) {
            SuggestionResult.SuggestionInfo suggestionInfo = data.getParcelableExtra("search_data");
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(suggestionInfo.getPt(), mapZoom));
            dealGeoCode(suggestionInfo.getPt());
        }
    }

    /**
     * 添加 批量Marker 点
     */
    private void markerBatchPointToView() {
        List<OverlayOptions> options = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < getLl.size(); i++) {
            LatLng latLng = new LatLng(getLl.get(i).getLatitude(), getLl.get(i).getLongitude());
            builder.include(latLng);
            options.add(setOverlayOption(latLng, getLl.get(i).getTitle(), getLl.get(i).getIcon()));
            mBaiduMap.addOverlays(options);
        }
        builder.include(currentLatLng);
        int mapWidth = mMapViewLarge.getWidth() - dp2px(100);
        int mapHeight = mMapViewLarge.getHeight() - dp2px(300);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(builder.build(), mapWidth, mapHeight);
        mBaiduMap.animateMapStatus(u);
    }

    @NonNull
    private OverlayOptions setOverlayOption(LatLng targetLl, String title, int icon) {
        return new MarkerOptions()
                .position(targetLl)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(icon))
                .animateType(MarkerOptions.MarkerAnimateType.grow);
    }

    /**
     * 绘制折线
     */
    private void taskDrawLineMethod() {
        // 构建折线点坐标
        LatLng p1 = new LatLng(39.97923, 116.357428);
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.437428);
        List<LatLng> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);

        //设置折线的属性
        OverlayOptions mOverlayOptions = new PolylineOptions()
                .width(10)
                .color(0xAAFF0000)
                .points(points);
        // 在地图上绘制折线
        mBaiduMap.addOverlay(mOverlayOptions);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getTaskType == TASK_TYPE_CHOOSE_POINT) {
            mMapView.onResume();
        } else {
            mMapViewLarge.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getTaskType == TASK_TYPE_CHOOSE_POINT) {
            mMapView.onPause();
        } else {
            mMapViewLarge.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        if (mLocationClient != null) {
            mLocationClient = null;
        }
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        if (mBaiduMap != null) {
            mBaiduMap = null;
        }
        if (locationListener != null) {
            locationListener = null;
        }
        if (getTaskType == TASK_TYPE_CHOOSE_POINT) {
            mMapView.onDestroy();
            mCoder.destroy();
            if (geoCoderListener != null) {
                geoCoderListener = null;
            }
        } else {
            mMapViewLarge.onDestroy();
        }
    }
}
