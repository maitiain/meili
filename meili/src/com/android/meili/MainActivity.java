package com.android.meili;

import org.kymjs.aframe.ui.KJActivityManager;
import org.kymjs.aframe.ui.activity.SlidTemplet;
import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.ui.widget.ResideMenuItem;

import android.app.ActionBar;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.view.BitmapDisplay;

public class MainActivity extends SlidTemplet {
	private ResideMenuItem item1;
	private ResideMenuItem item2;
	private ResideMenuItem item3;
	private ResideMenuItem item4;
	private ResideMenuItem item5;
	private static int MENU_TEXT_SIZE  = 20;
	private String TAG = "mainActvity";

	public ActionBar actionBar;

	private BaseFragment fragContent;

	public MainActivity() {
		setHiddenActionBar(true);
		setAllowFullScreen(true);
		
	}

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	}*/

	@Override
	public void changeSlidMenu() {
		// TODO Auto-generated method stub
		super.changeSlidMenu();
		Log.i(TAG,"changeSlidMenu");
	}
	
	/**
	 * 必须调用super()，否则界面触摸将被屏蔽
	 */
	@Override
	public void changeFragment(BaseFragment targetFragment) {
		super.changeFragment(targetFragment);
		changeFragment(R.id.content, targetFragment);
	}

	@Override
	public void onSlidMenuClick(View v) {
		// TODO Auto-generated method stub
		if (v == item1) {
		//	actionBar.setTitle("网络图片加载");
			// changeFragment(new BitmapDisplay());
		} else if (v == item2) {
		//	actionBar.setTitle("图片模糊效果");
			// changeFragment(new BitmapMistyExample());
		} else if (v == item3) {
		//	actionBar.setTitle("图片缩放效果");
			// changeFragment(new ScaleImageExample());
		} else if (v == item4) {
		//	actionBar.setTitle("多图选择效果");
			// changeFragment(new ChoiceImageExample());
		} else if (v == item5) {
			KJActivityManager.create().AppExit(MainActivity.this);
		}
		resideMenu.closeMenu();
	}

	@Override
	protected void initSlidMenu() {
		// TODO Auto-generated method stub
		item1 = new ResideMenuItem(this, R.drawable.menu_home, "精选");
		item2 = new ResideMenuItem(this, R.drawable.menu_collect, "收藏");
		item3 = new ResideMenuItem(this, R.drawable.menu_city, "城市");
		item4 = new ResideMenuItem(this, R.drawable.menu_about, "关于我们");
		item5 = new ResideMenuItem(this, R.drawable.menu_like, "喜欢梅丽?");
		
		item1.setTextColor(0xff3b96f5);
		item1.setTextSize(MENU_TEXT_SIZE);
		item2.setTextColor(0xff898989);
		item2.setTextSize(MENU_TEXT_SIZE);
		item3.setTextColor(0xff898989);
		item3.setTextSize(MENU_TEXT_SIZE);
		item4.setTextColor(0xff898989);
		item4.setTextSize(MENU_TEXT_SIZE);
		item5.setTextColor(0xff898989);
		item5.setTextSize(MENU_TEXT_SIZE);
		
		item1.setOnClickListener(this);
		item2.setOnClickListener(this);
		item3.setOnClickListener(this);
		item4.setOnClickListener(this);
		item5.setOnClickListener(this);
		resideMenu.addMenuItem(item1);
		resideMenu.addMenuItem(item2);
		resideMenu.addMenuItem(item3);
		resideMenu.addMenuItem(item4);
		resideMenu.addMenuItem(item5);
		resideMenu.setBackgroundColor(Color.WHITE);
	}

	@Override
	protected void initWidget() {
		super.initWidget();
		//actionBar.setTitle("侧滑效果演示");
		fragContent = new BitmapDisplay(resideMenu);

		changeFragment(fragContent);
	}

	@Override
	protected int setRootViewID() {
		// TODO Auto-generated method stub
		//actionBar = getActionBar();
		return R.layout.aty_slid_example;
	}
}
