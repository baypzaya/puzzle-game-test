package com.yujsh.android.app.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdpater extends BaseAdapter {
	
	private ArrayList<Bitmap> mBitmapList; 
	private Context mContext ;
	
	
	

	@Override
	public int getCount() {
		int count = mBitmapList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		mBitmapList.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView view = null;
		if(convertView==null){
			view = new ImageView(mContext);
		}else {
			view =(ImageView)convertView;
		}
		
		Bitmap bitmap = mBitmapList.get(position);
		view.setImageBitmap(bitmap);
		
		return view;
	}
}
