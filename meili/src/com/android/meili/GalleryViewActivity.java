package com.android.meili;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class GalleryViewActivity extends BaseActivity {
	private GridView mGridview;
	private int width, height;
	private String TAG = "GalleryViewActivity";
	private TextView mTitle;
	private ImageView mBackView;

	private GridView.OnItemClickListener mGridItemListener = new GridView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			/*
			 * if (position == 1) { Intent intent = new
			 * Intent(GalleryViewActivity.this, TestActivity.class);
			 * startActivity(intent); } else {
			 */
			Intent intent = new Intent();
			intent.putExtra("index", position);
			setResult(20, intent);
			finish();
			// startActivity(intent);
			// }
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gallery_view);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		Log.i(TAG, "width:" + width + ",height:" + height);

		mGridview = (GridView) findViewById(R.id.mygridview);
		mGridview.setAdapter(new ImageAdapter(this));
		mGridview.setOnItemClickListener(mGridItemListener);

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText(getIntent().getStringExtra("title"));

		mBackView = (ImageView) findViewById(R.id.back_btn);
		mBackView.setImageResource(R.drawable.btn_up);
		mBackView.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return GalleryItemsActivity.mEntity.results.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView img;
			String url = GalleryItemsActivity.mEntity.results.get(position).coverUrl.url;
			if (convertView == null) {
				ImageView image = new ImageView(mContext);
				// image.setBackgroundResource(mps[position]);
				image.setAdjustViewBounds(true);
				image.setLayoutParams(new AbsListView.LayoutParams(width / 3,
						width / 3));
				image.setScaleType(ScaleType.CENTER_CROP);
				imageLoader.displayImage(url, image);
				convertView = image;
		//		convertView.setTag(image);
			}else{
				img = (ImageView)convertView;
				imageLoader.displayImage(url, img);
			}
			return convertView;
		}
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case R.id.back_btn:
			finish();
			break;
		}
	}

}
