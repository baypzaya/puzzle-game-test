package com.idreamsky.ktouchread.common;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.idreamsky.ktouchread.bookread.BookPageFactory;
import com.idreamsky.ktouchread.bookread.PageWidget;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.util.BitmapUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.Util;

public class OpenBookAnimation  {
	private Handler mHandler;
	private Activity mActivity;
	public static Bitmap mCurPageBitmap = null;
	public static Bitmap mNextPageBitmap = null;
	public PageWidget mPageWidget;
	public Canvas mCurPageCanvas, mNextPageCanvas;
	private  Bitmap bgBitmap = null;
//	public static Bitmap bgBitmap;
//	public static Bitmap day_bgBitmap;
//	public static Bitmap night_bgBitmap;
	private int width = 0;
	private int height = 0;
	private LayoutInflater mLayoutInflater;
	public PopupWindow popupWindow;
	private ImageView imgBg;
	public static boolean model = true; // true 白天模式，false 夜间模式
	List<Book> books;
	public static void CleanData()
	{
//		if(OpenBookAnimation.bgBitmap != null)
//		{
//			if(!OpenBookAnimation.bgBitmap.isRecycled())
//			{
//				OpenBookAnimation.bgBitmap.recycle();
//				
//			}
//			OpenBookAnimation.bgBitmap = null;
//		}
//		if(OpenBookAnimation.day_bgBitmap != null)
//		{
//			if(!OpenBookAnimation.day_bgBitmap.isRecycled())
//			{
//				OpenBookAnimation.day_bgBitmap.recycle();
//			}
//			OpenBookAnimation.day_bgBitmap = null;
//		}
//		if(OpenBookAnimation.night_bgBitmap != null)
//		{
//			if(!OpenBookAnimation.night_bgBitmap.isRecycled())
//			{
//				OpenBookAnimation.night_bgBitmap.recycle();
//			}
//			OpenBookAnimation.night_bgBitmap = null;
//		}
		if(OpenBookAnimation.mCurPageBitmap != null)
		{
			if(!OpenBookAnimation.mCurPageBitmap.isRecycled())
			{
				OpenBookAnimation.mCurPageBitmap.recycle();
			}
			OpenBookAnimation.mCurPageBitmap = null;
		}
		if(mNextPageBitmap != null)
		{
			if(!OpenBookAnimation.mNextPageBitmap.isRecycled())
			{
				OpenBookAnimation.mNextPageBitmap.recycle();
			}
			OpenBookAnimation.mNextPageBitmap = null;
		}
		if(b != null && !OpenBookAnimation.b.isRecycled())
		{
			OpenBookAnimation.b.recycle();
			OpenBookAnimation.b = null;
		}
		
		System.gc();
		LogEx.Log_V("Data", "clean data");
	}
	public OpenBookAnimation(Activity activity,int width ,int height,ImageView imgBg,List<Book> books)
	{
		this.mActivity = activity;
		this.mLayoutInflater = LayoutInflater.from(activity);
		this.width = width;
		this.height = height;
		this.imgBg = imgBg;
		this.books = books;
		//读取当前模式 
		SharedPreferences modelSetting = activity.getSharedPreferences(Util.MODELSETTING,Context.MODE_PRIVATE);
		String model = modelSetting.getString(Util.MODEL, "");
		if (!model.equals("")) {
			this.model = Boolean.parseBoolean(model);
		}
		init();
	}
	
	public  static void initBitMap(Activity activity,int width,int height)
	{
		 if (null == OpenBookAnimation.mCurPageBitmap ||OpenBookAnimation.mCurPageBitmap.isRecycled()) {
			 OpenBookAnimation.mCurPageBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
			}

			if (null == OpenBookAnimation.mNextPageBitmap || OpenBookAnimation.mNextPageBitmap.isRecycled()) {
				OpenBookAnimation.mNextPageBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
			}
			
			SharedPreferences modelSetting = activity.getSharedPreferences(Util.MODELSETTING,Context.MODE_PRIVATE);
			String m = modelSetting.getString(Util.MODEL, "");
			
			if (!m.equals("")) {
				model= Boolean.parseBoolean(m);
			}
//			if (model) // 白天模式
//			{
//				if(day_bgBitmap==null || day_bgBitmap.isRecycled()){
//					day_bgBitmap = BitmapUtil.getBitmapFromRes(activity, R.drawable.page);
//				}
//				
//				if(bgBitmap!=null && !bgBitmap.isRecycled())
//				{
//					bgBitmap.recycle();
//					bgBitmap = null;
//				}
//				bgBitmap = day_bgBitmap;//ImgUtil.zoomImg(day_bgBitmap, width, height,true,activity);
////				if(bgBitmap== null || bgBitmap.isRecycled())
////				{
////					
////				}
//			} else {
//				
//				if(night_bgBitmap==null || night_bgBitmap.isRecycled()){
//					night_bgBitmap = BitmapUtil.getBitmapFromRes(activity, R.drawable.page_a);
//				}
//				
//				if(bgBitmap!=null && !bgBitmap.isRecycled())
//				{
//					bgBitmap.recycle();
//					bgBitmap = null;
//				}
//				bgBitmap = night_bgBitmap;// ImgUtil.zoomImg(night_bgBitmap, width, height,false,activity);
////				if(bgBitmap== null || bgBitmap.isRecycled())
////				{
////				    
////				}
//				
//			}
			LogEx.Log_V("Data", "Init data");
	}
	
	public void init()
	{
		initBitMap(mActivity,width,height);

		if(Configuration.ReadBackGroudUseColor)
		{
			if (model) // 白天模式
			{
				imgBg.setBackgroundColor(BookPageFactory.m_backColorDay);
			} else {
				imgBg.setBackgroundColor(BookPageFactory.m_backColorNight);
			}
		}
		else
		{
			if (model) // 白天模式
			{
				imgBg.setBackgroundDrawable(ImgUtil.drawableToBitmap(BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page)));
			} else {
				imgBg.setBackgroundDrawable(ImgUtil.drawableToBitmap(BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page_a)));	
			}
		}

        mPageWidget = new PageWidget(mActivity, width, height, mHandler);
        mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	
		if(b==null){

			b = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.conver);;
//			Bitmap	bitmap = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.conver);
//			
//			b = ImgUtil.zoomImg(bitmap, width, height);
//			bitmap.recycle();
//			bitmap = null;
		}
		
		view = mLayoutInflater.inflate(R.layout.animation_book, null);
//		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		lin_animation = (LinearLayout) view.findViewById(R.id.lin_animation);
	}
	LinearLayout lin_animation;
	View view;

	public static Bitmap b;
	public void start()
	{
		
		try {
			mCurPageCanvas.drawBitmap(b, 0, 0, null);
			

			if(Configuration.ReadBackGroudUseColor)
			{
				bgBitmap = null;
				if (model) // 白天模式
				{
					mNextPageCanvas.drawColor(BookPageFactory.m_backColorDay);
				} else {
					mNextPageCanvas.drawColor(BookPageFactory.m_backColorNight);
				}
			}
			else
			{
				if (model) // 白天模式
				{
					bgBitmap = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page);
				} else {
					bgBitmap = BitmapUtil.getBitmapFromRes(mActivity, R.drawable.page_a);	
				}
				
				mNextPageCanvas.drawBitmap(bgBitmap, 0, 0, null);
			}
			

			mPageWidget.setBitmaps(mCurPageBitmap,mNextPageBitmap);
			mPageWidget.postInvalidate();
			lin_animation.removeAllViews();
			lin_animation.addView(mPageWidget);
			
//			imgBg.startAnimation(Util.loadAnimation(mActivity, R.anim.popup_enter1));
			
			popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT, true);
//			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setAnimationStyle(R.style.PopupAnimation1);
			popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//			popupWindow.update();
			
			new Thread(){

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					if(books!=null && books.size()!=0)
//						books.clear();
					if(mPageWidget!=null)
					{
						mPageWidget.abortAnimation();
						mPageWidget.calcCornerXY(width-50, height-50);
						mPageWidget.mTouch.x = width-50;
						mPageWidget.mTouch.y = height-50;
						mPageWidget.mTouchToCornerDis = 50;
						
						if (mPageWidget.canDragOver()) {
							mPageWidget.startAnimation(1800);
							mPageWidget.postInvalidate();
						}
					}
					super.run();
				}
				
			}.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void dismiss()
	{
		if(popupWindow!=null && popupWindow.isShowing())
		{
			popupWindow.dismiss();
		}
		if(bgBitmap != null && !bgBitmap.isRecycled())
		{
			bgBitmap.recycle();
		}
		bgBitmap = null;
		
		if(OpenBookAnimation.b != null && !OpenBookAnimation.b.isRecycled())
		{
			OpenBookAnimation.b.recycle();
			
		}
		OpenBookAnimation.b = null;
		System.gc();
	}
	
	
}
