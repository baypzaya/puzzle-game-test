package com.idreamsky.ktouchread.Adapter;

import java.util.ArrayList;
import java.util.List;



import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.SearchData;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFileAdapter extends BaseAdapter {
//	private Activity activity;
	private LayoutInflater layoutInflater;
	private List<SearchData> searchDatas = new ArrayList<SearchData>();
	public AddFileAdapter(Activity activity)
	{
//		this.activity = activity;
		layoutInflater = LayoutInflater.from(activity);
	}
	
	public void setSearchDatas(List<SearchData> searchDatas) {
		this.searchDatas = searchDatas;
	}


	@Override
	public int getCount() {
		return searchDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return searchDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SearchData searchData = searchDatas.get(position);
		ViewHolder viewHolder;
		if(convertView==null)
		{
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.file_add_item, null);
			viewHolder.imageLogo = (ImageView) convertView.findViewById(R.id.imageLogo);
			viewHolder.fileName = (TextView) convertView.findViewById(R.id.fileName);
			viewHolder.fileCount = (TextView) convertView.findViewById(R.id.fileCount);
			convertView.setTag(viewHolder);
		}else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(searchData.isDirectoryOrFile()) //是目录
		{
			viewHolder.imageLogo.setBackgroundResource(R.drawable.icon_20);
		}else
		{
			viewHolder.imageLogo.setBackgroundResource(R.drawable.icon_21);
		}
		
		viewHolder.fileName.setText(searchData.getFile().getName());
		viewHolder.fileCount.setText(String.valueOf(searchData.getCount()));
		return convertView;
	}
	public class ViewHolder
	{
		public ImageView imageLogo;
		public TextView fileName;
		public TextView fileCount;
	}
}
