package com.gmail.txyjssr.reader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gmail.txyjssr.reader.data.Book;
import com.gmail.txyjssr.reader.logic.BookLogic;

public class ReadBookActivity extends BaseActivity {

	public static final String EXTRA_READ_BOOK = "EXTRA_READ_BOOK";

	private TextView mTVBookContent;
	private BookLogic mBookLogic = new BookLogic();
	private Book mBook;
	private ScrollView mScrollView;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(EXTRA_READ_BOOK)) {
			mBook = (Book) intent.getSerializableExtra(EXTRA_READ_BOOK);
		} else {
			showToast("no find book");
			finish();
		}

		setContentView(R.layout.layout_read_book);
		mScrollView = (ScrollView) findViewById(R.id.sv_read_book);
		mTVBookContent = (TextView) findViewById(R.id.tv_book_content);
		try {
			mTVBookContent.setText(mBookLogic.getBookContent(mBook));
		} catch (Exception e) {
			e.printStackTrace();
		}

		scrollLastReadPosion();

	}

	private void scrollLastReadPosion() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				int position = (int) mBook.progress;
				mScrollView.scrollTo(0, position);
			}

		};
		mHandler.postDelayed(runnable, 1000);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
