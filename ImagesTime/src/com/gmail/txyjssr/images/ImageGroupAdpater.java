package com.gmail.txyjssr.images;

import org.apache.http.client.utils.URIUtils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.txyjssr.images.ImagesCacheManager.ICacheChangeCallBack;

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
		
		return mICManager.getGroupCount();
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
			view = mInflater.inflate(R.layout.mediagroup_item_layout, null);
		}else{
			view = convertView;
		}
		
		ImageView iv = (ImageView) view.findViewById(R.id.iv_media_group);
		TextView tv = (TextView) view.findViewById(R.id.tv_media_group);

		MediaGroup mg = mICManager.getMediaGroupBy(position);
		MediaInfo  mi = mg.mediaInfoList.get(0);
		Uri miUri = Uri.withAppendedPath(Images.Media.EXTERNAL_CONTENT_URI, mi.id);
		//iv 显示 mediagroup内的第一张图片
//		iv.setImageResource(R.drawable.rect_128);
		iv.setImageBitmap(BitmapUtils.getBitmapBy(mi.filePath, 128, 128));
		//tx 显示信息
		tv.setText("image info");
		return view;
	}

	
	
	

}
