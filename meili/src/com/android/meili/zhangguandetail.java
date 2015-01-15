package com.android.meili;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class zhangguandetail extends Activity {
	private ImageView mClose,mZhanguan;
	private TextView mTitle, mContent;

	View.OnClickListener mCloseListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.zhanguan_detail);
		
		mClose = (ImageView)findViewById(R.id.close);
		mClose.setOnClickListener(mCloseListener);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("中央美术学院美术馆");
		mContent = (TextView)findViewById(R.id.content);
		mContent.setText(getString(R.string.zhanguan_content));
		
		mZhanguan = (ImageView)findViewById(R.id.zhanguan_img);
		mZhanguan.setImageResource(R.drawable.zhanguan3);
	}
}
