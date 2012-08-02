package com.gmail.txyjssr.reader;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gmail.txyjssr.reader.logic.BookLogic;

public class ReadBookActivity extends BaseActivity {
	
	public static final String EXTRA_READ_BOOK = "EXTRA_READ_BOOK"; 

	private TextView mTVBookContent;
	private BookLogic mBookLogic = new BookLogic();
	private Book mBook;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if(intent!=null && intent.hasExtra(EXTRA_READ_BOOK)){
			mBook = (Book) intent.getSerializableExtra(EXTRA_READ_BOOK);
		}else{
			showToast("no find book");
			finish();
		}

		setContentView(R.layout.layout_read_book);

		mTVBookContent = (TextView) findViewById(R.id.tv_book_content);
		try {
			mTVBookContent.setText(mBookLogic.getBookContent(mBook));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
