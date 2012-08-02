package com.gmail.txyjssr.reader;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class YReaderActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private static final int MENU_ADD_BOOK = 1;

	private ListView mListView;
	private BooksAdapter mAadapter;
	private Button mBtnOpenBook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mBtnOpenBook = (Button) findViewById(R.id.bt_open_local_book);
		mBtnOpenBook.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.lv_books);
		List<Book> books = new ArrayList<Book>();
		mAadapter = new BooksAdapter(this, books);
		mListView.setAdapter(mAadapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Book> books = BookDao.getInstance().getBooks();
		mAadapter.setBooks(books);
	}	

	private void openLocalBook() {
		Intent intent = new Intent();
		intent.setClass(this, AddBookActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		Book book = (Book)mAadapter.getItem(position);
		Intent intent = new Intent();
		intent.setClass(this, ReadBookActivity.class);
		intent.putExtra(ReadBookActivity.EXTRA_READ_BOOK, book);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.bt_open_local_book:
			openLocalBook();
			break;
		default:
			break;
		}
	}

}