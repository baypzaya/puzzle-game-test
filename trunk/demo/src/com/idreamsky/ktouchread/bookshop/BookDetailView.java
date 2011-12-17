package com.idreamsky.ktouchread.bookshop;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.BookInfo;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.NetBookShelf;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.directoy.DirectoryActivity;
import com.idreamsky.ktouchread.http.BitmapRequest;
import com.idreamsky.ktouchread.http.sync.RequestGetChapter;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.SDCardUtils;
import com.idreamsky.ktouchread.util.Util;

public class BookDetailView extends AbstractView {

	private int TitleID = 0x52;
	private NetBook mBook = null;
	private BookInfo mBookInfo;

	private Advert mAdvert = null;

	private ImageView Cover;
	private TextView bookName;
	private TextView bookAuthor;
	private TextView bookCategory;
	private TextView bookStatus;
	private TextView description;
	private Button btnCollect;
	private Button btnreadButton;
	private int mType = 0; // 0:book 1:advert
	private ProgressDialog syncDataDialog;

	public BookDetailView(Activity context, NetBook book) {
		super(context);
		mBook = book;
		mType = 0;
	}

	public BookDetailView(Activity context, Advert advert) {
		super(context);
		mAdvert = advert;
		mType = 1;
	}

	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;

		LinearLayout layoutBook = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.book_shop_details, null);

		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
		iconParams.addRule(RelativeLayout.BELOW, TitleID);
		iconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(layoutBook, iconParams);

//		Button btnBackButton = (Button) layoutBook
//				.findViewById(R.id.back_button);
//		btnBackButton.setText(R.string.back);
//		btnBackButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				((BookShopActivity) mContext).Back();
//			}
//		});

		ScrollView scrollview = (ScrollView) layoutBook
				.findViewById(R.id.scrollview);
		scrollview.setVerticalScrollBarEnabled(false);
		scrollview.setHorizontalScrollBarEnabled(false);

		btnreadButton = (Button) layoutBook.findViewById(R.id.read_book);
		btnCollect = (Button) layoutBook.findViewById(R.id.collect_book);

		btnreadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final BookDetailActivity activity = (BookDetailActivity) mContext;
				// TODO Auto-generated method stub
				if (mBookInfo != null && mBookInfo.bookid != null) {
					btnreadButton.setEnabled(false);
					ShowProcee();
					final Book book = ChangeBook(mBookInfo);

					NetChapter.GetBookChapter(book.rpid, book.cpcode,
							book.bookidNet,
							new NetChapter.GetBookChapterCallback() {

								@Override
								public void onFail(String msg) {
									// TODO Auto-generated method stub
									DismissProcess();
									btnreadButton.setEnabled(true);
									activity.makeToast(mContext.getString(R.string.cannotloadchanpterlist));

								}

								@Override
								public void onSuccess(NetChapter NetChapterInfo) {
									// TODO Auto-generated method stub
									if (NetChapterInfo.mChapterInfoList.size() > 0) {
										List<Chapter> chapterList = new ArrayList<Chapter>();
										for (int i = 0; i < NetChapterInfo.mChapterInfoList
												.size(); i++) {
											NetChapter.ChapterInfo chapterInfo = NetChapterInfo.mChapterInfoList
													.get(i);
											Chapter chapter = new Chapter();
											chapter.BookIDNet = NetChapterInfo.bookid;
											chapter.ChapterName = chapterInfo.chaptername;
											chapter.ChapterIDNet = chapterInfo.chapterid;
											chapter.WordCount = chapterInfo.wordcount;
											chapter.Price = chapterInfo.price;
											chapter.bFree = chapterInfo.isfree;
											chapter.bDownLoad = 0;
											chapterList.add(chapter);

										}

										book.SetChapterList(chapterList);
										DismissProcess();
										BookShelf.mCurrentBook = book;
										
										Intent intent = new Intent();
//										Bundle bundle = new Bundle();
//										bundle.putSerializable(
//												BookShelf.READBOOK, book);
//										intent.putExtras(bundle);
										intent.putExtra(Util.ENTRANCE,BookShelf.BOOKDETAIL);
										intent.setClass(mContext,DirectoryActivity.class);
										mContext.startActivityForResult(intent, BookShelf.REFRESHCODE);
										btnreadButton.setEnabled(true);
										
									}else
									{
										Toast.makeText(mContext, "服务器没有章节信息!", 1).show();
										DismissProcess();
									}

								}
							});

				}

			}
		});

		btnCollect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CollectBook();
//				if (mBookInfo != null && mBookInfo.bookid != null) {
//					final Book book = ChangeBook(mBookInfo);
					
//					new Thread(){
//						public void run() {
//							Book.AddBook(book);
//
//						};
//					}.start();
//					btnCollect.setEnabled(false);
//					btnCollect.setText(R.string.un_collect);
//					CustomPopup customPopup = new CustomPopup(mContext);
//					customPopup.show(R.id.id_bookshop,((BookShopActivity) mContext)
//									.getString(R.string.addToBookMark));
//					new Handler().post(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							Book.AddBook(book);
//							btnCollect.setEnabled(false);
//							btnCollect.setText(R.string.un_collect);
//							CustomPopup customPopup = new CustomPopup(mContext);
//							customPopup.show(R.id.id_bookshop,
//									((BookShopActivity) mContext)
//											.getString(R.string.addToBookMark));
//							
//						}
//					});

					((BookDetailActivity) mContext).SetCollectNewBook();
				//}
			}
		});

		Cover = (ImageView) layoutBook.findViewById(R.id.book_icon);
		Cover.setScaleType(ImageView.ScaleType.FIT_XY);
		Cover.setBackgroundResource(R.drawable.cover);


		

		
		bookName = (TextView) layoutBook.findViewById(R.id.book_name);
		bookAuthor = (TextView) layoutBook.findViewById(R.id.book_author);
		bookCategory = (TextView) layoutBook.findViewById(R.id.book_category);
		bookStatus = (TextView) layoutBook.findViewById(R.id.book_status);
		description = (TextView) layoutBook.findViewById(R.id.book_description);

		scrollview = (ScrollView) layoutBook.findViewById(R.id.scrollview);
		scrollview.setVerticalScrollBarEnabled(false);
		scrollview.setHorizontalScrollBarEnabled(false);
	}

	@Override
	public void onFinishInit() {
		if (mType == 0) {
			UpdateBookInfo();
		} else {
			UpdateBookInfoAdvert();
		}

	}

	@Override
	public void onRemovedFromWindow() {
	}

	Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == 0)
			{
				syncDataDialog = new ProgressDialog(mContext);
				syncDataDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				syncDataDialog.setTitle(R.string.select_book);
				syncDataDialog.setMessage(mContext.getString(R.string.select_book_wait));
				syncDataDialog.setIndeterminate(false);
				syncDataDialog.setIcon(R.drawable.icon);
				syncDataDialog.setCancelable(false);
				syncDataDialog.show();
			}else if(msg.what == 1)
			{
				if(syncDataDialog != null && syncDataDialog.isShowing())
				{
					syncDataDialog.dismiss();
				}
			}
			super.dispatchMessage(msg);
		}
	};
	private void CollectBook()
	{
		if (mBookInfo != null && mBookInfo.bookid != null) {
			final Book book = ChangeBook(mBookInfo);
			btnCollect.setEnabled(false);
			btnCollect.setText(R.string.un_collect);
			new Thread()
			{
				public void run() {
					
					mHandler.sendEmptyMessage(0);
					if(!Book.IsExit(book.bookidNet))
					{
						book.RecentReadTime = System.currentTimeMillis();
						Book.GetBookListEx().add(0, book);
						
						NetBookShelf.add(book.cpcode, book.rpid, book.bookidNet,  new NetBookShelf.AddBookCallback() {
							
							@Override
							public void onFail(String msg) {
								// TODO Auto-generated method stub
								book.Sync = 0;
								Book.GetSyncBooks().add(book);
								LogEx.Log_V("API", "AddBookShelfNet :" + msg);
								
							}
							
							@Override
							public void onSuccess(String strMsg) {
								// TODO Auto-generated method stub
								LogEx.Log_V("API", "AddBookShelfNet:Success");
								
							}
						});

					}
					else if(Book.GetBookSyncStatus(book.bookidNet) == 2)
					{
						//book.UpdateStatus(1);		
						Book.UpdateBookSyncStatusTime(book.bookidNet,0);
					}
					NetChapter chapters = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
					if(chapters != null && chapters.mChapterInfoList.size() >0)
					{
						BookDataBase.getInstance().InsertChapter(chapters, book.bookidNet, true);
					}
					Book.Save();
					mHandler.sendEmptyMessage(1);
				};
			}.start();
		}
	}
	public void SetBook(NetBook book) {
		mBook = book;
		UpdateBookInfo();
	}

	public void UpdateBookInfo() {
		ShowProcee();

		BookInfo.GetBookInfo(mBook.cpcode, mBook.rpid, mBook.bookid,
				new BookInfo.GetBookInforCallback() {

					@Override
					public void onFail(String msg) {
						DismissProcess();
						Message message = ((BookDetailActivity) mContext).mHandler.obtainMessage(12, msg);
						((BookDetailActivity) mContext).mHandler.sendMessage(message);
					}

					@Override
					public void onSuccess(BookInfo bookinfo) {

						mBookInfo = bookinfo;
						UpdateUI();
						DismissProcess();
					}
				});
	}

	public void SetAdvert(Advert advert) {
		mAdvert = advert;
		UpdateBookInfoAdvert();
	}

	public void UpdateBookInfoAdvert() {
		ShowProcee();
		BookInfo.GetBookInfo(mAdvert.cpcode, mAdvert.rpid, mAdvert.bookid,
				new BookInfo.GetBookInforCallback() {

					@Override
					public void onFail(String msg) {
						DismissProcess();
						Message message = ((BookDetailActivity) mContext).mHandler.obtainMessage(12, msg);
						((BookDetailActivity) mContext).mHandler.sendMessage(message);
					}

					@Override
					public void onSuccess(BookInfo bookinfo) {

						mBookInfo = bookinfo;
						UpdateUI();
						DismissProcess();
					}
				});
	}

	private void UpdateUI()
	{
		
		LogEx.Log_V("ImageUrl", mBookInfo.coverimageurl);
	   // BitmapRequest.fillImageView(mBookInfo.coverimageurl, Cover,mBookInfo.bookid);
		
		if(mBookInfo.bookid == null)
		{
			return;
		}
		final WeakReference<Bitmap> bitmapRef = ImgUtil.sBitmapPool.get(mBookInfo.bookid);
		Bitmap coverBitmap = null;
		if (null != bitmapRef) {
			Bitmap bitmap = bitmapRef.get();
			if (null != bitmap) {
				if(bitmap.isRecycled())
				{
					ImgUtil.sBitmapPool.remove(mBookInfo.bookid);
				}
				else {
					coverBitmap = bitmap;
				}
			}
		}
		if(coverBitmap == null)
		{
			if(SDCardUtils.isSDCardEnable())
			{
				coverBitmap = ImgUtil.getImageByPath(mBookInfo.bookid);
				if(coverBitmap != null)
				{
					Cover.setImageBitmap(coverBitmap);
					
					ImgUtil.sBitmapPool.put(mBookInfo.bookid, new WeakReference<Bitmap>(coverBitmap));
					
				}
			}
		}


		if(coverBitmap != null)
		{
			Cover.setImageBitmap(coverBitmap);
		}
		else
		{
			BitmapRequest.fillImageView(mBookInfo.coverimageurl, Cover, mBookInfo.bookid);
		}
		
	    bookName.setText(mBookInfo.bookname);
	    bookAuthor.setText(mContext.getString(R.string.book_detail_author)+ mBookInfo.authorname);
	    if(mBookInfo.CategoryName != null && mBookInfo.CategoryName.length() >0 && mBookInfo.CategoryName.compareTo("null") != 0)
	    {

	    	bookCategory.setText(mContext.getString(R.string.book_detail_itemize) + mBookInfo.CategoryName);
	    }
	    else {
	    	bookCategory.setText(mContext.getString(R.string.book_detail_itemize) + "无");
		}
	    

//	   String text=mBookInfo.description;
//	   StringBuffer buffer1=new StringBuffer();
//	   buffer1.append('【').append("br").append(" ").append('/').append('】');
//	   String newText=text.replaceAll(buffer1.toString(),"\r");
//	   
//	   StringBuffer buffer2=new StringBuffer();
//	   buffer2.append('<').append("br").append(" ").append("/>");
	   
//	    description.setText(Html.fromHtml(mBookInfo.description));
	    
	    
	    description.setText("        " + mBookInfo.description);
	    
	    if(mBookInfo.bookstatus == 0)
	    { 
	    	bookStatus.setText(R.string.book_datail_state_last);
	    }
	    else if(mBookInfo.bookstatus == 1)
	    {
	    	bookStatus.setText(R.string.book_datail_state_loading);
	    }
	    
	    if(Book.IsExit(mBookInfo.bookid) && (Book.GetBookSyncStatus(mBookInfo.bookid) != 2))
	    {
	    	btnCollect.setText(R.string.un_collect);
	    	btnCollect.setEnabled(false);
	    }
	    else {
	    	btnCollect.setEnabled(true);
	    	btnCollect.setText(R.string.collect);
		}
	}

	public Book ChangeBook(BookInfo bookInfo) {
		Book book = new Book();

		book.Book_Name = bookInfo.bookname;
		book.Author = bookInfo.authorname;
		book.pic_Path = "";
		book.Pic_Url = bookInfo.coverimageurl;
		book.Recent_Chapter_ID = "";
		book.Recent_Chapter = "";
		book.Recent_Chapter_Name = "";
		book.bUpdate = 1; // 0 不追更 ，1追更
		if(bookInfo.bookstatus == 0) //完结
			book.bUpdate = 2;
		book.bFree = bookInfo.billingtype; // 0 免费 ，1付费

		book.cpcode = bookInfo.cpcode; // CP编号
		book.rpid = bookInfo.rpid; // CP的CP编号
		book.bookidNet = bookInfo.bookid; // 书籍编号
		book.Price = bookInfo.price;
		book.Sync = 0;
		book.BookType = 0;

		book.RecentReadTime = System.currentTimeMillis();
		return book;
	}
	@Override
	public void UpdataUI()
	{
		if(mBookInfo != null && btnCollect!=null)
		{
		    if(Book.IsExit(mBookInfo.bookid) && (Book.GetBookSyncStatus(mBookInfo.bookid) != 2))
		    {
		    	btnCollect.setEnabled(false);
		    	btnCollect.setText(R.string.un_collect);
		    }
		    else {
		    	btnCollect.setEnabled(true);
		    	btnCollect.setText(R.string.collect);
			}
		}

	}

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		if (mType == 0) {
			UpdateBookInfo();
		} else {
			UpdateBookInfoAdvert();
		}
	}
}