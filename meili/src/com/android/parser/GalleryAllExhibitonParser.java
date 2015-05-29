package com.android.parser;

import com.android.entity.GalleryAllExhibitionEntity;

public class GalleryAllExhibitonParser extends BaseJsonParser {
	public GalleryAllExhibitionEntity entity = null;
	@Override
	public int parser(String json) {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			entity = gson.fromJson(json, GalleryAllExhibitionEntity.class);
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
