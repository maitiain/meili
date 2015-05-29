package com.android.parser;

import com.android.entity.pavilionDetailEntity;

public class PavilionDetailParser extends BaseJsonParser{
	public pavilionDetailEntity entity = null;
	@Override
	public int parser(String json) {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			entity = gson.fromJson(json, pavilionDetailEntity.class);
			result = 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
