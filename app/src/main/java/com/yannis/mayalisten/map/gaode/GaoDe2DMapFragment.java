package com.yannis.mayalisten.map.gaode;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.blankj.utilcode.util.ToastUtils;
import com.yannis.maplib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * GaoDe2DMapFragment 高德地图
 * <p>
 * 显示定位蓝点：https://lbs.amap.com/api/android-sdk/guide/create-map/mylocation
 * 获取地址描述数据：https://lbs.amap.com/api/android-sdk/guide/map-data/geo
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-06-29
 */
public class GaoDe2DMapFragment extends Fragment implements AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = "Map";

    private MapView mMapView = null;
    private AMap aMap = null;
    private MarkerOptions markerOption = null;
    private ArrayList<LatLng> mLatLngArrayList = new ArrayList<>();
    private ArrayList<Marker> mMarkers = new ArrayList<>();
    private Activity mActivity = null;
    private Bundle mSavedInstanceState = null;
    private GeocodeSearch mGeocodeSearch = null;
    private Marker mMarker = null;


    private MyLocationStyle myLocationStyle;
    private LatLng mLatLng = null;
    private PoiSearch.Query query = null;

    public GaoDe2DMapFragment() {
    }


    public static GaoDe2DMapFragment newInstance(String param1) {
        GaoDe2DMapFragment fragment = new GaoDe2DMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.gaode_2d_map_fragment_layout, container, false);
        initView(mView, savedInstanceState);
        return mView;
    }

    private void initView(View mView, Bundle savedInstanceState) {
        mMapView = mView.findViewById(R.id.map_gaode_2d_fragment);
        // TODO 权限检查
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (aMap != null) {
            aMap = null;
        }
        // 销毁地图
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity = null;
        }
    }

    // TODO 权限通过调用
    private void startLocation() {
        mGeocodeSearch = new GeocodeSearch(mActivity);
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        getMap();
    }

    private void getMap() {
        // 创建地图
        mMapView.onCreate(mSavedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setOnMapClickListener(this);
        }

        initLocationStyle();
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置 定位蓝点 移动监听
        aMap.setOnMyLocationChangeListener(this);
        // 设置 marker 点击事件监听
        aMap.setOnMarkerClickListener(this);

    }

    /**
     * 绘制固定坐标点的 marker
     */
    private void initLatLonFromServer() {
        // 济南广场汽车站售票处
        mLatLngArrayList.add(new LatLng(36.669851, 116.993246));
        // 山东大学(中心校区)明德楼A座
        mLatLngArrayList.add(new LatLng(36.67304, 117.060803));

        for (int i = 0; i < mLatLngArrayList.size(); i++) {
            MarkerOptions option = new MarkerOptions();
            option.draggable(false);
            option.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.wechatview_icon_fixed_location)));
            if (i == 0) {
                option.snippet("济南广场汽车站售票处");
            } else {
                option.snippet("山东大学(中心校区)");
            }
            option.position(mLatLngArrayList.get(i));
            Marker marker = aMap.addMarker(option);
            marker.hideInfoWindow();
            mMarkers.add(aMap.addMarker(option));
        }
    }

    private void initLocationStyle() {
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(false);// 地图定位标志是否可以点击
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.showMyLocation(true);
    }


    private void initMarkOptions() {
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }

        if (markerOption != null) {
            markerOption = null;
        }
        markerOption = new MarkerOptions();
        markerOption.draggable(false);
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.wechatview_icon_fixed_location)));
        markerOption.position(mLatLng);
        mMarker = aMap.addMarker(markerOption);

    }


    private void geocodeQuery() {
        // 2:地理编码（地址转坐标）。参数1：地址，参数2：查询城市，中文或者中文全拼，citycode、adcode
        //GeocodeQuery queryStrName = new GeocodeQuery(name, "010");
        //mGeocodeSearch.getFromLocationNameAsyn(queryStrName);
    }


    private void reGeocode(LatLonPoint latLonPoint) {
        // 1:逆地理编码（坐标转地址）。参数1：经纬度; 参数2：范围（米）, 参数3：是"火系坐标系"还是"GPS原生坐标系"
        RegeocodeQuery queryLatLon = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(queryLatLon);
    }

    /**
     * 当前坐标是否在中国
     */
    private void isInChina(LatLng sourceLatLng) {
        //返回true代表当前位置在大陆、港澳地区，反之不在。
        //第一个参数为纬度，第二个为经度，纬度和经度均为高德坐标系。
        boolean isAMapDataAvailable = CoordinateConverter.isAMapDataAvailable(sourceLatLng.latitude, sourceLatLng.longitude);
    }

    /**
     * 地图坐标转换
     */
    private void coordinateConverter(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 DPoint类型
        converter.coord(sourceLatLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
    }

    /**
     * 逆地理编码（坐标转地址）
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        if (rCode == 1000) {
            initMarkOptions();
            initPoiBoundSearch(regeocodeResult);

            RegeocodeAddress getAddressAll = regeocodeResult.getRegeocodeAddress();
            Log.e(TAG, "反向地理编码结果 : "
                    + "\n getCountry: " + getAddressAll.getCountry()
                    + ",\n getProvince: " + getAddressAll.getProvince()
                    + ",\n getCity: " + getAddressAll.getCity()
                    + ",\n getCityCode: " + getAddressAll.getCityCode()
                    + ",\n getDistrict: " + getAddressAll.getDistrict()
                    + ",\n getTownship: " + getAddressAll.getTownship()
                    + ",\n getTowncode: " + getAddressAll.getTowncode()
                    + ",\n getNeighborhood: " + getAddressAll.getNeighborhood()
                    + ",\n getStreetNumber-getDirection: " + getAddressAll.getStreetNumber().getDirection()
                    + ",\n getStreetNumber-getNumber: " + getAddressAll.getStreetNumber().getNumber()
                    + ",\n getStreetNumber-getStreet: " + getAddressAll.getStreetNumber().getStreet()
                    + ",\n getStreetNumber-getDistance: " + getAddressAll.getStreetNumber().getDistance()
                    + ",\n getStreetNumber-getLatLonPoint: " + getAddressAll.getStreetNumber().getLatLonPoint().toString()
                    + ",\n getBuilding: " + getAddressAll.getBuilding()
                    + ",\n getFormatAddress: " + getAddressAll.getFormatAddress());
        }
    }

    /**
     * 范围搜索
     */
    private void initPoiBoundSearch(RegeocodeResult regeocodeResult) {
        query = new PoiSearch.Query("", "", regeocodeResult.getRegeocodeAddress().getCityCode());
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        //设置查第一页
        query.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(mActivity, query);
        //设置周边搜索的中心点以及半径
        poiSearch.setBound(new PoiSearch.SearchBound(regeocodeResult.getRegeocodeQuery().getPoint(), 500));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 地理编码（地址转坐标）
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int code) {
        if (code == 1000) {
            // 返回成功
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            for (int i = 0; i < geocodeAddressList.size(); i++) {
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                LatLng latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
            }
        }
    }

    /**
     * 地图点击事件
     */
    @Override
    public void onMapClick(LatLng latLng) {
        mLatLng = latLng;
        float zoom = aMap.getCameraPosition().zoom;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom > 16 ? zoom : 16));
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        reGeocode(latLonPoint);
    }

    /**
     * 地图定位 监听
     */
    @Override
    public void onMyLocationChange(Location location) {
        //存放所有点的经纬度
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        LatLng latLonPoint = new LatLng(location.getLatitude(), location.getLongitude());
        initLatLonFromServer();

        mLatLngArrayList.add(latLonPoint);
        for (int i = 0; i < mLatLngArrayList.size(); i++) {
            //把所有点都include进去（LatLng类型）
            boundsBuilder.include(mLatLngArrayList.get(i));
        }
        // 在地图中显示所有的marker点
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 15));
        // 将当前坐标作为地图中心位置，同时设置比例尺
        //aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLonPoint, 16));
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        ArrayList<PoiItem> poiItems = new ArrayList<>();
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {
                if (poiResult.getQuery().equals(query)) {
                    poiItems = poiResult.getPois();
                    if (poiItems != null && poiItems.size() > 0) {
                        for (PoiItem item : poiItems) {
                            LatLonPoint llp = item.getLatLonPoint();
                            //返回POI的名称。
                            String getAddressName = item.getTitle();
                            // 返回POI的地址。
                            String text = item.getSnippet();
                            String provinceName = item.getProvinceName();
                            String cityName = item.getCityName();
                            // 返回POI 的行政区划代码。
                            String adName = item.getAdName();
                            Log.e(TAG, "POI结果: "
                                    + "LatLon: " + llp
                                    + ", getAddressName: " + getAddressName
                                    + ", text: " + text
                                    + ", province: " + provinceName
                                    + ", city: " + cityName
                                    + ", ad: " + adName
                            );
                        }
                    } else {
                        ToastUtils.showShort("无搜索结果");
                    }
                }
            } else {
                ToastUtils.showShort("无搜索结果");
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * Marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        //mMarkers = (ArrayList<Marker>) aMap.getMapScreenMarkers();
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }

        // return true 的意思是点击marker,marker不成为地图的中心坐标，反之，成为中心坐标。
        return true;
    }

    /**
     * TextView点击事件
     */
    @Override
    public void onClick(View v) {
        Location myLocation = aMap.getMyLocation();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude())));
    }

}
