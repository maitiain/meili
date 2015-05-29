package com.android.meili;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

abstract class BaseActivity extends Activity implements View.OnClickListener {
	private ProgressDialog progressDialog;
	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options;

	abstract void onClickListener(int id);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		onClickListener(v.getId());
	}

	/**
	 * 普通的提示框，只有确定按钮，点击关闭对话框，停留当前页
	 * 
	 * @param title
	 *            对话框标题
	 * @param des
	 *            对话框内容
	 */
	public void alertDialog(String title, String des, final boolean needfinish) {
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(des);
		adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (needfinish) {
					finish();
				}
			}
		});
		adb.show();
	}

	/**
	 * 取消加载框
	 */
	public void cancelProgressDialog() {

		if (!this.isFinishing()) {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.cancel();
				progressDialog = null;
			}
		}
	}
	
	public void setTextView(TextView tx,String str){
		if(str!=null&&tx!=null){
			tx.setText(str);
		}
	}

	public void showProgressDialog() {
		showProgressDialog("正在加载数据，请稍候...");
	}

	public void showProgressDialog(String message) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		progressDialog = new ProgressDialog(this);
		if (progressDialog != null) {
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}
}
