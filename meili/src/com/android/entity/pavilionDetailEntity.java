package com.android.entity;

import java.io.Serializable;
import java.util.List;

public class pavilionDetailEntity {
	public boolean status;
	public List<PavilionDetail> results;
	public static class PavilionDetail{
		public List<ContentPicArr> contentPicArr;
		public CoverUrl coverUrl;
		public Address address;
		public String objectId;
		public int endTime;
		public int entrancePrice;
		public String nameBase;
		public String information;
		public int recommended;
		public List<Exhibition> exhibitionAll;
		public int beginTime;
		public LargePicArr largePicUrl;
		
		public static class Address{
			public String citycode;
			public String address;
			public String detailsAddress;
			public String name;
			public String cityname;
			public String adname;
			public String pcode;
			public String pname;
			public String adcode;
		}
		
		public static class ContentPicArr{
			public String name;
			public String url;
		}
		public static class CoverUrl{
			public String name;
			public String url;
		}
		
		public static class Exhibition{
			/**
			 * 
			 */
			public List<ContentPicArr> contentPicArr;
			public CoverUrl coverUrl;
			public String objectId;
			public Time endTime;
			public String nameBase;
			public String information;
			public int recommended;
			public List<objectIdArray> items;
			public Time beginTime;
			public LargePicArr largePicUrl;
		}
		
		public static class objectIdArray{
			public String objectId;
		}
		
		public static class Time{
			String iso;
			String __type;
		}
		public static class LargePicArr{
			String name;
			String url;
		}
	}
}
