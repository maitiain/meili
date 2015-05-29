package com.android.meili;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entity.GalleryDetailEntity;
import com.android.net.MLHttpConnect;
import com.android.net.MLHttpConnect2;
import com.android.parser.GalleryDetailParser;
import com.android.util.ScreenInfo;
import com.umeng.analytics.MobclickAgent;

public class GalleryDetailActivity extends BaseActivity {

	private ImageView mBackBtn, mTopBackground;
	private TextView mTitle, mMaincontent;
	private TextView mTitle1, mTitle2, mTime, mPlace, mLoaction, mFee;
	private String TAG = "GalleryDetailActivity";
	private int verticalMinDistance = 20;
	private int minVelocity = 0;
	private String objectId = "";
	private GalleryDetailParser parser;
	private Context mContext;
	private GalleryDetailEntity mEntity;
	private Intent intent;
	private LinearLayout mImagelayout;
	private int SCREEN_WIDTH, SCREEN_HEIGHT;
	private LayoutInflater mInflater;
	private String pavilionname, address;

	private View.OnClickListener mBackListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	private GestureDetector mGestureDetector;

	class MyOnGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1.getX() - e2.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity
					&& Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
							- e2.getY())) {
				Log.i(TAG, "fling to left................");
				Intent intent = new Intent(GalleryDetailActivity.this,
						GalleryItemsActivity.class);
				intent.putExtra("title", mEntity.results.nameBase);
				startActivity(intent);
				finish();
		//		overridePendingTransition(android.R.anim.)
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			cancelProgressDialog();
			if (MLHttpConnect2.SUCCESS == msg.what) {
				mEntity = parser.entity;
				if (mEntity != null) {
					InitDataList();
				}
			} else {
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
			super.handleMessage(msg);
		}

	};

	class MyOnClickListenerWithNum implements View.OnClickListener {
		int number = 0;

		MyOnClickListenerWithNum(int i) {
			number = i;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Log.i(TAG, "number:" + number);
			Intent intent = new Intent(mContext, DisplayImageActivity.class);
			Log.i(TAG, "mEntity.results.contentPicArr.size:"
					+ mEntity.results.contentPicArr.size());
			intent.putExtra("image",
					mEntity.results.contentPicArr.get(number).url);
			startActivity(intent);
		}
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

	private void InitDataList() {
		mImagelayout.removeAllViews();
		mTitle.setText(mEntity.results.nameBase);
		mTitle1.setText(mEntity.results.nameBase);
		mTitle2.setText(mEntity.results.subName);

		mTime.setText(mEntity.results.beginTime.iso + "   "
				+ mEntity.results.endTime.iso);
		mFee.setText(mEntity.results.entrancePrice + " "
				+ mEntity.results.priceAddInfo);
		mMaincontent.setText(mEntity.results.information);
		imageLoader.displayImage(mEntity.results.coverUrl.url, mTopBackground,options);
		if (mEntity.results.contentPicArr.size() > 0) {
			for (int i = 0; i < mEntity.results.contentPicArr.size(); i = i + 2) {
				View v = mInflater.inflate(R.layout.gallery_detail_items, null);
				ImageView img1 = (ImageView) v.findViewById(R.id.img1);
				ImageView img2 = (ImageView) v.findViewById(R.id.img2);

				img1.setOnClickListener(new MyOnClickListenerWithNum(i));
				int imgwidth = (SCREEN_WIDTH - ScreenInfo.dip2px(mContext, 40) * 2) / 2;
				RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
						imgwidth, imgwidth);
				lp1.leftMargin = ScreenInfo.dip2px(mContext, 30);
				lp1.topMargin = ScreenInfo.dip2px(mContext, 18);
				img1.setLayoutParams(lp1);

				RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
						imgwidth, imgwidth);
				lp2.topMargin = ScreenInfo.dip2px(mContext, 18);
				lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				lp2.rightMargin = ScreenInfo.dip2px(mContext, 30);
				img2.setLayoutParams(lp2);

				mImagelayout.addView(v);
				imageLoader.displayImage(
						mEntity.results.contentPicArr.get(i).url, img1);
				if (i + 1 < mEntity.results.contentPicArr.size()) {
					img2.setOnClickListener(new MyOnClickListenerWithNum(i + 1));
					imageLoader.displayImage(
							mEntity.results.contentPicArr.get(i + 1).url, img2);

				}
			}
		}
	}

	private void getGalleryDetailJsonData() {
		showProgressDialog();
		parser = new GalleryDetailParser();
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("eid", objectId);
		MLHttpConnect.getGalleryDetailData(mContext, parmas, parser, mHandler);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.summarize);

		mInflater = getLayoutInflater();
		DisplayMetrics metrics = ScreenInfo.getScreenInfo(this);
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_HEIGHT = metrics.heightPixels;

		mContext = this;
		intent = getIntent();
		SharedPreferences sp = getSharedPreferences("gallery_detail",
				MODE_PRIVATE);
		if (intent.getStringExtra("objectId") != null) {
			objectId = intent.getStringExtra("objectId");
			pavilionname = intent.getStringExtra("pavilionname");
			address = intent.getStringExtra("address");
			Editor ed = sp.edit();
			ed.putString("objectId", objectId);
			ed.putString("pavilionname", pavilionname);
			ed.putString("address", address);
			ed.commit();
		} else {
			objectId = sp.getString("objectId", "");
			pavilionname = sp.getString("pavilionname", "");
			address = sp.getString("address", "");
		}

		mBackBtn = (ImageView) findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(mBackListener);

		mTitle = (TextView) findViewById(R.id.title);
		// mTitle.setText("M home:随遇而安");

		mTitle1 = (TextView) findViewById(R.id.title1);
		// mTitle1.setText("M Home 随遇而安");

		mTitle2 = (TextView) findViewById(R.id.title2);
		// mTitle2.setText("---------红星美凯龙艺术大展");

		mTime = (TextView) findViewById(R.id.time_content);
		// mTime.setText("2014.11.1-2014.12.31   10:00 - 18:30");

		mPlace = (TextView) findViewById(R.id.place_content);
		mPlace.setText(pavilionname);

		mLoaction = (TextView) findViewById(R.id.location_content);
		mLoaction.setText(address);

		mFee = (TextView) findViewById(R.id.fee_content);
		// mFee.setText("免费");

		mTopBackground = (ImageView) findViewById(R.id.top_background);
		// mTopBackground.setImageResource(R.drawable.zhanguan3);

		mMaincontent = (TextView) findViewById(R.id.main_content);
		// mMaincontent.setText(getString(R.string.main_content));
		mImagelayout = (LinearLayout) findViewById(R.id.img_layout);

		mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
		if (!objectId.equals("")) {
			getGalleryDetailJsonData();
		}
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub

	}
}
