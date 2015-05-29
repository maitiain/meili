package com.android.meili;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application implements AMapLocationListener {
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler;
	public static String MYCITY = "";
	private PostLocationCity mLocationCity = null;
	private String TAG = "MyApplication";

	private HandlerThread handleThread = new HandlerThread("MyApplication") {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (aMapLocation == null) {
						// ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
						// myLocation.setText("12秒内还没有定位成功，停止定位");
						stopLocation();// 销毁掉定位
					}
				}
			};
			handler.sendEmptyMessageDelayed(1, 12 * 1000);
			// handler.postDelayed(handleThread, 12000);// 设置超过12秒还没有定位到就停止定位
		}
	};

	public static void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}
		// 初始化sd卡缓存和内存缓存
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(false).cacheOnDisc(true).build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(defaultOptions)
				.memoryCacheExtraOptions(480, 800) 
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)// Not
																// necessary
																// in
																// common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Application onCreate .........");
		instance = (MyApplication) getApplicationContext();

		initImageLoader(instance);
		
		// aMapLocManager = LocationManagerProxy.getInstance(this);

		/*
		 * mAMapLocManager.setGpsEnable(false);//
		 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
		 * API定位采用GPS和网络混合定位方式
		 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		// aMapLocManager.requestLocationUpdates(
		// LocationProviderProxy.AMapNetwork, 2000, 10, this);
		// handleThread.start();
	}

	public static interface PostLocationCity {
		void PostCurrentCity(String city);
	}

	public void requesLocation(PostLocationCity mPost) {
		mLocationCity = mPost;
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		// handler.postDelayed(handleThread, 12000);// 设置超过12秒还没有定位到就停止定位
		handler.sendEmptyMessageDelayed(1, 12 * 1000);
	}

	public static MyApplication instance;

	public static MyApplication getSelf() {
		return instance;
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

			MYCITY = location.getCity();
			Log.i(TAG, "getCurrentCity:" + MYCITY);
			if (mLocationCity != null) {
				mLocationCity.PostCurrentCity(location.getCity());
			}

			/*
			 * String str = ("定位成功:(" + geoLng + "," + geoLat + ")" +
			 * "\n精    度    :" + location.getAccuracy() + "米" + "\n定位方式:" +
			 * location.getProvider() + "\n定位时间:" +
			 * AMapUtil.convertToTime(location.getTime()) + "\n城市编码:" + cityCode
			 * + "\n位置描述:" + desc + "\n省:" + location.getProvince() + "\n市:" +
			 * location.getCity() + "\n区(县):" + location.getDistrict() +
			 * "\n区域编码:" + location .getAdCode()); //myLocation.setText(str);
			 */
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}
}
