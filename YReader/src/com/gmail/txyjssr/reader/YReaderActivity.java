package com.gmail.txyjssr.reader;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class YReaderActivity extends BaseActivity implements OnItemClickListener{

	private static final int MENU_ADD_BOOK = 1;

	private ListView mListView;
	
	private BooksAdapter mAadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
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



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD_BOOK, 0, R.string.add_book).setIcon(android.R.drawable.ic_menu_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case MENU_ADD_BOOK:
			addBook();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void addBook() {
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
		
	}

}