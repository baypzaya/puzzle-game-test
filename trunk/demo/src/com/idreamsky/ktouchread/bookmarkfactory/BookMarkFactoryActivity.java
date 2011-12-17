package com.idreamsky.ktouchread.bookmarkfactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.Poster;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.net.BookMarkFactory;
import com.idreamsky.ktouchread.data.net.BookMarkFactory.BookMarkAdvert;
import com.idreamsky.ktouchread.data.net.BookMarkFactory.BookMarkPIC;
import com.idreamsky.ktouchread.data.net.BookMarkFactory.GetBookMarkAdvertListCallback;
import com.idreamsky.ktouchread.data.net.BookMarkFactory.GetBookMarkPicListCallback;
import com.idreamsky.ktouchread.http.BitmapRequest;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.NetUtil;
import com.idreamsky.ktouchread.util.SDCardUtils;
import com.idreamsky.ktouchread.util.SettingUtils;

public class BookMarkFactoryActivity extends SpiritActivity implements
		View.OnClickListener, OnTouchListener, OnGestureListener {

	private ViewFlipper flipper;
	private ViewFlipper bookmark_flipper;
	private GestureDetector detector;
	private GestureDetector deGestureDetector;
	// private BookMarkFactoryPoster bookMarkFactoryPoster;
	private BookMarkFactoryThem bookMarkFactoryThem;

	private RelativeLayout book_shop_head;

	private TextView poster_text;
	private ImageView bookmark_factory_banner;
	private ImageView bookmark_factory_barre;
	private LinearLayout star_layout;
	private Button setting_bookmark;

	private int index = -1; // 记录button锁点击的drawabla
	private int oldIndex = -1;
	// private LinearLayout bookmark_layout;
	private View currentView;

	private LayoutInflater inflater;
	private static final int BOOK_MARK = 10;
	private static final int NET_IMAGE_SETTING = -1;
	private static final int LOCAL_IMAGE_SETTING = 1;
	private static final int INIT_NET_BOOKMARK=3;
	private static final int INIT_NET_BOOKMARK_THEM=4;
	private static final int SLIDING_UPDATE =5;
	private static final int SLIDING_UPDATE_THEM =6;

	private File imageFile = null;
	private int star_index = 0;
	private ImageView arrow_big_left;
	private ImageView arrow_big_right;

	private List<BookMarkFactory.BookMarkAdvert> BookMarkAdvertList = null;
	private List<BookMarkFactory.BookMarkPIC> BookMarkPICList = null;

	private int[] BookMarkLoacalID = { R.drawable.bookmark_factory_1,
			R.drawable.bookmark_factory_2, R.drawable.bookmark_factory_3,
			R.drawable.bookmark_factory_4 };

	private ImageView oldImageView;
	private ImageView newImageView;
	private SettingUtils utils ;
	private static long mClickTime = 0;
	private boolean bookMarkFromNet = false;
	private View mSelectView = null;

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case LOCAL_IMAGE_SETTING:

				FileOutputStream ops = null;
				try {
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), index);
					File imgFile = new File(SDCardUtils.GetBookPath("png"));
					ops = new FileOutputStream(imgFile);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, ops);
					ops.flush();
					ops.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;
			case NET_IMAGE_SETTING:

				FileOutputStream opst = null;
				try {
					FileInputStream ips = new FileInputStream(imageFile);
					byte[] data = BookMarkFactoryActivity.readStream(ips);
					Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					String extention = null;
					String pathString = imageFile.getAbsolutePath();
					
					if(pathString.toLowerCase().endsWith("png"))
					{
						extention = "png";
						File imgFile = new File(SDCardUtils.GetBookPath(extention));
						opst = new FileOutputStream(imgFile);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, opst);
					}
					else
					{
						extention = "jpg";
						File imgFile = new File(SDCardUtils.GetBookPath(extention));
						opst = new FileOutputStream(imgFile);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, opst);
					}
					opst.flush();
					opst.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			case INIT_NET_BOOKMARK:
				
				star_layout.removeAllViews();
				flipper.removeAllViews();
				/* 动态的生成星星 */
				for (int i = 0; i < BookMarkAdvertList.size(); i++) {
					
					
					ImageView imageView = new ImageView(BookMarkFactoryActivity.this);
					imageView.setId(i);
					if (i == 0) {
						imageView.setBackgroundResource(R.drawable.dot02);
					} else {
						imageView.setBackgroundResource(R.drawable.dot01);
					}
					star_layout.addView(imageView);
					star_index = 0; // 初始化记录星星的id
					
					
					ImageView imageViewtheme = new ImageView(
							BookMarkFactoryActivity.this);
					imageViewtheme.setLayoutParams(params);
					imageViewtheme.setBackgroundResource(R.drawable.bookmark_factory_banner);
					BookMarkAdvert advert = BookMarkAdvertList
					.get(i);
			 
					if(SDCardUtils.isSDCardEnable())
					{
						Bitmap bitmap = ImgUtil.getImageByPath( "advet"+ BOOK_MARK + "" + advert.subjectname);
						if(bitmap != null)
						{
							imageViewtheme.setVisibility(View.VISIBLE);
							imageViewtheme.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
							
						}
						else {
					        BitmapRequest.fillImageView(
									advert.subjectImgUrl,
									imageViewtheme, "advet"
											+ BOOK_MARK + ""
											+ advert.subjectname,null);
						}
					}
					else {
				        BitmapRequest.fillImageView(
								advert.subjectImgUrl,
								imageViewtheme, "advet"
										+ BOOK_MARK + ""
										+ advert.subjectname,null);	    
					}

					flipper.addView(imageViewtheme);
				}
				flipper.setOnTouchListener(BookMarkFactoryActivity.this);
				/* 只有一套图，则隐藏豆豆区 ，否则就显示 */
				star_layout.setVisibility(View.VISIBLE);
				if (BookMarkAdvertList.size() == 1) {
					star_layout.setVisibility(View.INVISIBLE);
				}


				BookMarkAdvert advert = BookMarkAdvertList
				.get(0);
				poster_text.setText(advert.subjectname);
				
				
			break;
			
			case INIT_NET_BOOKMARK_THEM:
				
				/* 书签区 */

				deGestureDetector = new GestureDetector(bookMarkFactoryThem);
				bookmark_flipper.setLongClickable(true);
				bookmark_flipper.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(
									View v,
									MotionEvent event) {
								return deGestureDetector.onTouchEvent(event);
							}
						});
				
				

				ShowBookMark(BookMarkPICList);
				bookMarkFromNet = true;
			
			break;
			
			case SLIDING_UPDATE:
				
				getCurrentTheme(star_index);
				
			break;
			case SLIDING_UPDATE_THEM:
				ShowBookMark(BookMarkPICList);	
			break;
			

			}

			super.handleMessage(msg);
		}

	};
	LayoutParams params;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//设置 NavigationBar
				NavigationBar.Builder builder = getNavigationBarBuilder();
				builder.setTitle(R.string.book_mark);
				builder.showBackButton(false);
		
		setContentView(R.layout.bookmark_factory);
		
		utils = new SettingUtils(BookMarkFactoryActivity.this, "SaveParameter",
				 Context.MODE_PRIVATE);
		
		params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		/* 设置头部 */

		TextView textview_head_title = (TextView) findViewById(R.id.textview_head_title);
//		ImageView imageIcon = (ImageView) findViewById(R.id.imageIcon);

		textview_head_title.setText("书签工场"); // 设置标题文字
//		imageIcon.setVisibility(View.VISIBLE); // 设置logo图片不显示

		/* 初始化组件 */
		initUI();
		
	}


	GestureDetector gestureDetector = null;

	public void initUI() {

		inflater = LayoutInflater.from(this);

		/* 初始化手势滑动组件 */
		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.flipper);
		flipper.setLongClickable(true);
		flipper.setOnTouchListener(this);

		/* 设置按钮，回到我的书架 */
//		Button back_button = (Button) findViewById(R.id.back_button);
//		back_button.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//				overridePendingTransition(R.anim.left_in,R.anim.left_out);
//			}
//		});

		/* 广告区 */
		poster_text = (TextView) findViewById(R.id.poster_text);
		bookmark_factory_barre = (ImageView) findViewById(R.id.bookmark_factory_barre);
		bookmark_factory_banner = (ImageView) findViewById(R.id.bookmark_factory_banner);

		/* 豆豆区 */
		star_layout = (LinearLayout) findViewById(R.id.star_layout);

		/* 头部 */
		book_shop_head = (RelativeLayout) findViewById(R.id.book_shop_head);

		/* 左右点击区 */
		arrow_big_left = (ImageView) findViewById(R.id.arrow_big_left);
		arrow_big_right = (ImageView) findViewById(R.id.arrow_big_right);
		arrow_big_left.setOnClickListener(this);
		arrow_big_right.setOnClickListener(this);

		/* 设置确定书签按钮 */
		setting_bookmark = (Button) findViewById(R.id.setting_bookmark);
		setting_bookmark.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mSelectView != null)
				     utils.putString("BookMark", (String)mSelectView.getTag());
				
				if (index != -1) {
					if (bookMarkFromNet ) {
						if (imageFile.exists()) {
							handler.sendEmptyMessage(NET_IMAGE_SETTING);
						}

					} else {
						new Thread(new Runnable() {

							@Override
							public void run() {

								handler.sendEmptyMessage(LOCAL_IMAGE_SETTING);
							}
						}).start();
					}
					Toast.makeText(BookMarkFactoryActivity.this,getString(R.string.setting_newBookMark),
							Toast.LENGTH_SHORT).show();
				} else {
					if(oldIndex == -1)
					{
						Toast.makeText(BookMarkFactoryActivity.this, getString(R.string.no_choose_setting_hip),
								Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(BookMarkFactoryActivity.this,getString(R.string.setting_newBookMark),
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		

		/* 一开始进行加载本地的书签，如果获取服务器数据失败，则不进行处理 */
		initLocalUI();

		/* 根据网络状况加载网络书签 */
		if (NetUtil.checkNetwork(this)) {
			BookMarkFactory
					.GetBookMarkAdvertList(new GetBookMarkAdvertListCallback() {

						@Override
						public void onFail(String msg) {
						
						}

						@Override
						public void onSuccess(
								List<BookMarkAdvert> listBookMarkAdvert) {
							BookMarkAdvertList = listBookMarkAdvert;

							/* 如果能够在网络获取书签，则使用第一套主题 */
							if (BookMarkAdvertList != null) {

				               new Thread(new Runnable() {
								
								@Override
								public void run() {
									handler.sendEmptyMessage(INIT_NET_BOOKMARK);
								}
							}).start();
								

								

								BookMarkFactory.GetBookMarkPicList(
										BookMarkAdvertList.get(0).subjectid,
										new GetBookMarkPicListCallback() {

											@Override
											public void onFail(String msg) {

											}

											@Override
											public void onSuccess(
													List<BookMarkPIC> listBookMarkAdvert) {
												
												BookMarkPICList = listBookMarkAdvert;
													bookMarkFactoryThem = new BookMarkFactoryThem(BookMarkFactoryActivity.this,bookmark_flipper,listBookMarkAdvert);
													handler.sendEmptyMessage(INIT_NET_BOOKMARK_THEM);
												
//												 new Thread(new Runnable() {
//														
//														@Override
//														public void run() {
//															
//														}
//													}).start();
												
												
											}

										},BookMarkAdvertList.get(0).bUpdateBookmarkpic);

							}
							
							BookMarkAdvertList.get(0).bUpdateBookmarkpic = false;

						}
					});

		}

	}


	
	
	/* 展示数据，每次滑动主题的时候会访问此方法*/
	private void ShowBookMark(List<BookMarkPIC> BookMarkList) {
		
		bookmark_flipper.removeAllViews();
		
		int nTotal = BookMarkList.size(); // 总书签数
		int nPageNum = nTotal / 4; // 总页数
		if (nTotal % 4 != 0)
			nPageNum++;

		int width = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		int imgWidth = arrow_big_left.getLayoutParams().width;
		width = width - imgWidth * 2;
		int bmImg = width / 4;

		String tag = utils.getString("BookMark", null);
		boolean bGetTag = false;
		
		if (nTotal <= 4&&nTotal>0) { // 如果书签数不足4个。
			LinearLayout layout = showLayout();
			for (int i = 0; i < nTotal; i++) {

				LinearLayout item = (LinearLayout) inflater.inflate(
						R.layout.bookmark_factory_item, null);

				BookMarkPIC pic = BookMarkList.get(i);

				showBookMarkList(bmImg, layout, item, pic);
				if(tag != null && !bGetTag)
				{
					//index = Integer.parseInt(tag);
					View view = layout.findViewWithTag(tag);
					if(view != null)
					{	
						bGetTag = true;
						oldIndex = i;
						view.setSelected(true);
						if(currentView != null)
						{
							currentView.setSelected(false);
						}
						currentView = view;
					}
				}
				
			}
			bookmark_flipper.addView(layout);
		} else if (nTotal > 4) {
			arrow_big_left.setVisibility(View.VISIBLE);
			arrow_big_right.setVisibility(View.VISIBLE);
			for (int i = 1; i <= nPageNum; i++) {
				LinearLayout layout = showLayout();
				if (i == nPageNum) {
					int mCount = nTotal - (i - 1) * 4; // 求出最后一页剩余多少个
					for (int k = 0; k < mCount; k++) {
						LinearLayout item = (LinearLayout) inflater.inflate(
								R.layout.bookmark_factory_item, null);
						BookMarkPIC pic = BookMarkList.get(((i - 1) * 4 + k));
						showBookMarkList(bmImg, layout, item, pic);
						if(tag != null&&!bGetTag)
						{
							//index = Integer.parseInt(tag);
							View view = layout.findViewWithTag(tag);
							if(view != null)
							{	
								oldIndex = i*4 + k;
								bGetTag = true;
								view.setSelected(true);
								if(currentView != null)
								{
									currentView.setSelected(false);
								}
								currentView = view;
							}
						}
					}

				} else {
					for (int j = 0; j < 4; j++) {
						LinearLayout item = (LinearLayout) inflater.inflate(
								R.layout.bookmark_factory_item, null);
						BookMarkPIC pic = BookMarkList.get((i - 1) * 4 + j);
						showBookMarkList(bmImg, layout, item, pic);
						if(tag != null&&!bGetTag)
						{
							//index = Integer.parseInt(tag);
							View view = layout.findViewWithTag(tag);
							if(view != null)
							{	
								oldIndex = i*4 + j;
								bGetTag = true;
								view.setSelected(true);
								if(currentView != null)
								{
									currentView.setSelected(false);
								}
								currentView = view;
							}
						}

					}

				}
				bookmark_flipper.addView(layout);
			}

		}
	
		

	}




	private void showBookMarkList(int bmImg, LinearLayout layout,
			LinearLayout item, BookMarkPIC pic) {
		
		final String imageName = "advet" + BOOK_MARK + ""
				+ pic.bookmarkid;
		String Extention = null;
		if(pic.ImgUrl.toLowerCase().endsWith("png"))
		{
			Extention = ".png";
		}
		else
		{
			Extention = ".jpg";
		}
		final String imageNameExtention = imageName + Extention;

		final ImageView imageView = (ImageView) item.findViewById(R.id.bookmark_image);

		if(SDCardUtils.isSDCardEnable())
		{
			Bitmap bitmap = ImgUtil.getImageByPath(imageName);
			if(bitmap != null)
			{
				imageView.setVisibility(View.VISIBLE);
				imageView.setBackgroundDrawable(ImgUtil.drawableToBitmap(bitmap));
			}
			else {
				BitmapRequest.fillImageView(pic.ImgUrl, imageView, imageName,null);
			}
		}
		else
		{
			BitmapRequest.fillImageView(pic.ImgUrl, imageView, imageName,null);
		}
		

		final ImageButton btn = (ImageButton) item
				.findViewById(R.id.bookmark_button);
		btn.setTag(imageName);

		final TextView textView = (TextView) item
				.findViewById(R.id.bookmark_text);

		addView(bmImg, layout, item, pic, textView);
		
		imageView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					imgClickEvent(imageNameExtention, btn);
				}
				return deGestureDetector.onTouchEvent(event);
			}
		});

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//utils.putString("BookMark", (String)v.getTag());
				mSelectView = v;
				btnClickEvent(imageNameExtention, v);

			}
		});
	}

	/* 滑动的时候，把一个layout作为一个背景添加进viewfliping*/
	private LinearLayout showLayout() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		return layout;
	}

	private void addView(int bmImg, LinearLayout layout, LinearLayout item,
			BookMarkPIC pic, final TextView textView) {
		if (pic.isFree == 1) { // 收费
			textView.setText(pic.price);

		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bmImg,
				ViewGroup.LayoutParams.FILL_PARENT);
		params.gravity = Gravity.CENTER;
		item.setLayoutParams(params);
		layout.addView(item);
	}

	protected void initLocalUI() {

		BookMarkAdvertList = new ArrayList<BookMarkFactory.BookMarkAdvert>();
		BookMarkFactory.BookMarkAdvert BookMarkAdvert = new BookMarkFactory.BookMarkAdvert();
		BookMarkAdvert.subjectid = "0";
		BookMarkAdvert.subjectname = "Local";
		BookMarkAdvert.subjectImgUrl = String
				.valueOf(R.drawable.bookmark_factory_banner);
		BookMarkAdvertList.add(BookMarkAdvert);

		LinearLayout layout = showLayout();

		int width = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
//		int defaultWidth = width;
		int imgWidth = arrow_big_left.getLayoutParams().width;
		width = width - imgWidth * 2;
		int bmImg = width / 4;

		bookmark_flipper = (ViewFlipper) findViewById(R.id.bookmark_flipper);

		for (int i = 0; i < 4; i++) {
			BookMarkFactory.BookMarkPIC BookMarkPIC = new BookMarkFactory.BookMarkPIC();
			BookMarkPIC.bookmarkid = Integer.toString(i);
			BookMarkPIC.ImgUrl = Integer.toString(BookMarkLoacalID[i]);
			BookMarkPIC.isFree = 0;
			BookMarkPIC.price = "0";
			BookMarkPIC.textdesc = "local";
			BookMarkPIC.title = "local";
			
			LinearLayout item = (LinearLayout) inflater.inflate(
					R.layout.bookmark_factory_item, null);
			final ImageView imageView = (ImageView) item
					.findViewById(R.id.bookmark_image);
			imageView.setBackgroundResource(BookMarkLoacalID[i]);

			final ImageButton btn = (ImageButton) item
					.findViewById(R.id.bookmark_button);
			btn.setId(BookMarkLoacalID[i]); // 把图片的地址当做按钮的id，方便获得图片。
			btn.setTag(Integer.toString(i));

			final TextView textView = (TextView) item
					.findViewById(R.id.bookmark_text);

			addView(bmImg, layout, item, BookMarkPIC, textView);
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (currentView != null) {
						currentView.setSelected(false);
						v.setSelected(true);
						currentView = null;
						
					}
					//utils.putString("BookMark", (String)v.getTag());
					mSelectView = v;
					v.setSelected(true);
					currentView = v;

					index = v.getId();

				}
			});

			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (currentView != null) {
						currentView.setSelected(false);
						btn.setSelected(true);
						currentView = null;
						
					}
					//utils.putString("BookMark", (String)v.getTag());
					mSelectView = v;
					btn.setSelected(true);
					currentView = btn;

					index = btn.getId();
				}

			});

		}

		bookmark_flipper.addView(layout);

		/* 只有一套图，隐藏豆豆区 */
		star_layout.setVisibility(View.INVISIBLE);
		/* 使用默认的广告图 */
		bookmark_factory_banner.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.bookmark_factory_banner));
		/* 使用默认的主题文字 */
		poster_text.setText(getString(R.string.local_text));
		/* 默认主题去掉左右图标 */
		arrow_big_left.setVisibility(View.INVISIBLE);
		arrow_big_right.setVisibility(View.INVISIBLE);
		
		String tag = utils.getString("BookMark", null);
		if(tag != null)
		{
			View view = bookmark_flipper.findViewWithTag(tag);
			
			if(view != null)
			{
				index = view.getId();
				view.setSelected(true);
				if(currentView != null)
				{
					currentView.setSelected(false);
				}
				currentView = view;
			}
		}

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

//		int headHeight = book_shop_head.getHeight();
//		int bannerHeight = flipper.getHeight();
//		int barreHeight = bookmark_factory_barre.getHeight();

//		if (e1.getY() > headHeight
//				&& e1.getY() < headHeight + bannerHeight - barreHeight) { // 通过计算保证手势滑动只能在广告区域起效

			if (BookMarkAdvertList.size() > 1) { // 判断是否有数据，如果没有数据或者只有1套主题就无需滑动。

				if (e1.getX() - e2.getX() > Poster.FLING_MIN_DISTANCE && Math.abs(velocityX) > Poster.FLING_MIN_VELOCITY) { // 往左滑动
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							if (star_index > 0) {

								oldImageViewGone();

								star_index--;

								newImageViewShow();

							} else if (star_index == 0) {
								oldImageViewGone();

								star_index = BookMarkAdvertList.size() - 1;

								newImageViewShow();
							}
							
						}
					});
					

					this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
					this.flipper.setOutAnimation(AnimationUtils.loadAnimation(
							this, R.anim.left_out));
					this.flipper.showNext();
					return true;
				}

				if (e2.getX() - e1.getX() > Poster.FLING_MIN_DISTANCE && Math.abs(velocityX) > Poster.FLING_MIN_VELOCITY) { // 往右滑动
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (star_index < BookMarkAdvertList.size() - 1) {
							oldImageViewGone();

							star_index++;

							newImageViewShow();

						} else if (star_index == BookMarkAdvertList.size() - 1) { // 已经滑到最后一颗星星

							oldImageViewGone();

							star_index = 0;

							newImageViewShow();
						}
					}
				});

					this.flipper.setInAnimation(AnimationUtils.loadAnimation(
							this, R.anim.right_in));
					this.flipper.setOutAnimation(AnimationUtils.loadAnimation(
							this, R.anim.right_out));
					this.flipper.showPrevious();
					return true;
				}

			}

//		}

		return true;
	}




	private void oldImageViewGone() {
		oldImageView = (ImageView) star_layout
				.findViewById(star_index);
		oldImageView.setImageDrawable(getResources()
				.getDrawable(R.drawable.dot01));
	}


	private void newImageViewShow() {
		newImageView = (ImageView) star_layout
				.findViewById(star_index);
		newImageView.setImageDrawable(getResources()
				.getDrawable(R.drawable.dot02));

		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			handler.sendEmptyMessage(SLIDING_UPDATE);
			}
		}).start();
	
	}


	public void getCurrentTheme(int index) {

//		if (view != null) {
//			flipper.removeView(view);
//		}

		BookMarkAdvert advert = BookMarkAdvertList.get(index);

		/* 设置主题文字 */
		poster_text.setText(advert.subjectname);

		/* 设置主题图片 */
//		ImageView imageView = new ImageView(BookMarkFactoryActivity.this);
//		BitmapRequest.fillImageView(advert.subjectImgUrl, imageView, "advet"
//				+ BOOK_MARK + "" + advert.subjectname);
//
//		view = imageView;
//		flipper.addView(imageView);

		/* 通过主题的编号改变书签 */
		BookMarkFactory.GetBookMarkPicList(advert.subjectid,
				new GetBookMarkPicListCallback() {

					@Override
					public void onFail(String msg) {

					}

					@Override
					public void onSuccess(List<BookMarkPIC> listBookMarkAdvert) {
						BookMarkPICList = listBookMarkAdvert;
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								handler.sendEmptyMessage(SLIDING_UPDATE_THEM);
							}
						}).start();
					}
				},advert.bUpdateBookmarkpic);
		advert.bUpdateBookmarkpic = false;

	}

	@Override
	public void onClick(View v) {

		if(System.currentTimeMillis() - mClickTime < 1000)
			return ;
		
		mClickTime = System.currentTimeMillis();
		switch (v.getId()) {
		case R.id.arrow_big_left:

				bookmark_flipper.setInAnimation(AnimationUtils
						.loadAnimation(this, R.anim.left_in));
				bookmark_flipper.setOutAnimation(AnimationUtils
						.loadAnimation(this, R.anim.left_out));
				bookmark_flipper.showPrevious();
			

			break;

		case R.id.arrow_big_right:

			
					bookmark_flipper.setInAnimation(AnimationUtils
							.loadAnimation(this, R.anim.right_in));
					bookmark_flipper.setOutAnimation(AnimationUtils
							.loadAnimation(this, R.anim.right_out));
					bookmark_flipper.showNext();
				
			

			break;

		}

	}

	private void imgClickEvent(final String imageName, final ImageButton btn) {
		if (currentView != null) {
			currentView.setSelected(false);
			btn.setSelected(true);
			currentView = null;
		}
		btn.setSelected(true);
		//utils.putString("BookMark", (String)btn.getTag());
		mSelectView = btn;
		currentView = btn;

		imageFile = new File(SDCardUtils.GetpicPath(), imageName );

		index = 2;
	}

	private void btnClickEvent(final String imageName, View v) {
		if (currentView != null) {
			currentView.setSelected(false);
			v.setSelected(true);
			currentView = null;
		}
		
		v.setSelected(true);
		currentView = v;

		imageFile = new File(SDCardUtils.GetpicPath(), imageName );

		index = 2;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}
	

	/* 一个读取流的辅助方法 */
	public static byte[] readStream(InputStream ips) throws Exception {
		ByteArrayOutputStream ops = new ByteArrayOutputStream();
		final int buffer_size = 1024;
		byte[] bytes = new byte[buffer_size];
		for (;;) {
			int length = ips.read(bytes, 0, buffer_size);
			if (length == -1)
				break;
			ops.write(bytes, 0, length);
		}
		ips.close();
		ops.close();
		return ops.toByteArray();
	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				exitApplication();
			
		}
		return false;
	}

	/**
	 * 退出应用程序
	 */
	public void exitApplication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.bookshelf_exit);
		builder.setMessage(R.string.bookshelf_enter);
		builder.setPositiveButton(getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.create().show();
	}
	
	
}
