package com.android.entity;

import java.util.List;

public class GalleryAllItemEntity {
	public boolean status;
	public List<GalleryDetail> results;

	public static class GalleryDetail {
		public GDCreateTime creationTime;
		public List<GDUrl> contentPicArr;

		public GDUrl coverUrl;

		public String creator;// : "罗丹",
		public String objectId;// : "54b478dfe4b06e8b5a87c645",
		public String nameBase;// : "雕塑1",
		public String information;// : "罗丹雕塑吊炸天",
		public int recommended;// : 0,

		public static class GDCreateTime {
			public String iso;
			public String __type;
		}

		public static class GDUrl {
			public String name;
			public String url;
		}
	}

}
