package com.yannis.maplib.gaode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Tip;
import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yannis.maplib.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * GaoDe2DMapActivity 高德地图
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-29
 */
public class GaoDe2DMapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    public static final int LOCATION_RESULT_CODE = 41;
    public static final int SEARCH_SUG_REQUEST_CODE = 11;
    private static final String TAG = "GaoDe2DMapActivity";
    /**
     * 完整地址
     */
    protected String getAddressAll = null;
    /**
     * 当前地点名称
     */
    protected String getAddressName = null;
    /**
     * 地址门牌号
     */
    protected String getAddressSnippet = null;
    /**
     * 国家
     */
    protected String getCountry = null;
    /**
     * 省
     */
    protected String getProvince = null;
    /**
     * 市
     */
    protected String getCity = null;
    private AMap aMap = null;
    private OnLocationChangedListener mLocationChangedListener = null;
    private AMapLocationClient mlocationClient = null;
    private Marker locationMarker = null;
    private Marker marker = null;
    private MapView mapGaode2d = null;
    private TextView tvCancel = null;
    private TextView tvConfirm = null;
    private TextView tvSearch = null;
    /**
     * 区
     */
    private String getDistrict = null;


    /**
     * 选中条目的纬度
     */
    private double latitude = -1;


    /**
     * 选中条目的经度
     */
    private double longitude = -1;


    /**
     * 选中条目事件的标识
     */
    private boolean isSelectList = false;

    private ArrayList<Gaode2dMarkerOfPointBean> mMarkerOfPointBeans = new ArrayList<>();
    private Gaode2DMarkerOfPointAdapter mMarkerOfPointAdapter = null;

    /**
     * point周围地点选择index
     */
    private int mSelectedPos = 0;
    /**
     * camera滑动事件
     */
    AMap.OnCameraChangeListener cameraChangeListener = new AMap.OnCameraChangeListener() {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            // 设置定位图标位置
            LatLng latLng = cameraPosition.target;
            if (locationMarker != null) {
                locationMarker.setPosition(latLng);
            }
            if (null != marker) {
                marker.setPosition(latLng);
            }
        }

        @Override
        public void onCameraChangeFinish(CameraPosition position) {
            if (isSelectList) {
                isSelectList = false;
                // 更新定位图标位置
                double latitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLatitude();
                double longitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                locationMarker.setPosition(latLng);
            } else {
                regeocodeSearch(position.target.latitude, position.target.longitude, 3000);
            }
        }
    };

    public static void start(Activity context) {
        Intent intent = new Intent(context, GaoDe2DMapActivity.class);
        context.startActivityForResult(intent, SEARCH_SUG_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gaode_2d_map_activity_map_layout);

        initToolbar();
        initMap(savedInstanceState);
        initRecyclerWithAdapter();
        doClick();
    }

    private void initToolbar() {
        tvCancel = findViewById(R.id.tv_cancel);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvTitle.setText("地点选择");
        tvSearch = findViewById(R.id.ed_input_gaode_2d_activity);
    }

    private void initMap(Bundle savedInstanceState) {
        mapGaode2d = (MapView) findViewById(R.id.map_gaode_2d_activity);
        mapGaode2d.onCreate(savedInstanceState);
        MapView mapGaode2dLarge = (MapView) findViewById(R.id.map_gaode_2d_activity_large);
        if (aMap == null) {
            aMap = mapGaode2d.getMap();
            // 设置定位图标样式
            aMap.setMyLocationStyle(getMyLocationStyle());
            // 地图显示设置
            setMapUi();
            setMapListener();
        }
    }

    private void initRecyclerWithAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_gaode_2d_activity);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        mMarkerOfPointAdapter = new Gaode2DMarkerOfPointAdapter(R.layout.gaode_2d_marker_point_list_item_layout, mMarkerOfPointBeans);
        recyclerView.setAdapter(mMarkerOfPointAdapter);
    }

    private void doClick() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取消
                setResult(LOCATION_RESULT_CODE, null);
                finish();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定--返回地址
                // getLocationImageAndFinish();
                returnData();
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 搜索
                Intent data = new Intent(GaoDe2DMapActivity.this, Gaode2dSugSearchActivity.class);
                startActivityForResult(data, SEARCH_SUG_REQUEST_CODE);
            }
        });

        mMarkerOfPointAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                getBasicAddressInfo(position);
            }
        });

        mMarkerOfPointAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                getBasicAddressInfo(position);
            }
        });
    }

    private void getBasicAddressInfo(int position) {
        isSelectList = true;
        mMarkerOfPointBeans.get(mSelectedPos).setChecked(false);
        mSelectedPos = position;
        mMarkerOfPointBeans.get(mSelectedPos).setChecked(true);
        mMarkerOfPointAdapter.notifyDataSetChanged();
        // 更新定位图标位置
        double latitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLatitude();
        double longitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        // latLng：可视区域框移动目标点屏幕中心位置的经纬度。
        // zoom：可视区域的缩放级别，高德地图支持3-19 级的缩放级别。
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        getItemChooseData();
    }

    private void getItemChooseData() {
        PoiItem poiItem = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem();
        getAddressSnippet = poiItem.getSnippet();
        getAddressName = poiItem.getTitle();
        latitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLatitude();
        longitude = mMarkerOfPointBeans.get(mSelectedPos).getPoiItem().getLatLonPoint().getLongitude();
    }

    private void setMapListener() {
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(true);
        // 触摸监听
        aMap.setOnCameraChangeListener(cameraChangeListener);
    }

    private void setMapUi() {
        UiSettings uiSettings = aMap.getUiSettings();
        // 设置默认定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(true);
        // 设置地图Logo位置
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        // 比例尺控件。位于地图右下角
        // 控制比例尺控件是否显示
        uiSettings.setScaleControlsEnabled(true);
        // 指南针
        uiSettings.setCompassEnabled(false);
        // 缩放按钮
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
    }

    private MyLocationStyle getMyLocationStyle() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 设置小蓝点的图标
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
        // myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        // 设置圆形的填充颜色
        //myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 90));
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        // myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        // 设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，
        // 设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.showMyLocation(true);
        return myLocationStyle;
    }

    /**
     * 反地理编码设置
     */
    private void regeocodeSearch(double lat, double lng, float radius) {
        LatLonPoint point = new LatLonPoint(lat, lng);
        GeocodeSearch geocodeSearch = new GeocodeSearch(GaoDe2DMapActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(GaoDe2DMapActivity.this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, radius, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
    }

    /**
     * 返回地址信息
     */
    private void returnData() {
        Intent data = new Intent();
        Gaode2dTransferBean transferBean = new Gaode2dTransferBean(
                getCountry,
                getProvince,
                getCity,
                getDistrict,
                getAddressName,
                getAddressSnippet,
                latitude,
                longitude
        );
        data.putExtra("content", transferBean);
        setResult(LOCATION_RESULT_CODE, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapGaode2d.onDestroy();
        // 销毁定位对象
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapGaode2d.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapGaode2d.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapGaode2d.onSaveInstanceState(outState);
    }


    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mLocationChangedListener = listener;
        if (mlocationClient == null) {
            //初始化定位  需要传Context类型的参数。推荐用getApplicationConext()方法
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置单次定位 该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //选择定位模式 SDK默认选择使用高精度定位模式。
            // 高精度定位模式:Hight_Accuracy 同时使用网络定位和GPS定位
            //低功耗定位模式：Battery_Saving 不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）
            //仅用设备定位模式：Device_Sensors 不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms 最低1000ms
            mLocationOption.setInterval(5000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否强制刷新WIFI，默认为true，强制刷新,会增加电量消耗。
            mLocationOption.setWifiActiveScan(true);
            // 设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(30000);
            //设置是否开启定位缓存机制 缓存机制默认开启true 网络定位结果均会生成本地缓存，
            // 不区分单次定位还是连续定位。GPS定位结果不会被缓存
            mLocationOption.setLocationCacheEnable(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }


    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mLocationChangedListener = null;
        if (mlocationClient != null) {
            //停止定位后，本地定位服务并不会被销毁
            mlocationClient.stopLocation();
        }
        mlocationClient = null;
    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLocationChangedListener != null && amapLocation != null) {
            LogUtils.e("==onLocationChanged.amapLocation====" + amapLocation.toString());
            if (amapLocation.getErrorCode() == 0) {
                isSelectList = false;
                getCountry = amapLocation.getCountry();
                getProvince = amapLocation.getProvince();
                getCity = amapLocation.getCity();
                getDistrict = amapLocation.getDistrict();
                // 显示系统小蓝点
                mLocationChangedListener.onLocationChanged(amapLocation);
                double latitudeIn = amapLocation.getLatitude();
                double longitudeIn = amapLocation.getLongitude();
                LatLng latLng = new LatLng(latitudeIn, longitudeIn);
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                // 添加 描点
                addMarker(latLng, "");
                // 显示信息窗口
                locationMarker.showInfoWindow();
                // 反地理编码搜索
                regeocodeSearch(latitudeIn, longitudeIn, 3000);
                // 定位时间
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);
            } else {
                Log.e("AmapErr", amapLocation.getErrorInfo());
                getAddressName = "";
                getAddressAll = "";
            }
        }
    }

    /**
     * 定位成功后往地图上添加marker
     */
    private void addMarker(LatLng latLng, String desc) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.wechatview_icon_fixed_location));

        /*MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_teal_700_24dp));
        //保证小蓝点的中心对应经纬度位置
        options.anchor(0.5f, 0.5f);*/
        /*markerOptions.getAddressName("[我的位置]");
        markerOptions.snippet(desc);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());*/
        /*marker = aMap.addMarker(options);*/
        locationMarker = aMap.addMarker(markerOptions);
        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        //marker = aMap.addMarker(markerOptions);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (regeocodeResult != null && 1000 == rCode) {
            // 设置信息：在地里编码后再获取 "省、市" 更加准确。
            getCountry = regeocodeResult.getRegeocodeAddress().getCountry();
            getProvince = regeocodeResult.getRegeocodeAddress().getProvince();
            getCity = regeocodeResult.getRegeocodeAddress().getCity();
            getDistrict = regeocodeResult.getRegeocodeAddress().getDistrict();
            // regeocodeResult.getRegeocodeAddress().getFormatAddress()
            // 天津市和平区劝业场街道和平区城市管理综合执法局繁华区大队中国瓷房子博物馆
            if (mMarkerOfPointBeans.size() != 0) {
                mMarkerOfPointBeans.clear();
                mMarkerOfPointAdapter.notifyDataSetChanged();
            }
            if (regeocodeResult.getRegeocodeAddress() != null &&
                    regeocodeResult.getRegeocodeAddress().getPois() != null &&
                    regeocodeResult.getRegeocodeAddress().getPois().size() != 0) {
                for (int i = 0; i < regeocodeResult.getRegeocodeAddress().getPois().size(); i++) {
                    if (i == 0) {
                        mMarkerOfPointBeans.add(new Gaode2dMarkerOfPointBean(
                                regeocodeResult.getRegeocodeAddress().getPois().get(i), true)
                        );
                    } else {
                        mMarkerOfPointBeans.add(new Gaode2dMarkerOfPointBean(
                                regeocodeResult.getRegeocodeAddress().getPois().get(i), false)
                        );
                    }
                }
                mMarkerOfPointAdapter.setNewData(mMarkerOfPointBeans);
                mMarkerOfPointAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    /**
     * 获取 用户查询的sug后返回的 信息
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == LOCATION_RESULT_CODE && data != null) {
            Tip tip = data.getParcelableExtra("search_data");
            latitude = tip.getPoint().getLatitude();
            longitude = tip.getPoint().getLongitude();
            getAddressName = tip.getName();
            getAddressSnippet = tip.getAddress();
            LatLng latLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }


    private void reGeocodeSuccess(AMapLocation amapLocation) {
        //定位成功回调信息，设置相关消息
        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getAccuracy();//获取精度信息
        getAddressAll = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        getCountry = amapLocation.getCountry();//国家信息
        amapLocation.getProvince();//省信息
        amapLocation.getCity();//城市信息
        amapLocation.getDistrict();//城区信息
        amapLocation.getStreet();//街道信息
        amapLocation.getStreetNum();//街道门牌号信息
        amapLocation.getCityCode();//城市编码
        amapLocation.getAdCode();//地区编码
        getAddressName = amapLocation.getAoiName();//获取当前定位点的AOI信息
        String name = amapLocation.getPoiName();//获取当前定位点的POI信息
        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
        amapLocation.getFloor();//获取当前室内定位的楼层
        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
        String desc = "";
        Bundle locBundle = amapLocation.getExtras();
        if (locBundle != null) {
            desc = locBundle.getString("desc");
        }
    }
}
