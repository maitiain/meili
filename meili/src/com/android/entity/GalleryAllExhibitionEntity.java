package com.android.entity;

import java.util.List;

import com.android.entity.pavilionDetailEntity.PavilionDetail.ContentPicArr;
import com.android.entity.pavilionDetailEntity.PavilionDetail.CoverUrl;
import com.android.entity.pavilionDetailEntity.PavilionDetail.LargePicArr;
import com.android.entity.pavilionDetailEntity.PavilionDetail.Time;
import com.android.entity.pavilionDetailEntity.PavilionDetail.objectIdArray;

public class GalleryAllExhibitionEntity {
	public boolean status;
	public List<Exhibition> results;
	
	public static class Exhibition{
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
}
