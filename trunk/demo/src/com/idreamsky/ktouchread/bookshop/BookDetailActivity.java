package com.idreamsky.ktouchread.bookshop;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.aliyun.aui.widget.spirit.NavigationBar.OnSearchBarDoSearchingListener;
import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.ImgUtil;

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
		//builder.showSearchButton(true);
        builder.setCommand("搜索", new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookDetailActivity.this,BookSearchActivity.class);
				intent.putExtra(BookSearchActivity.EXTRA_SEARCH_CONTENT, "");
				BookDetailActivity.this.startActivity(intent);
				builder.hideSearchbar();
			}
		});
		/*
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
		*/
		Drawable drawable = new ColorDrawable(0x66000000);
		//setSearchContentBackground(drawable);
		//setSearchMaskDrawable(drawable);
		
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

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 10) {
				GetUserInforError();
			} else if (msg.what == 11) {
				new Thread() {
					public void run() {
						Iterator iter = ImgUtil.sBitmapPool.entrySet()
								.iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							String key = (String) entry.getKey();
							WeakReference<Bitmap> bitmapRef = (WeakReference<Bitmap>) entry
									.getValue();
							if (bitmapRef != null && key != null) {
								Bitmap bitmap = bitmapRef.get();
								if (null != bitmap) {
									if (!bitmap.isRecycled()) {
										bitmap.recycle();
									}

								}
								ImgUtil.sBitmapPool.remove(key);
							}
						}
					};
				}.start();
			} else if (msg.what == 12) {
				InitTryDialog((String) msg.obj);

			} else if (msg.what == 13) {
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						// ViewStrategy viewStrategy = getViewStrategy();
						// AbstractView view = viewStrategy.getCurrentView();
						// view.ReLoadData();
					}
				}, 500);

			}
			super.handleMessage(msg);
		}
	};
	
	private void InitTryDialog(String Msg) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					this);
			builder.setTitle(R.string.book_ol_network_error);
			builder.setMessage(Msg);
			builder.setPositiveButton(getString(R.string.book_ol_network_agin),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							mHandler.sendEmptyMessage(13);

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Back();
						}
					});
			builder.create().show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void GetUserInforError() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
			builder.setTitle(R.string.splash_notify);
			builder.setMessage(R.string.splash_get_user_info_error);
			builder.setPositiveButton(getString(R.string.book_ol_network_agin),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SwitchtoKPayAccount();

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							UrlUtil.TokenTPL = null;
							BookDetailActivity.this
									.setResult(BookShelf.COLLECTREQUESTCOLDE);
							BookDetailActivity.this.finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
}
	private void SwitchtoKPayAccount() {
		Intent intent = KPayAccount.GetUserIntent();
		startActivityForResult(intent,
				KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
		Book.Save();
	}
}
