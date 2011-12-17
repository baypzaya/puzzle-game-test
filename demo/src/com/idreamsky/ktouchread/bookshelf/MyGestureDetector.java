package com.idreamsky.ktouchread.bookshelf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.idreamsky.ktouchread.bookmarkfactory.BookMarkFactoryActivity;
import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.bookshop.BookShopActivity;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.util.LogEx;

public class MyGestureDetector extends SimpleOnGestureListener {
	private static final int FLING_MIN_DISTANCE = 150;
	private static final int FLING_MIN_VELOCITY = 250;
	private Activity mActivity;
	public MyGestureDetector(Activity mActivity)
	{
		this.mActivity = mActivity;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
			if(e1!=null && e2!=null){
				if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
					LogEx.Log_I("operate", "onFling touch off");
	//				Util.toActivity(mActivity,BookShopActivity.class);
					Intent intent = new Intent();
					intent.putExtra(BookShelf.ENTRANCE, BookShelf.MYBOOKSHELF);
					intent.setClass(mActivity, BookShopActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					//mActivity.startActivityForResult(intent,BookShelf.COLLECTREQUESTCOLDE);
				}else if(e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY)
				{
//					IF(!CONFIGURATION.VERSION_0)
//					{
						Intent intent = new Intent();
						intent.setClass(mActivity, BookMarkFactoryActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						//mActivity.startActivity(intent);
						mActivity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//					}

				}
			}
		return false; 
	} 
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		LogEx.Log_I("operate", "onSingleTapUp touch off 2");
//		Util.toActivity(mActivity, BookReadActivity.class);
//		return super.onSingleTapUp(e);
//	}
	
	public void setBook(Book book)
	{
		LogEx.Log_I("operate", "onSingleTapUp touch off 1");
//		Util.toActivity(mActivity, BookReadActivity.class);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(BookShelf.READBOOK, book);
		intent.putExtras(bundle);
		intent.setClass(mActivity,  BookReadActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		mActivity.startActivity(intent);
	}
}
