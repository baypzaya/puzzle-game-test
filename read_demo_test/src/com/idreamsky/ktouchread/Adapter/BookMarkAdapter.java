package com.idreamsky.ktouchread.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.BookMark;

public class BookMarkAdapter extends BaseAdapter {
	
	private LayoutInflater layoutInflater;
	private List<BookMark> bookMarks;
	private Context mContext;
	public BookMarkAdapter(Context mContext)
	{
		this.mContext = mContext;
		layoutInflater  = LayoutInflater.from(mContext);
		bookMarks = new ArrayList<BookMark>(0);
	}
	
	public void setBookMarks(List<BookMark> bookMarks) {
		this.bookMarks = bookMarks;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bookMarks.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookMarks.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder v ;
		if(convertView==null)
		{
			v = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.bookmark_item, null);
			v.tvSectionName = (TextView) convertView.findViewById(R.id.tvSectionName);
			v.tvPageNumber = (TextView) convertView.findViewById(R.id.tvPageNumber);
			v.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(v);
		}else
		{
			v = (ViewHolder) convertView.getTag();
		}
		BookMark bookMark = bookMarks.get(position);
		String[] chapterName = bookMark.Mark_Text.split(",");
		v.tvSectionName.setText(chapterName[1]);
		v.tvPageNumber.setText(mContext.getString(R.string.the)+bookMark.Pos+mContext.getString(R.string.page));
		v.tvTime.setText(bookMark.Date);
		return convertView;
	}

	public class ViewHolder
	{
		public ImageView ivBookMark;
		public TextView tvSectionName;
		public TextView tvPageNumber;
		public TextView tvTime;
	}

}
