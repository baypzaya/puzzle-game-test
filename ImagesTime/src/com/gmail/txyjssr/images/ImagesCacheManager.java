package com.gmail.txyjssr.images;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

public class ImagesCacheManager {

	private static ImagesCacheManager manager;

	// MediaInfo 集合
	private List<MediaGroup> medieGroupList = new ArrayList<MediaGroup>();

	// 图片信息变化时回调接口
	private ICacheChangeCallBack callBack;

	private Context context = ImageTimeApplication.sContext;

	public static ImagesCacheManager getInstance() {
		if (manager == null) {
			manager = new ImagesCacheManager();
			manager.refresh();
		}

		return manager;
	}

	private void refresh() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				String selection = null;
				String[] projection = { Images.Media._ID, Images.Media.DATA,
						Images.Media.BUCKET_ID,
						Images.Media.BUCKET_DISPLAY_NAME , Images.Media.TITLE};
				String[] selectionArgs = null;
				String sortOrder = Images.Media.BUCKET_ID;

				Cursor cursor = context.getContentResolver().query(contentUri,
						projection, selection, selectionArgs, sortOrder);

				if (cursor == null || cursor.moveToFirst()) {
					return;
				}

				// 处理所有图片，并进行分组排序
				while (cursor.moveToNext()) {
					MediaGroup mg = new MediaGroup();
					mg.id = cursor.getLong(cursor.getColumnIndex(Images.Media.BUCKET_ID));
					int index = medieGroupList.indexOf(mg);
					if(index >=0 ){
						mg = medieGroupList.get(index);
					}else{
						mg.name = cursor.getString(cursor.getColumnIndex(Images.Media.BUCKET_DISPLAY_NAME));
						medieGroupList.add(mg);
					}
					
					MediaInfo mi = new MediaInfo();
					mi.filePath = cursor.getString(cursor.getColumnIndex(Images.Media.DATA));
					mi.createTime = cursor.getLong(cursor.getColumnIndex(Images.Media.DATE_ADDED));
					mi.modifyTime = cursor.getLong(cursor.getColumnIndex(Images.Media.DATE_MODIFIED));
					mi.name = cursor.getString(cursor.getColumnIndex(Images.Media.TITLE));
					mi.type = MediaInfo.TYPE_IMAGE;
					
					mg.addItem(mi);
								
				}
				cursor.close();
			}
		};

		new Thread(runnable).start();
	}

//	public void getCount() {
//		medieInfoList.size();
//	}

	// 获取图片组个数
	public int getGroupCount() {
		return medieGroupList.size();
	}

	public int getCountBy(int groupIndex) {
		return getMediaInfoListBy(groupIndex).size();
	}

	public List<MediaInfo> getMediaInfoListBy(int groupIndex) {
		return medieGroupList.get(groupIndex).mediaInfoList;
	}

//	public MediaInfo getChildBy(int position) {
//		return medieInfoList.get(position);
//	}

	// 图片信息变化时回调接口
	public interface ICacheChangeCallBack {
		public void callBack();
	}

	public MediaGroup getMediaGroupBy(int position) {
		return medieGroupList.get(position);
	}
}
