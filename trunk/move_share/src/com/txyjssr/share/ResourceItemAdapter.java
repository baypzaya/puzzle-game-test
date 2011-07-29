package com.txyjssr.share;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ResourceItemAdapter extends BaseAdapter implements ListAdapter {

	private Context mContext;
	private CacheService mCacheService;

	public ResourceItemAdapter(Context context) {
		mContext = context;
		mCacheService = new CacheService();
		mCacheService.initResource(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.i("yujsh log", "position:" + position);
		if (null == view) {
			view = View.inflate(mContext, R.layout.resource_item_layout, null);
		}

		ImageView iv = (ImageView) view.findViewById(R.id.show_image);
		Bitmap bitmap = mCacheService.getBitamp(position);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
		} else {
			iv.setImageResource(R.drawable.icon);
		}
		TextView tv = (TextView) view.findViewById(R.id.show_info);
		tv.setText("" + position);
		mCacheService.updateResourceBitmap(position);
		return view;

	}

	private Bitmap getBitmapFromUri(Context context, Uri uri) {
		Bitmap bm = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(context.getContentResolver()
					.openInputStream(uri), 16384);
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return bm;
	}

	@Override
	public int getCount() {
		return mCacheService.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		if (mCacheService.size() <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
