package com.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class HorizonalViewPage extends ViewPager {
	// private ResideMenu resideMenu;
	private String TAG = "HorizonalViewPage";
	private long timeEclipse = -1, currentTime;
	private boolean canScroll = true;
	private GestureDetector mGestureDetector;
	private int verticalMinDistance = 20;
	private int minVelocity = 0;
	private FirstPageFlingToRightListener mlistener = null;

	public HorizonalViewPage(Context context) {
		super(context);
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(context,
					new MyOnGestureListener());
		}
		// TODO Auto-generated constructor stub
	}

	public HorizonalViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(context,
					new MyOnGestureListener());
		}
	}

	/*
	 * public void setResideMenu(ResideMenu menu) { resideMenu = menu; }
	 */
	public interface FirstPageFlingToRightListener {
		void FlingToRight();
	}

	public void setFirstPageFlingToLeftListener(
			FirstPageFlingToRightListener listener) {
		mlistener = listener;
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
			if (e2.getX() - e1.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity
					&& Math.abs(e2.getX() - e1.getX()) > Math.abs(e1.getY()
							- e2.getY())) {
				Log.i(TAG, "fling to right ..........");
				mlistener.FlingToRight();
			} else if (e1.getX() - e2.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity
					&& Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
							- e2.getY())) {

			}

			return false;
		}
	}

	private float downX,downY,UpX,UpY;
	
	

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (this.getCurrentItem() == 0 && mlistener != null) {
			mGestureDetector.onTouchEvent(arg0);
		}
		return super.onTouchEvent(arg0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (this.getCurrentItem() == 0 && mlistener != null) {
			mGestureDetector.onTouchEvent(arg0);
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		/*
		 * if (resideMenu.isOpened()) { canScroll = false; timeEclipse =
		 * SystemClock.elapsedRealtime(); } else { currentTime =
		 * SystemClock.elapsedRealtime(); if (currentTime - timeEclipse > 1 *
		 * 1000) { canScroll = true; super.scrollTo(x, y); } }
		 */
	}
}
