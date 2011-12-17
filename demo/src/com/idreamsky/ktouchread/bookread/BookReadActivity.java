package com.idreamsky.ktouchread.bookread;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.Book_System_SettingAct;
import com.idreamsky.ktouchread.bookshelf.GuideActivity;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.common.OpenBookAnimation;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.BookMark;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.data.ChapterContent;
import com.idreamsky.ktouchread.data.SyncThread;
import com.idreamsky.ktouchread.data.net.BookOrder;
import com.idreamsky.ktouchread.data.net.NetBookShelf;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.directoy.DirectoryActivity;
import com.idreamsky.ktouchread.http.sync.RequestGetToken;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.service.AddBookService;
import com.idreamsky.ktouchread.util.Base64;
import com.idreamsky.ktouchread.util.BitmapUtil;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;
import com.idreamsky.ktouchread.util.ProcessDialog;
import com.idreamsky.ktouchread.util.SDCardUtils;
import com.idreamsky.ktouchread.util.SettingUtils;
import com.idreamsky.ktouchread.util.Util;
import com.idreamsky.ktouchread.xmlparase.ErrorResult;
import com.idreamsky.ktouchread.yunpay.AliyunPayAsyncTask;
import com.idreamsky.ktouchread.yunpay.AliyunPayAsyncTask.Listener;



import com.aliyun.aui.widget.spirit.DropDownList;
import com.aliyun.aui.widget.spirit.NavigationBar;
//TY:liushan add
import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
//TY:liushan end
//TY:liushan modify start
public class BookReadActivity extends SpiritActivity {
//TY:liushan moddify end	
	public static final String FILERECEIVER = "android.file.receiver";
	public Canvas mCurPageCanvas, mNextPageCanvas; //当前的canvas 和 next 的canvas
	public static int width = 0;
	public static int height = 0;
	public static final int RefreshTool = 101;
	public static long mClickTime = 0;
	public static String Price = null;
	public static String PayInRedPacket = null;
	public static String PayInCash = null;
	private LinearLayout linRead;
	public String TAG = "BookReadActivity";
	private boolean isGetChapter = false;
	
	private int checkCount = 0;
	public static BookOrder curBookOrder;
	private AliyunPayAsyncTask payTask    = null;
	private Handler handler = new Handler();
	
	/**
	 * 处理翻页的类
	 */
	private PageWidget mPageWidget;
	/**
	 * current page bitmap
	 */
	public static Bitmap mCurPageBitmap = null; 
	/**
	 * next page bitmap
	 */
	public static Bitmap mNextPageBitmap = null; 
	/**
	 *  dispose words display
	 */
	private BookPageFactory pagefactory; 
	/**
	 * font max size
	 */
	private int maxSize = 34;
	/**
	 * font min size
	 */
	private int minSize = 24; 
	/**
	 * current show size
	 */
	private int showSize = 28; //当前显示ide字体大小
	/**
	 * Whether the middle click
	 */
	private boolean isMiddle = false; 
	public long currentOperateTime = 0;
	public long nextOpetateTime = 0;
	/*
	private Button luminancePlus; // 亮度+
	private Button luminanceReduce; // 亮度-
	private Button fontSizePlus; // 字体+
	private Button fontSizeReduce;// 字体-
	private Button dayModel; // 白天模式
	private Button nightModel;// 夜间模式*/
	private float defaultLuminance = 0.4f; // 默认亮度
	public Boolean isShowToastModel = null; //白天
	/*
	public Boolean isLuminanceToast = null;
	*/
	public Boolean isFontSizeToast = null;

	//private Button back; // 返回
	private Button toDirectory; // 去通讯录
	//private Button addBookMark; // 书签
	private Button collect; // 收藏
	private Button changeMode;
	private Button lightMode;
	private Button font;
	private boolean nightMode;
	private DropDownList mDropList;
	private DropDownList mDropListfont;
	//private float[]mLuminance = {0.4f,0.6f,0.8f,1.0f};
	//private Button deleteBookMark; //删除书签按钮
	//TY:liushan delete start
	//private LinearLayout bookRead_title_tool; // 标题工具栏 
	//TY:liushan delete end
	private RelativeLayout bookRead_floor_tool;//底部工具栏
	private RelativeLayout collectionLayout;
	private RelativeLayout listLayout;
	private RelativeLayout lightlessLayout;
	private RelativeLayout fontLayout;
	private RelativeLayout modeLayout;
	private TextView collectionTextView;
	private ImageView img_bookmark;
	int floor_tool_height; // 底部工具栏高度
	private LinearLayout pageNumber;
	private Book book; //书籍对象
	private List<Chapter> chapters; //所有章节
	private int currentIndex = 0; // 当前第几页
	private ChapterContent mChapterContent = null; //章节内容data
	private Chapter chapter;
	private List<BookMark> bookMarks = null;
	private String bookMarkPages = "";
	private SeekBar progress_bar_font_size;
	private Integer bookMarkCurrentPage = 0; // bookMark传过来的页码
	private String bookMarkCurrentIndex = ""; // bookMark当前的位置
	private String[] markText;
	public boolean model = true; // true 白天模式，false 夜间模式
	public boolean isFirstEntrance = false;//是否第一次进入activity
	public boolean leftOrRight = false; // false 翻左边,ture 右边
	public boolean entranceBookDetail = false; // 详细页面入口
	public boolean isAddToBookShelf = false; // 是否已加入书架
	public boolean isExitsBook = false;
	public boolean isPageing = false;
	public static boolean isLeftOrRight = true;
	private String entrance; 
	public boolean readIsFrist = true;
	private boolean page = false;
	public static boolean isExistActivity = false; //current activity is runing?
	private TextView tvPageNumber;
	private String en = null;
	private boolean  bAddBook = false;
	private RelativeLayout boodRead_parent;
	private Timer timer;
	public boolean isRunning = true;
	private Toast toast;
	private boolean loadingData = false;
	private SyncThread syncThread = null;
	private boolean bTopage = false;
	private boolean bPageing = false;
	private boolean bigBookMarkFlag = false;
	private boolean isCloseTool = false;
	public static int preTotal = 0;
	public boolean isToNetWorkGetContent = false;
	AlertDialog.Builder builder = null; //记住未显示的dialog	
	//TY:liushan add start
	NavigationBar.Builder NavigationBarbuilder;
	
	//TY:liushan add end
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		if(width==320 && height==480)
		{
			showSize = 16;
			maxSize = 26;
			minSize = 16;
		}
		OpenBookAnimation.initBitMap(this,width,height);
		mCurPageBitmap = OpenBookAnimation.mCurPageBitmap;
		mNextPageBitmap = OpenBookAnimation.mNextPageBitmap;
		setContentView(R.layout.book_read);
		loadingData = true;
		ProcessDialog.ShowProcess(this);
		
		System.gc();
		System.gc();
		//book = BookShelf.mCurrentBook;// (Book) //获取当前book
		init(); //初始化

		book = BookShelf.mCurrentBook;// (Book) //获取当前book
		//Button collect = (Button)findViewById(R.id.collection);
		 if (Book.IsExit(book.bookidNet)&& (Book.GetBookSyncStatus(book.bookidNet) != 2)) {
			  collect.setBackgroundResource(R.drawable.collectionn);
			  collectionLayout.setClickable(false);
			  collectionTextView.setText("已收藏");
			  
		  }
		
		if(book.BookType==1)//如果是本地文件
		{
			IntentFilter intentFilter = new IntentFilter(BookReadActivity.FILERECEIVER);
			registerReceiver(broadcastReceiver, intentFilter);
		}
		
		new Thread(){

			@Override
			public void run() {
				if (book != null) {
					book.OpenBook();
					if(chapters == null)
					    chapters = book.GetChapterList(); //7
					initHandler.sendEmptyMessage(0);
					
				}
				super.run();
			}
			
		}.start();
	}
	
	private class MyArrayAdapter extends BaseAdapter{
       private LayoutInflater mInflater;
       private Context mContext = null;
       private float Luminance ;
       private int curPosition ;
       String[] items = getResources().getStringArray(R.array.items);
//       private ArrayList<RadioButton>Radioitems = new ArrayList<RadioButton>();    
       
       public MyArrayAdapter(){
    	   WindowManager.LayoutParams luminancePlus = getWindow().getAttributes();
    	   Luminance = luminancePlus.screenBrightness;
    	  
    	   curPosition = getCurPositon();
       }
       
		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int position) {			
			return items[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = null;
			if(convertView==null){			
				view=LayoutInflater.from(getApplicationContext()).inflate
				(R.layout.drop_choice_item,null);
			}else{
				view=convertView;
			}
						
			TextView mTextView = (TextView)view.findViewById(R.id.textlight);
			
			mTextView.setText(items[position]);			
			RadioButton mRadioButton =(RadioButton)view.findViewById(R.id.radiobutton);		
			
			if(curPosition==position){
				mRadioButton.setChecked(true);
			}else{
				mRadioButton.setChecked(false);
			}			
			
			view.setTag(position);
			view.setOnClickListener(listener);
			return view;
		}
		
		OnClickListener listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//getWindow().setAttributes(luminancePlus);
				Log.v("liushan","convertView.onClick");
				int pos = (Integer)v.getTag();
				curPosition = pos;
				MyArrayAdapter.this.notifyDataSetChanged();
				setLight(curPosition);
				 mDropList.dismiss();	
				//getWindow().setAttributes(luminancePlus);
			};
		};
		private int getCurPositon(){
			Log.v("liushan","Luminance: "+Luminance);
			if(Luminance>=1.0){
				return 0;
			}else if(Luminance>=0.8){
				return 1;
			}else if(Luminance>=0.6){
				return 2;
			}else if(Luminance>=0.4){
				return 3;
			}else {			
				return 4;
			}
			
		}
	}
	
	private void setLight(int pos){
		WindowManager.LayoutParams luminancePlus = getWindow().getAttributes();
		luminancePlus.screenBrightness = defaultLuminance;
		if(pos == 0){
			luminancePlus.screenBrightness = 1.0f;
		}else if(pos == 1){
			luminancePlus.screenBrightness = 0.8f;
		}else if(pos == 2){
			luminancePlus.screenBrightness = 0.6f;
		}else if(pos == 3){
			luminancePlus.screenBrightness = 0.4f;
		}else{
		    luminancePlus.screenBrightness = 0.2f;
		}
		Log.v("liushan","luminancePlus.screenBrightness: "+luminancePlus.screenBrightness);
		getWindow().setAttributes(luminancePlus);
	}
	
	
	private class MyArrayAdapterfont extends BaseAdapter{
	       private LayoutInflater mInflater;
	       private Context mContext = null;
	       private float Luminance ;
	       private int curPosition ;
	       String[] items = getResources().getStringArray(R.array.fontitems);
//	       private ArrayList<RadioButton>Radioitems = new ArrayList<RadioButton>();    
	       
	       public MyArrayAdapterfont(){
	    	 //读取字体
	   		SharedPreferences sizeSetting = getSharedPreferences(Util.SIZESETTING,
	   				Context.MODE_PRIVATE);
	   		String fSzie = sizeSetting.getString(Util.FONTSIZE, "");
	   		if (!fSzie.equals("")) {
	   			showSize = Integer.parseInt(fSzie);
	   		}
	   		curPosition = getFontCurPositon();
	       }
	       
	   	private int getFontCurPositon(){
			
			if(showSize>=32){
				return 0;
			}else if(showSize>=30){
				return 1;
			}else if(showSize>=28){
				return 2;
			}else if(showSize>=26){
				return 3;
			}else 	
				return 4;
			
	   	}
			@Override
			public int getCount() {
				return items.length;
			}

			@Override
			public Object getItem(int position) {			
				return items[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				View view = null;
				if(convertView==null){			
					view=LayoutInflater.from(getApplicationContext()).inflate
					(R.layout.drop_choice_front,null);
				}else{
					view=convertView;
				}
							
				TextView mTextView = (TextView)view.findViewById(R.id.textfont);
				
				mTextView.setText(items[position]);			
				RadioButton mRadioButton =(RadioButton)view.findViewById(R.id.radiobuttonfont);		
				
				if(curPosition==position){
					mRadioButton.setChecked(true);
				}else{
					mRadioButton.setChecked(false);
				}			
				
				view.setTag(position);
				view.setOnClickListener(listener);
				return view;
			}
			
			OnClickListener listener=new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.v("liushan","convertView.onClick");
					int pos = (Integer)v.getTag();
					curPosition = pos;
					MyArrayAdapterfont.this.notifyDataSetChanged();
					setFont(curPosition);
					 mDropListfont.dismiss();	
				};
			};
			
		}
	
	private void setFont(int pos){
		Message msg = new Message();
		if(pos == 0){
			showSize= 32;
			msg.what = 1;
			fontHandler.sendMessage(msg);
			}else if(pos == 1){
				showSize= 30;
				msg.what = 1;
				fontHandler.sendMessage(msg);
				
			}else if(pos == 2){
				showSize= 28;
				msg.what = 2;
				fontHandler.sendMessage(msg);
			}else if(pos == 3){
				showSize= 28;
				msg.what = 2;
				fontHandler.sendMessage(msg);
			}else{
				showSize= 26;
		       msg.what = 2;
		       fontHandler.sendMessage(msg);
			}

	}
	@Override
	public void onResume() {
		super.onResume();
		if (builder != null)
			builder.show();
	}
	
	public Handler initHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
	
			entrance = getIntent().getStringExtra(Util.ENTRANCE); //入口
			currentIndex = 0;
			if (entrance != null) {
				/**
				 * 章节目录跳转过来
				 */
				if (entrance.equals(Util.CHAPTER)) 
				{
					int chapterIndex = getIntent().getIntExtra(Util.CHAPTER, 0);
					currentIndex = chapterIndex;
					 en =  getIntent().getStringExtra(Util.ENTRANCE);
					 if(en!=null)
					 {
						 entranceBookDetail = true;
					 }
				} 
				/**
				 * 书签页跳转过来
				 */
				else if (entrance.equals(Util.BOOKMARK)) // 书签
				{
					BookMark bookMark = (BookMark) getIntent().getExtras().getSerializable(BookShelf.DIRECTORY);
					markText = bookMark.Mark_Text.split(",");
					bookMarkCurrentPage = Integer.parseInt(bookMark.Pos) - 1;
					currentIndex = Integer.parseInt(markText[AddBookMark.currIndex]);
					Animation a = Util.loadAnimation(BookReadActivity.this,R.anim.book_mark_top_in);
					bigBookMark(a);
					Log.d("BookReadActivity","-------------*******>BookMark switch");
//					
				} 
				/**
				 * 书架跳转过来
				 */
				else if (entrance.equals(Util.RECENTLYREAD))// bookShelf
				{
					if (book.Recent_Chapter == null|| book.Recent_Chapter.equals("")) {
						currentIndex = 0;
					} else {
						currentIndex = Integer.parseInt(book.Recent_Chapter_ID);
						bookMarkCurrentPage = Integer.parseInt(book.Recent_Chapter) - 1;
					}

				} 
				/**
				 * 未读跳转过来
				 */
				else if (entrance.equals(Util.UNREAD)) {
					Chapter chapter = (Chapter) getIntent().getExtras().getSerializable(Util.UNREAD); //获取最近阅读
					currentIndex = 0;
					if (chapters.size() > 0) {
						for (int i = 0; i < chapters.size(); i++) {
							Chapter c = chapters.get(i);
							if (chapter.ChapterIDNet.equals(c.ChapterIDNet)) {
								currentIndex = i;
								break;
							}
						}
					}
				} 
			} 
			if ( chapters != null && currentIndex>=0 && currentIndex<chapters.size()) {
				if (chapters.get(currentIndex) != null)
					bookMarks = book.GetBookMarkList(chapters.get(currentIndex).ChapterIDNet);
			}
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					//获取内容
					getChapterContent(currentIndex);

				}
			});

			if (Book.IsExit(book.bookidNet)&& (Book.GetBookSyncStatus(book.bookidNet) != 2)) {
				isExitsBook = true;
				isAddToBookShelf = true;
				//collect.setText(R.string.un_collect);
			} else {
				isExitsBook = false;
				isAddToBookShelf = false;
			}

			/**
			 * 引导页
			 */
			guide();
			
			/**
			 * 获取书签
			 */			
			super.handleMessage(msg);
		}
		
	};
	//add for test
	Thread mBookMarkThread = new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			BookMark bookMark = (BookMark) getIntent().getExtras().getSerializable(BookShelf.DIRECTORY);
//			markText = bookMark.Mark_Text.split(",");
//			bookMarkCurrentPage = Integer.parseInt(bookMark.Pos) - 1;
//			currentIndex = Integer.parseInt(markText[AddBookMark.currIndex]);
//			Animation a = Util.loadAnimation(BookReadActivity.this,R.anim.book_mark_top_in);
//			bigBookMark(a);
			chapters =  BookShelf.mCurrentBook.GetChapterList();
		}
		
	});
	/**
	 * 引导页
	 */
	public void guide()
	{
		//引导页
		SettingUtils utils = new SettingUtils(this, "SaveParameter",Context.MODE_PRIVATE);
		String key = utils.getString("read_guide", null);
		
		if (key == null) {
			Intent intent = new Intent(BookReadActivity.this,GuideActivity.class); //引导页
			intent.putExtra("read_flag", "true");
			BookShelf.jumpFlag = true;
			utils.putString("read_guide", "true");
			utils.commitOperate();
			startActivity(intent);

		}
	}
	/**
	 * 初始化
	 */
	public void init()
	{
		  setNavigationBarVisibility(false);
		  setNavigationBarFloating(true);
		 // collect = (Button) this.findViewById(R.id.collection);
		  NavigationBarbuilder = this.getNavigationBarBuilder();
		  NavigationBarbuilder.setCommand("书签",  new OnClickListener()
	        {
	            public void onClick(View v)
	            {   
	            	boolean isExitBookMark = bookMarkPages.indexOf(String.valueOf(pagefactory.currentPage + 1)) != -1 ;// 判断是否有书签
	    			isBookMark(isExitBookMark);
	            }    
	        });
		
		  
		  NavigationBarbuilder.setOnBackListener(new NavigationBar.OnBackListener() {
			public void onBack() {
				finish();
			}
		});
		  Book Currentbook = BookShelf.mCurrentBook;
		  NavigationBarbuilder.setTitle(Currentbook.Book_Name);
		 //setText(book.Book_Name)
		isExistActivity = true;
		boodRead_parent  = (RelativeLayout) this.findViewById(R.id.boodRead_parent);
		img_bookmark = (ImageView) this.findViewById(R.id.img_bookmark);
		tvPageNumber = (TextView) this.findViewById(R.id.tvPageNumber);
		linRead = (LinearLayout) this.findViewById(R.id.linRead);
		changeMode = (Button)findViewById(R.id.changemode);
		
		changeMode.setOnClickListener(floorToolOnClickListener);
		//读取当前模式 
		SharedPreferences modelSetting = getSharedPreferences(Util.MODELSETTING,Context.MODE_PRIVATE);
		String model = modelSetting.getString(Util.MODEL, "");
		if (!model.equals("")) {
			this.model = Boolean.parseBoolean(model);
		}
		if(Configuration.ReadBackGroudUseColor)
		{
			if(this.model)
			{
				linRead.setBackgroundColor(BookPageFactory.m_backColorDay);
				changeMode.setBackgroundResource(R.drawable.day);
			}else {
				linRead.setBackgroundColor(BookPageFactory.m_backColorNight);
				changeMode.setBackgroundResource(R.drawable.night);
			}
		}else {
			if(this.model)
			{
				linRead.setBackgroundResource(R.drawable.page);
			}else {
				linRead.setBackgroundResource(R.drawable.page_a);
			}
		}
		
        //TY:liushan delete start
		//bookRead_title_tool = (LinearLayout) this.findViewById(R.id.bookRead_title_tool); // 上面工具栏
		//TY:liushan delete end
		bookRead_floor_tool = (RelativeLayout) this.findViewById(R.id.bookRead_floor_tool); // 下面工具栏
		collectionLayout = (RelativeLayout) this.findViewById(R.id.collectionlayout);
		listLayout = (RelativeLayout) this.findViewById(R.id.listlayout);
		lightlessLayout = (RelativeLayout) this.findViewById(R.id.lightlesslayout);
		fontLayout = (RelativeLayout) this.findViewById(R.id.fontlayout);
		modeLayout = (RelativeLayout) this.findViewById(R.id.modelayout);
		modeLayout.setOnClickListener(floorToolOnClickListener);
		collectionTextView = (TextView) this.findViewById(R.id.collectiontext);
		pageNumber = (LinearLayout) this.findViewById(R.id.pageNumber); // 页码气泡
		progress_bar_font_size = (SeekBar) this.findViewById(R.id.progress_bar_font_size); // 进度条
		progress_bar_font_size.setOnSeekBarChangeListener(onSeekBarChangeListener);
		/*
		luminancePlus = (Button) this.findViewById(R.id.luminancePlus); // 增加亮度
		luminanceReduce = (Button) this.findViewById(R.id.luminanceReduce); // 减少亮度
		fontSizePlus = (Button) this.findViewById(R.id.fontSizePlus); // 加大字体
		fontSizeReduce = (Button) this.findViewById(R.id.fontSizeReduce); // 减少字体
		nightModel = (Button) this.findViewById(R.id.nightModel); // 夜间模式
		dayModel = (Button) this.findViewById(R.id.dayModel); // 白天模式
*/
		//back = (Button) this.findViewById(R.id.back);
		//TY:liushan delete start
		font = (Button)this.findViewById(R.id.front);
        toDirectory = (Button) this.findViewById(R.id.list);
		//addBookMark = (Button) this.findViewById(R.id.addBookMark);
		collect = (Button) this.findViewById(R.id.collection);
		lightMode = (Button)findViewById(R.id.lightless);
		  
		//deleteBookMark = (Button) this.findViewById(R.id.deleteBookMark);
	
        //TY:liushan delete end
		//back.setOnClickListener(titleToolOnClickListener);
		listLayout.setOnClickListener(titleToolOnClickListener);
		//addBookMark.setOnClickListener(titleToolOnClickListener);
		collectionLayout.setOnClickListener(titleToolOnClickListener);
		//deleteBookMark.setOnClickListener(titleToolOnClickListener);

		/*
		luminancePlus.setOnClickListener(floorToolOnClickListener);
		luminanceReduce.setOnClickListener(floorToolOnClickListener);
		fontSizePlus.setOnClickListener(floorToolOnClickListener);
		fontSizeReduce.setOnClickListener(floorToolOnClickListener);
		nightModel.setOnClickListener(floorToolOnClickListener);
		dayModel.setOnClickListener(floorToolOnClickListener);
		*/

		bookRead_floor_tool.setOnClickListener(new View.OnClickListener() { //给title加一个点击不错任何操作的onClick 这样防止误操作关闭工具栏

			@Override
			public void onClick(View v) {

			}
		});
		
		//TY:liushan delete start
		/*
		bookRead_title_tool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
        */
		//TY:liushan delete end
		
		WindowManager.LayoutParams luminancePlus = getWindow().getAttributes();
		luminancePlus.screenBrightness = 0.6f;
		getWindow().setAttributes(luminancePlus);
		
		
		//读取字体
		SharedPreferences sizeSetting = getSharedPreferences(Util.SIZESETTING,
				Context.MODE_PRIVATE);
		String fSzie = sizeSetting.getString(Util.FONTSIZE, "");
		if (!fSzie.equals("")) {
			showSize = Integer.parseInt(fSzie);
		}
		//设置亮度按钮
			final DropDownList droplist = new DropDownList(this);
			BaseAdapter adapter = new MyArrayAdapter();
			droplist.setAdapter(adapter);
			 mDropList = droplist;
			 lightlessLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mDropList.show(v);	
				}
			});

		//设置字号按钮
			final DropDownList droplistfont = new DropDownList(this);
			BaseAdapter adapterfont = new MyArrayAdapterfont();
			droplistfont.setAdapter(adapterfont);
			mDropListfont = droplistfont;
			//font.setOnClickListener(new OnClickListener() {
			fontLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					mDropListfont.show(v);	
				}
			});
			

	}
	
	private void isBookMark(boolean isBookMark){
		if(!isBookMark){
			 //add bookMark
				
				bAddBook = false;
				if (Book.IsExit(book.bookidNet)&& (Book.GetBookSyncStatus(book.bookidNet) != 2)) {
				} else {
					CollectBook();
					isAddToBookShelf = true;
					bookMarks = book.GetBookMarkList(chapter.ChapterIDNet);
					bAddBook = true;
		
				}
				//addBookMark.setVisibility(View.GONE);
				//deleteBookMark.setVisibility(View.VISIBLE);
				Animation bigAnimation = Util.loadAnimation(BookReadActivity.this,R.anim.book_mark_top_in);
				bigBookMark(bigAnimation);
				new Thread() {
					@Override
					public void run() {
						BookMark bookMark = new BookMark();
						bookMark.Pos = String.valueOf(pagefactory.currentPage + 1);
						if(pagefactory.pos.contains(","))
						{
							pagefactory.pos = pagefactory.pos.replaceAll(",", "");
						}
						if(chapter.ChapterName.contains(","))
						{
							chapter.ChapterName =chapter.ChapterName.replaceAll(",", "");
						}
						bookMark.Mark_Text = pagefactory.pos + ","+ chapter.ChapterName + "," + currentIndex;
						bookMark.Book_ID_Net = book.bookidNet;
						bookMark.Chapter_ID_Net = chapter.ChapterIDNet;
						if(book.BookType == 0)
						{
							bookMark.cpcode = book.cpcode;
							bookMark.rpid = book.rpid;
						}
						else {
							bookMark.cpcode = "local";
							bookMark.rpid = "local";
						}

						bookMark.Date = Util.getBookMarkDate();
						if(bAddBook)
						{
							boolean bFindBookMark = false;
							for (BookMark Mark : bookMarks) {

								if(Mark.Pos.compareTo(bookMark.Pos) == 0)
								{
									bFindBookMark = true;
									break;
								}
							}
							if(!bFindBookMark)
							{
								book.AddBookMark(bookMark, chapter.ChapterIDNet);
								bookMarks.add(bookMark);
							}
						}else{
							book.AddBookMark(bookMark, chapter.ChapterIDNet);
							bookMarks.add(bookMark);
						}

						refreshBookMark();
						isExitsBook = true;
						super.run();
					}

				}.start();
			}
			else{
				new Thread() {

					@Override
					public void run() {
						BookMark b = null;
						int count = 0;
						List<BookMark> ls = new ArrayList<BookMark>();
						for (BookMark bookMark : bookMarks) {
							if (Integer.parseInt(bookMark.Pos) == pagefactory.currentPage + 1) {
								count++;
								b = bookMark;
								ls.add(b);
								LogEx.Log_D("delete", count + "");

							}
						}
						for (int i = 0; i < ls.size(); i++) { //排除相同的书签
							b = ls.get(i);
							book.DeleteBookMark(b);
							bookMarks.remove(b);
						}
						refreshBookMark();
						isAddToBookShelf = false;
						super.run();
					}

				}.start();
				closeBookMark();//关闭大书签
	}
	}
	private void SyncData()
	{
		if (NetUtil.checkNetwork(this)) {
			mHandler.sendEmptyMessage(12);
			new Thread(new Runnable() {

				@Override
				public void run() {
					syncThread = new SyncThread(new SyncThread.SyncCallback() {
						
						@Override
						public void onSuccess() {
							if(syncDataDialog!=null && syncDataDialog.isShowing())
							{
								syncDataDialog.dismiss();
								syncDataDialog = null;
							}
								
							mHandler.sendEmptyMessage(13);
							
						}
						
						@Override
						public void onFail() {
							
						}
					},getApplicationContext());
					if (syncThread.IsStop()) {
						syncThread.StartThread();
					}
					
				}
			}).start();
		}
	}
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == RefreshTool)
			{    Log.v("liushan","msg.what: "+msg.what);
				// bookRead_title_tool.postInvalidate(); // 标题工具栏
			    // NavigationBarbuilder.hideCloudView();
				// setNavigationBarVisibility(true);
				 //setNavigationBarVisibility(false);
				bookRead_floor_tool.postInvalidate(); 
				 
			}else if(msg.what == 10)
			{
				GetUserInforError();
			}
			else if(msg.what == 11)
			{
				SyncData();
			}else if(msg.what == 12)
			{
				Log.d("BookReadActivity","---------syncData");
				syncDataDialog = new ProgressDialog(BookReadActivity.this);
				syncDataDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				syncDataDialog.setTitle(R.string.sync_data);
				syncDataDialog.setMessage(getString(R.string.sync_data_loading));
				syncDataDialog.setIndeterminate(false);
				syncDataDialog.setIcon(R.drawable.icon);
				syncDataDialog.setCancelable(false);
				syncDataDialog.setButton(getString(R.string.setting_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								if (syncThread != null && !syncThread.IsStop()) {
									syncThread.StopThread();
								}
								if(syncDataDialog!=null && syncDataDialog.isShowing())
									syncDataDialog.dismiss();
							}
						});
				syncDataDialog.show();
			}
			else if(msg.what == 13)
			{
				try{
					new AlertDialog.Builder(BookReadActivity.this)
					.setTitle(R.string.sync_data_update)
					.setMessage(R.string.sync_data_update_complete)
					.setNegativeButton(R.string.setting_enter, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							BookReadActivity.this.finish();
						}
						
					})
					
					.create().show();
				}catch (Exception e) {
				}
			}
			else if(msg.what == 14)
			{
				InitTryDialog((String)msg.obj);
			}else if(msg.what == 15)
			{
				if(bTopage)
				{
					toPage();
				}
				else
				{
					GetChapterContentFromNet(bPageing);
				}
			}
			super.handleMessage(msg);
		}
	};
	//更新最近阅读
	public void isCanUpdate(boolean bSaveChapter) {
		if (!entranceBookDetail) {
			updateRecent();
			if(bSaveChapter)
			{
				if(book.BookType == 0)
				{
					Chapter.SaveChapters(book.bookidNet, chapters);
				}
				
			}
		} else {
			if (isExitsBook) {
				updateRecent();
				if(book.BookType == 0)
				{
					Chapter.SaveChapters(book.bookidNet, chapters);
				}
			}
		}
	}

	/**
	 * 刷新bookmark
	 */
	public void refreshBookMark() {
		bookMarkPages="";
		bookMarkPages.trim();
		if (bookMarks.size() != 0) {
			for (BookMark bookMark : bookMarks) {
				String[] markText = bookMark.Mark_Text.split(",");
				int bm = Integer.parseInt(markText[AddBookMark.currIndex]);
				if (bm == currentIndex) {
					bookMarkPages+=bookMark.Pos+",";
				}
			}
		}
	}

	@Override
	protected void onRestart() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//获取 书签
		chapters = book.GetChapterList();
		if (chapters.size() != 0 && chapters != null) {
			
			if(pagefactory!=null){
				if(currentIndex+1>chapters.size())
				{
					currentIndex-=1;
				}
				if(currentIndex<=0)
				{
					currentIndex=0;
				}
				
				if (chapters.get(currentIndex) != null){
					bookMarks = book.GetBookMarkList(chapters.get(currentIndex).ChapterIDNet);
					refreshBookMark();
					isExistBookMark();
				}
			}
		}
		
		super.onRestart();
	}
	/**
	 * 排除相同的书签
	 * 
	 * @author encore.liang
	 */
	public void bookMarkReset() {
		List<BookMark> ls = new ArrayList<BookMark>();
		int count=0;
		BookMark b = null;
		for (BookMark bookMark : bookMarks) {
			if (Integer.parseInt(bookMark.Pos) == pagefactory.currentPage + 1) {
				count++;
				b = bookMark;
				ls.add(b);
				LogEx.Log_D("delete", count + "");

			}
		}
		for (int i = 0; i < ls.size()-1; i++) { //排除相同的书签
			b = ls.get(i);
			book.DeleteBookMark(b);
			bookMarks.remove(b);
		}
	}
	/**
	 * 更新最近阅读
	 */
	public void updateRecent() // 更新最近阅读
	{
		if (book != null && chapter != null) {
			if(pagefactory!=null)
			{
				book.Recent_Chapter_Name = chapter.ChapterName; // 章节名称
				book.Recent_Chapter_ID = currentIndex + ""; // 第几章
				book.Recent_Chapter = (pagefactory.currentPage + 1) + ""; // 第几页
				book.RecentReadTime  = System.currentTimeMillis();
				book.UpdateRecent();
			}
		}
	}
	/**
	 * 整本付费弹出的对话框
	 * @param pageing
	 */
	public void initDialogBook(final boolean pageing) {
			ProcessDialog.Dismiss();
		// 弹出对话框,
		try {
			builder = new AlertDialog.Builder(BookReadActivity.this);
			builder.setTitle(R.string.bookread_pay);
			if (chapters != null && chapters.size() != 0) {
				chapter = chapters.get(currentIndex);
				if (chapter != null){
					String msg = null;
					if(PayInRedPacket!=null){
						msg = getString(R.string.bookread_future_see)+ book.Book_Name + getString(R.string.bookread_all_pay)+ BookReadActivity.Price + getString(R.string.bookread_rp_pay);
						MessageFormat form = new MessageFormat(msg);

						String[] args = new String[] { PayInRedPacket };

						msg = form.format(args);
					}else{
						msg = getString(R.string.bookread_future_see)+ book.Book_Name + getString(R.string.bookread_all_pay)+ BookReadActivity.Price + getString(R.string.bookread_where_pay);
					}
					builder.setMessage(msg);
				}
			}
			builder.setPositiveButton(getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							builder = null;
							PayByKpayAccount(pageing, "0", null,BookReadActivity.Price,BookReadActivity.PayInRedPacket);

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
//							builder = null;
//							if (entrance.equals(Util.CHAPTER)
//									|| entrance.equals(Util.BOOKMARK)) {
//								BookReadActivity.this.finish();
//							}
//							if (isLeftOrRight) // 下一章
//							{
//								currentIndex--; // 如果取消 -1
//								if (currentIndex < 0) {
//									currentIndex = 0;
//								}
//								if (chapter != null && chapters != null)
//									chapter = chapters.get(currentIndex);
//							} else { // 上一章
//								currentIndex++;
//								if (currentIndex + 1 > chapters.size()) {
//									currentIndex = chapters.size() - 1;
//								}
//								if (chapter != null && chapters != null)
//									chapter = chapters.get(currentIndex);
//							}
//							isCanUpdate(false);
							finish();
						}
					});
			builder.setCancelable(true);
			builder.create().show();
		} catch (Exception e) {
		}
	
	}
	/**
	 * 章节付费
	 * @param pageing
	 */
	public void initDialogChapter(final boolean pageing) {
			ProcessDialog.Dismiss();
		// 弹出对话框,
		try {
			builder = new AlertDialog.Builder(
					BookReadActivity.this);
			builder.setTitle(R.string.bookread_pay_content);
			if (chapters != null && chapters.size() != 0) {
				if (chapters.get(currentIndex) != null)
					builder.setMessage(getString(R.string.bookread_pay_chapter)
							+ BookReadActivity.Price
							+ getString(R.string.bookread_where_pay));
			}
			builder.setPositiveButton(getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							builder = null;
							PayByKpayAccount(pageing,chapters.get(currentIndex).ChapterIDNet,chapters.get(currentIndex).ChapterName,BookReadActivity.Price,PayInRedPacket);

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							builder = null;
							if (entrance.equals(Util.CHAPTER)|| entrance.equals(Util.BOOKMARK)) {
								BookReadActivity.this.finish();
							}
							if (isLeftOrRight) // 下一章
							{
								currentIndex--; // 如果取消 -1
								if (currentIndex < 0) {
									currentIndex = 0;
								}
								if (chapter != null && chapters != null)
									chapter = chapters.get(currentIndex);
							} else { // 上一章
								currentIndex++;
								if (currentIndex + 1 > chapters.size()) {
									currentIndex = chapters.size() - 1;
								}
								if (chapter != null && chapters != null)
									chapter = chapters.get(currentIndex);
							}
							isCanUpdate(false);
						}
					});
			builder.setCancelable(false);
			builder.create().show();
		} catch (Exception e) {
		}

	}
	/**
	 * 获取章节内容方法
	 * @param currentIndex 当前章节index
	 */
	public void getChapterContent(int currentIndex) {
		
		if (currentIndex>=0 && currentIndex<chapters.size()) {
			chapter = chapters.get(currentIndex);
			if(chapter!=null){
				if (ChapterContent.IsExitInLoacal(chapter.ChapterIDNet,book.bookidNet)) { //如果过本地存在书籍
					mChapterContent = ChapterContent.GetChapterContentFromDB(chapter.ChapterIDNet, book.bookidNet,book.BookType);
					if (mChapterContent != null) {
						initBook(width, height, showSize,BookPageFactory.m_textColor); // 初始化书籍
					} 
					ProcessDialog.Dismiss();
					chapter.UpdateChapterRead(book.bookidNet);
					loadingData = false;
				} else {
					if (NetUtil.checkNetwork(BookReadActivity.this)) {
						GetChapterContentFromNet(false);
					} else {
						Toast.makeText(BookReadActivity.this,R.string.bookread_not_network, 1).show();
						ProcessDialog.Dismiss();
					}
				}
			}
		} else {
			Toast.makeText(BookReadActivity.this,R.string.bookread_not_content, 1).show();
		}
		loadingData = false;
	}
	/**
	 * 从网络获取章节内容方法
	 * @param pageing
	 */
	private void GetChapterContentFromNet(final boolean pageing) {
		loadingData = true;
		bTopage = false;
		bPageing = pageing;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (currentIndex<0 && currentIndex>=chapters.size()) {
					return;
				}
				ChapterContent.GetChapterContent(book.cpcode, book.rpid,book.bookidNet,chapters.get(currentIndex).ChapterIDNet,
						new ChapterContent.GetChapterContentCallback() {

							@Override
							public void onFail(String msg) {
								// Toast.makeText(BookReadActivity.this, msg,
								// 1).show();
								LogEx.Log_V("GetChapterContent","GetChapterContent Fail" + msg);
								ProcessDialog.Dismiss();
								loadingData = false;
							}

							@Override
							public void onSuccess(ChapterContent chaptercontent) {
								if(isExistActivity)
								{
									try {
										if(chaptercontent == null)
										{
											ProcessDialog.Dismiss();
											if(leftOrRight && isFirstEntrance) //下一页
											{
												currentIndex--;
												if(currentIndex < 0 )
													currentIndex = 0;
											}else if(!leftOrRight &&isFirstEntrance)
											{
												currentIndex++;
												if(currentIndex >= chapters.size() )
													currentIndex = chapters.size() -1;
											}
											Toast.makeText(BookReadActivity.this,"该章节内容不存在！", 0).show();
										}
										else {
											mChapterContent = chaptercontent;
											if (pageing) // 如果是翻页触发的
											{
												pageIngInitContent();
											} else {
												initBook(width, height, showSize,BookPageFactory.m_textColor); // 初始化书籍
											}
											ProcessDialog.Dismiss();
											if(chapter!=null && book!=null)
												chapter.UpdateChapterRead(book.bookidNet);
										}
									} catch (Exception e) {
									}finally{
										loadingData = false;
									}
								}
							}

							@Override
							public void onFail(ErrorResult result) {
								loadingData = false;
								if (result.ResultCode == UrlUtil.Order_chapter_not_exist)// 章节不存在
								{
									Toast.makeText(BookReadActivity.this,R.string.bookread_not_exist, 1).show();
								} else if (result.ResultCode == UrlUtil.Order_relation_chapter_not_exist) // 章节购买
								{
									if (isExistActivity)
										initDialogChapter(pageing);
								} else if (result.ResultCode == UrlUtil.Order_relation_book_not_exist) // 整本购买
								{
									if (isExistActivity)
										initDialogBook(pageing);
								}
								else 
								{
									Message message = mHandler.obtainMessage(14, result.ResultMsg);
									mHandler.sendMessage(message);
								}
								ProcessDialog.Dismiss();

							}
						});

			}
		});

	}



	private int progressIndex = 0;
	private boolean isOpenTool = false;
	public OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					pagefactory.currentPage = progressIndex; // 跳转到哪一页
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.printContent(mNextPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					mPageWidget.postInvalidate(); // 刷新ui
					isExistBookMark();	

				}
			});

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (isOpenTool) // 适配阿里云系统
			{
				tvPageNumber.setText((progress + 1) + "/"+ BookPageFactory.totalSize);
				progressIndex = progress;
			}
		}
	};
	
	/**
	 * 大张书签
	 * @param 动画
	 */
	public void bigBookMark(Animation a)
	{
		bigBookMarkFlag = true;
		String path = SDCardUtils.GetBookPath();
		if(path!=null){
			img_bookmark.setBackgroundDrawable(ImgUtil.drawableToBitmap(BitmapFactory.decodeFile(path)));
		}else {
			img_bookmark.setBackgroundResource(R.drawable.bookmark_factory_2);
		}
		img_bookmark.setVisibility(View.VISIBLE);
		img_bookmark.startAnimation(a);
		
	}
	/**
	 * 书签退出动画
	 * @return
	 */
	public boolean closeBookMark()
	{
		if(bigBookMarkFlag)
		{
			new Thread(){

				@Override
				public void run() {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							Animation out = Util.loadAnimation(BookReadActivity.this, R.anim.book_mark_top_out);
							img_bookmark.startAnimation(out);
						}
					});
					try {
						Thread.sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							img_bookmark.setVisibility(View.GONE);
						}
					});
					super.run();
				}
				
			}.start();
			bigBookMarkFlag = false;
			return false;
		}else
		{
			return true;
		}
	}
	
	public OnClickListener titleToolOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			/*
			case R.id.back: //back
				isCanUpdate(true);
				if(mPageWidget != null)
				     mPageWidget.bReturn = true;
				BookReadActivity.this.finish();
				break;
				*/
				
			case R.id.listlayout: // directory activity 
				BookShelf.mCurrentBook = book;
				Intent intent = new Intent();
				intent.setClass(BookReadActivity.this, DirectoryActivity.class);
				startActivity(intent);
				break;
				/*
			case R.id.addBookMark: //add bookMark
				
				bAddBook = false;
				if (Book.IsExit(book.bookidNet)&& (Book.GetBookSyncStatus(book.bookidNet) != 2)) {
				} else {
					CollectBook();
					isAddToBookShelf = true;
					bookMarks = book.GetBookMarkList(chapter.ChapterIDNet);
					bAddBook = true;
		
				}
				addBookMark.setVisibility(View.GONE);
				deleteBookMark.setVisibility(View.VISIBLE);
				
				Animation bigAnimation = Util.loadAnimation(BookReadActivity.this,R.anim.book_mark_top_in);
				bigBookMark(bigAnimation);
				
				collect.setText(R.string.un_collect);
				new Thread() {

					@Override
					public void run() {

						BookMark bookMark = new BookMark();
						bookMark.Pos = String.valueOf(pagefactory.currentPage + 1);
						if(pagefactory.pos.contains(","))
						{
							pagefactory.pos = pagefactory.pos.replaceAll(",", "");
						}
						if(chapter.ChapterName.contains(","))
						{
							chapter.ChapterName =chapter.ChapterName.replaceAll(",", "");
						}
						bookMark.Mark_Text = pagefactory.pos + ","+ chapter.ChapterName + "," + currentIndex;
						bookMark.Book_ID_Net = book.bookidNet;
						bookMark.Chapter_ID_Net = chapter.ChapterIDNet;
						if(book.BookType == 0)
						{
							bookMark.cpcode = book.cpcode;
							bookMark.rpid = book.rpid;
						}
						else {
							bookMark.cpcode = "local";
							bookMark.rpid = "local";
						}

						bookMark.Date = Util.getBookMarkDate();
						if(bAddBook)
						{
							boolean bFindBookMark = false;
							for (BookMark Mark : bookMarks) {

								if(Mark.Pos.compareTo(bookMark.Pos) == 0)
								{
									bFindBookMark = true;
									break;
								}
							}
							if(!bFindBookMark)
							{
								book.AddBookMark(bookMark, chapter.ChapterIDNet);
								bookMarks.add(bookMark);
							}
						}else{
							book.AddBookMark(bookMark, chapter.ChapterIDNet);
							bookMarks.add(bookMark);
						}

						refreshBookMark();
						isExitsBook = true;
						super.run();
					}

				}.start();
				break;

			case R.id.deleteBookMark:

				deleteBookMark.setVisibility(View.GONE);
				addBookMark.setVisibility(View.VISIBLE);
				deleteBookMark.setEnabled(true);
				new Thread() {

					@Override
					public void run() {
						BookMark b = null;
						int count = 0;
						List<BookMark> ls = new ArrayList<BookMark>();
						for (BookMark bookMark : bookMarks) {
							if (Integer.parseInt(bookMark.Pos) == pagefactory.currentPage + 1) {
								count++;
								b = bookMark;
								ls.add(b);
								LogEx.Log_D("delete", count + "");

							}
						}
						for (int i = 0; i < ls.size(); i++) { //排除相同的书签
							b = ls.get(i);
							book.DeleteBookMark(b);
							bookMarks.remove(b);
						}
						refreshBookMark();
						isAddToBookShelf = false;
						super.run();
					}

				}.start();
				closeBookMark();//关闭大书签
				break;
*/
			case R.id.collectionlayout: //收藏
				
				//if (entranceBookDetail) {

					//if (Book.IsExit(book.bookidNet)&& (Book.GetBookSyncStatus(book.bookidNet) != 2)) {
					//	Toast.makeText(BookReadActivity.this,R.string.bookread_yes_shlef, 1).show();
						//isAddToBookShelf = true;
					//} else {
								CollectBook();
								//collect.setText(R.string.un_collect);
								collect.setBackgroundResource(R.drawable.collectionn);
								collectionLayout.setClickable(false);
								collectionTextView.setText("已收藏");
								isAddToBookShelf = true;
					//}
				//} else {
					//Toast.makeText(BookReadActivity.this,
					//		R.string.bookread_yes_shlef, 1).show();
					//isAddToBookShelf = true;
				//}
				break;

				//TY:liushan delete end
			}
		}
	};

	public OnClickListener floorToolOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Message msg = new Message();
			switch (v.getId()) {
			case R.id.modelayout:
				if(model){
					    changeMode.setBackgroundResource(R.drawable.night);
					  
						Toast.makeText(BookReadActivity.this,"黑夜模式", 0).show();
						nightModel();
				}else{
					    changeMode.setBackgroundResource(R.drawable.day);
					    Toast.makeText(BookReadActivity.this,"白天模式", 0).show();
					    dayModel();
				}
			//case R.id.modelayout:
				//break;
			/*
			case R.id.luminancePlus: // 增加亮度
				WindowManager.LayoutParams luminancePlus = getWindow().getAttributes();
				if(defaultLuminance<1.0f)
					defaultLuminance += 0.2;
				if(defaultLuminance==1.0f)
				{
					if(isLuminanceToast==null)
					{
						isLuminanceToast = true;
						Toast.makeText(BookReadActivity.this,"最大亮度", 0).show();
					}else
					{
						if(!isLuminanceToast)
						{
							Toast.makeText(BookReadActivity.this,"最大亮度", 0).show();
							isLuminanceToast = true;
						}
					}
					
				}else {
					isLuminanceToast = null;
					luminancePlus.screenBrightness = defaultLuminance;
					getWindow().setAttributes(luminancePlus);
				}
				break;
			case R.id.luminanceReduce: // 降低亮度
				WindowManager.LayoutParams luminanceReduce = getWindow().getAttributes();
				if (defaultLuminance >= 0.4f)
					defaultLuminance -= 0.2;
				if(defaultLuminance<=0.4f)
				{
					if(isLuminanceToast==null)
					{
						isLuminanceToast = false;
						Toast.makeText(BookReadActivity.this,"最小亮度", 0).show();
					}else
					{
						if(isLuminanceToast)
						{
							Toast.makeText(BookReadActivity.this,"最小亮度", 0).show();
							isLuminanceToast = false;
						}
					}
					
				}else
				{
					isLuminanceToast = null;
					luminanceReduce.screenBrightness = defaultLuminance;
					getWindow().setAttributes(luminanceReduce);
				}
				
				break;
			case R.id.fontSizePlus:
				msg.what = 1;
				fontHandler.sendMessage(msg);
				break;
			case R.id.fontSizeReduce:
				msg.what = 2;
				fontHandler.sendMessage(msg);
				break;
			case R.id.nightModel:
				if(isShowToastModel==null)
				{
					Toast.makeText(BookReadActivity.this,"黑夜模式", 0).show();
					isShowToastModel = true;
				}else
				{
					if(!isShowToastModel)
					{
						Toast.makeText(BookReadActivity.this,"黑夜模式", 0).show();
						isShowToastModel = true;
					}
				}
				nightModel();
				break;
			case R.id.dayModel:
				if(isShowToastModel==null)
				{
					isShowToastModel = false;
					Toast.makeText(BookReadActivity.this,"白天模式", 0).show();
				}else
				{
					if(isShowToastModel)
					{
						Toast.makeText(BookReadActivity.this,"白天模式", 0).show();
						isShowToastModel = false;
					}
				}
				
				dayModel();
				break;
				*/
			}

		}
	};
	
	/**
	 * 计算书签
	 */
	public void accountBookMark() {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < pagefactory.getPageList().size(); i++) {
			for (String str : pagefactory.getPageList().get(i)) {
				sBuffer.append(str);
			}

			for (int j = 0; j < bookMarks.size(); j++) {
				BookMark bookMark = bookMarks.get(j);
				String[] markText = bookMark.Mark_Text.split(",");
				if (Integer.parseInt(markText[AddBookMark.currIndex]) == currentIndex) {
					if (sBuffer.indexOf(markText[AddBookMark.mark_text]) != -1) {
						bookMark.Pos = String.valueOf(i + 1);
						bookMark.Mark_Text = markText[AddBookMark.mark_text]+ "," + chapter.ChapterName + ","+ currentIndex;
						bookMark.Update();
					}
				}
			}
			sBuffer.setLength(0);
		}

	}
	/**
	 * 计算书签
	 */
	public void accountBm()
	{
		new Thread() {

			@Override
			public void run() {
				accountBookMark(); // 计算书签
				refreshBookMark(); // 刷新书签
				bookMarkReset();
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						isExistBookMark(); // 判断当前页是否有书签
					}
				});
				super.run();
			}

		}.start();
		// pageNumber.setVisibility(View.GONE);
		
		Map<String, String> maps = new HashMap<String, String>();
		maps.put(Util.FONTSIZE, String.valueOf(showSize));
		Util.saveSharedPreferences(getApplicationContext(),Util.SIZESETTING, maps);
	}
	
	
	private Handler fontHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						showSize += 2;
						if (showSize > maxSize) {
							if(isFontSizeToast==null)
							{
								isFontSizeToast = false;
								Toast.makeText(BookReadActivity.this,R.string.bookread_size_max, 0).show();
							}else
							{
								if(isFontSizeToast)
								{
									Toast.makeText(BookReadActivity.this,R.string.bookread_size_max, 0).show();
									isFontSizeToast = false;
								}
							}
							showSize = maxSize;
						} else {
							isFontSizeToast = null;
							pagefactory.changeTextSize(showSize, mPageWidget,mCurPageCanvas, mCurPageBitmap);
							progress_bar_font_size.setMax((BookPageFactory.totalSize - 1));
						}
						accountBm();
						
					}
				});
				
				
				
				break;
			case 2:
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						showSize -= 2;
						if (showSize < minSize) {
							if(isFontSizeToast==null)
							{
								isFontSizeToast = true;
								Toast.makeText(BookReadActivity.this,R.string.bookread_default_size, 0).show();
							}else
							{
								if(!isFontSizeToast)
								{
									Toast.makeText(BookReadActivity.this,R.string.bookread_default_size, 0).show();
									isFontSizeToast = true;
								}
							}
							
							showSize = minSize;
						} else {
							isFontSizeToast = null;
							pagefactory.changeTextSizeReduce(showSize,mPageWidget, mCurPageCanvas,mCurPageBitmap);
							progress_bar_font_size.setMax((BookPageFactory.totalSize - 1));
						}
						accountBm();
					}
				});
				
				break;
			case 3:
				if(pagefactory!=null){
					pagefactory.printContent(mCurPageCanvas);
					mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
					mPageWidget.postInvalidate();
				}
				break;
			}
			super.handleMessage(msg);
		}

	};
	
	/**
	 * 是否存在bookMark
	 */
	public void isExistBookMark() {
		bookMarkPages.trim();
		if(bookMarkPages.length()!=0 && pagefactory!=null){
			if (bookMarkPages.indexOf(String.valueOf(pagefactory.currentPage + 1)) != -1) // 判断是否有书签
			{
				bookMarkCurrentIndex = pagefactory.currentPage + 1 + "";
				//deleteBookMark.setVisibility(View.VISIBLE);
				//addBookMark.setVisibility(View.GONE);
			} else {
				//deleteBookMark.setVisibility(View.GONE);
				//addBookMark.setVisibility(View.VISIBLE);
				bookMarkCurrentIndex = "";
			}
		}else {
			//deleteBookMark.setVisibility(View.GONE);
			//addBookMark.setVisibility(View.VISIBLE);
			bookMarkCurrentIndex = "";
		}
	}



	public String getCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		return format.format(new Date());
	}

	/**
	 * 
	 * @param width mobile phone width
	 * @param height mobile phone height
	 * @param textSize font size
	 * @param textColor font Color
	 */
	public void initBook(int width, int height, int textSize, int textColor) {
		mPageWidget = new PageWidget(BookReadActivity.this, width, height,mHandler);
		pagefactory = new BookPageFactory(this,width, height, textSize,textColor);
		
		mPageWidget.isFirst = true;
		
		if (bookMarkCurrentPage != 0) {
			pagefactory.currentPage = bookMarkCurrentPage;
			bookMarkCurrentPage = 0;
		}
		
		if (bookMarks.size() != 0 && bookMarks != null) {
			refreshBookMark();
			isExistBookMark();// 判断是否存在bookMark
		}
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mCurPageCanvas.drawColor(Color.WHITE);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		mNextPageCanvas.drawColor(Color.WHITE);

		
		if(Configuration.ReadBackGroudUseColor)
		{
			pagefactory.setBgBitmap(null,model);
		}
		else {
			Bitmap bg = null;
			if(model)
			{
				bg = BitmapUtil.getBitmapFromRes(this,R.drawable.page);
			}
			else {
				bg = BitmapUtil.getBitmapFromRes(this,R.drawable.page_a);
			}
			pagefactory.setBgBitmap(bg,model);
		}

		
		if (chapters.size() != 0) { //如果有章节信息
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					pagefactory.openbook(book, chapters.get(currentIndex),mChapterContent);
					pagefactory.onDraw(mCurPageCanvas);
					pagefactory.printContent(mNextPageCanvas);
					readIsFrist = false;
					if (pagefactory.getPageList() != null) {
						if (pagefactory.getPageList().size() != 0) {
							pagefactory.pos = pagefactory.getPageList().get(pagefactory.currentPage).get(0);// 记录当前页的第一行文字;
							progress_bar_font_size.setMax((BookPageFactory.totalSize - 1));
							mPageWidget.setBitmaps(mCurPageBitmap,mCurPageBitmap);
							mPageWidget.setOnTouchListener(onTouchListener);
							linRead.removeAllViews();
							linRead.addView(mPageWidget);

						}
					}
				}
			});
		} else {
			Toast.makeText(BookReadActivity.this,R.string.bookread_not_content, 1).show();
		}
		/**
		 * 每分钟刷新一次时间
		 */
		new Thread() {

			@Override
			public void run() {
				try {
					while (flag) {
						Thread.sleep(60000);
						Message msg = new Message();
						msg.what = 3;
						fontHandler.sendMessage(msg);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				super.run();
			}

		}.start();
		/**
		 * 处理 黑夜或白天模式
		 */
		if(this.model)
		{
			dayModelResource();
		}else {
			nightModelResource();
		}
	}
	
	
	//上一页
	public boolean DragToRight()
	{
		isFirstEntrance = true; 
		leftOrRight = false;
		isPageing = true;
			try {
				pagefactory.prePage();
				isExistBookMark();// 判断是否存在bookMark
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (pagefactory.isfirstPage()) { 
				fristFlag = true;
				currentIndex--;
				if (currentIndex < 0) {
					mPageWidget.SetDrawAble(false);
					if (isPageing) {
						mPageWidget.isFirst = false;
					} else {
						mPageWidget.isFirst = true;
					}
					currentIndex = 0;
					Toast.makeText(BookReadActivity.this,R.string.bookread_has_first, 0).show();
					return false;
				} else {
					mPageWidget.mTouchToCornerDis = 50; //触发翻页的关键
					mPageWidget.SetDrawAble(true);
					isLeftOrRight = false; // 上一页的标志
					mHandler.post(new Runnable() {
		
						@Override
						public void run() {
							isToNetWorkGetContent = toPage(); // 上一页
							chapter.UpdateChapterRead(book.bookidNet);
							// mPageWidget.setBitmaps(mCurPageBitmap,mNextPageBitmap);
						}
					});
					
				}
			} else {
				pagefactory.onDraw(mNextPageCanvas);
			}
			return true;
	}
	//下一页
	public boolean DragToLeft()
	{
		isFirstEntrance = true; 
		leftOrRight = true;
		isPageing = true;
		try {
			pagefactory.nextPage();
			isExistBookMark();// 判断是否存在bookMark
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (pagefactory.islastPage()) {
			
			currentIndex++;
			if (currentIndex + 1 > chapters.size()) {
				mPageWidget.SetDrawAble(false);
				currentIndex = chapters.size() - 1;
				Toast.makeText(BookReadActivity.this,R.string.bookread_has_last, 1).show();
				return false;
			} else {
				mPageWidget.mTouchToCornerDis = 50; //模拟翻页的触摸点
				mPageWidget.SetDrawAble(true);
				isLeftOrRight = true; // 下一页的标志
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						isToNetWorkGetContent = toPage(); // 下一页
						chapter.UpdateChapterRead(book.bookidNet);
					}
				});
			
				
			}

		} else {
			pagefactory.onDraw(mNextPageCanvas);
		}
		return true;
	}
	//没有做翻页
	public void onUp()
	{
		if (!isMiddle && !isCloseTool) {
			if (!mPageWidget.canDragOver()) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						if (!leftOrRight) // 左翻
						{
							try {
								pagefactory.nextPage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							isExistBookMark();// 判断是否存在bookMark
							pagefactory.printContent(mNextPageCanvas);
						} else {
							try {
								pagefactory.prePage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							isExistBookMark();// 判断是否存在bookMark
							pagefactory.printContent(mNextPageCanvas);
						}
					}
				});
			}
		}
	}
    //显示工具栏
	public void showTool()
	{
		//TY:liushan delete start
		//bookRead_title_tool.setVisibility(View.VISIBLE);
		//TY:liushan delete end
		Log.v("liushan","showTool");
	
		bookRead_floor_tool.setVisibility(View.VISIBLE);
		setNavigationBarVisibility(!isNavigationBarVisible());
	}
	
	public void hideTool()
	{
		//TY:liushan delete start
		//bookRead_title_tool.setVisibility(View.GONE);
		//TY:liushan delete end
		Log.v("liushan","hideTool");
		setNavigationBarVisibility(!isNavigationBarVisible());
		bookRead_floor_tool.setVisibility(View.GONE);
	}
	//页码赋值
	public void toolPageNumber()
	{
		
		tvPageNumber.setText((progress_bar_font_size.getProgress() + 1)+ "/" + BookPageFactory.totalSize);
	}

	
	private boolean flag = true;
	private boolean fristFlag = false;
	public OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			
			boolean ret = false;
			int wXLeft = (BookReadActivity.width / 2) - 100;
			int wXRight = (BookReadActivity.width / 2) + 100;
			int hYTop = 150;
			int hYBottom = BookReadActivity.height -150;
			if (v == mPageWidget) {

				if (e.getAction() == MotionEvent.ACTION_DOWN) {
					if(isGetChapter)//正在获取章节不能点
					{ 
						return false;
					}
					if(!closeBookMark())
					{
						return false;
					}
					if ((e.getX() >= wXLeft && e.getX() <= wXRight)&& (e.getY() >= hYTop && e.getY() <= hYBottom))// 点击了屏幕中间
					{
						progress_bar_font_size.setProgress(pagefactory.currentPage);
						if (isMiddle) {
							isMiddle = false;
							isCloseTool = true; // 第一次关闭不用做翻页效果
							isOpenTool = false;
							mPageWidget.SetDrawAble(true);
							hideTool(); //隐藏工具栏
						} else {
							isMiddle = true; // 已弹出工具栏
							isOpenTool = true;
							mPageWidget.SetDrawAble(false);
							toolPageNumber();
							showTool();
							getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //处理sdk屏幕缩下来的bug
						}
					} else { // 翻页
						if (isMiddle) { // 如果已打开工具栏,就关闭
							isCloseTool = true;// 第一次关闭不用做翻页效果
							isMiddle = false; 
							isOpenTool = false;
							mPageWidget.SetDrawAble(true);
							hideTool();
						} else {
								if (Math.abs(mClickTime- System.currentTimeMillis()) < 500) {  //翻页必须间隔500毫秒
									return false;
								}
								
								mPageWidget.mTouchToCornerDis = 50;
								leftOrRight = false;
								mPageWidget.isFirst = false;
								mPageWidget.SetDrawAble(true);
								mClickTime = System.currentTimeMillis();
								
								
								mPageWidget.abortAnimation();
								mPageWidget.calcCornerXY(e.getX(), e.getY());
								pagefactory.onDraw(mCurPageCanvas);
								mPageWidget.SetFlyFlag(true);
								
								if (mPageWidget.DragToRight()) { //上一页
									boolean f =  DragToRight();
									if(!f)
									{
										return f;
									}
								} else { //下一页
									boolean f =  DragToLeft();
									if(!f)
									{
										return f;
									}
								}
							mPageWidget.setBitmaps(mCurPageBitmap,mNextPageBitmap);
							isMiddle = false;
							isCloseTool = false;
						}
					}

				} else if (e.getAction() == MotionEvent.ACTION_UP) { //没翻页
					onUp();
				}
				if (!isMiddle && !isCloseTool) {
					ret = mPageWidget.doTouchEvent(e);
				}
				return ret;
			}
			return false;
		}
	};

	/**
	 * 下一章或上一章
	 */
	public boolean toPage() {

		bTopage = true;
		chapter = chapters.get(currentIndex);
		
		if (ChapterContent.IsExitInLoacal(chapter.ChapterIDNet, book.bookidNet)) {
			mChapterContent = ChapterContent.GetChapterContentFromDB(chapter.ChapterIDNet, book.bookidNet,book.BookType);
			if (mChapterContent != null&& mChapterContent.ChapterContent.length() != 0) {

				pageIngInitContent();// 填充内容
				//获取 书签
				if (chapters.size() != 0 && chapters != null) {
					if (chapters.get(currentIndex) != null)
						bookMarks = book.GetBookMarkList(chapters.get(currentIndex).ChapterIDNet);
						LogEx.Log_I(TAG, "book mark size" + bookMarks.size());
						refreshBookMark();
						isExistBookMark();
				}
				chapter.UpdateChapterRead(book.bookidNet);
				loadingData = false;
			}
			return false;

		} else {
			isGetChapter = true; //正在获取章节标识
			isRunning = true;
			loadingData = true;
			toast = Toast.makeText(BookReadActivity.this, "正从网络获取章节数据...请稍后...", 0);
			timer = new Timer(); 
			timer.schedule(new TimerTask(){ 

			    @Override 
			    public void run() { 
			        while(isRunning){ 
			            toast.show(); 
			        } 

			    } 
			}, 10);

			ChapterContent.GetChapterContent(book.cpcode, book.rpid,
					book.bookidNet, chapters.get(currentIndex).ChapterIDNet,
					new ChapterContent.GetChapterContentCallback() {

						@Override
						public void onFail(String msg) {
							loadingData = false;
							ProcessDialog.Dismiss();
							isRunning = false;
							isGetChapter = false;
							if(toast!=null)
								toast.cancel(); 
						}

						@Override
						public void onSuccess(ChapterContent chaptercontent) {
							if(isExistActivity)
							{
								try {
									isGetChapter = false;
									if(toast!=null)
										toast.cancel(); 
									isRunning = false;
									if(chaptercontent == null)
									{
										ProcessDialog.Dismiss();
										if(leftOrRight && isFirstEntrance) //下一页
										{
											currentIndex--;
											if(currentIndex < 0 )
												currentIndex = 0;
										}else if(!leftOrRight &&isFirstEntrance)
										{
											currentIndex++;
											if(currentIndex >= chapters.size() )
												currentIndex = chapters.size() -1;
										}
										Toast.makeText(BookReadActivity.this,"该章节内容不存在！", 0).show();
									}
									else{
										mChapterContent = chaptercontent;
										pageIngInitContent(); // 填充内容
										//获取 书签
										if (chapters.size() != 0 && chapters != null) {
											if (chapters.get(currentIndex) != null)
												bookMarks = book.GetBookMarkList(chapters.get(currentIndex).ChapterIDNet);
												refreshBookMark();
												isExistBookMark();
										}
										ProcessDialog.Dismiss();
										chapter.UpdateChapterRead(book.bookidNet);
									}
								} catch (Exception e) {
								}finally{
									loadingData = false; //改变是否正在获取内容的标记
								}
								

							}
						}

						@Override
						public void onFail(ErrorResult result) {
							loadingData = false;
							if (result.ResultCode == UrlUtil.Order_chapter_not_exist)// 章节不存在
							{
								Toast.makeText(BookReadActivity.this,R.string.bookread_not_exist, 1).show();
							} else if (result.ResultCode == UrlUtil.Order_relation_book_not_exist) // 整本购买
							{
								initDialogBook(true);
							} else if (result.ResultCode == UrlUtil.Order_relation_chapter_not_exist) // 章节购买
							{
								initDialogChapter(true);
							}
							else 
							{
								Message message = mHandler.obtainMessage(14, result.ResultMsg);
								mHandler.sendMessage(message);
							}

							isGetChapter = false;
							if(toast!=null)
								toast.cancel(); 
							ProcessDialog.Dismiss();
						}
					});
			return true;
		}
	}

	public void nightModel() {
		if (model) {
			model = false;
			nightModelResource();

//			if (OpenBookAnimation.night_bgBitmap == null|| OpenBookAnimation.night_bgBitmap.isRecycled()){
//					OpenBookAnimation.night_bgBitmap = BitmapUtil.getBitmapFromRes(this,R.drawable.page_a);
//			}
//			OpenBookAnimation.bgBitmap.recycle();
//			OpenBookAnimation.bgBitmap = null;
//			OpenBookAnimation.bgBitmap = OpenBookAnimation.night_bgBitmap;//ImgUtil.zoomImg(OpenBookAnimation.night_bgBitmap,width,height,false,BookReadActivity.this);
//			
//			if (OpenBookAnimation.day_bgBitmap != null && !OpenBookAnimation.day_bgBitmap.isRecycled()) {
//				OpenBookAnimation.day_bgBitmap.recycle();
//				OpenBookAnimation.day_bgBitmap = null;
//			}
			if(Configuration.ReadBackGroudUseColor)
			{
				pagefactory.setBgBitmap(null,model);
			}
			else
			{
				Bitmap bg = BitmapUtil.getBitmapFromRes(this,R.drawable.page_a);

				pagefactory.setBgBitmap(bg,model);
			}

			pagefactory.printContent(mCurPageCanvas);
			pagefactory.printContent(mNextPageCanvas);
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.postInvalidate();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put(Util.MODEL, "false");
			Util.saveSharedPreferences(getApplicationContext(),
					Util.MODELSETTING, maps);
			progress_bar_font_size.setMax(BookPageFactory.totalSize - 1);
			progress_bar_font_size.setProgress(pagefactory.currentPage);
		}
	}

	public void nightModelResource() {
//		btn_pageNumber.setBackgroundResource(R.drawable.bar_bg_11);
		linRead.setBackgroundResource(R.drawable.page_a);
		/*
		luminancePlus.setBackgroundResource(R.drawable.btn_luminance_plus_selector);
		luminanceReduce.setBackgroundResource(R.drawable.btn_luminance_reduce_selector); // 减少亮度
		fontSizePlus.setBackgroundResource(R.drawable.btn_fontsize_plus_selector); // 加大字体
		fontSizeReduce.setBackgroundResource(R.drawable.btn_fontsize_reduce_selector); // 减少字体
		nightModel.setBackgroundResource(R.drawable.btn_nightmodel_selector); // 夜间模式
		dayModel.setBackgroundResource(R.drawable.btn_daymodel_selector); // 白天模式
        */
		bookRead_floor_tool.setBackgroundResource(R.drawable.dt);
		//TY:liushan delete start
		//bookRead_title_tool.setBackgroundResource(R.drawable.page_b);
		//TY:liushan delete end
		//back.setBackgroundResource(R.drawable.input_b);
		//back.setTextColor(Color.parseColor("#AAAAAA"));
		//toDirectory.setBackgroundResource(R.drawable.btn_directory_selector);
		//toDirectory.setTextColor(Color.parseColor("#AAAAAA"));
		//addBookMark.setBackgroundResource(R.drawable.btn_boo_mark_selector);
		//collect.setBackgroundResource(R.drawable.input_b);
		//collect.setTextColor(Color.parseColor("#AAAAAA"));
		//deleteBookMark.setBackgroundResource(R.drawable.bookmark_2);
		BookPageFactory.m_textColor = Color.parseColor("#a0a0a0");
		pagefactory.getPaint().setColor(BookPageFactory.m_textColor);
		pagefactory.getTitlePaint().setColor(BookPageFactory.m_textColor);
//		time.setTextColor(BookPageFactory.m_textColor);
	}

	public void dayModelResource() {
//		btn_pageNumber.setBackgroundResource(R.drawable.bar_bg_10);
        /*
		luminancePlus.setBackgroundResource(R.drawable.btn_luminance_plus_selector);
		luminanceReduce.setBackgroundResource(R.drawable.btn_luminance_reduce_selector); // 减少亮度
		fontSizePlus.setBackgroundResource(R.drawable.btn_fontsize_plus_selector); // 加大字体
		fontSizeReduce.setBackgroundResource(R.drawable.btn_fontsize_reduce_selector); // 减少字体
		nightModel.setBackgroundResource(R.drawable.btn_nightmodel_selector); // 夜间模式
		dayModel.setBackgroundResource(R.drawable.btn_daymodel_selector); // 白天模式
		*/
		bookRead_floor_tool.setBackgroundResource(R.drawable.dt);
		//TY:liushan delete start
		//bookRead_title_tool.setBackgroundResource(R.drawable.page_b);
		//TY:liushan delete end
		linRead.setBackgroundResource(R.drawable.page);
		//back.setBackgroundResource(R.drawable.input_a);
		//back.setTextColor(Color.WHITE);
		//toDirectory.setBackgroundResource(R.drawable.btn_directory_selector);
		//toDirectory.setTextColor(Color.WHITE);
		//addBookMark.setBackgroundResource(R.drawable.btn_boo_mark_selector);
		//collect.setBackgroundResource(R.drawable.input_a);
		//collect.setTextColor(Color.WHITE);
		//deleteBookMark.setBackgroundResource(R.drawable.bookmark_1);
		BookPageFactory.m_textColor = Color.parseColor("#2B2B2B");
		pagefactory.getPaint().setColor(BookPageFactory.m_textColor);
		pagefactory.getTitlePaint().setColor(BookPageFactory.m_textColor);
//		time.setTextColor(BookPageFactory.m_textColor);

	}

	public void dayModel() {
		if(!model){
			model = true;
			dayModelResource();
//			if (OpenBookAnimation.day_bgBitmap == null|| OpenBookAnimation.day_bgBitmap.isRecycled()){
//				OpenBookAnimation.day_bgBitmap = BitmapUtil.getBitmapFromRes(this,R.drawable.page);
//			}
//			OpenBookAnimation.bgBitmap.recycle();
//			OpenBookAnimation.bgBitmap = null;
//			OpenBookAnimation.bgBitmap = OpenBookAnimation.day_bgBitmap;// ImgUtil.zoomImg(OpenBookAnimation.day_bgBitmap, width, height,true,BookReadActivity.this);
////			OpenBookAnimation.bgBitmap.recycle();
////			OpenBookAnimation.bgBitmap = null;
//			
//			if (OpenBookAnimation.night_bgBitmap != null&& !OpenBookAnimation.night_bgBitmap.isRecycled()) {
//				OpenBookAnimation.night_bgBitmap.recycle();
//				OpenBookAnimation.night_bgBitmap = null;
//			}
			if(Configuration.ReadBackGroudUseColor)
			{
				pagefactory.setBgBitmap(null,model);
			}
			else
			{
				Bitmap bg = BitmapUtil.getBitmapFromRes(this,R.drawable.page);
				pagefactory.setBgBitmap(bg,model);
			}
			
			pagefactory.printContent(mCurPageCanvas);
			pagefactory.printContent(mNextPageCanvas);
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
			mPageWidget.postInvalidate();
			progress_bar_font_size.setMax(BookPageFactory.totalSize-1);
			progress_bar_font_size.setProgress(pagefactory.currentPage);
			Map<String, String> maps = new HashMap<String, String>();
			maps.put(Util.MODEL, "true");
			Util.saveSharedPreferences(getApplicationContext(), Util.MODELSETTING, maps);
		}

	}

	@Override
	protected void onDestroy() {
		Log.v("liushan","onDestroy");
		flag = false;
		isMiddle = false;
		isCloseTool = false; // 第一次关闭不用做翻页效果
		//TY:liushan delete start
		//bookRead_title_tool.setVisibility(View.GONE);
		setNavigationBarVisibility(!isNavigationBarVisible());
		//TY:liushan delete end
		bookRead_floor_tool.setVisibility(View.GONE);
		
		
		if (pagefactory != null) {
			if (pagefactory.getPageList() != null &&pagefactory.getPageList().size()!=0)
				pagefactory.getPageList().clear();
		}
		if(pagefactory!=null)
			pagefactory.currentPage = 0;
		mCurPageCanvas = null;
		mNextPageCanvas = null;
		isLeftOrRight = true;
		isExistActivity = false;
		pagefactory = null;
//		chapter = null;
		if(book.BookType==1)
		{
			unregisterReceiver(broadcastReceiver);
		}
		mChapterContent = null;
		if(toast!=null)
			toast.cancel(); 
		isRunning = false;
//		if(!mCurPageBitmap.isRecycled())
//		     mCurPageBitmap.recycle();
//		mCurPageBitmap = null;
//		if(!mNextPageBitmap.isRecycled())
//		     mNextPageBitmap.recycle();
//		mNextPageBitmap = null;
		
		OpenBookAnimation.CleanData();
		System.gc();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(loadingData)
		{
			return false;
		}else {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				isCanUpdate(true);

				if(mPageWidget != null)
				   mPageWidget.bReturn = true;
				BookReadActivity.this.setResult(BookShelf.REFRESHCODE);
				BookReadActivity.this.finish();
				System.gc();
			}
			return super.onKeyDown(keyCode, event);
		}
	}


	/**
	 * 翻页首页,末页触发
	 * 
	 * @params 
	 */
	public void pageIngInitContent() {
		pagefactory.getPageList().clear();
		pagefactory.content = mChapterContent.ChapterContent;
		pagefactory.chapter_Name = chapter.ChapterName;
		pagefactory.initContent();
		if (isLeftOrRight)// 如果是下一页 默认是第一页
		{
			pagefactory.currentPage = 0;
			pagefactory.printContent(mCurPageCanvas);
			mPageWidget.setBitmaps(mNextPageBitmap, mCurPageBitmap);
		} else {
			pagefactory.currentPage = pagefactory.getPageList().size() - 1;
			pagefactory.printContent(mNextPageCanvas);
			mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		}
		progress_bar_font_size.setMax(pagefactory.totalSize-1);
		mPageWidget.postInvalidate();
	}

	public void PayByKpayAccount(final boolean pageing,
			final String ChapterIDNet,String chapterName, String price,String priceRP) {
		page = pageing;
		String productId = book.cpcode + "_" + book.rpid + "_" + book.bookidNet
				+ "_" + ChapterIDNet;
		
		String productName = book.Book_Name;	
		if(chapterName!=null&&chapterName.trim().length()>0){
			productName = productName+"_"+chapterName;
		}
		
//		Intent intent = KPayAccount.GetPayIntent(productId, book.Book_Name,
//				price);
//		startActivityForResult(intent, KPayAccount.REQUESTCODE_FLAG_FOR_PAYMENT);
		
		//获取订单
		
		BookOrder.createOrder(productId,productName,price,priceRP,new BookOrder.BookOrderCallback(){

			@Override
			public void onSuccess(Object o) {
				 curBookOrder = (BookOrder) o;
				 alipay(curBookOrder);
				 
			}

			@Override
			public void onFaile(String msg) {
				LogEx.Log_I(TAG, "createOrder onFaile:"+msg);
				curBookOrder = null;
			}
					
		});
			
		
	}
	
	private void alipay(BookOrder bookOrder){
		String orderNo = null;
		String order = null ;
		if(curBookOrder!=null){
			orderNo = curBookOrder.orderId;
			order = curBookOrder.paySignStr;
		}
		
		LogEx.Log_I(TAG, "orderNo:"+orderNo);
		LogEx.Log_I(TAG, "createOder:"+order);
		if(order!=null){
			payTask = new AliyunPayAsyncTask(this);
            payTask.setListener(listener);
            payTask.submit(order);
		}
	}
	/**
	 * 本地文件广播
	 */
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(BookReadActivity.FILERECEIVER))
			{
				if(AddBookService.isAddChapter)
				{
					Chapter chapter = (Chapter) intent.getSerializableExtra(AddBookService.CHAPTER);
					if(book.bookidNet.equals(chapter.BookIDNet))
					{
						if(chapter!=null)
						{
							if(chapters!=null)
								chapters.add(chapter);
							else {
								//chapters =  BookShelf.mCurrentBook.GetChapterList();
								mBookMarkThread.start();
							}
						}
					}
					
//					Toast.makeText(BookReadActivity.this, "broadcastReceiver ", 0).show();
				}
			}
		}
	};
	
//	public boolean onCreateOptionsMenu(Menu menu) {
//		 MenuInflater menuInflater = getMenuInflater();
//		 menuInflater.inflate(R.menu.menu_file, menu); //设置菜单显示的xml
////		 new com.idreamsky.ktouchread.menu.Menu(this,R.id.boodRead_parent).init();
//		return true;
//	}
//	
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		if (item.getTitle().equals(getString(R.string.MyAccount))) // 我的账户
//		{
//			SwitchtoKPayAccount();
//
//		} else if (item.getTitle().equals(getString(R.string.menu_more)))// 设置
//		{
//			Util.toActivity(this, Book_System_SettingAct.class);
//		} 
//		return true;
//	}
	private void SwitchtoKPayAccount() {
		 Intent intent = KPayAccount.GetUserIntent();
		 startActivityForResult(intent,
		 KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
		 Book.Save();
	}
	public void GetUserInforError()
	{
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(BookReadActivity.this);
			builder.setTitle(R.string.splash_notify);
			builder.setMessage(R.string.splash_get_user_info_error);
			builder.setPositiveButton(getString(R.string.book_ol_network_agin),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SwitchtoKPayAccount();

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							UrlUtil.TokenTPL = null;
							BookReadActivity.this.setResult(BookShelf.COLLECTREQUESTCOLDE);
							BookReadActivity.this.finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
		}

	}
	public boolean  GetLongToken()
	{
		final String Token = KPayAccount.getLongtermToken();

		if (Token != null && Token.length() > 0) {
			UrlUtil.TokenTPL = Token;
			LogEx.Log_V("Token", "UrlUtil.TokenTPL :" + UrlUtil.TokenTPL);
			byte[] buffer = Base64.decodeBase64(UrlUtil.TokenTPL);
			if(buffer != null && buffer.length > 4)
			{
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(buffer[0]);
				stringBuffer.append(buffer[1]);
				stringBuffer.append(buffer[2]);
				stringBuffer.append(buffer[3]);
				UrlUtil.USERID  = stringBuffer.toString();
				LogEx.Log_V("Token","User ID :" + stringBuffer.toString());
				
			}
			return true;
		} else {
			if (UrlUtil.TokenT != null && UrlUtil.TokenT.length() > 0) {
				LogEx.Log_V("Token", "UrlUtil.TokenT :" + UrlUtil.TokenT);

				new Thread()
				{
					public void run() {
						if (new RequestGetToken().GetToken()) {
							KPayAccount.setLongtermToken(UrlUtil.TokenTPL);
							LogEx.Log_V("Token", "Add UrlUtil.TokenTPL :"
									+ UrlUtil.TokenTPL);
							
							mHandler.sendEmptyMessage(11);

						}
						else {
							
							mHandler.sendEmptyMessage(10);
						}
					};
				}.start();
				return false;

			}
			else {
				SwitchtoKPayAccount();
				return false;
			}
		}
	}
	/**
	 * 还原章节index
	 * 支付失败的情况等
	 */
	public void restored()
	{
		if (isLeftOrRight) // 下一章
		{
			currentIndex--; // 如果取消 -1
			if (currentIndex < 0) {
				currentIndex = 0;
			}
			if (chapter != null && chapters != null)
				chapter = chapters.get(currentIndex);
		} else { // 上一章
			currentIndex++;
			if (currentIndex + 1 > chapters.size()) {
				currentIndex = chapters.size() - 1;
			}
			if (chapter != null && chapters != null)
				chapter = chapters.get(currentIndex);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_PAYMENT) {
			if (resultCode == RESULT_OK) {
				ProcessDialog.ShowProcess(BookReadActivity.this);
				Toast.makeText(BookReadActivity.this, "支付成功",Toast.LENGTH_SHORT).show();
				if(pagefactory!=null)
					pagefactory.currentPage =0;
				isLeftOrRight = true;
				GetChapterContentFromNet(page);

			} else if (resultCode == RESULT_CANCELED) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BookReadActivity.this, "已经取消！",
								Toast.LENGTH_SHORT).show();
						restored();
					}
				});

			} else if (resultCode == KPayAccount.RESULT_PAYMENT_UPDATING) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BookReadActivity.this, "升级中，请稍候！",
								Toast.LENGTH_SHORT).show();
					}
				});

			} else if (resultCode == KPayAccount.RESULT_PAYMENT_ERROR) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BookReadActivity.this, "支付失败！",Toast.LENGTH_SHORT).show();
						restored();
					}
				});

			} else if (resultCode == KPayAccount.RESULT_PAYMENT_NOMONEY) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BookReadActivity.this, "余额不足，请充值！",Toast.LENGTH_SHORT).show();
						Intent intent = KPayAccount.GetUserIntent();
						BookReadActivity.this.startActivityForResult(intent,KPayAccount.REQUESTCODE_FLAG_FOR_MYACCOUNT);
						
					}
				});
			}
		}else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_MYACCOUNT) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						String Token = extras.getString("token");
						if (null != Token) {
							UrlUtil.TokenT = Token;
						}
					}
				}
			}
			if (KPayAccount.getLongtermToken() == null
					|| KPayAccount.getLongtermToken().length() < 1) {
				UrlUtil.TokenTPL = null;
				this.setResult(BookShelf.COLLECTREQUESTCOLDE);
				this.finish();
			}
		}else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						if(KPayAccount.getLongtermToken() == null  || KPayAccount.getLongtermToken().length() < 1)
						{
							UrlUtil.TokenTPL = null;
							
							UrlUtil.TokenT = extras.getString("token");
							LogEx.Log_V("Token", UrlUtil.TokenT);
							if(UrlUtil.TokenT!= null && UrlUtil.TokenT.length() > 0)
							{
								GetLongToken();
							}
							else
							{
								GetUserInforError();
							}
						}
					}
					else {
						UrlUtil.TokenT = null;
						this.setResult(BookShelf.COLLECTREQUESTCOLDE);
						this.finish();
					}
				}
				else {
					UrlUtil.TokenT = null;
					this.setResult(BookShelf.COLLECTREQUESTCOLDE);
					this.finish();
				}

			}
			else
			{
				UrlUtil.TokenTPL = null;
				UrlUtil.TokenT = null;
				this.setResult(BookShelf.COLLECTREQUESTCOLDE);
				this.finish();
				
			}
		} 

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private ProgressDialog syncDataDialog;
	private Handler mCollectHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			if(msg.what == 0)
			{
				syncDataDialog = new ProgressDialog(BookReadActivity.this);
				syncDataDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				syncDataDialog.setTitle(R.string.select_book);
				syncDataDialog.setMessage(BookReadActivity.this.getString(R.string.select_book_wait));
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
			new Thread()
			{
				public void run() {
					
					mCollectHandler.sendEmptyMessage(0);
					if(!Book.IsExit(book.bookidNet))
					{
						
						book.RecentReadTime = System.currentTimeMillis();
						Book.GetBookListEx().add(0, book);
						
						NetBookShelf.add(book.cpcode, book.rpid, book.bookidNet,  new NetBookShelf.AddBookCallback() {
							
							@Override
							public void onFail(String msg) {
								book.Sync = 0;
								Book.GetSyncBooks().add(book);
								LogEx.Log_V("API", "AddBookShelfNet :" + msg);
								
							}
							
							@Override
							public void onSuccess(String strMsg) {
								LogEx.Log_V("API", "AddBookShelfNet:Success");
								
							}
						});

					}
					else if(Book.GetBookSyncStatus(book.bookidNet) == 2)
					{
						Book.UpdateBookSyncStatusTime(book.bookidNet,0);
					}
					//NetChapter chapters = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
					if(chapters != null && chapters.size()>0)
					{
						BookDataBase.getInstance().InsertChapter(chapters, book.bookidNet, false);
					}
					Book.Save();
					mCollectHandler.sendEmptyMessage(1);
				};
			}.start();
		}
	private void CollectBookEx()
	{
		new Thread()
		{
			public void run() {
				
				if(!Book.IsExit(book.bookidNet))
				{
					
					book.RecentReadTime = System.currentTimeMillis();
					Book.GetBookListEx().add(0, book);
					
					NetBookShelf.add(book.cpcode, book.rpid, book.bookidNet,  new NetBookShelf.AddBookCallback() {
						
						@Override
						public void onFail(String msg) {
							book.Sync = 0;
							Book.GetSyncBooks().add(book);
							LogEx.Log_V("API", "AddBookShelfNet :" + msg);
							
						}
						
						@Override
						public void onSuccess(String strMsg) {
							LogEx.Log_V("API", "AddBookShelfNet:Success");
							
						}
					});

				}
				else if(Book.GetBookSyncStatus(book.bookidNet) == 2)
				{
					Book.UpdateBookSyncStatusTime(book.bookidNet,0);
				}
				//NetChapter chapters = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
				if(chapters != null && chapters.size()>0)
				{
					BookDataBase.getInstance().InsertChapter(chapters, book.bookidNet, false);
				}
				Book.Save();
			};
		}.start();
	}
	
	private void InitTryDialog(String Msg)
	{
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					BookReadActivity.this);
			builder.setTitle(R.string.book_ol_network_error);
			builder.setMessage(Msg);
			builder.setPositiveButton(getString(R.string.book_ol_network_agin),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mHandler.sendEmptyMessage(15);

						}
					});
			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							isCanUpdate(true);
							if(mPageWidget != null)
							     mPageWidget.bReturn = true;
							BookReadActivity.this.finish();
						}
					});
			builder.create().show();
		} catch (Exception e) {
		}
	}
	
	
	Listener listener  = new Listener(){

		@Override
		public void onPreExecute() {
			LogEx.Log_I(TAG,"onPreExecute");
		}

		@Override
		public void onPostExecute(String result) {
//			result ="memo={用户已取消};result={};resultStatus={9000};";
			
			LogEx.Log_I(TAG,"onPostExecute:"+result);
			if(result.contains("resultStatus={9000}")){
				checkCount = 10;
				checkOrderStatus();
			}else{
				int start = result.indexOf("memo={")+6;
				int end = result.indexOf("};",start);
				String msg = result.substring(start, end);
				Toast.makeText(BookReadActivity.this, msg,Toast.LENGTH_SHORT).show();
				restored();
				BookReadActivity.this.finish();
			}
			
			if (payTask != null) {
	            payTask.cancel(true);
	            payTask.unbind();
	            payTask = null;
			}
				
		}
		

		@Override
		public void onProgressUpdate(Object... values) {
			LogEx.Log_I(TAG,"onProgressUpdate");
		}

		@Override
		public void onCancelled() {
			LogEx.Log_I(TAG,"onCancelled");
		}
		
	};
	
	private void checkOrderStatus() {

		// int countRequest = 10;
		// int orderStatus = 2;
		// while (countRequest > 0) {
		BookOrder.checkOrder(curBookOrder.orderId,
				new BookOrder.BookOrderCallback() {

					@Override
					public void onSuccess(Object o) {
						curBookOrder = (BookOrder) o;
						if (curBookOrder.orderStatus == 0
								|| curBookOrder.orderStatus == 1) {
							proccessCheckOrder(curBookOrder);
						} else {
							if (checkCount > 0) {
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										checkOrderStatus();
									}
								}, 1000);
								checkCount--;
							}else{
								proccessCheckOrder(curBookOrder);
							}
						}
					}

					@Override
					public void onFaile(String msg) {
						LogEx.Log_I(TAG, "checkOrder onFaile:" + msg);
						curBookOrder = null;
						if (checkCount > 0) {
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									checkOrderStatus();
								}
							}, 1000);
							checkCount--;
						}
					}

				});
		// if (curBookOrder != null) {
		// orderStatus = curBookOrder.orderStatus;
		// }
		//
		// if (countRequest == 0 || countRequest == 1) {
		// break;
		// }
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// countRequest--;

	}
	
	private void proccessCheckOrder(BookOrder bookOrder){
		int orderStatus = bookOrder.orderStatus;
		switch (orderStatus) {
		case 0:
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(BookReadActivity.this, "支付失败！",
							Toast.LENGTH_SHORT).show();
					restored();
					BookReadActivity.this.finish();
				}
			});
		case 1:
			ProcessDialog.ShowProcess(BookReadActivity.this);
			Toast.makeText(BookReadActivity.this, "支付成功",
					Toast.LENGTH_SHORT).show();
			if (pagefactory != null)
				pagefactory.currentPage = 0;
			isLeftOrRight = true;
			GetChapterContentFromNet(page);
			break;
		default:
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(BookReadActivity.this, "支付超时！",
							Toast.LENGTH_SHORT).show();
					restored();
					BookReadActivity.this.finish();
				}
			});
		}
	}
}
