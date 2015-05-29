package com.android.meili;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MyListViewAdapter;
import com.android.entity.GalleryAllExhibitionEntity;
import com.android.net.MLHttpConnect;
import com.android.net.MLHttpConnect2;
import com.android.parser.GalleryAllExhibitonParser;
import com.umeng.analytics.MobclickAgent;
import com.view.PullDownView;
import com.view.PullDownView.OnItemClickListener;
import com.view.PullDownView.OnRefreshListener;

public class DisplayExhibitonActivity extends BaseActivity {
	private String objectId;
	private Context mContext;
	private GalleryAllExhibitionEntity entity;
	private PullDownView pullDownView;
	private int offset = 0;
	private ListView listView;
	private String pavilionname = "", address = "";
	private MyListViewAdapter mAdapter;
	private TextView mTitle;
	private List<Map<String, Object>> adapterlist = new ArrayList<Map<String, Object>>();
	private ImageView mBackBtn;

	private void initListView() {
		pullDownView = (PullDownView) findViewById(R.id.feeds);
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
		mAdapter = new MyListViewAdapter(mContext, adapterlist,
				R.layout.zhanlan_list, new String[] { "background", "name",
						"desc" }, new int[] { R.id.background, R.id.name,
						R.id.desc }, false);
		mAdapter.setGalleryListLayoutParam(true);
		listView.setAdapter(mAdapter);
		pullDownView
				.setPullDownViewOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(mContext,
								GalleryDetailActivity.class);
						intent.putExtra("address", address);
						intent.putExtra("objectId",
								entity.results.get(position).objectId);
						intent.putExtra("pavilionname", pavilionname);

						startActivity(intent);
						startActivity(intent);
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_exhibition);
		mContext = this;
		objectId = getIntent().getStringExtra("objectId");
		pavilionname = getIntent().getStringExtra("pavilionname");
		address = getIntent().getStringExtra("address");

		mTitle = (TextView) findViewById(R.id.title);
		mTitle.setText(pavilionname);

		mBackBtn = (ImageView) findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(this);

		initListView();
		getDataList();
	}

	private GalleryAllExhibitonParser parser;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			cancelProgressDialog();
			if (MLHttpConnect2.SUCCESS == msg.what) {
				entity = parser.entity;
				if (entity != null) {
					if (entity.results.size() > 0) {
						InitDataList();
					} else {
						alertDialog("展馆展览", "该展馆没有展览！", true);
					}
				}
			} else {
				Toast.makeText(mContext, "网络连接失败，请稍后重试", Toast.LENGTH_SHORT)
						.show();
			}
			super.handleMessage(msg);
		}

	};

	private void InitDataList() {
		adapterlist.clear();
		for (int i = 0; i < entity.results.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("background", entity.results.get(i).coverUrl.url);
			map.put("name", entity.results.get(i).nameBase);
			map.put("desc", pavilionname);
			adapterlist.add(map);
		}
		mAdapter.notifyDataSetChanged();
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

	private void getDataList() {
		showProgressDialog();
		parser = new GalleryAllExhibitonParser();
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("gid", objectId);
		parmas.put("limit", 10 + "");
		parmas.put("skip", 0 + "");

		MLHttpConnect.getAllExhibitonData(this, parmas, parser, mHandler);
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
