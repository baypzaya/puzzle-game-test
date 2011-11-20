package com.gmail.txyjssr.images;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageGroupAdpater extends BaseAdapter {

	private Context mContext;
	LayoutInflater mInflater;

	// 图片缓存管理器
	private ImagesCacheManager mICManager = ImagesCacheManager.getInstance();

	public ImageGroupAdpater(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		mICManager.getGroupCount();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		MediaGroup mg = mICManager.getMediaGroupBy(position);
		return mg;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null ;
		if(convertView == null){
			mInflater.inflate(R.layout.mediagroup_item_layout, null);
		}else{
			view = convertView;
		}
		
		ImageView iv = (ImageView) view.findViewById(R.id.iv_media_group);
		TextView tv = (TextView) view.findViewById(R.id.tv_media_group);
		
		//iv 显示 mediagroup内的第一张图片
		//tx 显示信息
		return null;
	}

}
