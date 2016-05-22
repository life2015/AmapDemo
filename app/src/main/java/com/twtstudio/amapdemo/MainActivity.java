package com.twtstudio.amapdemo;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.twtstudio.amapdemo.LaLngData.*;

public class MainActivity extends AppCompatActivity implements AMap.OnMarkerClickListener,LocationSource,AMapLocationListener,RadioGroup.OnCheckedChangeListener{
    public AMapLocationClient mlocationClient;
    public OnLocationChangedListener mListener;
    public AMapLocationClientOption mLocationOption;
    private AMap aMap;

    @BindView(R.id.amap_view)
     MapView mMapView;
    @BindView(R.id.sliding_layout)
     SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.icon_slided)
    ImageView imageView;
    @BindView(R.id.content_slide)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);
        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        assert (aMap==null);
        aMap=mMapView.getMap();
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });
        aMap.getCameraPosition();
        Marker marker1=aMap.addMarker(new MarkerOptions().position(fistPostion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_selected)));
        marker1.setTitle("A1");
        aMap.setOnMarkerClickListener(this);
        //aMap.animateCamera(CameraUpdateFactory.zoomIn(),1000000000,null);
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        fistPostion, 18, 30, 0)), null);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener!=null&&aMapLocation!=null)
        {
            if (aMapLocation.getErrorCode()==0)
            {
                System.out.println(aMapLocation.getLatitude());
                mListener.onLocationChanged(aMapLocation);
            }else {
                Log.d("jcy","定位失败"+aMapLocation.getErrorCode());
            }
        }else {
            Log.d("jcy","初始化问题");
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {

            aMap.animateCamera(update, 1000, callback);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        slidingUpPanelLayout.setTouchEnabled(false);
        imageView.setImageResource(R.drawable.ic_session);
        textView.setText("剩余车辆 5 车位 17");
            Log.d("jcy","xians");
        return true;

    }
}
