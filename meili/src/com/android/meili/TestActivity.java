package com.android.meili;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TestActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_activity);
		WebView myWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView
				.loadUrl("https://photosynth.net/embed.aspx?cid=8e11ec2c-3656-4fbc-b15d-f47cad575504&delayLoad=true&slideShowPlaying=false");
	}

	@Override
	void onClickListener(int id) {
		// TODO Auto-generated method stub

	}

}
