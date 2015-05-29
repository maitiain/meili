package com.android.entity;

import java.util.List;

public class GalleryDetailEntity {
	public boolean status;
	public GalleryDetail results;

	public static class GalleryDetail {
		public List<GDUrl> contentPicArr;
		public GDUrl coverUrl;
		public String objectId;
		public Time endTime;
		public String priceAddInfo;
		public String timeAddInfo;
		public String entrancePrice;

		public String nameBase;// : "雕塑1",
		public String information;// : "罗丹雕塑吊炸天",
		public int recommended;// : 0,
		public List<Items> items;
		public Time beginTime;
		public GDUrl largePicUrl;
		public String subName;
		
		public static class Items{
			public Time creationTime;
			public List<GDUrl> contentPicArr;
			public GDUrl coverUrl;
			public String nameBase;// : "雕塑1",
			public String information;// : "罗丹雕塑吊炸天",
			public int recommended;// : 0,
			public String creator;
			public GDUrl largePicUrl;
			public String objectId;
			
		}

		public static class GDUrl {
			public String name;
			public String url;
		}
		public static class Time{
			public String iso;
			public String __type;
		}
	}

}
