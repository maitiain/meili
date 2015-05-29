package com.android.meili;

import org.kymjs.aframe.ui.fragment.BaseFragment;
import org.kymjs.aframe.ui.widget.ResideMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class AboutUsFragment extends BaseFragment {
	private TextView mTitle;
	private ImageView mMenuImg;
	private ResideMenu resideMenu;
	private String TAG = "AboutUsFragment";

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText("关于我们");

		mBackView = (ImageView) findViewById(R.id.back_btn);
		mBackView.setImageResource(R.drawable.btn_menu);
		mBackView.setOnClickListener(this);
	}*/

	/*@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case R.id.back_btn:
			finish();
			break;
		}
	}*/
	
	public AboutUsFragment(ResideMenu menu) {
		resideMenu = menu;
	}
	
	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.about_us, null);
		return view;
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
		mTitle = (TextView)parentView.findViewById(R.id.title);
		mTitle.setText("关于我们");
		
		mMenuImg = (ImageView) parentView.findViewById(R.id.back_btn);
		mMenuImg.setImageResource(R.drawable.btn_menu);
		mMenuImg.setOnClickListener(mMenuListener);
	}
}
