package com.idreamsky.ktouchread.bookshop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.idreamsky.ktouchread.bookshelf.R;

public class BookSearchActivity extends SpiritActivity {
	
	public static final String EXTRA_SEARCH_CONTENT = "extra_search_content";
	private SearchView searchView;
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置 NavigationBar
		NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.book_search);
		builder.showBackButton(true);
		
		searchView = new SearchView(this);
		Intent intent = getIntent();
		final String content = intent.getStringExtra(EXTRA_SEARCH_CONTENT);
		searchView.setSearchContent(content);
		
		searchView.initializeIfNecessary();
		setContentView(searchView.getContentView());
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				searchView.search(content.trim());
			}
		};

		mHandler.postDelayed(runnable, 200);
		
		
	}
	
	public void makeToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void makeToast(final int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (searchView != null) {
			searchView.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}
	
	
}
