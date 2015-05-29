package com.android.parser;

import com.android.entity.GalleryAllItemEntity;


public class GalleryAllItemsParser extends BaseJsonParser {

	public GalleryAllItemEntity entity = null;

	@Override
	public int parser(String json) {

		int result = 0;
		try {
			entity = gson.fromJson(json, GalleryAllItemEntity.class);
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
