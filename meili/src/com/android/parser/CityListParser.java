package com.android.parser;

import com.android.entity.CityListEntity;

public class CityListParser extends BaseJsonParser{
	public CityListEntity entity = null;
	@Override
	public int parser(String json) {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			entity = gson.fromJson(json, CityListEntity.class);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
