package com.android.parser;

import com.android.entity.GalleryDetailEntity;


public class GalleryDetailParser extends BaseJsonParser {
	public GalleryDetailEntity entity = null;
	@Override
	public int parser(String json) {

		int result = 0;
		try {
			entity = gson.fromJson(json, GalleryDetailEntity.class);
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
