package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.ui.widget.ResideMenu;
import org.kymjs.aframe.ui.widget.ResideMenu.OnMenuListener;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MyListViewAdapter;
import com.android.entity.pavilionDetailEntity;
import com.android.entity.pavilionDetailEntity.PavilionDetail;
import com.android.entity.pavilionDetailEntity.PavilionDetail.ContentPicArr;
import com.android.meili.DisplayExhibitonActivity;
import com.android.meili.GalleryDetailActivity;
import com.android.meili.PavilionDetailActivity;
import com.android.meili.R;
import com.android.meili.VerticalPagerAdapter;
import com.android.net.MLHttpConnect;
import com.android.net.MLHttpConnect2;
import com.android.parser.PavilionDetailParser;
import com.android.util.ScreenInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.view.PullDownView.OnItemClickListener;
import com.view.PullDownView.OnRefreshListener;

@SuppressLint("ValidFragment")
public class BitmapDisplay extends BaseFragment {
	private HorizonalViewPage viewPager;//
	// private ImageView imageView;//
	private TextView textView1, textView2;
	private List<View> views;//
	private int offset = 0;//
	public static int currIndex = 0;//
	private int bmpW;//
	private View view1, view2, view3;//
	private View vertialView1, vertialView2;
	private TextView mLine1, mLine2;
	private VerticalViewPager verticalViewPager;
	private List<View> listViews = new ArrayList<View>();// verpage 的view
	private int verticalbmpH;
	private ImageView mMenuBtn;
	private ResideMenu resideMenu;
	private String TAG = "BitmapDisplay";
	private PullDownView pullDownView;
	private Context mContext;
	public ImageLoader imageLoader = ImageLoader.getInstance();;
	public DisplayImageOptions options;
	private ProgressDialog progressDialog;
	// private ArrayList<Map<String, Object>> mCollectList;
	private SharedPreferences mPre;

	// private Map<String, ?> mCollectAll;

	public class Params {
		String objectId;
		String ExhibitionName;
		String address;
		String detailaddress;
		String pavilionname;
	}

	private ArrayList<Params> mParamList;

	public BitmapDisplay(ResideMenu menu) {
		resideMenu = menu;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		mPre = mContext.getSharedPreferences("collect_gallery",
				mContext.MODE_PRIVATE);
		// mCollectAll = mPre.getAll();
		/*
		 * for(Map.Entry<String, ?> entry : mCollectAll.entrySet()){ String
		 * key=(entry.getKey()); String value=(String) (entry.getValue());
		 * Log.i("测试输出", key+" "+value); }
		 */
	}

	private View.OnClickListener mMenuListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!resideMenu.isOpened())
				resideMenu.openMenu();
		}

	};

	private OnMenuListener mMenuOpenListener = new OnMenuListener() {

		@Override
		public void openMenu() {
			// TODO Auto-generated method stub
			Log.i(TAG, "openMenu ..........");
			// listView.setEnabled(false);
			// textView1.setEnabled(false);
			// textView2.setEnabled(false);
			// mMenuBtn.setEnabled(false);
		}

		@Override
		public void closeMenu() {
			// TODO Auto-generated method stub
			Log.i(TAG, "close Menu ..........");
			// listView.setEnabled(true);
			// textView1.setEnabled(true);
			// textView2.setEnabled(true);
			// mMenuBtn.setEnabled(true);
		}

	};

	@Override
	protected void initWidget(View parentView) {
		// TODO Auto-generated method stub
		super.initWidget(parentView);
		mLine1 = (TextView) parentView.findViewById(R.id.cursor1);
		mLine2 = (TextView) parentView.findViewById(R.id.cursor2);
		mLine2.setVisibility(android.view.View.INVISIBLE);
		mMenuBtn = (ImageView) parentView.findViewById(R.id.menu_btn);
		mMenuBtn.setOnClickListener(mMenuListener);
		InitImageView();
		InitTextView(parentView);
		InitViewPager(parentView);
		resideMenu.setMenuListener(mMenuOpenListener);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPavilionJsonData();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View view = inflater.inflate(R.layout.activity_main, null);
		return view;
	}

	private void InitTextView(View view) {
		textView1 = (TextView) view.findViewById(R.id.text1);
		textView2 = (TextView) view.findViewById(R.id.text2);
		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
	}

	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

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

	public class MyOnPageChangeListener implements
			android.support.v4.view.ViewPager.OnPageChangeListener {

		int one = offset * 2 + bmpW;//
		int two = one * 2;//

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			currIndex = arg0;
			if (currIndex == 0) {
				mLine1.setVisibility(android.view.View.VISIBLE);
				mLine2.setVisibility(android.view.View.INVISIBLE);
				if (resideMenu != null) {
					Log.i(TAG, "set menu true");
					resideMenu.setResideMenuEnable(true);
				}
			} else {
				mLine2.setVisibility(android.view.View.VISIBLE);
				mLine1.setVisibility(android.view.View.INVISIBLE);
				if (resideMenu != null) {
					Log.i(TAG, "set menu false");
					resideMenu.setResideMenuEnable(false);
				}
			}
		}

	}

	/**
	 *
	 */

	private void InitImageView() {
		// imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();//
		DisplayMetrics dm = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenW = dm.widthPixels;//
		offset = (screenW / 2 - bmpW) / 2;//
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		// imageView.setImageMatrix(matrix);//
	}

	public class VertialOnPageChangeListener implements
			VerticalViewPager.OnPageChangeListener {

		int one = offset * 2 + verticalbmpH;//
		int two = one * 2;//

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			// Log.i(TAG,"onPageSelected:"+arg0);
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(0, 0, one, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(0, 0, two, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, 0, offset, one);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(0, 0, two, one);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, 0, offset, two);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(0, 0, one, two);
				}
				break;
			}
			currIndex = arg0;
		}

	}

	class SetBackgroundTask extends AsyncTask<String, String, BitmapDrawable> {
		String message = "";
		View view;
		int resourceid;

		public SetBackgroundTask(View v, int id) {
			view = v;
			resourceid = id;
		}

		@Override
		protected BitmapDrawable doInBackground(String... params) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					resourceid);
			// bitmap = Bitmap.createBitmap(100, 20, Config.ARGB_8888);
			BitmapDrawable drawable = new BitmapDrawable(bitmap);

			drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
			drawable.setDither(true);
			return drawable;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(BitmapDrawable drawable) {
			view.setBackgroundDrawable(drawable);
			Log.i(TAG, "setBackgroundDrawable ........");
			// view.setBackground(drawable);
		}
	}

	private void setBackgroundFitXY(View v, int resourceid) {
		new SetBackgroundTask(v, resourceid).execute();
	}

	private RelativeLayout mSmallBg;
	private TextView mText1, mText2;
	private ListView listView;
	private MyListViewAdapter mAdapter;
	boolean isRoundImg = false;
	List<Map<String, Object>> adapterlist = new ArrayList<Map<String, Object>>();

	private void initListView(View v) {
		pullDownView = (PullDownView) v.findViewById(R.id.feeds);
		pullDownView.init();
		pullDownView.setFooterView(R.layout.footer_item);

		pullDownView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				offset = 0;
				// operate();
			}
		});
		listView = pullDownView.getListView();
		pullDownView.showFooterView(false);
		pullDownView.setPullDownEnabled(false);

		listView.setDividerHeight(0);
		mAdapter = new MyListViewAdapter(getActivity(), adapterlist,
				R.layout.zhanlan_list, new String[] { "background", "name",
						"desc" }, new int[] { R.id.background, R.id.name,
						R.id.desc }, isRoundImg);
		mAdapter.setGalleryListLayoutParam(true);
		listView.setAdapter(mAdapter);
		pullDownView
				.setPullDownViewOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(mContext,
								GalleryDetailActivity.class);
						intent.putExtra("address",
								mParamList.get(position).address);
						intent.putExtra("addressname",
								mParamList.get(position).pavilionname);
						intent.putExtra("detailaddress",
								mParamList.get(position).detailaddress);
						intent.putExtra("ExhibitionName",
								mParamList.get(position).ExhibitionName);
						intent.putExtra("objectId",
								mParamList.get(position).objectId);
						intent.putExtra("pavilionname",
								mParamList.get(position).pavilionname);

						startActivity(intent);
					}
				});
	}

	private void InitDataList() {
		adapterlist.clear();
		mParamList = new ArrayList<Params>();
		for (int i = 0; i < results.size(); i++) {
			if (results.get(i).exhibitionAll != null) {
				for (int j = 0; j < results.get(i).exhibitionAll.size(); j++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("background",
							results.get(i).exhibitionAll.get(j).coverUrl.url);
					map.put("name",
							results.get(i).exhibitionAll.get(j).nameBase);
					map.put("desc", results.get(i).address.name);
					Params mParams = new Params();
					mParams.address = results.get(i).address.address;
					mParams.objectId = results.get(i).exhibitionAll.get(j).objectId;
					mParams.ExhibitionName = results.get(i).exhibitionAll
							.get(j).nameBase;
					mParams.detailaddress = results.get(i).address.detailsAddress;
					mParams.pavilionname = results.get(i).address.name;
					mParamList.add(mParams);
					adapterlist.add(map);

				}
			}
		}
		mAdapter.notifyDataSetChanged();
	}

	class MyHomeInfoOnClickListener implements View.OnClickListener {
		int number;

		public MyHomeInfoOnClickListener(int i) {
			number = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, PavilionDetailActivity.class);
			intent.putExtra("nameBase", results.get(number).nameBase);
			intent.putExtra("information", results.get(number).information);
			List<ContentPicArr> PicList = results.get(number).contentPicArr;
			if (PicList != null) {
				String[] contentPic = new String[PicList.size()];
				for (int i = 0; i < PicList.size(); i++) {
					contentPic[i] = PicList.get(i).url;
				}
				intent.putExtra("contentPicArr", contentPic);
			}
			startActivity(intent);
		}
	}

	class MyCollectOnClickListener implements View.OnClickListener {
		int number;
		ImageView mImage;

		public MyCollectOnClickListener(int i, ImageView img) {
			number = i;
			mImage = img;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (mPre.contains(results.get(number).objectId)) {
				mImage.setImageResource(R.drawable.home_collect);
				mPre.edit().remove(results.get(number).objectId).commit();
			} else {
				mImage.setImageResource(R.drawable.home_collect_on);
				mPre.edit().putBoolean(results.get(number).objectId, true)
						.commit();
			}
		}
	}

	class GalleryOnClickListener implements View.OnClickListener {
		int number;

		public GalleryOnClickListener(int i) {
			number = i;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, DisplayExhibitonActivity.class);
			intent.putExtra("objectId", results.get(number).objectId);
			intent.putExtra("pavilionname", results.get(number).address.name);
			intent.putExtra("address", results.get(number).address.address);
			startActivity(intent);
		}
	}

	pavilionDetailEntity mpavilionDetailEntity = null;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			cancelProgressDialog();
			if (MLHttpConnect2.SUCCESS == msg.what) {
				mpavilionDetailEntity = parser.entity;
				if (mpavilionDetailEntity != null) {
					setVerPageData();
					InitDataList();
				}
			} else {
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
			super.handleMessage(msg);
		}

	};

	PavilionDetailParser parser;

	private void getPavilionJsonData() {
		parser = new PavilionDetailParser();
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("limit", 10 + "");
		parmas.put("skip", 0 + "");
		parmas.put("city", "全部");

		MLHttpConnect.getPavilionDetailData(mContext, parmas, parser, mHandler);
	}

	public List<PavilionDetail> results;// 请求回来得数据列表
	LayoutInflater mInflater = null;
	MyVerPagerAdapter verAdapter;

	public void setVerPageData() {
		results = mpavilionDetailEntity.results;
		listViews.clear();
		mInflater = getActivity().getLayoutInflater();

		for (int i = 0; i < results.size(); i++) {
			vertialView1 = mInflater.inflate(R.layout.zhanguan1, null);
			PavilionDetail detail = results.get(i);
			ImageView mMainBg = (ImageView) vertialView1
					.findViewById(R.id.mainbg);
			// mMainBg.setImageResource(R.drawable.zhanguan1);
			imageLoader.displayImage(results.get(i).coverUrl.url, mMainBg,
					options);
			ImageView bg1 = (ImageView) vertialView1.findViewById(R.id.bg1);
			bg1.setOnClickListener(new GalleryOnClickListener(i));
			imageLoader.displayImage(results.get(i).coverUrl.url, bg1, options);
			RelativeLayout shadowbg = (RelativeLayout) vertialView1
					.findViewById(R.id.shadowbg);

			DisplayMetrics metrics = ScreenInfo.getScreenInfo(getActivity());
			int width = metrics.widthPixels;
			int hight = width * 13 / 10;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					width, hight);
			lp.topMargin = ScreenInfo.dip2px(getActivity(), 50);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			shadowbg.setLayoutParams(lp);

			ImageView mHomeInfo = (ImageView) vertialView1
					.findViewById(R.id.home_info);
			mHomeInfo.setTag(detail);
			mHomeInfo.setOnClickListener(new MyHomeInfoOnClickListener(i));

			ImageView home_collect = (ImageView) vertialView1
					.findViewById(R.id.home_collect);

			if (mPre.contains(detail.objectId)) {
				home_collect.setImageResource(R.drawable.home_collect_on);
			} else {
				home_collect.setImageResource(R.drawable.home_collect);
			}
			home_collect.setOnClickListener(new MyCollectOnClickListener(i,
					home_collect));

			mText1 = (TextView) vertialView1.findViewById(R.id.text1);
			mText1.setText(detail.nameBase);
			mText2 = (TextView) vertialView1.findViewById(R.id.text2);
			if (detail.exhibitionAll != null) {
				mText2.setText(detail.exhibitionAll.size() + "个展览进行中");
			} else {
				mText2.setText(0 + "个展览进行中");
			}

			listViews.add(vertialView1);

		}
		verAdapter.notifyDataSetChanged();

	}

	public void showProgressDialog(String message) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		progressDialog = new ProgressDialog(mContext);
		if (progressDialog != null) {
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}

	public void cancelProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	private void initVerticalView() {
		verAdapter = (MyVerPagerAdapter) new VerticalPagerAdapter(listViews);
		verticalViewPager.setAdapter(verAdapter);
		verticalViewPager.setCurrentItem(0);
		verticalViewPager
				.setOnPageChangeListener(new VertialOnPageChangeListener());
	}

	private void InitViewPager(View view) {
		viewPager = (HorizonalViewPage) view.findViewById(R.id.vPager);
		// viewPager.setResideMenu(resideMenu);
		views = new ArrayList<View>();
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		view1 = inflater.inflate(R.layout.lay1, null);
		view2 = inflater.inflate(R.layout.lay2, null);
		// pullDownView = (PullDownView) view2.findViewById(R.id.feeds);
		initListView(view2);

		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		verticalViewPager = (VerticalViewPager) view1
				.findViewById(R.id.verticalViewPager);
		initVerticalView();
		showProgressDialog("正在加载数据，请稍候...");

		// listViews = new ArrayList<View>();
		// LayoutInflater mInflater = this.getActivity().getLayoutInflater();
		//
		// vertialView1 = mInflater.inflate(R.layout.zhanguan1, null);
		// ImageView mMainBg = (ImageView)
		// vertialView1.findViewById(R.id.mainbg);
		// mMainBg.setImageResource(R.drawable.zhanguan1);
		// ImageView mHomeInfo = (ImageView) vertialView1
		// .findViewById(R.id.home_info);
		// mHomeInfo.setOnClickListener(new MyHomeInfoOnClickListener(0));
		// listViews.add(vertialView1);
		//
		// vertialView2 = mInflater.inflate(R.layout.zhanguan1, null);
		// mText1 = (TextView) vertialView2.findViewById(R.id.text1);
		// mText1.setText("中央美术学院美术馆");
		// mText2 = (TextView) vertialView2.findViewById(R.id.text2);
		// mText2.setText("2个展览正在进行");
		// // mSmallBg = (RelativeLayout) vertialView2.findViewById(R.id.bg1);
		// // mSmallBg.setBackgroundResource(R.drawable.zhanguan2);
		// // vertialView2.setBackgroundResource(R.drawable.zhanguan2);
		// // setBackgroundFitXY(vertialView2, R.drawable.zhanguan2);
		// mMainBg = (ImageView) vertialView2.findViewById(R.id.mainbg);
		// mMainBg.setImageResource(R.drawable.zhanguan2);
		//
		// listViews.add(vertialView2);
		//
		// verticalViewPager
		// .setAdapter((MyVerPagerAdapter) new VerticalPagerAdapter(
		// listViews));
		// verticalViewPager.setCurrentItem(0);
		// verticalViewPager
		// .setOnPageChangeListener(new VertialOnPageChangeListener());
		// verticalViewPager.setResideMenu(resideMenu);
	}
}
