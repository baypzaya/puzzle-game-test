package com.idreamsky.ktouchread.bookshelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.DropDownList;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.idreamsky.ktouchread.Adapter.MyBookShelfAdapter;
import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.bookshop.BookDetailActivity;
import com.idreamsky.ktouchread.bookshop.BookShopActivity;
import com.idreamsky.ktouchread.common.OpenBookAnimation;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.CheckCatchThread;
import com.idreamsky.ktouchread.data.SyncThread;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.NetChapter;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.db.BookDataBase;
import com.idreamsky.ktouchread.directoy.DirectoryActivity;
import com.idreamsky.ktouchread.http.sync.RequestBookUpdateTime;
import com.idreamsky.ktouchread.http.sync.RequestGetChapter;
import com.idreamsky.ktouchread.http.sync.RequestGetToken;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.service.Book_System_SettingService;
import com.idreamsky.ktouchread.service.ServiceBackup;
import com.idreamsky.ktouchread.service.ServiceChase;
import com.idreamsky.ktouchread.util.Base64;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;
import com.idreamsky.ktouchread.util.ProcessDialog;
import com.idreamsky.ktouchread.util.SettingUtils;
import com.idreamsky.ktouchread.util.Util;

public class BookShelf extends SpiritActivity implements OnTouchListener,
		OnItemClickListener {

	public static final String ENTRANCE = "ENTRANCE";
	public static final String MYBOOKSHELF = "MYBOOKSHELF";
	public static final String MYBOOKSHELFSEARCH = "MYBOOKSHELFSEARCH";
	public static final String ADVERT = "ADVERT";
	public static final String BUNDLE = "BUNDLE";
	public static final String BOOKINFO = "BOOKINFO";
	public static final String READBOOK = "READBOOK";
	public static final String DIRECTORY = "DIRECTORY";
	public static final String BOOKDETAIL = "BOOKDETAIL";
	public static final int REFRESHCODE = 888;
	public static final int REQ_SELECT_DELETE_BOOK = 889;
	public static final int SHOWGUIDE = 898;
	public static final int COLLECTREQUESTCOLDE = 500;
	public static final String ACTION_BOOKSHELF_UPDATE_CHASE = "com.idreamsky.www.action.updateui";
	public static final String ACTION_BOOKSHELF_BACKUP = "com.idreamsky.www.action.backup";
	public static final String INTENT_KEY_BOOKIDNET = "BOOKIDNET";
	public static final String INTENT_KEY_COMPLETE = "BOOK_COMPLETE";
	private static final int SYNC_DATA = 11;
	private static final int SYNC_SUCCESS = 12;
	private static final int SYNC_FAIL = 13;
	private static final int START_SYNC = 14;
	public static final String DELETE_BOOKS = "delete_book";

	private RelativeLayout bg_height;
	private ListView lvMyBookShelf;
	private Poster poster;
	private View layout_book_poster;
	private MyGestureDetector myGestureDetector;// 触发手势
	private List<Book> books = null;
	private LinearLayout book_shelf_parent;
	private GestureDetector mGestureDetector;
	private MyBookShelfAdapter myBookShelfAdapter; // 适配器

	private LinearLayout menu_lin_directory;
	private LinearLayout menu_lin_authorRestBook;
	private LinearLayout menu_lin_deleteTishiBook;
	private PopupWindow pw; // 菜单\

	private ImageView fenge;
	private ImageView fenge1;

	public static Book mCurrentBook = null;
	public static boolean jumpFlag = false; // 跳转引导页标志。
	public static boolean backFlag = false; // 控制该作者其他书进入的搜索
	public String mBookIDNetNotice = null;
	public static int width= 0;
	public static int height=0;
	public ImageView imgBg;
	private SyncThread syncThread = null;
	private ProgressDialog syncDataDialog;
	private boolean ShowGuid = false;
	private boolean BookHasInit = false;
	private boolean bExist = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置 NavigationBar
		NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.myBookShelf);
		builder.showBackButton(false);
		
		
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.deleteBook));
		list.add(getString(R.string.phoneBook));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.aui_drop_item, list);
		builder.setCommand("选项", adapter,
				new DropDownList.OnItemClickListener() {
					public void onItemClick(DropDownList parent, View view,
							int position, long id) {
						switch (position) {
						case 0:
							deleteBooksGo();
							break;
						case 1:
							importPhoneBooksGo();
							break;
						default:
							break;
						}

					}					
				});
		
		width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
		setContentView(R.layout.book_shelf);
		imgBg = (ImageView) this.findViewById(R.id.imgBg);
		lvMyBookShelf = (ListView) this.findViewById(R.id.lvMyBookShelf);
		layout_book_poster = this.findViewById(R.id.layout_book_poster);
		book_shelf_parent = (LinearLayout) this
				.findViewById(R.id.book_shelf_parent);

		BookDataBase.Initial(getApplicationContext());
		UrlUtil.Init(this);
		books = new ArrayList<Book>();// Book.GetBookList();	
		myBookShelfAdapter = new MyBookShelfAdapter(this);
		myBookShelfAdapter.setMyBooks(books);
		fenge = (ImageView) this.findViewById(R.id.fenge);
		fenge1 = (ImageView) this.findViewById(R.id.fenge1);
		fenge.getBackground().setAlpha(75);
		fenge.invalidate();
		fenge1.getBackground().setAlpha(75);
		fenge1.invalidate();
		lvMyBookShelf.setAdapter(myBookShelfAdapter);
		myBookShelfAdapter.SetListView(lvMyBookShelf);
		lvMyBookShelf.setOnItemClickListener(this);
		lvMyBookShelf.setOnItemLongClickListener(onItemLongClickListener);

		book_shelf_parent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return true;
			}
		});
		lvMyBookShelf.setOnTouchListener(this);
		myGestureDetector = new MyGestureDetector(this);
		mGestureDetector = new GestureDetector(myGestureDetector);

		poster = new Poster(this, Poster.myBookShelf);
		poster.showPosterList();
		poster.SetCallback(new Poster.clickAdvertDataCallBack() {

			@Override
			public void getAdverDataCallBack(Advert advert) {
				if(advert!=null){
					if(advert.bookid != null && advert.bookid.length() > 0 && advert.bookid.compareTo("-1") != 0)
					{
						Intent intent = new Intent();
//						Bundle bundle = new Bundle();
//						bundle.putSerializable(BookShelf.ADVERT, advert);
//						intent.putExtra(BookShelf.BUNDLE, bundle);
//						intent.putExtra(BookShelf.ENTRANCE, BookShelf.ADVERT);
						intent.putExtra(BookDetailActivity.EXTRA_SCR_TYPE, BookDetailActivity.TYPE_ADVERT);
						intent.putExtra(BookDetailActivity.EXTRA_BOOKID, advert.bookid);
						intent.putExtra(BookDetailActivity.EXTRA_CPCODE, advert.cpcode);
						intent.putExtra(BookDetailActivity.EXTRA_RPID, advert.rpid);
						intent.setClass(BookShelf.this, BookDetailActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						BookShelf.this.startActivity(intent);
					}
//					else
//					{
//						Intent intent = new Intent();
//						intent.putExtra(BookShelf.ENTRANCE, BookShelf.MYBOOKSHELF);
//						intent.setClass(BookShelf.this, BookShopActivity.class);
//						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//						BookShelf.this.startActivityForResult(intent,BookShelf.COLLECTREQUESTCOLDE);
//					}
				}
//				else
//				{
//					Intent intent = new Intent();
//					intent.putExtra(BookShelf.ENTRANCE, BookShelf.MYBOOKSHELF);
//					intent.setClass(BookShelf.this, BookShopActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
//					BookShelf.this.startActivityForResult(intent,BookShelf.COLLECTREQUESTCOLDE);
//				}
				

			}
		});

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BOOKSHELF_UPDATE_CHASE);
		this.registerReceiver(mBroadcastReceiver, intentFilter);


		
		final SettingUtils utils = new SettingUtils(this, "SaveParameter",
				 Context.MODE_PRIVATE);
		UrlUtil.BookUpdateTime = utils.getString("BookUpdateTime", null);
		UrlUtil.BookMarkUpdateTime = utils.getString("BookUpdateTime", null);
		String key = utils.getString("guide", null);
		if(key==null){
			Intent intent = new Intent(this, GuideActivity.class);
			intent.putExtra("flag","true");
			Book_SplashAct1.jumpFlag=true;
			utils.putString("guide","true");
			ShowGuid = true;
			startActivityForResult(intent, SHOWGUIDE);
		}
		
		Intent intent = getIntent();
		mBookIDNetNotice = intent.getStringExtra("BookIDNet");
		LogEx.Log_V("BookIDNet", mBookIDNetNotice != null ? mBookIDNetNotice : "null");
		
		
		if(!ShowGuid)
		{
			if( GetLongToken() )//
			{
				if( mBookIDNetNotice != null && mBookIDNetNotice.length() >0)
				     OpenUnReadChapter();
				else {
					new Thread()
					{
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if( new RequestBookUpdateTime().GetUpdate())
						{
							handler.sendEmptyMessage(START_SYNC);
							LogEx.Log_V("START_SYNC", "2");
						}
						
						super.run();
					}
					}.start();

				}
			}
			StartService();
		}

		handler.sendEmptyMessage(0);
	}

	private void  GetLongTokenAfterGuide()
	{
		
		final String Token = KPayAccount.getLongtermToken();

		if (Token != null && Token.length() > 0) {
			UrlUtil.TokenTPL = Token;
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
			if(!BookHasInit)
			{
				BookHasInit = true;
				Book.Init();
			}
			
			LogEx.Log_V("Token", "UrlUtil.TokenTPL :" + UrlUtil.TokenTPL);
		} else {
			if (UrlUtil.TokenT != null && UrlUtil.TokenT.length() > 0) {
				LogEx.Log_V("Token", "UrlUtil.TokenT :" + UrlUtil.TokenT);

				new Thread()
				{
					public void run() {
						if (new RequestGetToken().GetToken()) {
							KPayAccount.setLongtermToken(UrlUtil.TokenTPL);
							if(!BookHasInit)
							{
								BookHasInit = true;
								Book.Init();
							}
							LogEx.Log_V("Token", "Add UrlUtil.TokenTPL :"
									+ UrlUtil.TokenTPL);
							if (!ShowGuid) {
								handler.sendEmptyMessage(START_SYNC);
								LogEx.Log_V("START_SYNC", "1");
							}

						}
						else {
							
							handler.sendEmptyMessage(10);
						}
						UrlUtil.TokenT = null;
					};
				}.start();

			}
		}
		StartService();
	}
	public void OpenUnReadChapter()
	{
		new Thread(){
			public void run() {
				
				Book book = Book.GetBook(mBookIDNetNotice);
				if(book != null)
				{
					Intent intent = new Intent();
					BookShelf.mCurrentBook = book;
					intent.setClass(BookShelf.this, UnReadActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					startActivityForResult(intent, BookShelf.REFRESHCODE);
				}

			};
		}.start();
	}
	public boolean  GetLongToken()
	{
		final String Token = KPayAccount.getLongtermToken();

		if (Token != null && Token.length() > 0) {
			UrlUtil.TokenTPL = Token;
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
			if(!BookHasInit)
			{
				BookHasInit = true;
				Book.Init();
			}
			
			LogEx.Log_V("Token", "UrlUtil.TokenTPL :" + UrlUtil.TokenTPL);
			return true;
		} else {
			if (UrlUtil.TokenT != null && UrlUtil.TokenT.length() > 0) {
				LogEx.Log_V("Token", "UrlUtil.TokenT :" + UrlUtil.TokenT);

				new Thread()
				{
					public void run() {
						if (new RequestGetToken().GetToken()) {
							KPayAccount.setLongtermToken(UrlUtil.TokenTPL);
							if(!BookHasInit)
							{
								BookHasInit = true;
								Book.Init();
							}
							LogEx.Log_V("Token", "Add UrlUtil.TokenTPL :"
									+ UrlUtil.TokenTPL);
							if (!ShowGuid) {
								handler.sendEmptyMessage(START_SYNC);
								LogEx.Log_V("START_SYNC", "1");
							}

						}
						else {
							
							handler.sendEmptyMessage(10);
						}
						UrlUtil.TokenT = null;
						if(mBookIDNetNotice != null && mBookIDNetNotice.length() > 0)
						{
							OpenUnReadChapter();
						}
					};
				}.start();
				return false;

			}
			else {
//				SwitchtoKPayAccount();
				return false;
			}
		}
	}
	private void SwitchtoKPayAccount() {
		 Book.Save();
		 Intent intent = KPayAccount.GetUserIntent();
		 startActivityForResult(intent,
		 KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
	}
	public void MakeToast(final String msg) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(BookShelf.this, msg, Toast.LENGTH_SHORT).show();

				}
			});
	}

	public void StartService() {
		
	//	ReadInternal.getInstance().getPoolExecutor().execute(new CheckCatchThread(handler));
				// TODO Auto-generated method stub
		unNotify();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Configuration.VERSION)
				{
					Intent intentChase = new Intent(BookShelf.this,
							ServiceChase.class);
					intentChase.setAction(ACTION_BOOKSHELF_UPDATE_CHASE);
					startService(intentChase);
				}
				else {
					Intent intentChase = new Intent(BookShelf.this,
							Book_System_SettingService.class);
					intentChase.setAction(ACTION_BOOKSHELF_UPDATE_CHASE);
					startService(intentChase);
				}
				StartBackService();
			}
		}, 20000);
	}

	private void StartBackService()
	{
		Intent intentBackUp = new Intent(BookShelf.this,
				ServiceBackup.class);
		intentBackUp.setAction(ACTION_BOOKSHELF_BACKUP);
		startService(intentBackUp);
	}
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.compareTo(ACTION_BOOKSHELF_UPDATE_CHASE) == 0) {
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
			}

		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
	public static OpenBookAnimation oba;
	private Book book;
	private Timer timer;
	private TimerTask task;
	private long mClickTime = 0;
	@Override
	public void onItemClick(AdapterView<?> arg0, final View v, int index, long arg3) {
		book = books.get(index);
				if(book.GetChapterCount() < 1 && book.BookType ==0)
				{
					ProcessDialog.ShowProcess(BookShelf.this,false);
					Toast.makeText(BookShelf.this, "获取章节中，请稍候！", 1).show();
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									NetChapter netChapterInfo = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
									if(netChapterInfo != null && netChapterInfo.mChapterInfoList.size() > 0)
									{
										BookDataBase.getInstance().InsertChapterEx(netChapterInfo,  book.bookidNet);
										OpenBook(v);
										netChapterInfo.mChapterInfoList.clear();
										netChapterInfo.mChapterInfoList = null;
										netChapterInfo = null;
									}else
									{
										Toast.makeText(BookShelf.this, "该书籍服务器没有书籍信息", 1).show();
									}
									ProcessDialog.Dismiss();
								}
							});
					return;
				}else {
					if (Math.abs(mClickTime- System.currentTimeMillis()) < 2000) { 
					}else {
						OpenBook(v);
					}
					mClickTime = System.currentTimeMillis();
				}
				
//				if (Math.abs(mClickTime- System.currentTimeMillis()) < 2000) { 
//				}else {
//					OpenBook(v);
//				}
				mClickTime = System.currentTimeMillis();
	}

	public void OpenBook(final View v)
	{
		new Thread(){

			@Override
			public void run() {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
//						ViewHolder viewHolder = (ViewHolder) v.getTag();
//						ImageView image = viewHolder.myBookShelfLogo;
						book.UpdateReadTime();
						oba = null;
						oba = new OpenBookAnimation(BookShelf.this,width,height,imgBg,books);
						oba.start();
						timer = new Timer();
						task = new TimerTask() {
								
								@Override
								public void run() {
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											imgBg.setVisibility(View.VISIBLE);
											Intent intent = new Intent();
											mCurrentBook = book;
											if (!book.Recent_Chapter_Name.equals(""))
												intent.putExtra(Util.ENTRANCE,Util.RECENTLYREAD);
											else {
												intent.putExtra(Util.ENTRANCE,Util.NOMARLREAD);
											}

											intent.setClass(BookShelf.this, BookReadActivity.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											oba.dismiss();
											oba.mPageWidget = null;
											startActivityForResult(intent, BookShelf.REFRESHCODE);
											
											
//											oba.dismiss();
											
											
										}
									});
								}
							};
						timer.schedule(task, 2000);
					}
				});
				super.run();
			}
			
		}.start();
		
	}
	
	@Override
	protected void onDestroy() {
		if (poster != null)
			poster.stopPoster();
		if (UrlUtil.TokenTPL != null && UrlUtil.TokenTPL.length() > 0) {
			KPayAccount.setLongtermToken(UrlUtil.TokenTPL);
		}

        Book.Save();
		final SettingUtils utils = new SettingUtils(this, "SaveParameter",
				 Context.MODE_PRIVATE);
		if(UrlUtil.BookUpdateTime != null)
		{
			utils.putString("BookUpdateTime", UrlUtil.BookUpdateTime);
		}
		if(UrlUtil.BookMarkUpdateTime != null)
		{
			utils.putString("BookMarkUpdateTime", UrlUtil.BookMarkUpdateTime);
		}
		
		BookDataBase.getInstance().close();
		Intent intentBackUp = new Intent(this, ServiceBackup.class);
		intentBackUp.setAction(ACTION_BOOKSHELF_BACKUP);
		this.stopService(intentBackUp);
		System.gc();
		this.unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Util.isFirstOpenBookShop = false;
		imgBg.setVisibility(View.GONE);
		try {
			if(oba != null)
			{
				oba.dismiss();
				oba = null;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
		
		//在 onResume时重新刷新UI
		if(KPayAccount.getLongtermToken() == null  || KPayAccount.getLongtermToken().length() < 1)
		{
			if(UrlUtil.TokenT != null && UrlUtil.TokenT.length() >0)
			{
				GetLongToken();
			}
			else
			{
				UrlUtil.TokenTPL = null;
				GetUserInforError();
			}
			return;
		}
		Message msg = new Message(); // 提示msg
		msg.what = 0;
		handler.sendMessage(msg);
	}
	
	
	
	@Override
	protected void onRestart() {
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		});
	
		
		try {
			if(oba != null)
			{
				oba.dismiss();
				oba = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		imgBg.setVisibility(View.GONE);
		super.onRestart();
	}

	private ImageView menu_img_directory;
	private ImageView menu_img_authorRestBook;
	private ImageView menu_img_deleteTishiBook;
	public OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View v, int index,
				long arg3) {

			int location[] = new int[] { v.getBottom(), v.getTop() };
			LogEx.Log_I("bottom", location[0] + "");
			v.getLocationInWindow(location);
			// v.setBackgroundResource(R.drawable.bar_bg_8);
			Book book = books.get(index);
			LayoutInflater layoutInflater = LayoutInflater.from(BookShelf.this);
			View view = layoutInflater.inflate(R.layout.book_shelf_menu, null);
			menu_lin_directory = (LinearLayout) view.findViewById(R.id.menu_lin_directory);
			menu_img_directory = (ImageView) view.findViewById(R.id.menu_img_directory);
			menu_lin_authorRestBook = (LinearLayout) view.findViewById(R.id.menu_lin_authorRestBook);
			menu_img_authorRestBook = (ImageView) view.findViewById(R.id.menu_img_authorRestBook);
			menu_lin_deleteTishiBook = (LinearLayout) view.findViewById(R.id.menu_lin_deleteTishiBook);
			menu_img_deleteTishiBook = (ImageView) view.findViewById(R.id.menu_img_deleteTishiBook);
			bg_height = (RelativeLayout) view.findViewById(R.id.bg_height);
			menu_lin_directory.setOnClickListener(menuClickListener);
			menu_lin_directory.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == event.ACTION_DOWN) { 
						menu_img_directory.setBackgroundResource(R.drawable.icon_11_a); 
					 } else if (event.getAction() == event.ACTION_UP) { 
						 menu_img_directory.setBackgroundResource(R.drawable.icon_11); 
					} 
					return false;
				}
			});
			
			
			menu_lin_authorRestBook.setOnClickListener(menuClickListener);
			menu_lin_authorRestBook.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == event.ACTION_DOWN) { 
						menu_img_authorRestBook.setBackgroundResource(R.drawable.icon_12_a); 
					 } else if (event.getAction() == event.ACTION_UP) { 
						 menu_img_authorRestBook.setBackgroundResource(R.drawable.icon_12); 
					} 
					return false;
				}
			});
			menu_lin_deleteTishiBook.setOnClickListener(menuClickListener);
			menu_lin_deleteTishiBook.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == event.ACTION_DOWN) { 
						menu_img_deleteTishiBook.setBackgroundResource(R.drawable.icon_13_a); 
					 } else if (event.getAction() == event.ACTION_UP) { 
						 menu_img_deleteTishiBook.setBackgroundResource(R.drawable.icon_13); 
					} 
					return false;
				}
			});

			menu_lin_directory.setTag(book);
			menu_lin_authorRestBook.setTag(book);
			menu_lin_deleteTishiBook.setTag(book);
			pw = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT, true);
			ColorDrawable dw = new ColorDrawable(-00000);
			pw.setBackgroundDrawable(dw);
			pw.setAnimationStyle(R.style.PopupAnimation);
			if (index == 0) {
				if (!Poster.arrowFlag) // 如果广告关闭,设置不同的位置
				{
					pw.showAtLocation(book_shelf_parent, Gravity.NO_GRAVITY, 0,(int)(location[0] + (float)bg_height.getLayoutParams().height*1.5));
				} else {
					int h = location[1] - bg_height.getLayoutParams().height;
					if(location[1]<bg_height.getLayoutParams().height)
					{
						h = bg_height.getLayoutParams().height;
					}
					pw.showAtLocation(book_shelf_parent, Gravity.NO_GRAVITY, 0,h);
				}
			} else {
				int h = location[1] - bg_height.getLayoutParams().height;
				pw.showAtLocation(book_shelf_parent, Gravity.NO_GRAVITY, 0,h);
			}
			pw.update();
			return false;
		}

	};
	com.idreamsky.ktouchread.menu.Menu m = null;
	/**
	 * 创建菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
//		if(!menuFlag){
//			menuFlag = true;
//			m = new com.idreamsky.ktouchread.menu.Menu(BookShelf.this,R.id.book_shelf_parent);
//			m.init();
//		}else {
//			if(m!=null)
//			{
//				if(m.pw.isShowing())
//				{
//					m.pw.dismiss();
//					Toast.makeText(this, "dismiss success", 0).show();
//				}
//			}
//			menuFlag = false;
//		}
		
//		MenuInflater menuInflater = getMenuInflater();
//		menuInflater.inflate(R.menu.menu_file_book_shelf, menu);
//		return true;
		return super.onCreateOptionsMenu(menu);
	}

	public static Activity context;

	/**
	 * 菜单项选择
	 */
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getTitle().equals(getString(R.string.MyAccount))) // 我的账户
		{
			poster.stopPoster();
//			Intent intent = KPayAccount.GetUserIntent();
//			this.startActivityForResult(intent,
//					KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
			SwitchtoKPayAccount();

		} else if (item.getTitle().equals(getString(R.string.menu_more)))// 设置
		{
			poster.stopPoster();
		    //Util.toActivity(this, Book_System_SettingAct.class);
			
			Intent intent = new Intent();
			intent.setClass(this, Book_System_SettingAct.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			this.startActivityForResult(intent, BookShelf.REFRESHCODE);
			
			
		} else if (item.getTitle().equals(getString(R.string.phoneBook)))// 设置
		{
			poster.stopPoster();
			Intent intent = new Intent();
//			Bundle bundle = new Bundle(); 
//			bundle.putSerializable(BookShelf.READBOOK, book);
//			intent.putExtras(bundle);
			intent.setClass(BookShelf.this,  AddFile.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivityForResult(intent, BookShelf.REFRESHCODE);
		}
		return true;
	}

	public OnClickListener menuClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.menu_lin_directory: // 打开目录
				if (pw.isShowing())
					pw.dismiss();
				final Book book = (Book) v.getTag();
				if(book.GetChapterCount() < 1 && book.BookType == 0)
				{
					ProcessDialog.ShowProcess(BookShelf.this,false);
					MakeToast("获取章节中，请稍候！");
					new Thread(){
						@Override
					 public void run() {
							NetChapter NetChapterInfo = new RequestGetChapter(book.cpcode, book.rpid, book.bookidNet).GetChapter();
							if(NetChapterInfo != null)
							{
								BookDataBase.getInstance().InsertChapterEx(NetChapterInfo,  book.bookidNet);
								NetChapterInfo.mChapterInfoList.clear();
								NetChapterInfo.mChapterInfoList = null;
								NetChapterInfo = null;
								
							}
							ProcessDialog.Dismiss();
					 };
					}.start();
					return;
				}
				Intent intent = new Intent();


				mCurrentBook = (Book) v.getTag();

				intent.setClass(BookShelf.this, DirectoryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, BookShelf.REFRESHCODE);
				break;
			case R.id.menu_lin_authorRestBook: // 该作者其他书
				if (pw.isShowing()) {
					pw.dismiss();
				}
				backFlag = true;
				Intent intentCol = new Intent();
				intentCol.putExtra(BookShelf.ENTRANCE,BookShelf.MYBOOKSHELFSEARCH);
				intentCol.putExtra("Author", ((Book) v.getTag()).Author);
				intentCol.setClass(BookShelf.this, BookShopActivity.class);
				startActivityForResult(intentCol, BookShelf.COLLECTREQUESTCOLDE);

				break;
			case R.id.menu_lin_deleteTishiBook:// 删除书籍

				initDialog(((Book) v.getTag()));
				break;
			}
		}
	};

	public void initDialog(final Book book) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.deleteBook));
		builder.setMessage(getString(R.string.confirmDeleteBook) + "'"
				+ book.Book_Name + "'?");
		builder.setPositiveButton(getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (pw.isShowing())
							pw.dismiss();
						boolean isDelete = Book.DeleteBook(book); // 删除书籍
						StartBackService();
						if (isDelete) {
							Message msg = new Message(); // 提示msg
							msg.what = 1;
							handler.sendMessage(msg);
						} else {
							Toast.makeText(BookShelf.this,
									getString(R.string.deleteFail), 1).show();
						}

					}
				});
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		backFlag = false;
		if (resultCode == BookShelf.COLLECTREQUESTCOLDE || requestCode == BookShelf.REFRESHCODE) {
			if(KPayAccount.getLongtermToken() == null  || KPayAccount.getLongtermToken().length() < 1)
			{
				if(UrlUtil.TokenT != null && UrlUtil.TokenT.length() >0)
				{
					GetLongToken();
				}
				else
				{
					UrlUtil.TokenTPL = null;
					GetUserInforError();
				}
				return;
			}
			Message msg = new Message(); // 提示msg
			msg.what = 0;
			handler.sendMessage(msg);
		} else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN) {
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
								if( GetLongToken() && mBookIDNetNotice != null && mBookIDNetNotice.length() >0)
								{
									OpenUnReadChapter();
								}
							}
							else
							{
								GetUserInforError();
								
							}
						}				
					}
					else {
						GetUserInforError();
					}
				}
				else {
					GetUserInforError();
				}
			}
			else
			{
				GetUserInforError();
//				UrlUtil.TokenTPL = null;
//				this.finish();
			}
		} else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_MYACCOUNT) {
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
			if(KPayAccount.getLongtermToken() == null  || KPayAccount.getLongtermToken().length() < 1)
			{
				UrlUtil.TokenTPL = null;
				this.finish();
			}
		} else if(requestCode == BookShelf.SHOWGUIDE)
		{
			if(ShowGuid )
			{
				ShowGuid = false;
				GetLongTokenAfterGuide();
				  
			}
		}else if(REQ_SELECT_DELETE_BOOK == requestCode){
			Log.i("yujsh log","activity result delete");
			if(data!= null && data.hasExtra(DELETE_BOOKS)){
				String[] bookIdNets = data.getStringArrayExtra(DELETE_BOOKS);
				Log.i("yujsh log","bookIdNets"+bookIdNets);
				if (bookIdNets != null) {
					deleteBooks(bookIdNets);
				}
			}
		}else 
		{
			Message msg = new Message(); // 提示msg
			msg.what = 0;
			handler.sendMessage(msg); //刷新ui
		}

		if (mCurrentBook != null) {
			mCurrentBook.unreadChapterNumber = -1;
			new Thread() {

				@Override
				public void run() {
					mCurrentBook.CloseBook();
					mCurrentBook = null;
						
					super.run();
				}

			}.start();
		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void deleteBooks(final String[] bookIdNets) {
		Log.i("yujsh log","deleteBooks count:"+bookIdNets.length);
		ProcessDialog.ShowProcess(this);
		
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				boolean result = true;
				for(String bookIdNet:bookIdNets){
					Book book = Book.GetBook(bookIdNet);
					boolean isDelete = Book.DeleteBook(book); // 删除书籍
					result = result && isDelete;
				}
				
				StartBackService();
				if (result) {
					Message msg = new Message(); // 提示msg
					msg.what = 1;
					handler.sendMessage(msg);
				} else {
					handler.post(new Runnable(){

						@Override
						public void run() {
							Toast.makeText(BookShelf.this,
									getString(R.string.deleteFail), 1).show();
						}});
					
				}
				ProcessDialog.Dismiss();
			}};
			
		new Thread(runnable).start();
		

	}

	//	private void FinishWithNoToken()
//	{
//		if(KPayAccount.getLongtermToken() == null  || KPayAccount.getLongtermToken().length() < 1)
//		{
//			UrlUtil.TokenTPL = null;
//			this.finish();
//		}
//	}
	public void GetUserInforError()
	{
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(BookShelf.this);
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
							BookShelf.this.finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

		
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(bExist)
				return;
			switch (msg.what) {
			case 0: // 刷新ui
			books = Book.GetBookList();
			handler.sendEmptyMessage(3);

				break;
			case 1: // 刷新ui
				new Thread(){
					public void run() {
						books = Book.GetBookList();
						handler.sendEmptyMessage(4);

					};
				}.start();

				break;
			case 2:
				myBookShelfAdapter.notifyDataSetChanged();
				break;
			case 3:
			{
				if(books != null)
				{
					myBookShelfAdapter.setMyBooks(books);
					lvMyBookShelf.setAdapter(myBookShelfAdapter);
					myBookShelfAdapter.notifyDataSetChanged();
				}
			}
			
			break;
			case 4:
			{
				if(books != null)
				{
					myBookShelfAdapter.setMyBooks(books);
					lvMyBookShelf.setAdapter(myBookShelfAdapter);
					myBookShelfAdapter.notifyDataSetChanged();
					Toast.makeText(BookShelf.this,
							getString(R.string.deleteSuccess), 1).show();
				
				}
			}
			
			break;
			case CheckCatchThread.DATA_CATCH_IS_FULL: {
				this.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(BookShelf.this,
								getString(R.string.bookshelft_data),
								Toast.LENGTH_LONG);

					}
				});
			}
				break;
			case 10:         //长效token获取失败
			{
				GetUserInforError();
			}
			break;
			case SYNC_DATA:
			{
				try{
					syncDataDialog = new ProgressDialog(BookShelf.this);
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
				}catch (Exception e) {
					// TODO: handle exception
				}

			}
			break;
			case SYNC_SUCCESS:
			{
				try{
					new AlertDialog.Builder(BookShelf.this)
					.setTitle(R.string.sync_data_update)
					.setMessage(R.string.sync_data_update_complete)
					.setNegativeButton(R.string.setting_enter, null)
					.create().show();
			        handler.sendEmptyMessage(0);
			        syncThread = null;
			        
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			break;
			case START_SYNC:
			{
				SyncData();
			}
			break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

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
						bExist = true;
						BookShelf.this.finish();
					}
				});
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.create().show();
	}

	private void SyncData()
	{
		if (NetUtil.checkNetwork(this)) {
			handler.sendEmptyMessage(SYNC_DATA);
			new Thread(new Runnable() {

				@Override
				public void run() {
					if(syncThread == null)
					{
						syncThread = new SyncThread(new SyncThread.SyncCallback() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								if(syncDataDialog!=null && syncDataDialog.isShowing())
								{
									syncDataDialog.dismiss();
									syncDataDialog = null;
								}
									
								handler.sendEmptyMessage(SYNC_SUCCESS);
								
							}
							
							@Override
							public void onFail() {
								// TODO Auto-generated method stub
								
							}
						},getApplicationContext());
						if (syncThread.IsStop()) {
							syncThread.StartThread();
						}
					}
				
					
				}
			}).start();
		}
	}
	public void unNotify() {
		if(Configuration.VERSION)
		{
			NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			nM.cancel(ServiceChase.NEW_MSG_NOTIF_ID);
		}

	}
	
	private void importPhoneBooksGo() {
//		poster.stopPoster();
		Intent intent = new Intent();
//		Bundle bundle = new Bundle(); 
//		bundle.putSerializable(BookShelf.READBOOK, book);
//		intent.putExtras(bundle);
		intent.setClass(BookShelf.this,  AddFile.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivityForResult(intent, BookShelf.REFRESHCODE);
		
	}

	private void deleteBooksGo() {
		Intent intent = new Intent();
		intent.setClass(BookShelf.this,  BookDeleteActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivityForResult(intent, BookShelf.REQ_SELECT_DELETE_BOOK);
		
	}
}