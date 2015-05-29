package com.android.meili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MyListViewAdapter;
import com.android.entity.GalleryAllItemEntity;
import com.android.net.MLHttpConnect;
import com.android.net.MLHttpConnect2;
import com.android.parser.GalleryAllItemsParser;
import com.android.tools.ImageTools;
import com.android.util.ScreenInfo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.view.HorizonalViewPage;
import com.view.PullDownView;
import com.view.PullDownView.OnItemClickListener;
import com.view.PullDownView.OnRefreshListener;

public class GalleryItemsActivity extends BaseActivity {
	private TextView mTitle, mHeadname, mAuthor, mCreateTime, mInfomation;
	private ImageView mCloseBtn, mHeadBg;
	private ImageView mMatrixBtn;
	boolean isRoundImg = false;
	private int offset = 0;
	private Context mContext;
	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 20;
	private int minVelocity = 0;
	private GalleryAllItemsParser parser;
	public static GalleryAllItemEntity mEntity;
	private int mCurrentIndex = 0;
	private String title = "";
	private HorizonalViewPage mViewPage;
	private String TAG = "GalleryItemsActivity";
	private DisplayMetrics metrics;

	List<Map<String, Object>> adapterlist = new ArrayList<Map<String, Object>>();
	private View.OnClickListener mCloselistener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	private MyListViewAdapter initListView(View v, final int page) {
		PullDownView pullDownView = (PullDownView) v.findViewById(R.id.feeds);
		pullDownView.init();
		pullDownView.setFooterView(R.layout.footer_item);

		pullDownView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				offset = 0;
				// operate();
			}
		});
		ListView listView = pullDownView.getListView();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.gallery_page_head, null);
		mHeadBg = (ImageView) view.findViewById(R.id.img_bg);

		int height = metrics.heightPixels;
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, height / 2);
		mHeadBg.setLayoutParams(lp);
		mHeadname = (TextView) view.findViewById(R.id.name);
		mAuthor = (TextView) view.findViewById(R.id.author_content);
		mCreateTime = (TextView) view.findViewById(R.id.createtime_content);
		mInfomation = (TextView) view.findViewById(R.id.main_content);

		listView.addHeaderView(view);

		pullDownView.showFooterView(false);
		pullDownView.setPullDownEnabled(false);

		listView.setDividerHeight(0);
		MyListViewAdapter mAdapter = new MyListViewAdapter(this, adapterlist,
				R.layout.gallery_items, new String[] { "image" },
				new int[] { R.id.image }, isRoundImg);
		listView.setAdapter(mAdapter);
		pullDownView
				.setPullDownViewOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						int lenght = mEntity.results.get(page).contentPicArr
								.size();
						Log.i(TAG, "lenght:" + lenght + ",position:" + position);
						if (lenght > 0) {
							Intent intent = new Intent(mContext,
									DisplayImageActivity.class);
							intent.putExtra("image",
									mEntity.results.get(page).contentPicArr
											.get(position).url);
							startActivity(intent);
						}
					}
				});
		return mAdapter;
	}

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
			Intent intent;
			if (e2.getX() - e1.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity
					&& Math.abs(e2.getX() - e1.getX()) > Math.abs(e1.getY()
							- e2.getY())) {
				// Log.i(TAG, "fling to left................");
				if (mCurrentIndex == 0) {
					intent = new Intent(GalleryItemsActivity.this,
							GalleryDetailActivity.class);
					// intent.putExtra("objectId", value)
					startActivity(intent);
					finish();
				} else {
					mCurrentIndex--;
					intent = new Intent(GalleryItemsActivity.this,
							GalleryItemsActivity.class);
					startActivity(intent);
					// finish();
					// InitDataList();
				}
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			} else if (e1.getX() - e2.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity
					&& Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
							- e2.getY())) {
				if (mCurrentIndex + 1 < mEntity.results.size()) {
					// CURRENT_INDEX++;
					mCurrentIndex++;
					intent = new Intent(GalleryItemsActivity.this,
							GalleryItemsActivity.class);
					startActivity(intent);
					// finish();
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					// InitDataList();
				}
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

	private HorizonalViewPage.FirstPageFlingToRightListener mlistenr = new HorizonalViewPage.FirstPageFlingToRightListener() {
		@Override
		public void FlingToRight() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(GalleryItemsActivity.this,
					GalleryDetailActivity.class);
			// intent.putExtra("objectId", value)
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horizonal_viewpage);

		metrics = ScreenInfo.getScreenInfo(this);

		mViewPage = (HorizonalViewPage) findViewById(R.id.vPager);
		mViewPage.setFirstPageFlingToLeftListener(mlistenr);

		// mGestureDetector = new GestureDetector(this, new
		// MyOnGestureListener());

		mTitle = (TextView) findViewById(R.id.title);
		title = getIntent().getStringExtra("title");
		mTitle.setText(title);

		mCloseBtn = (ImageView) findViewById(R.id.back_btn);
		mCloseBtn.setOnClickListener(mCloselistener);

		mMatrixBtn = (ImageView) findViewById(R.id.button1);
		mMatrixBtn.setVisibility(android.view.View.VISIBLE);
		mMatrixBtn.setOnClickListener(this);

		Log.i(TAG, "mCurrentIndex:" + mCurrentIndex);
		// mCurrentIndex = getIntent().getIntExtra("index", 0);

		mContext = this;
		getGalleryItemsData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		/*
		 * if (CHANGE_INDEX != CURRENT_INDEX) { CURRENT_INDEX = CHANGE_INDEX;
		 * InitDataList(); }
		 */
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	class MyOnClickListener implements View.OnClickListener {
		int page, num;

		public MyOnClickListener(int i, int j) {
			page = i;
			num = j;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, DisplayImageActivity.class);
			intent.putExtra("image",
					mEntity.results.get(page).contentPicArr.get(num).url);
			startActivity(intent);
		}
	}
	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void InitViewPage() {
		LayoutInflater inflater = getLayoutInflater();
		ArrayList<View> viewlist = new ArrayList<View>();
		// mViewPage.removeAllViews();
		for (int i = 0; i < mEntity.results.size(); i++) {
			View view = inflater.inflate(R.layout.gallery_page, null);
			LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);

			View viewhead = inflater.inflate(R.layout.gallery_page_head, null);
			mHeadBg = (ImageView) viewhead.findViewById(R.id.img_bg);
			// metrics = ScreenInfo.getScreenInfo(this);
			int height = metrics.heightPixels;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, height / 2);
			mHeadBg.setLayoutParams(lp);
			mHeadname = (TextView) viewhead.findViewById(R.id.name);
			mAuthor = (TextView) viewhead.findViewById(R.id.author_content);
			mCreateTime = (TextView) viewhead
					.findViewById(R.id.createtime_content);
			mInfomation = (TextView) viewhead.findViewById(R.id.main_content);

			layout.addView(viewhead);

			// initListView(view, i);
			imageLoader.displayImage(mEntity.results.get(i).coverUrl.url,
					mHeadBg, options);
			// setTextView(mTitle, mEntity.results.get(i).nameBase);
			setTextView(mHeadname, mEntity.results.get(i).nameBase);
			setTextView(mAuthor, mEntity.results.get(i).creator);
			setTextView(mInfomation, mEntity.results.get(i).information);

			if (mEntity.results.get(i).creationTime != null) {
				setTextView(mCreateTime,
						mEntity.results.get(i).creationTime.iso);
			}

			// adapterlist.clear();
			if (mEntity.results.get(i).contentPicArr != null) {
				int length = mEntity.results.get(i).contentPicArr.size();
				Log.i(TAG, "i:" + i + ",contentPicArr length:" + length);
				for (int j = 0; j < length; j++) {
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("image",
					// mEntity.results.get(i).contentPicArr.get(j).url);
					// adapterlist.add(map);
					final ImageView image = new ImageView(this);
					image.setOnClickListener(new MyOnClickListener(i, j));
					// image.setScaleType(ScaleType.MATRIX);
					image.setPadding(20, 0, 20, 20);
					layout.addView(image);
					imageLoader.loadImage(
							mEntity.results.get(i).contentPicArr.get(j).url,
							options, new ImageLoadingListener() {

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									// TODO Auto-generated method stub
									int width = metrics.widthPixels
											- ScreenInfo.dip2px(mContext, 20)
											* 2;
									int height = width * arg2.getHeight()
											/ arg2.getWidth();
									LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
											width, height);
									lp.gravity = Gravity.CENTER_HORIZONTAL;
									lp.setMargins(0, 0, 0, 40);
									image.setLayoutParams(lp);
									image.setBackgroundDrawable(ImageTools
											.bitmapToDrawable(arg2));
								}

								@Override
								public void onLoadingFailed(String arg0,
										View arg1, FailReason arg2) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onLoadingStarted(String arg0,
										View arg1) {
									// TODO Auto-generated method stub

								}

							});
					// imageLoader.displayImage(
					// mEntity.results.get(i).contentPicArr.get(j).url,
					// image);
				}
			}
			viewlist.add(view);
			// mAdapter.notifyDataSetChanged();
		}
		mViewPage.setAdapter(new MyViewPagerAdapter(viewlist));
		mViewPage.setCurrentItem(mCurrentIndex);
		// mViewPage.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			cancelProgressDialog();
			if (MLHttpConnect2.SUCCESS == msg.what) {
				mEntity = parser.entity;
				if (mEntity != null) {
					// InitDataList();
					InitViewPage();
				}
			} else {
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
			super.handleMessage(msg);
		}

	};

	private void getGalleryItemsData() {
		this.showProgressDialog();
		String objectId = getSharedPreferences("gallery_detail", MODE_PRIVATE)
				.getString("objectId", "");
		parser = new GalleryAllItemsParser();
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("limit", 10 + "");
		parmas.put("skip", 0 + "");
		// parmas.put(":gid", "54b3a51de4b06e8b5a7fae79");
		parmas.put(":gid", objectId);
		MLHttpConnect.getGalleryItemsData(mContext, parmas, parser, mHandler);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "resultCode:" + resultCode);
		if (resultCode == 20) {
			mCurrentIndex = data.getIntExtra("index",
					mViewPage.getCurrentItem());
			if (mViewPage != null) {
				mViewPage.setCurrentItem(mCurrentIndex);
			}
		}
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case R.id.button1:
			Intent intent = new Intent(GalleryItemsActivity.this,
					GalleryViewActivity.class);
			intent.putExtra("title", title);
			this.startActivityForResult(intent, 0);
			break;
		}
	}
}
