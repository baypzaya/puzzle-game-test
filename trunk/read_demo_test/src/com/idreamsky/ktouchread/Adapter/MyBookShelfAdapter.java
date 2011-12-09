package com.idreamsky.ktouchread.Adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshelf.UnReadActivity;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.http.bitmapasync.AsyncImageLoader;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.SDCardUtils;

public class MyBookShelfAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private Activity mContext;
	private List<Book> myBooks;
	public static boolean afterMoreFlag = false;

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	

	public MyBookShelfAdapter(Activity mContext) {
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(mContext);
		myBooks = new ArrayList<Book>(0);
		asyncImageLoader = new AsyncImageLoader();
	}

	public void SetListView(ListView listView) {
		this.listView = listView;
	}

	public void setMyBooks(List<Book> myBooks) {
		this.myBooks = myBooks;
	}

	@Override
	public int getCount() {
		return myBooks.size();
	}

	@Override
	public Object getItem(int position) {
		return myBooks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// if ( view [ position ] != null ) {
		// return view[ position ] ;
		// }
		final ViewHolder v;
		if (convertView == null) {
			v = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.my_book_shelf_item,
					null);
			v.myBookShelfLogo = (ImageView) convertView
					.findViewById(R.id.myBookShelfLogo);
			v.myBookShelfUpdateCount = (Button) convertView
					.findViewById(R.id.myBookShelfUpdateCount);
			v.myBookShelfBookName = (TextView) convertView
					.findViewById(R.id.myBookShelfBookName);
			v.myBookShelfAuthor = (TextView) convertView
					.findViewById(R.id.myBookShelfAuthor);
			v.myBookShelfLatelyReadTitle = (TextView) convertView
					.findViewById(R.id.myBookShelfLatelyReadTitle);
			v.myBookShelfLatelyRead = (TextView) convertView
					.findViewById(R.id.myBookShelfLatelyRead);
			v.myBookShelfAfterMore = (Button) convertView
					.findViewById(R.id.myBookShelfAfterMore);
			convertView.setTag(v);
		} else {
			v = (ViewHolder) convertView.getTag();

		}
		final Book book = myBooks.get(position);
		v.myBookShelfLogo.setScaleType(ImageView.ScaleType.FIT_XY);
	//	v.myBookShelfLogo.setBackgroundResource(R.drawable.cover);// ///////////
		v.myBookShelfLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cover));
		
		if (book.BookType == 0) {
			v.myBookShelfLogo.setTag(book.bookidNet);

			final WeakReference<Bitmap> bitmapRef = ImgUtil.sBitmapPool
					.get(book.bookidNet);

			Bitmap coverBitmap = null;
			if (null != bitmapRef) {
				Bitmap bitmap = bitmapRef.get();
				if (null != bitmap) {
					if (bitmap.isRecycled()) {
						ImgUtil.sBitmapPool.remove(book.bookidNet);
					} else {
						coverBitmap = bitmap;
					}
				}
			}
			if (coverBitmap == null) {
				if (SDCardUtils.isSDCardEnable()) {
					coverBitmap = ImgUtil.getImageByPath(book.bookidNet);
					if (coverBitmap != null) {
						v.myBookShelfLogo.setImageBitmap(coverBitmap);

						ImgUtil.sBitmapPool.put(book.bookidNet,
								new WeakReference<Bitmap>(coverBitmap));

					}
				}
				if (coverBitmap == null) {
					v.myBookShelfLogo.setImageResource(R.drawable.cover);

					asyncImageLoader.loadDrawable(book.Pic_Url, book.bookidNet,
							new AsyncImageLoader.ImageCallback() {

								@Override
								public void imageLoaded(Bitmap imageDrawable,
										String bookidNet) {
									// TODO Auto-generated method stub
									ImageView imageViewByTag = (ImageView) listView
											.findViewWithTag(bookidNet);
									if (imageViewByTag != null) {
										imageViewByTag
												.setImageBitmap(imageDrawable);
										ImgUtil.sBitmapPool.put(bookidNet, new WeakReference<Bitmap>(imageDrawable));
									}
					                 else
					                 {
					                	 imageDrawable.recycle();
					                	 imageDrawable = null;
					                 }

								}
							});
				}
			} else {
				v.myBookShelfLogo.setImageBitmap(coverBitmap);
			}
		}
		else {
			v.myBookShelfLogo.setTag(book.pic_Path);

		}
//		long start = SystemClock.uptimeMillis();
		if(book.bUpdate==1){
			
		}

		v.myBookShelfBookName.setText(book.Book_Name);
		v.myBookShelfAuthor.setText(mContext.getString(R.string.bookshelf_author)+book.Author);
		if (book.bUpdate == 0) // 0不追更,1追更
		{
			v.myBookShelfUpdateCount.setVisibility(View.GONE); // 不追更,不显示更新了多少章节
			v.myBookShelfAfterMore.setBackgroundResource(R.drawable.button_b);
			v.myBookShelfAfterMore.setTextColor(Color.WHITE);
			v.myBookShelfAfterMore.setEnabled(true);
			v.myBookShelfAfterMore.setText(mContext.getString(R.string.bookshelf_no_update));
		} else if(book.bUpdate == 1){
			
			if(book.unreadChapterNumber == -1)
			{
				int count = book.GetUnreadChapterCount();
				if(count>999)
				{
					count = 999;
				}
				book.unreadChapterNumber = count;
			}
			
			
//			Log.e("getCount","" +(SystemClock.uptimeMillis() - start));

			v.myBookShelfAfterMore.setBackgroundResource(R.drawable.button_h);
			v.myBookShelfAfterMore.setText(mContext.getString(R.string.bookshelf_yes_update));
			v.myBookShelfAfterMore.setTextColor(Color.WHITE);
			v.myBookShelfAfterMore.setEnabled(true);
			v.myBookShelfUpdateCount.setVisibility(View.VISIBLE);
			if(book.unreadChapterNumber!=0 && book.unreadChapterNumber != -1)
				v.myBookShelfUpdateCount.setText(Integer.toString(book.unreadChapterNumber));
		}else
		{
			v.myBookShelfAfterMore.setBackgroundResource(R.drawable.button_a);
			v.myBookShelfAfterMore.setText(mContext.getString(R.string.bookshelf_yes_end));
			v.myBookShelfAfterMore.setTextColor(Color.parseColor("#999999"));
			v.myBookShelfAfterMore.setEnabled(false);
			v.myBookShelfUpdateCount.setVisibility(View.GONE);
//			if(count!=0)
//				v.myBookShelfUpdateCount.setText(Integer.toString(count));
//			else{
//				v.myBookShelfUpdateCount.setVisibility(View.GONE);
//			}
		}
		v.myBookShelfLatelyRead.setText(book.Recent_Chapter_Name);
		v.myBookShelfAfterMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (book.bUpdate==0) {
					int ChapterCount = book.GetUnreadChapterCount();
					v.myBookShelfAfterMore.setBackgroundResource(R.drawable.button_h);
					v.myBookShelfUpdateCount.setVisibility(View.VISIBLE);
					if(ChapterCount>999)
					{
						ChapterCount = 999;
					}
					v.myBookShelfUpdateCount.setText(Integer.toString(ChapterCount));
					v.myBookShelfAfterMore.setText(mContext.getString(R.string.bookshelf_yes_update));
					v.myBookShelfAfterMore.setTextColor(Color.WHITE);
					afterMoreFlag = true;
					book.UpdateStatus(1);
				} else {
					v.myBookShelfAfterMore.setBackgroundResource(R.drawable.button_b);
					v.myBookShelfAfterMore.setText(mContext.getString(R.string.bookshelf_no_update));
					v.myBookShelfAfterMore.setTextColor(Color.WHITE);
					afterMoreFlag = false;
					book.UpdateStatus(0);
					v.myBookShelfUpdateCount.setVisibility(View.GONE);
				}

			}
		});
		v.myBookShelfUpdateCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				BookShelf.mCurrentBook = book;
//				Bundle bundle = new Bundle();
//				bundle.putSerializable(BookShelf.BOOKINFO, book); //传值
//				intent.putExtras(bundle);
				intent.setClass(mContext, UnReadActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				mContext.startActivityForResult(intent, BookShelf.REFRESHCODE);
			}
		});
		// view[position] = convertView;
		return convertView;
	}

	public class ViewHolder {
		public ImageView myBookShelfLogo;
		public Button myBookShelfUpdateCount;
		public TextView myBookShelfBookName;
		public TextView myBookShelfAuthor;
		public TextView myBookShelfLatelyReadTitle;
		public TextView myBookShelfLatelyRead;
		public Button myBookShelfAfterMore;

	}

}
