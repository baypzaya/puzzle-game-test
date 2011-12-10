package com.idreamsky.ktouchread.bookshop;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.aliyun.aui.widget.spirit.NavigationBar.OnSearchBarDoSearchingListener;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.NetBook;

public class BookDetailActivity extends SpiritActivity {
	
	public static final int TYPE_BOOK = 0;
	public static final int TYPE_ADVERT = 1;
	public static final String EXTRA_SCR_TYPE = "extra_src_type"; 
	public static final String EXTRA_CPCODE = "extra_cpcode"; 
	public static final String EXTRA_RPID = "extra_rpid"; 
	public static final String EXTRA_BOOKID = "extra_book_id"; 
	
	private BookDetailView bookDetailView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置 NavigationBar
		final NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.book_detail);
		builder.showBackButton(true);
		builder.showSearchButton(true);
		
builder.setOnSearchBarDoSearchingListener(new OnSearchBarDoSearchingListener() {
			
			@Override
			public void doSearching(CharSequence arg0) {
				Log.i("yujsh log","search button onclick");
				
				String searchContent = builder.getSearchText().toString();
				Log.i("yujsh log","doSearching search content:"+searchContent);
				Intent intent = new Intent(BookDetailActivity.this,BookSearchActivity.class);
				intent.putExtra(BookSearchActivity.EXTRA_SEARCH_CONTENT, searchContent);
				BookDetailActivity.this.startActivity(intent);
				builder.hideSearchbar();
			}
		});
		
		Drawable drawable = new ColorDrawable(0x66000000);
		setSearchContentBackground(drawable);
		setSearchMaskDrawable(drawable);
		
		Intent intent = getIntent();
		String cpCode = intent.getStringExtra(EXTRA_CPCODE);
		String bookId = intent.getStringExtra(EXTRA_BOOKID);
		String rpId = intent.getStringExtra(EXTRA_RPID);
		
		
		
		if(intent.hasExtra(EXTRA_SCR_TYPE)){
			int type = intent.getIntExtra(EXTRA_SCR_TYPE, -1);
			switch (type) {
			case TYPE_BOOK:
				NetBook book = new NetBook();
				book.bookid = bookId;
				book.rpid = rpId;
				book.cpcode = cpCode;
				bookDetailView = new BookDetailView(this, book);
				break;
			case TYPE_ADVERT:
				Advert advert = new Advert();
				advert.bookid = bookId;
				advert.rpid = rpId;
				advert.cpcode = cpCode;
				bookDetailView = new BookDetailView(this, advert);
				break;
			default:
				break;
			}
		}
		
		
		bookDetailView.initializeIfNecessary();
		setContentView(bookDetailView.getContentView());
		
	}

	public void makeToast(final String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void makeToast(final int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	public void SetCollectNewBook() {
		//提醒书架在查看书籍详情时，有书籍已被收藏，更新书架
		
	}

	

}
