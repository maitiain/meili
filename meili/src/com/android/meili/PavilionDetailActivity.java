package com.android.meili;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tools.ImageTools;
import com.android.util.ScreenInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

public class PavilionDetailActivity extends BaseActivity {
	private ImageView mClose;
	private TextView mTitle, mContent;
	private String[] mPicArr;
	private LinearLayout mlinearlayout;
	private ArrayList<ImageView> mImgList;
	private int mWinWidth;
	private Context mContext;

	View.OnClickListener mCloseListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.zhanguan_detail);

		Intent intent = this.getIntent();

		mClose = (ImageView) findViewById(R.id.close);
		mClose.setOnClickListener(mCloseListener);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText(intent.getStringExtra("nameBase"));
		mContent = (TextView) findViewById(R.id.content);
		mContent.setText(intent.getStringExtra("information"));

		mPicArr = intent.getExtras().getStringArray("contentPicArr");

		mlinearlayout = (LinearLayout) findViewById(R.id.content_layout);
		if (mPicArr != null) {
			if (mPicArr.length > 0) {
				AddPicImage();
			}
		}
	}

	private void AddPicImage() {
		DisplayMetrics metrics = ScreenInfo.getScreenInfo(this);
		mWinWidth = metrics.widthPixels;

		mImgList = new ArrayList<ImageView>();
		LinearLayout.LayoutParams lp;
		for (int i = 0; i < mPicArr.length; i++) {
			ImageView mImageView = new ImageView(this);
			//lp = new LinearLayout.LayoutParams(
			//		LinearLayout.LayoutParams.WRAP_CONTENT,
			//		LinearLayout.LayoutParams.WRAP_CONTENT);
			//lp.gravity = Gravity.CENTER_HORIZONTAL;

			//mImageView.setLayoutParams(lp);
			mlinearlayout.addView(mImageView);
			mImgList.add(mImageView);
		}
		DisplayImage();
	}
	
	class MyImageLoadingListener implements ImageLoadingListener{
		int num;
		public MyImageLoadingListener(int i){
			num = i;
		}
		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			// TODO Auto-generated method stub
			int imgwidth = mWinWidth - ScreenInfo.dip2px(mContext, 38)*2;
			int imgheight = imgwidth*arg2.getHeight()/arg2.getWidth();
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					imgwidth,
					imgheight);
			lp.topMargin = 28;
			mImgList.get(num).setLayoutParams(lp);
			mImgList.get(num).setBackgroundDrawable(ImageTools
									.bitmapToDrawable(arg2));
		}

		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private void DisplayImage() {
		for (int i = 0; i < mImgList.size(); i++) {	
			imageLoader.loadImage(mPicArr[i], new MyImageLoadingListener(i));
		}
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub

	}

}
