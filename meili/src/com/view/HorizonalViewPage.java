package com.view;

import org.kymjs.aframe.ui.widget.ResideMenu;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizonalViewPage extends ViewPager {
	private ResideMenu resideMenu;
	private String TAG = "HorizonalViewPage";
	private long timeEclipse = -1, currentTime;
	private boolean canScroll = true;

	public HorizonalViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HorizonalViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setResideMenu(ResideMenu menu) {
		resideMenu = menu;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (canScroll)
			return super.onTouchEvent(arg0);
		else
			return true;
	}

	@Override
	public void scrollTo(int x, int y) {
		if (resideMenu.isOpened()) {
			canScroll = false;
			timeEclipse = SystemClock.elapsedRealtime();
		} else {
			currentTime = SystemClock.elapsedRealtime();
			if (currentTime - timeEclipse > 1 * 1000) {
				canScroll = true;
				super.scrollTo(x, y);
			}
		}
	}

}
