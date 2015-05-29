package com.android.meili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.adapter.MyListViewAdapter;
import com.android.net.MLHttpConnect;
import com.android.net.MLHttpConnect2;
import com.android.parser.CityListParser;
import com.android.parser.PavilionDetailParser;
import com.umeng.analytics.MobclickAgent;

public class ChooseCityActivity extends BaseActivity implements AMapLocationListener, Runnable {
	private ListView mlist;
	private RelativeLayout mTitleBg;
	private TextView mTitle;
	private MyListViewAdapter mAdapter;
	private TextView mlocationCity;
	List<Map<String, Object>> adapterlist = new ArrayList<Map<String, Object>>();
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();
	private String TAG = "ChooseCityActivity";
	private Context mContext;

	
	private CityListParser parser;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			cancelProgressDialog();
			if (MLHttpConnect2.SUCCESS == msg.what) {
				String result=(String)msg.obj;
				Log.i(TAG,"result:"+result);
				InitListView();
			//	result+="\n"+parser.entity.status;
			//	result+="\n"+parser.entity.results.get(0).creationTime.iso;

			//	text.setText(result);
			} else {
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}

	};

	private void getCityList(){
		showProgressDialog();
		parser = new CityListParser();
		Map<String, String> parmas = new HashMap<String, String>();
		MLHttpConnect.getCityListData(this, parmas, parser, mHandler);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_city);
		mContext = this;

		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 12*1000);

		mTitleBg = (RelativeLayout) findViewById(R.id.title_layout);
		mTitleBg.setBackgroundColor(Color.WHITE);
		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText("切换城市");
		mTitle.setTextColor(Color.BLACK);

		mlist = (ListView) findViewById(R.id.city_list);

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.city_headview, null);
		mlocationCity = (TextView) view.findViewById(R.id.location_city);

		mlist.addHeaderView(view);

		mAdapter = new MyListViewAdapter(this, adapterlist, R.layout.city_item,
				new String[] { "city" }, new int[] { R.id.city }, false);
		mlist.setAdapter(mAdapter);
		
		getCityList();
	}

	private void InitListView() {
		adapterlist.clear();
		for(int i = 0;i<parser.entity.results.citys.size();i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("city", parser.entity.results.citys.get(i));
			adapterlist.add(map);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (aMapLocation == null) {
			// ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
			// myLocation.setText("12秒内还没有定位成功，停止定位");
			stopLocation();// 销毁掉定位
		}
	}
	
	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		// TODO Auto-generated method stub
		if (location != null) {
			this.aMapLocation = (AMapLocation) location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}

			String city = location.getCity();
			mlocationCity.setText(city);
			Log.i(TAG, "getCurrentCity:" + city);
		}
	}
}
