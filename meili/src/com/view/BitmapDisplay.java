package com.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.ui.widget.ResideMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.android.adapter.MyListViewAdapter;
import com.android.meili.R;
import com.android.meili.SummarizeActivity;
import com.android.meili.VerticalPagerAdapter;
import com.android.meili.zhangguandetail;
import com.view.PullDownView.OnItemClickListener;
import com.view.PullDownView.OnRefreshListener;

public class BitmapDisplay extends BaseFragment {
	private HorizonalViewPage viewPager;// ҳ������
	// private ImageView imageView;// ����ͼƬ
	private TextView textView1, textView2;
	private List<View> views;// Tabҳ���б�
	private int offset = 0;// ����ͼƬƫ����
	public static int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private View view1, view2, view3;// ����ҳ��
	private View vertialView1, vertialView2;
	private TextView mLine1, mLine2;
	private VerticalViewPager verticalViewPager;
	private List<View> listViews = null;
	private int verticalbmpH;
	private ImageView mMenuBtn;
	private ResideMenu resideMenu;
	private String TAG = "BitmapDisplay";
	private PullDownView pullDownView;
	private Context mContext;

	public BitmapDisplay(ResideMenu menu) {
		resideMenu = menu;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = this.getActivity();
	}

	private View.OnClickListener mMenuListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (!resideMenu.isOpened())
				resideMenu.openMenu();
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

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

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
	 * 2 * ��ʼ������ 3
	 */

	private void InitImageView() {
		// imageView = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		this.getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 2 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		// imageView.setImageMatrix(matrix);// ���ö�����ʼλ��
	}

	public class VertialOnPageChangeListener implements
			VerticalViewPager.OnPageChangeListener {

		int one = offset * 2 + verticalbmpH;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

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
		listView.setAdapter(mAdapter);
		pullDownView
				.setPullDownViewOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(mContext,SummarizeActivity.class);
						startActivity(intent);
					}
				});
		InitDataList();
	}

	private void InitDataList() {
		adapterlist.clear();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("background", R.drawable.zhanguan1);
		map.put("name", "作为启蒙的艺术");
		map.put("desc", "中国国家博物馆.0.6km");
		adapterlist.add(map);

		map = new HashMap<String, Object>();
		map.put("background", R.drawable.zhanguan2);
		map.put("name", "作为启蒙的艺术");
		map.put("desc", "中国国家博物馆.0.6km");
		adapterlist.add(map);

		map = new HashMap<String, Object>();
		map.put("background", R.drawable.zhanguan3);
		map.put("name", "一个中的一个个：无形工作室个展");
		map.put("desc", "中国国家博物馆.0.6km");
		adapterlist.add(map);

		mAdapter.notifyDataSetChanged();
	}

	class MyHomeInfoOnClickListener implements View.OnClickListener {
		int i;

		public MyHomeInfoOnClickListener(int num) {
			i = num;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext,zhangguandetail.class);
			startActivity(intent);
		}
	}

	private void InitViewPager(View view) {
		viewPager = (HorizonalViewPage) view.findViewById(R.id.vPager);
		viewPager.setResideMenu(resideMenu);
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
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = this.getActivity().getLayoutInflater();

		vertialView1 = mInflater.inflate(R.layout.zhanguan1, null);
		ImageView mMainBg = (ImageView) vertialView1.findViewById(R.id.mainbg);
		mMainBg.setImageResource(R.drawable.zhanguan1);
		ImageView mHomeInfo = (ImageView) vertialView1
				.findViewById(R.id.home_info);
		mHomeInfo.setOnClickListener(new MyHomeInfoOnClickListener(0));
		listViews.add(vertialView1);

		vertialView2 = mInflater.inflate(R.layout.zhanguan1, null);
		mText1 = (TextView) vertialView2.findViewById(R.id.text1);
		mText1.setText("中央美术学院美术馆");
		mText2 = (TextView) vertialView2.findViewById(R.id.text2);
		mText2.setText("2个展览正在进行");
		mSmallBg = (RelativeLayout) vertialView2.findViewById(R.id.bg1);
		mSmallBg.setBackgroundResource(R.drawable.zhanguan2);
		// vertialView2.setBackgroundResource(R.drawable.zhanguan2);
		// setBackgroundFitXY(vertialView2, R.drawable.zhanguan2);

		mMainBg = (ImageView) vertialView2.findViewById(R.id.mainbg);
		mMainBg.setImageResource(R.drawable.zhanguan2);
		listViews.add(vertialView2);
		verticalViewPager
				.setAdapter((MyVerPagerAdapter) new VerticalPagerAdapter(
						listViews));
		verticalViewPager.setCurrentItem(0);
		verticalViewPager
				.setOnPageChangeListener(new VertialOnPageChangeListener());
		verticalViewPager.setResideMenu(resideMenu);
	}
}
