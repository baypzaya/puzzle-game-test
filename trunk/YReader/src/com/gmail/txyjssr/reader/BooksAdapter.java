package com.gmail.txyjssr.reader;

import java.util.List;

import com.gmail.txyjssr.reader.data.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BooksAdapter extends BaseAdapter {

	private Context mContext;
	private List<Book> mBooks;
	private LayoutInflater mLayoutInflater;

	public BooksAdapter(Context context, List<Book> books) {
		mBooks = books;
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public void setBooks(List<Book> books) {
		mBooks = books;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		int size = 0;
		if (mBooks != null) {
			size = mBooks.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		Book book = null;
		if (mBooks != null && position < mBooks.size()) {
			book = mBooks.get(position);
		}
		return book;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = mLayoutInflater.inflate(R.layout.layout_book_item, null);
		}else{
			view = convertView;
		}
		
		Book book = mBooks.get(position);
		
		ImageView iv = (ImageView)view.findViewById(R.id.book_cover);
		iv.setImageResource(R.drawable.ic_launcher);
		
		TextView tv = (TextView)view.findViewById(R.id.book_name);
		tv.setText(book.name);
		
		return view;
	}

}
