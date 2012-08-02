package com.gmail.txyjssr.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FilesAdapter extends BaseAdapter {

	private Context mContext;
	private List<File> mFiles;
	private LayoutInflater mLayoutInflater;
	
	public FilesAdapter(Context context) {
		mFiles = new ArrayList<File>();
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public FilesAdapter(Context context, List<File> books) {
		mFiles = books;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void setFiles(List<File> files) {
		mFiles = files;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int size = 0;
		if (mFiles != null) {
			size = mFiles.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		File file = null;
		if (mFiles != null && position < mFiles.size()) {
			file = mFiles.get(position);
		}
		return file;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = mLayoutInflater.inflate(R.layout.layout_file_item, null);
		}else{
			view = convertView;
		}
		
		File file = mFiles.get(position);
		
		ImageView iv = (ImageView)view.findViewById(R.id.file_cover);
		iv.setImageResource(R.drawable.ic_launcher);
		
		TextView tv = (TextView)view.findViewById(R.id.file_name);
		tv.setText(file.getName());
		
		return view;
	}

}
