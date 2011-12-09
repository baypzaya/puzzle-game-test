package com.idreamsky.ktouchread.bookshelf;


import java.util.ArrayList;
import java.util.List;

import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.Advert.GetAdvertListCallback;
import com.idreamsky.ktouchread.http.BitmapRequest;

import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;

import android.app.Activity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

import android.widget.ViewFlipper;
/**
 * 
 * @author encore.liang
 * 广告轮换
 */
public class Poster implements OnGestureListener,OnTouchListener,GetAdvertListCallback {
	public static final String myBookShelf = "1"; //我的书架pos
	public static final String bookShop = "2"; //在线书城
	public static final String bookMark = "3"; //书签
	public static final int FLING_MIN_DISTANCE = 100;
	public static final int FLING_MIN_VELOCITY = 200;
	private GestureDetector mGestureDetector;
	private ViewFlipper mViewFlipper;
	public PosterRunnable posterRunnable = null;
	private Activity mContext;
	private LayoutInflater mLayoutInflater;
	private String pos;
	private static View[] posterViews; //广告view
	private ListView lvMyBookShelf;
	private Button btnArrow;
	private Button btnArrow1;
	public static boolean arrowFlag = true;
	private FrameLayout posterControl;
	private LinearLayout layout_book_poster;
	private clickAdvertDataCallBack mclickAdvertDataCallBack = null;
	private int posterSize = 0;
	private FrameLayout posterFloor;
	private boolean mHasTouchSlide = false;
	private Handler mHandler = new Handler(){

		@Override 
		public void handleMessage(Message msg) {
			if(mHasTouchSlide)
			{
				mHasTouchSlide = false;
			}
			else {
				mViewFlipper.showNext(); 
			}
			
		}
		
	};
	/**
	 * @author encore.liang
	 * @param mViewFlipper ViewFlipper
	 * @param mHandler current Handler
	 * @param mContext current Activity
	 * @param pos string 广告位置  1：我的书架2：在线书城3：书签   调用, Advert 类的静态变量赋值
	 */
	public Poster(Activity mActivity,String pos)
	{
//		this.mViewFlipper = mViewFlipper;
		this.mContext = mActivity;
		this.pos = pos;
		mLayoutInflater  = LayoutInflater.from(mContext);
		mGestureDetector = new GestureDetector(this);
        this.mViewFlipper = (ViewFlipper) this.mContext.findViewById(R.id.viewFlipper);
		this.mViewFlipper.setOnTouchListener(this);  
		this.mViewFlipper.setLongClickable(true);
        
        layout_book_poster  = (LinearLayout) this.mContext.findViewById(R.id.layout_book_poster);
        lvMyBookShelf = (ListView) this.mContext.findViewById(R.id.lvMyBookShelf);
        posterControl = (FrameLayout) this.mContext.findViewById(R.id.frameLayoutPoster);
        posterFloor = (FrameLayout) this.mContext.findViewById(R.id.posterFloor);
        btnArrow1 = (Button) this.mContext.findViewById(R.id.btnArrow);
        btnArrow = (Button) this.mContext.findViewById(R.id.btnArrow);
        btnArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				btnArrow.setBackgroundResource(R.drawable.btn_arrow_up);
				btnArrow1.setVisibility(View.VISIBLE);
				posterControl.setVisibility(View.GONE);
				arrowFlag = false;
				
			}
		});
        btnArrow1 = (Button) this.mContext.findViewById(R.id.btnArrow1);
        btnArrow1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnArrow.setBackgroundResource(R.drawable.btn_arrow);
				btnArrow1.setVisibility(View.GONE);
				posterControl.setVisibility(View.VISIBLE);
				arrowFlag = true;
			}
		});
	}
	public Poster(Activity mActivity,LinearLayout layout,String pos)
	{
		this.mContext = mActivity;
		this.pos = pos;
		mLayoutInflater  = LayoutInflater.from(mContext);
		mGestureDetector = new GestureDetector(this);
        this.mViewFlipper = (ViewFlipper) layout.findViewById(R.id.viewFlipper);
		this.mViewFlipper.setOnTouchListener(this);  
		this.mViewFlipper.setLongClickable(true);
		posterFloor = (FrameLayout) this.mContext.findViewById(R.id.posterFloor);
        layout_book_poster  = (LinearLayout) layout.findViewById(R.id.layout_book_poster);
        lvMyBookShelf = (ListView) layout.findViewById(R.id.lvMyBookShelf);
        posterControl = (FrameLayout) layout.findViewById(R.id.frameLayoutPoster);      
	}
	LayoutParams layoutParams;
	public void showPosterList()
	{	
		final int width = mContext.getWindow().getWindowManager().getDefaultDisplay().getWidth();
//		layoutParams=  ;
		if(NetUtil.checkNetwork(mContext))//pos.compareTo(bookShop) == 0
		{
			ImageView imageView = new ImageView(mContext);
//			imageView.setLayoutParams(new LayoutParams(width,LayoutParams.FILL_PARENT));
			if(pos.compareTo(myBookShelf) == 0)
			{
				imageView.setBackgroundResource(R.drawable.book_bg);
			}
			else if(pos.compareTo(bookShop) == 0){
				imageView.setBackgroundResource(R.drawable.book_bg_1);
			}
			
			mViewFlipper.addView(imageView);
			
			
			Advert.GetAdvertList(pos, new Advert.GetAdvertListCallback() {
				
				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					LogEx.Log_V("Token", msg);
					
				}
				
				@Override
				public void onSuccess(List<Advert> AdvertList) {
					// TODO Auto-generated method stub
					
					mViewFlipper.removeAllViews();
					List<Advert> listAdvert = AdvertList;
					
					
					posterSize = listAdvert.size();
					if(posterSize>5) //最多5张
					{
						posterSize=5;
					}
					posterViews  = new View[posterSize];
					for(int i =0;i<posterSize;i++)
					{
						Advert a = listAdvert.get(i);
						final View poster_item  = mLayoutInflater.inflate(R.layout.poster_item, null);
						TextView tvBookName = (TextView) poster_item.findViewById(R.id.posterBookName);
						ImageView posterBg = (ImageView) poster_item.findViewById(R.id.posterBg);
						ImageView posterTurn1 = (ImageView) poster_item.findViewById(R.id.posterTurn1);
						ImageView posterTurn2 = (ImageView) poster_item.findViewById(R.id.posterTurn2);
						ImageView posterTurn3 = (ImageView) poster_item.findViewById(R.id.posterTurn3);
						ImageView posterTurn4 = (ImageView) poster_item.findViewById(R.id.posterTurn4);
						ImageView posterTurn5 = (ImageView) poster_item.findViewById(R.id.posterTurn5);
						if(posterSize>1){ //数量大于1才计算有多少个turn
							if(posterSize>0)
							{
								posterTurn1.setVisibility(View.VISIBLE);
								if(posterSize>1)
								{
									posterTurn2.setVisibility(View.VISIBLE);
									if(posterSize>2)
									{
										posterTurn3.setVisibility(View.VISIBLE);
										if(posterSize>3)
										{
											posterTurn4.setVisibility(View.VISIBLE);
											if(posterSize>4)
											{
												posterTurn5.setVisibility(View.VISIBLE);
											}
										}
									}
									
								}
							}
						}
						tvBookName.setText(a.bookname);
//						posterBg.setLayoutParams(new LayoutParams(width,LayoutParams.FILL_PARENT));
						
						if(pos.compareTo(myBookShelf) == 0)
						{
							posterBg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.book_bg));
						}else {
							posterBg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.book_bg_1));
						}
						
						BitmapRequest.fillImageView(a.ImgUrl, posterBg,"advet"+pos + "_"+ a.bookid,null);
						
						
						poster_item.setTag(a);
						posterViews[i] = poster_item;
						mViewFlipper.addView(poster_item);
			      }
					
					//更换状态
					if(posterSize>0)
					{
						posterViews[0].findViewById(R.id.posterTurn1).setBackgroundResource(R.drawable.dot_green);
						if(posterSize>1)
						{
							posterViews[1].findViewById(R.id.posterTurn2).setBackgroundResource(R.drawable.dot_green);
							if(posterSize>2)
							{
								posterViews[2].findViewById(R.id.posterTurn3).setBackgroundResource(R.drawable.dot_green);
								if(posterSize>3)
								{
									posterViews[3].findViewById(R.id.posterTurn4).setBackgroundResource(R.drawable.dot_green);
									if(posterSize>4)
									{
										posterViews[4].findViewById(R.id.posterTurn5).setBackgroundResource(R.drawable.dot_green);
									}
								}
							}
							
						}
					}

					if(posterSize>1){
						posterRunnable = new PosterRunnable(mViewFlipper, mHandler); //启动轮播
						posterRunnable.start();
					}
					
					
				}
			});
		}
		else 
		{
			if(pos.compareTo(myBookShelf) == 0)
			{
				List<Advert> listAdvert = new ArrayList<Advert>();
				Advert advert = new Advert();
				
				advert.bookname="";
				advert.ImgUrl=Environment.getExternalStorageState()+"/1.jpg";
				advert.bookid = "";
				listAdvert.add(advert);
				posterSize = listAdvert.size();
				
				if(posterSize>5) //最多5张
				{
					posterSize=5;
				}
				posterViews  = new View[posterSize];
				for(int i =0;i<posterSize;i++)
				{
					Advert a = listAdvert.get(i);
					View poster_item  = mLayoutInflater.inflate(R.layout.poster_item, null);
					TextView tvBookName = (TextView) poster_item.findViewById(R.id.posterBookName);
					ImageView posterBg = (ImageView) poster_item.findViewById(R.id.posterBg);
					ImageView posterTurn1 = (ImageView) poster_item.findViewById(R.id.posterTurn1);
					ImageView posterTurn2 = (ImageView) poster_item.findViewById(R.id.posterTurn2);
					ImageView posterTurn3 = (ImageView) poster_item.findViewById(R.id.posterTurn3);
					ImageView posterTurn4 = (ImageView) poster_item.findViewById(R.id.posterTurn4);
					ImageView posterTurn5 = (ImageView) poster_item.findViewById(R.id.posterTurn5);
					if(posterSize>1){ //数量大于1才计算有多少个turn
						if(posterSize>0)
						{
							posterTurn1.setVisibility(View.VISIBLE);
							if(posterSize>1)
							{
								posterTurn2.setVisibility(View.VISIBLE);
								if(posterSize>2)
								{
									posterTurn3.setVisibility(View.VISIBLE);
									if(posterSize>3)
									{
										posterTurn4.setVisibility(View.VISIBLE);
										if(posterSize>4)
										{
											posterTurn5.setVisibility(View.VISIBLE);
										}
									}
								}
								
							}
						}
					}
					tvBookName.setText(a.bookname);
//					posterBg.setLayoutParams(new LayoutParams(width,LayoutParams.FILL_PARENT));
//					posterBg.setMinimumWidth(width);
//					posterBg.setBackgroundResource(R.drawable.book_bg);
					posterBg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.book_bg));
					Integer currentView = i; //设置当前view
					poster_item.setTag(a);
					posterViews[i] = poster_item;
					mViewFlipper.addView(poster_item);
		}
	
	
		//更换状态
				if(posterSize>0)
				{
					posterViews[0].findViewById(R.id.posterTurn1).setBackgroundResource(R.drawable.dot_green);
					if(posterSize>1)
					{
						posterViews[1].findViewById(R.id.posterTurn2).setBackgroundResource(R.drawable.dot_green);
						if(posterSize>2)
						{
							posterViews[2].findViewById(R.id.posterTurn3).setBackgroundResource(R.drawable.dot_green);
							if(posterSize>3)
							{
								posterViews[3].findViewById(R.id.posterTurn4).setBackgroundResource(R.drawable.dot_green);
								if(posterSize>4)
								{
									posterViews[4].findViewById(R.id.posterTurn5).setBackgroundResource(R.drawable.dot_green);
								}
							}
						}
						
					}
				}
//				mViewFlipper.setOnClickListener(mViewFlipperOnClickListener);
				if(posterSize>1){
					posterRunnable = new PosterRunnable(mViewFlipper, mHandler); //启动轮播
					posterRunnable.start();
				}
			}
		}
//		else
//		{

//		}

	}
	
	public OnClickListener mViewFlipperOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			LogEx.Log_I("Poster", "click");
		}
	};
	
	/**
	 * get Poster Success
	 */
	@Override
	public void onSuccess(List<Advert> listAdvert) {

	}
	/**
	 * get Poster Fail
	 */
	@Override
	public void onFail(String msg) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		LogEx.Log_I("Poster", "on Down click");
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		LogEx.Log_I("Poster", "on Fling tuo dong"); 
		if(posterSize>1){
			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
	
				// 当像左侧滑动的时候 
	
				// 设置View进入屏幕时候使用的动画
	
				mViewFlipper.setInAnimation(inFromRightAnimation());
	
				// 设置View退出屏幕时候使用的动画
	
				mViewFlipper.setOutAnimation(outToLeftAnimation());
	
				mHasTouchSlide = true;
				mViewFlipper.showNext();
				
				if(posterRunnable != null)
				{
					posterRunnable.Pause();
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							posterRunnable.Resume();
							
						}
					},  6000);
				}

	//			posterRunnable = new PosterRunnable(mViewFlipper, mHandler);
	//			posterRunnable.start();
				
				
	
			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
	
				// 当像右侧滑动的时候
	
				mViewFlipper.setInAnimation(inFromLeftAnimation());
	
				mViewFlipper.setOutAnimation(outToRightAnimation());
	
				mHasTouchSlide = true;
				mViewFlipper.showPrevious();
				if(posterRunnable != null)
				{
					posterRunnable.Pause();
					mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							posterRunnable.Resume();
							
						}
					},  6000);
				}
	//			posterRunnable.stop();
	//			posterRunnable = new PosterRunnable(mViewFlipper, mHandler);
	//			posterRunnable.start();
	
			}
		}

		return false;
	}

	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		LogEx.Log_I("Poster", "on Scroll tuo dong");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	private static Advert advertCallBack;
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		LogEx.Log_I("Poster", "on SingleTapUP");
		if(mViewFlipper!=null && mViewFlipper.getCurrentView()!=null && mViewFlipper.getCurrentView().getTag()!=null)
			advertCallBack = (Advert) mViewFlipper.getCurrentView().getTag();
		if(mclickAdvertDataCallBack != null)
		{
			mclickAdvertDataCallBack.getAdverDataCallBack(advertCallBack);
		}
		return false;
	}
	
	public void SetCallback(final clickAdvertDataCallBack cb)
	{
		mclickAdvertDataCallBack = cb;
	}
	
	public static interface clickAdvertDataCallBack
	{
		public void getAdverDataCallBack(Advert advert);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event); 
	}
	
	
	/**
	 * 
	 * 定义从右侧进入的动画效果
	 * 
	 * @return
	 */

	protected Animation inFromRightAnimation() {

		Animation inFromRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, +1.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f);

		inFromRight.setDuration(500);

		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;

	}

	/**
	 * 
	 * 定义从左侧退出的动画效果
	 * 
	 * @return
	 */

	protected Animation outToLeftAnimation() {

		Animation outtoLeft = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, -1.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f);

		outtoLeft.setDuration(500);

		outtoLeft.setInterpolator(new AccelerateInterpolator());

		return outtoLeft;

	}

	/**
	 * 
	 * 定义从左侧进入的动画效果
	 * 
	 * @return
	 */

	protected Animation inFromLeftAnimation() {

		Animation inFromLeft = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, -1.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f);

		inFromLeft.setDuration(500);

		inFromLeft.setInterpolator(new AccelerateInterpolator());

		return inFromLeft;

	}

	/**
	 * 
	 * 定义从右侧退出时的动画效果
	 * 
	 * @return
	 */

	protected Animation outToRightAnimation() {

		Animation outtoRight = new TranslateAnimation(

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, +1.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f,

		Animation.RELATIVE_TO_PARENT, 0.0f);

		outtoRight.setDuration(500);

		outtoRight.setInterpolator(new AccelerateInterpolator());

		return outtoRight;

	}

	public void stopPoster()
	{
		if(posterRunnable!=null)
			posterRunnable.stop();
	}
}
