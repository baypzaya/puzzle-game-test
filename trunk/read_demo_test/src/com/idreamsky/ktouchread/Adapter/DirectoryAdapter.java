package com.idreamsky.ktouchread.Adapter;



import java.util.ArrayList;
import java.util.List;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Chapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 目录
 * @author a
 *
 */
public class DirectoryAdapter extends BaseAdapter {
	private LayoutInflater layoutInflater;
	private List<Chapter> chapters;
	public DirectoryAdapter(Context context)
	{
		layoutInflater = LayoutInflater.from(context);
		chapters  = new ArrayList<Chapter>(0);
	}
	
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	@Override
	public int getCount() {
		return chapters.size();
	}

	@Override
	public Object getItem(int position) {
		return chapters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder v ;
		if(convertView==null)
		{
			v = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.directory_bookmark_item, null);
			v.tvSection = (TextView) convertView.findViewById(R.id.tvSection);
//			v.tvPrice = (TextView) convertView.findViewById(R.id.tvprice);
			convertView.setTag(v);
		}else
		{
			v = (ViewHolder) convertView.getTag();
		}
		Chapter chapter = chapters.get(position);
		v.tvSection.setText(chapter.ChapterName);
//		v.tvPrice.setVisibility(View.VISIBLE);
//		if(chapter.bFree == 1)
//		{
//			v.tvPrice.setText(R.string.bookfree);
//		}
//		else {
//			v.tvPrice.setText(chapter.Price);
//		}
		return convertView;
	}

	public class ViewHolder
	{
		public TextView tvSection;
		public TextView tvPrice;
	}
}
