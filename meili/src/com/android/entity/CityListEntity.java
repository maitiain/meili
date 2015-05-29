package com.android.entity;

import java.util.List;

public class CityListEntity {
	public boolean status;
	public CityDetail results;
	
	public static class CityDetail{
		public String updatedAt;
		public String ukey;
		public String createdAt;
		public List<String> citys;
		public String objectId;
	}
}
