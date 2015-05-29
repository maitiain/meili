package com.android.net;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.parser.BaseJsonParser;
import com.android.parser.GalleryDetailParser;

public class MLHttpConnect {

	/***
	 * 如果不需要读取sd卡缓存 可以添加一个参数 parmas.put(MLHttpConnect2.USE_CASH, "false");
	 * 
	 * @param context
	 * @param parmas
	 * @param jsonParser
	 * @param handler
	 */

	public static void getGalleryItemsData(Context context,
			Map<String, String> parmas, BaseJsonParser jsonParser,
			Handler handler) {
		String url = MLHttpConstant.URL_START + "/exhibition/:gid/item";
		if (parmas.containsKey(":gid")) {
			url = url.replace(":gid", parmas.get(":gid"));
			parmas.remove(":gid");

			MLHttpConnect2.getData(context, url, parmas, jsonParser, handler);
		} else {
			Toast.makeText(context, "参数传错了", Toast.LENGTH_SHORT).show();
		}
	}

	public static void getAllExhibitonData(Context context,
			Map<String, String> parmas, BaseJsonParser jsonParser,
			Handler handler) {
		String url = MLHttpConstant.URL_START + "/gallery/" + parmas.get("gid")
				+ "/exhibition";
		parmas.remove("gid");
		MLHttpConnect2.getData(context, url, parmas, jsonParser, handler);
	}

	public static void getGalleryDetailData(Context context,
			Map<String, String> parmas, BaseJsonParser jsonParser,
			Handler handler) {
		String url = MLHttpConstant.URL_START + "/exhibition/"
				+ parmas.get("eid");
		MLHttpConnect2.getData(context, url, parmas, jsonParser, handler);
	}

	public static void getPavilionDetailData(Context context,
			Map<String, String> parmas, BaseJsonParser jsonParser,
			Handler handler) {
		String url = MLHttpConstant.URL_START + "/gallery/recommend";
		MLHttpConnect2.getData(context, url, parmas, jsonParser, handler);
	}

	public static void getCityListData(Context context,
			Map<String, String> parmas, BaseJsonParser jsonParser,
			Handler handler) {
		String url = MLHttpConstant.URL_START + "/availableCity";
		MLHttpConnect2.getData(context, url, parmas, jsonParser, handler);
	}
}
