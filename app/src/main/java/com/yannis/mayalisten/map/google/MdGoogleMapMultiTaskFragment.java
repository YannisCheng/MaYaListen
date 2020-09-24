package com.yannis.mayalisten.map.google;

/**
 * MdGoogleMapMultiTaskFragment google地图
 *
 * @author wenjia.Cheng  cwj1714@163.com
 * @date 2019-07-14
 * <p>
 * 坐标点
 * <p>
 * 谷歌地图定位
 * <p>
 * 谷歌定位
 * <p>
 * 坐标转换
 * <p>
 * 坐标转换
 * <p>
 * 坐标点
 * <p>
 * 谷歌地图定位
 * <p>
 * 谷歌定位
 * <p>
 * 坐标转换
 * <p>
 * 坐标转换
 */
/*public class MdGoogleMapMultiTaskFragment extends Fragment implements OnMapReadyCallback {

    private Activity mActivity = null;
    private GoogleMap googlemap;
    *//**
 * 坐标点
 *//*
    protected Location mLastKnownLocation;
    *//**
 * 谷歌地图定位
 *//*
    protected FusedLocationProviderClient mFusedLocationProviderClient;
    private MapView map = null;
    private ArrayList<MapMarkersData> lonBeans = new ArrayList<>();
    private boolean isFromDetail = false;

    public static MdGoogleMapMultiTaskFragment getInstance(ArrayList<MapMarkersData> lonBeans,boolean isFromDetail) {
        MdGoogleMapMultiTaskFragment googleMapMultiTaskFragment = new MdGoogleMapMultiTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lonBeans", lonBeans);
        bundle.putBoolean("is_from_detail",isFromDetail);
        googleMapMultiTaskFragment.setArguments(bundle);
        return googleMapMultiTaskFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lonBeans = (ArrayList<MapMarkersData>) getArguments().getSerializable("lonBeans");
            isFromDetail =  getArguments().getBoolean("is_from_detail");
        }
        for (int i = 0; i < lonBeans.size(); i++) {
            Log.e(TAG, "ヽ(｀Д´)ﾉ -> onCreate : " +lonBeans.get(i).toString());
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
        View rootView = inflater.inflate(R.layout.md_google_task_map, container, false);
        map = rootView.findViewById(R.id.google_map);
        map.onCreate(savedInstanceState);
        map.onResume();
        int errorCode = (int) GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (ConnectionResult.SUCCESS != errorCode) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, mActivity, 0).show();
        } else {
            map.getMapAsync(this);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (map != null) {
            map.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mActivity != null) {
            mActivity = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (map != null) {
            map.onDestroy();
            map = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap mGoogleMap) {
        googlemap = mGoogleMap;
        if (isFromDetail) {

        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        updateLocationUI();
        getDeviceLocation();
        final LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
        MarkerOptions markerOptions = new MarkerOptions();
        for (int i = 0; lonBeans != null && i < lonBeans.size(); i++) {
            MapMarkersData data = (MapMarkersData) lonBeans.get(i);
            LatLng latLng = new LatLng(data.getLongitude(), data.getLatitude());
            Log.e(TAG, "ヽ(｀Д´)ﾉ -> onMapReady latLng : " +latLng.toString());
            latLngBounds.include(latLng);
            markerOptions
                    .position(latLng)
                    .getAddressName(data.getTitle())
                    .icon(BitmapDescriptorFactory.fromResource(data.getIcon()));
            googlemap.addMarker(markerOptions).setTag(i);
        }
        //googlemap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 50));
        //获得ViewTreeObserver
        final ViewTreeObserver observer = map.getViewTreeObserver();
        //注册观察者，监听变化
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                observer.removeOnGlobalLayoutListener(this);
                //获得宽高
                int viewWidth = map.getMeasuredWidth();
                int viewHeight = map.getMeasuredHeight();

                final float maxZoomLevel = googlemap.getMaxZoomLevel();

                googlemap.setMaxZoomPreference(15);

                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), viewWidth, viewHeight, ConvertUtils.dp2px(50));
                googlemap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        googlemap.setMaxZoomPreference(maxZoomLevel);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    *//**
 * 谷歌定位
 *//*
    public void getDeviceLocation() {
        try {
            if (EasyPermissionUtils.checkLocationPermission()) {
                final Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(_mActivity, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            if(mLastKnownLocation == null){
                                return;
                            }
                            transformWGS84toGCJ20();
                            googlemap.setLocationSource(new LocationSource() {
                                @Override
                                public void activate(OnLocationChangedListener onLocationChangedListener) {
                                    onLocationChangedListener.onLocationChanged(mLastKnownLocation);
                                }

                                @Override
                                public void deactivate() {

                                }
                            });
                            googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new com.google.android.gms.maps.model.LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch(Exception e)  {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void transformWGS84toGCJ20() {
        double wgLat = mLastKnownLocation.getLatitude();
        double wgLon = mLastKnownLocation.getLongitude();
        double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
        double dLon = transformLong(wgLon - 105.0, wgLat - 35.0);
        double radLat = wgLat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - 0.00669342162296594323 * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((6378245.0 * (1 - 0.00669342162296594323)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (6378245.0 / sqrtMagic * Math.cos(radLat) * Math.PI);
        mLastKnownLocation.setLatitude(wgLat + dLat);
        mLastKnownLocation.setLongitude(wgLon + dLon);
        LogUtils.d("坐标Lat--" + mLastKnownLocation.getLatitude());
        LogUtils.d("坐标Long--" + mLastKnownLocation.getLongitude());
    }

    *//**
 * 坐标转换
 *//*
    private double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    *//**
 * 坐标转换
 *//*
    private double transformLong(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

    private void updateLocationUI() {
        if (googlemap == null) {
            return;
        }
        googlemap.setMyLocationEnabled(true);
        // 关闭定位按钮
        googlemap.getUiSettings().setMyLocationButtonEnabled(false);
    }
}*/
