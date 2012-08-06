package com.gmail.txyjssr.reader;

import java.util.ArrayList;
import java.util.List;

import com.gmail.txyjssr.reader.dao.BookDao;
import com.gmail.txyjssr.reader.data.Book;
import com.gmail.txyjssr.reader.logic.BookLogic;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class YReaderActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	private final int MENU_REMOVE = 1;
	private final int MENU_DELETE = 2;

	private ListView mListView;
	private BooksAdapter mBooksAadapter;
	private Button mBtnOpenBook;
	private BookLogic mBookLogic = new BookLogic();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mBtnOpenBook = (Button) findViewById(R.id.bt_open_local_book);
		mBtnOpenBook.setOnClickListener(this);

		mListView = (ListView) findViewById(R.id.lv_books);
		List<Book> books = new ArrayList<Book>();
		mBooksAadapter = new BooksAdapter(this, books);
		mListView.setAdapter(mBooksAadapter);
		mListView.setOnItemClickListener(this);
		registerForContextMenu(mListView);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Book book = (Book) mBooksAadapter.getItem(menuInfo.position);
		int itemId = item.getItemId();
		switch (itemId) {
		case MENU_REMOVE:
			revomeBookFromList(book);
			break;
		case MENU_DELETE:
			deleteBook(book);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void deleteBook(Book book) {
		mBookLogic.deleteBook(book);
		List<Book> books = BookDao.getInstance().getBooks();
		mBooksAadapter.setBooks(books);
	}

	private void revomeBookFromList(Book book) {
		mBookLogic.revomeBookFromList(book);
		List<Book> books = BookDao.getInstance().getBooks();
		mBooksAadapter.setBooks(books);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_REMOVE, 0, R.string.remove_list);
		menu.add(0, MENU_DELETE, 0, R.string.delete_book);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Book> books = BookDao.getInstance().getBooks();
		mBooksAadapter.setBooks(books);
	}

	private void openLocalBook() {
		Intent intent = new Intent();
		intent.setClass(this, AddBookActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		Book book = (Book) mBooksAadapter.getItem(position);
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