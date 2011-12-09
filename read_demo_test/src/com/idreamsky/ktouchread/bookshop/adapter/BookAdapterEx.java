
package com.idreamsky.ktouchread.bookshop.adapter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.http.bitmapasync.AsyncImageLoader;
import com.idreamsky.ktouchread.http.sync.RequestBitmap;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.SDCardUtils;

public class BookAdapterEx extends IdreamSkyBaseAdapter<NetBook>{
	
	public static  ConcurrentHashMap<String, WeakReference<Bitmap>> sBitmapPool = new ConcurrentHashMap<String, WeakReference<Bitmap>>();
	private LayoutInflater mLayoutInflater;
	
    private ListView listView;   
    private AsyncImageLoader asyncImageLoader; 
    
	public BookAdapterEx(Context profile, List<NetBook> Books) {
		super(profile, Books);
		mLayoutInflater= LayoutInflater.from(profile);
		asyncImageLoader = new AsyncImageLoader();  
	

		
	}

	public void SetListView(ListView listView)
	{
		this.listView = listView;
	}
	@Override
	protected boolean isUpdatable() {
		return true;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){		
		final NetBook book = getObject(position);
		final ViewHolder v;
		if(convertView==null)
		{
			v = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.book_shop_item, null);
			v.BookIcon = (ImageView) convertView.findViewById(R.id.myBookShelfLogo);
			v.BookName = (TextView) convertView.findViewById(R.id.myBookShelfBookName);
			v.bookAuthor = (TextView) convertView.findViewById(R.id.myBookShelfAuthor);
			convertView.setTag(v);
		}else
		{
			v = (ViewHolder) convertView.getTag();
		
		}
		
		v.BookIcon.setScaleType(ImageView.ScaleType.FIT_XY);
		//v.BookIcon.setBackgroundResource(R.drawable.cover);/////////////
		v.BookIcon.setImageResource(R.drawable.cover);
		if(book.bookid.compareTo("-1") == 0)
		{
			return convertView;
		}
		try {

			v.BookIcon.setTag(book.bookid);
			
			LogEx.Log_V("ImageUrl", book.coverimageurl);
			
			final WeakReference<Bitmap> bitmapRef = sBitmapPool.get(book.bookid);
			
			Bitmap coverBitmap = null;
			if (null != bitmapRef) {
				Bitmap bitmap = bitmapRef.get();
				if (null != bitmap) {
					if(bitmap.isRecycled())
					{
						sBitmapPool.remove(book.bookid);
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
					coverBitmap = ImgUtil.getImageByPath(book.bookid);
					if(coverBitmap != null)
					{
						v.BookIcon.setImageBitmap(coverBitmap);
						
						sBitmapPool.put(book.bookid, new WeakReference<Bitmap>(coverBitmap));
						
					}
				}
				if(coverBitmap == null)
				{
					v.BookIcon.setImageResource(R.drawable.cover);
					if(RequestBitmap.DownLoadCount < 5)
					{
						asyncImageLoader.loadDrawable(book.coverimageurl, book.bookid, new AsyncImageLoader.ImageCallback() {
							
							@Override
							public void imageLoaded(Bitmap imageDrawable, String BookIDNet) {
								// TODO Auto-generated method stub
								  ImageView imageViewByTag = (ImageView) listView.findViewWithTag(BookIDNet);
					                 if (imageViewByTag != null) {
					                     imageViewByTag.setImageBitmap(imageDrawable); 
					                     
					                     sBitmapPool.put(BookIDNet, new WeakReference<Bitmap>(imageDrawable));
					                     imageDrawable = null;
					                 }
					                 else
					                 {
					                	 imageDrawable.recycle();
					                	 imageDrawable = null;
					                 }
								
							}
						});
					}

				}
			}
			else {
				v.BookIcon.setImageBitmap(coverBitmap);
			}
			

		} catch (Exception e) {
			// TODO: handle exception
			LogEx.Log_V("Exception", e.toString());
		}

		v.BookName.setText(book.bookname);
		v.bookAuthor.setText(book.authorname);
		return convertView;
	}
	final private class ViewHolder {
		ImageView BookIcon;
		TextView BookName;
		TextView bookAuthor;		
	}
}
