package com.android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.meili.R;
import com.android.tools.ImageTools;
import com.android.util.ScreenInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class MyListViewAdapter extends BaseAdapter {
	Context mContext;
	LayoutInflater inflater;
	String TAG = "MyListViewAdapter";
	String[] mFrom;
	int[] mTo;
	List<Map<String, Object>> dataList;
	int colorResId, marginResId;
	int setColorViewId, marginLeftLen;
	int layoutId;
	private ArrayList<CallBacks> mCallBacklist;
	private ArrayList<Integer> mlistenerId;
	private ArrayList<CallBacks> mLongClickCallBacklist;
	private ArrayList<Integer> mLongClickListenerId;
	// private CallBacks mCallBacks;
	// private int mListenerId = -1;
	private boolean mIsRoundImg;
	private boolean mGalleryListlayoutParam = false;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
			.build();

	private ArrayList<ChangeImage> mArrayList = new ArrayList<ChangeImage>();

	public class ChangeImage {
		int mPosition, mViewId, mImageId;

		public ChangeImage(int position, int viewid, int imageid) {
			mPosition = position;
			mViewId = viewid;
			mImageId = imageid;
		}
	}

	public MyListViewAdapter(Context context, List<Map<String, Object>> mlist,
			int layout, String[] str, int[] ids, boolean roundImg) {
		this.mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFrom = str;
		mTo = ids;
		dataList = mlist;
		layoutId = layout;
		mIsRoundImg = roundImg;

		mCallBacklist = new ArrayList<CallBacks>();
		mlistenerId = new ArrayList<Integer>();

		mLongClickCallBacklist = new ArrayList<CallBacks>();
		mLongClickListenerId = new ArrayList<Integer>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private void setViewText(TextView v, Boolean flag) {
		if (flag)
			v.setVisibility(android.view.View.VISIBLE);
		else
			v.setVisibility(android.view.View.GONE);
	}

	private void setViewText(TextView v, String text, int position) {
		if (text.equals("") || text.equals("null")) {
			v.setVisibility(android.view.View.GONE);
			return;
		}
		v.setText(text);

		/*
		 * if(v.getId() == R.id.name){
		 * if(((String)dataList.get(position).get("sex"))!=null){ String sex =
		 * (String)dataList.get(position).get("sex"); if(sex.equals("female")){
		 * v.setTextColor(Color.RED); }else{ v.setTextColor(0xff2e5d86); } } }
		 */

		if (v.getId() == setColorViewId)
			v.setTextColor(colorResId);
		if (v.getId() == marginResId) {
			LayoutParams lp = (LayoutParams) v.getLayoutParams();
			lp.setMargins(marginLeftLen, 0, 0, 0);
			v.setLayoutParams(lp);
		}
	}
	
	private void setViewBackGround(final View v,String value){
		imageLoader.loadImage(value, new ImageLoadingListener() {
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1,
					Bitmap arg2) {
				// TODO Auto-generated method stub
				v.setBackgroundDrawable(ImageTools.bitmapToDrawable(arg2));
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1,
					FailReason arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void setViewImage(final ImageView v, String value) {
		v.setBackgroundDrawable(null);
		v.setImageBitmap(null);
		if (value.equals("") || value.equals("null")) {
			v.setBackgroundResource(R.drawable.default_img);
			return;
		}
		try {
			// v.setImageResource(Integer.parseInt(value));
			if (mIsRoundImg) {
				imageLoader.displayImage(value, v, new ImageLoadingListener() {
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						// TODO Auto-generated method stub
						Bitmap tmp = ImageTools.toRoundBitmap(arg2);
						v.setImageBitmap(tmp);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub

					}

				});
			} else {
				imageLoader.displayImage(value, v, options);
			}
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));

		}
	}

	public void setChangeImageView(int position, int resid, int imgid) {
		ChangeImage mImg = new ChangeImage(position, resid, imgid);
		for (int i = 0; i < mArrayList.size(); i++) {
			if (position == mArrayList.get(i).mPosition
					&& resid == mArrayList.get(i).mViewId)
				mArrayList.remove(i);
		}
		mArrayList.add(mImg);
	}

	public void setTextMarginleft(int resId, int marginleftlen) {
		marginResId = resId;
		marginLeftLen = marginleftlen;

	}

	public void setTextColor(int resId, int colorId) {
		setColorViewId = resId;
		colorResId = colorId;
	}

	private void setViewImage(ImageView v, int value, int position) {
		boolean needchange = false;
		if (value == 0) {
			v.setVisibility(android.view.View.GONE);
			return;
		} else {
			v.setVisibility(android.view.View.VISIBLE);
		}
		ChangeImage mImg;
		for (int i = 0; i < mArrayList.size(); i++) {
			mImg = mArrayList.get(i);
			if ((position == mImg.mPosition) && (v.getId() == mImg.mViewId)) {
				v.setImageResource(mImg.mImageId);
				needchange = true;
				break;
			}
		}
		if (!needchange) {
			v.setBackgroundResource(value);
		}
	}

	private void bindView(int position, View view) {
		if (dataList.size() == 0)
			return;
		final Map dataSet = dataList.get(position);
		if (dataSet == null) {
			return;
		}

		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			v.setVisibility(android.view.View.VISIBLE);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				if (v instanceof Checkable) {
					if (data instanceof Boolean) {
						((Checkable) v).setChecked((Boolean) data);
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text, position);
					} else {
						throw new IllegalStateException(v.getClass().getName()
								+ " should be bound to a Boolean, not a "
								+ (data == null ? "<unknown type>"
										: data.getClass()));
					}
				} else if (v instanceof TextView) {
					// Note: keep the instanceof TextView check at the bottom of
					// these
					// ifs since a lot of views are TextViews (e.g. CheckBoxes).
					if (data instanceof Boolean) {
						setViewText((TextView) v, (Boolean) data);
					} else
						setViewText((TextView) v, text, position);
				} else if (v instanceof ImageView) {
					if (data instanceof Integer) {
						setViewImage((ImageView) v, (Integer) data, position);
					} else {
						setViewImage((ImageView) v, text);
					}
				} else if (v instanceof RelativeLayout) {
					if (data instanceof Integer) {
						v.setBackgroundResource((Integer) data);
					} else if (data instanceof String) {
						setViewBackGround(v,(String)data);
					} else {
						setViewVisable((ViewGroup) v, (Boolean) data);
					}
				} else if (v instanceof LinearLayout) {
					if (data instanceof Integer) {
						v.setBackgroundResource((Integer) data);
					} else {
						setViewVisable((ViewGroup) v, (Boolean) data);
					}
				} else {
					throw new IllegalStateException(v.getClass().getName()
							+ " is not a "
							+ " view that can be bounds by this SimpleAdapter");
				}

			}
		}
	}

	private void setViewVisable(ViewGroup v, boolean flag) {
		if (flag) {
			v.setVisibility(android.view.View.VISIBLE);
		} else {
			v.setVisibility(android.view.View.GONE);
		}
	}
	
	public void setGalleryListLayoutParam(boolean flag){
		mGalleryListlayoutParam = flag;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		if (convertView == null) {
			v = inflater.inflate(layoutId, parent, false);
			if (mGalleryListlayoutParam) {
				Log.i(TAG, "instanceof MainActivity ......");
				RelativeLayout bg = (RelativeLayout) v
						.findViewById(R.id.background);

				DisplayMetrics metrics = ScreenInfo
						.getScreenInfo((Activity) mContext);
				int width = metrics.widthPixels;
				int hight = metrics.heightPixels;
				AbsListView.LayoutParams mLayoutParams = new AbsListView.LayoutParams(
						width, hight / 3);
				bg.setLayoutParams(mLayoutParams);
			}
		} else {
			v = convertView;
		}
		bindView(position, v);
		for (int i = 0; i < mCallBacklist.size(); i++) {
			ItemListener mlistener = new ItemListener(position,
					mCallBacklist.get(i));
			v.findViewById(mlistenerId.get(i)).setOnClickListener(mlistener);
		}
		// if (mListenerId != -1) {
		// v.findViewById(mListenerId).setOnClickListener(mlistener);
		// }
		for (int j = 0; j < mLongClickCallBacklist.size(); j++) {
			LongClickItemListener mLongClicklistener = new LongClickItemListener(
					position, mLongClickCallBacklist.get(j));
			v.findViewById(mLongClickListenerId.get(j)).setOnLongClickListener(
					mLongClicklistener);
		}
		return v;
	}

	public void setOnClickViewLisener(int viewid, CallBacks callbacks) {
		mCallBacklist.add(callbacks);
		mlistenerId.add(viewid);

	}

	public void setOnLongClickViewLisener(int viewid, CallBacks callbacks) {
		mLongClickCallBacklist.add(callbacks);
		mLongClickListenerId.add(viewid);

	}

	public interface CallBacks {
		void onViewClicked(int positon, int viewId);
	}

	class LongClickItemListener implements View.OnLongClickListener {
		private int m_position;
		private CallBacks mCallBacks;

		LongClickItemListener(int pos, CallBacks callback) {
			m_position = pos;
			mCallBacks = callback;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			Log.i(TAG, "m_position:" + m_position);
			mCallBacks.onViewClicked(m_position, v.getId());
			return true;
		}
	}

	class ItemListener implements View.OnClickListener {
		private int m_position;
		private CallBacks mCallBacks;

		ItemListener(int pos, CallBacks callback) {
			m_position = pos;
			mCallBacks = callback;
		}

		@Override
		public void onClick(View viewId) {
			Log.i(TAG, "m_position:" + m_position);
			mCallBacks.onViewClicked(m_position, viewId.getId());
		}
	}
}
