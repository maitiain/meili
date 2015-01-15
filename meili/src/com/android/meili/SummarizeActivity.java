package com.android.meili;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SummarizeActivity extends Activity{

	private ImageView mBackBtn;
	private TextView mTitle;
	private View.OnClickListener mBackListener = new View.OnClickListener(){
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
		setContentView(R.layout.summarize);
		
		mBackBtn = (ImageView)findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(mBackListener);
		
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText("M home:随遇而安");
		
		
	}

}
