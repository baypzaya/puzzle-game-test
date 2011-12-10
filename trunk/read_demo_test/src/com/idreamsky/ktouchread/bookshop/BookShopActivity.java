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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.MenuTabActivity;
import com.aliyun.aui.widget.spirit.MenuTabHost;
import com.aliyun.aui.widget.spirit.MenuTabHost.OnTabChangeListener;
import com.aliyun.aui.widget.spirit.MenuTabHost.TabSpec;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.aliyun.aui.widget.spirit.NavigationBar.Builder;
import com.aliyun.aui.widget.spirit.NavigationBar.OnBackListener;
import com.aliyun.aui.widget.spirit.NavigationBar.OnSearchBarDoSearchingListener;
import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.http.sync.RequestGetToken;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.Base64;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;

public class BookShopActivity extends MenuTabActivity implements OnTabChangeListener{

	public static final int TAB_RECOMMEND = 0x10, TAB_CATEGORY = 0x11,
			TAB_LEADBOARD = 0x12, TAB_SEARCH = 0x13, TAB_BOOKDETAIL = 0x14,
			TAB_NETERRORVIEW = 0x15;

	private static final int INDEX_RECOMMEND = 0;
	private static final int INDEX_LEADBOARD = 1;
	private static final int INDEX_CATEGORYVIEW = 2;

	private boolean isShowLeadBoardBack;
	private String leadBoardTitle;

	private boolean isShowCategoryBack;
	private String categorTitle;

	// private ViewStrategy mViewStrategy;
	private LinearLayout mContent;
	private View mCurrentTab;
	private int buttons[] = { R.id.id_recommend, R.id.id_category, R.id.id_top,
			R.id.id_search };

	private int mEntranceType = 0; // 0:BookShelf 1:Advert

	private final Handler mToastHandler = new Handler();

	private boolean mIsCollectNewBook = false;

	public SearchView mSearchview = null;

	private RecommendView mRecommendView = null;
	private LeadBoardView mLeadBoardView = null;
	private CategoryView mCategoryView = null;

	private TabSpec tabRecommend;
	private TabSpec tabLeadBoard;
	private TabSpec tabCategory;

	private MenuTabHost tabHost;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置 NavigationBar
		final NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.toBookShop);
		builder.showBackButton(false);
		builder.showSearchButton(true);
		
		builder.setOnSearchBarDoSearchingListener(new OnSearchBarDoSearchingListener() {
			
			@Override
			public void doSearching(CharSequence arg0) {
				Log.i("yujsh log","search button onclick");
				
				String searchContent = builder.getSearchText().toString();
				Log.i("yujsh log","doSearching search content:"+searchContent);
				Intent intent = new Intent(BookShopActivity.this,BookSearchActivity.class);
				intent.putExtra(BookSearchActivity.EXTRA_SEARCH_CONTENT, searchContent);
				BookShopActivity.this.startActivity(intent);
				builder.hideSearchbar();
			}
		});
		
		Drawable drawable = new ColorDrawable(0x66000000);
		setSearchContentBackground(drawable);
		setSearchMaskDrawable(drawable);

		mRecommendView = new RecommendView(BookShopActivity.this);
		mRecommendView.initializeIfNecessary();

		mLeadBoardView = new LeadBoardView(BookShopActivity.this);
		mLeadBoardView.initializeIfNecessary();

		mCategoryView = new CategoryView(BookShopActivity.this);
		mCategoryView.initializeIfNecessary();

		tabHost = this.getTabHost();

		tabRecommend = tabHost.newTabSpec("推荐").setContent(
				mRecommendView.getContentView());
		tabLeadBoard = tabHost.newTabSpec("排行").setContent(
				mLeadBoardView.getContentView());
		tabCategory = tabHost.newTabSpec("分类").setContent(
				mCategoryView.getContentView());

		tabHost.addTab(tabRecommend);
		tabHost.addTab(tabLeadBoard);
		tabHost.addTab(tabCategory);
		tabHost.setOnTabChangedListener(this);

		this.setDefaultTab(0);

		// final ViewStrategy strategy = new ViewStrategy(this, mContent);
		// setViewStrategy(strategy);

		if (checkNetWork()) {
			// SwitchView();
		} else {
			// mViewStrategy.switchToTab(TAB_NETERRORVIEW);
			new NetworkErrorView(this).bringSelfToFront();
		}

		this.getNavigationBarBuilder().setOnBackListener(new OnBackListener() {
			@Override
			public void onBack() {
				back();
			}

		});

	}

	private void back() {
		
		int index = tabHost.getCurrentTab();
		AbstractView abstractView =  null;
		switch (index) {
		case INDEX_LEADBOARD:
			if (isShowLeadBoardBack) {
				isShowLeadBoardBack = false;
				abstractView = new LeadBoardView(this);
				break;
			}
		case INDEX_CATEGORYVIEW:
			if (isShowCategoryBack) {
				isShowCategoryBack = false;				
				abstractView = new CategoryView(this);;
				break;
			}
		}
		
		if (abstractView != null) {
			backContent("书城", abstractView);
		}
		
	}

	private void backContent(String title,AbstractView abstractView) {
		Builder builder = getNavigationBarBuilder();
		builder.showBackButton(false);
		builder.setTitle(title);
		
		backUpdateUI(abstractView);
		
	}

	// public void SwitchView() {
	// Intent intent = getIntent();
	//
	// String entranceString = intent.getExtras()
	// .getString(BookShelf.ENTRANCE);
	// if (entranceString.compareTo(BookShelf.ADVERT) == 0) {
	// mViewStrategy.switchToTab(TAB_BOOKDETAIL);
	// Bundle bundle = intent.getExtras().getBundle(BookShelf.BUNDLE);
	// Advert advert = (Advert) bundle.getSerializable(BookShelf.ADVERT);
	//
	// new BookDetailView(this, advert).bringSelfToFront();
	// mEntranceType = 1;
	// } else if (entranceString.compareTo(BookShelf.MYBOOKSHELF) == 0) {
	// mViewStrategy.switchToTab(TAB_RECOMMEND);
	// new RecommendView(this).bringSelfToFront();
	// ImageButton recommendBtn = (ImageButton) findViewById(R.id.id_recommend);
	// recommendBtn.setSelected(true);
	// mCurrentTab = recommendBtn;
	// mEntranceType = 0;
	// } else if (entranceString.compareTo(BookShelf.MYBOOKSHELFSEARCH) == 0) {
	// String author = intent.getExtras().getString("Author");
	// mViewStrategy.switchToTab(TAB_SEARCH);
	// new SearchView(this, author).bringSelfToFront();
	// ImageButton searchBtn = (ImageButton) findViewById(R.id.id_search);
	// searchBtn.setSelected(true);
	// mCurrentTab = searchBtn;
	//
	// }

	// }

	private void backUpdateUI(AbstractView abstractView) {
		
		int index = tabHost.getCurrentTab();	
		Log.i("yujsh log","backUpdateUI index:"+index);
		abstractView.initializeIfNecessary();
		View activity1 = tabHost.getCurrentView();
		View activity2 = abstractView.getContentView();
		

		TabSpec tabSpec = this.getTabSpecBy(index);

		if (tabSpec != null) {
			tabSpec.setContent(activity2);
		}
		
//		Animation anim = AnimationUtils.loadAnimation(this,
//		        android.R.anim.aui_activity_close_exit);
//		
//		anim.setAnimationListener(new AnimationListenerImpl());
//		activity1.setAnimation(anim);
//		activity2.setAnimation(AnimationUtils.loadAnimation(this,
//				android.R.anim.aui_activity_close_enter));
//		
//		activity1.invalidate();
//		activity2.invalidate();
		
		refreshTabView(index);
		
	}
	
	private void refreshTabView(int index){
		Log.i("yujsh log","refreshTabView");
		if(tabHost==null){
			tabHost = getTabHost();
		}
		tabHost.setCurrentTab(0);
		tabHost.clearAllTabs();
		tabHost.addTab(tabRecommend);
		tabHost.addTab(tabLeadBoard);
		tabHost.addTab(tabCategory);
		tabHost.setCurrentTab(index);
	}

	public boolean checkNetWork() { // 检测网络
		return NetUtil.checkNetwork(this);
	}

	// private void setViewStrategy(ViewStrategy strategy) {
	// mViewStrategy = strategy;
	// }
	//
	// public ViewStrategy getViewStrategy() {
	// return mViewStrategy;
	// }

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (mViewStrategy != null && keyCode == KeyEvent.KEYCODE_BACK) {
	// if (mViewStrategy.getCurrentView() != null) {
	// if (mSearchview != null) {
	// mSearchview.ed.clearFocus();
	// }
	// if (mViewStrategy.getCurrentView().ProcessBack()) {
	// if (BookShelf.backFlag) {
	// mViewStrategy.ClearAllView();
	// this.setResult(BookShelf.COLLECTREQUESTCOLDE);
	// this.finish();
	// BookShelf.backFlag = false;
	// return false;
	// }
	// return true;
	// }
	// if (mViewStrategy.backToPreviousView()) {
	// return true;
	// } else {
	// if (mIsCollectNewBook) {
	// mViewStrategy.ClearAllView();
	// this.setResult(BookShelf.COLLECTREQUESTCOLDE);
	// this.finish();
	// } else {
	// mViewStrategy.ClearAllView();
	// this.setResult(BookShelf.COLLECTREQUESTCOLDE);
	// this.finish();
	// }
	// return true;
	// }
	//
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	// public void Back() {
	//
	// if (mViewStrategy != null && mViewStrategy.getCurrentView() != null) {
	// if (mViewStrategy.getCurrentView().ProcessBack()) {
	//
	// } else {
	// if (!mViewStrategy.backToPreviousView()) {
	// if (mIsCollectNewBook) {
	// mViewStrategy.ClearAllView();
	// this.setResult(BookShelf.COLLECTREQUESTCOLDE);
	// this.finish();
	// }
	//
	// else {
	// mViewStrategy.ClearAllView();
	// this.setResult(BookShelf.COLLECTREQUESTCOLDE);
	// this.finish();
	// }
	// }
	// }
	// }
	//
	// }

	public void SetCollectNewBook() {
		mIsCollectNewBook = true;
	}

	SearchView pv;

	// private View.OnClickListener mTabListener = new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// final int id = v.getId();
	// // final ViewStrategy strategy = mViewStrategy;
	//
	// if (!strategy.isAnimationOver()) {
	// return;
	// }
	//
	// if (v == mCurrentTab) {
	// // if (R.id.id_recommend == id) {
	// // strategy.bringBottomToFront(TAB_RECOMMEND);
	// // } else if (R.id.id_category == id) {
	// // strategy.bringBottomToFront(TAB_CATEGORY);
	// // } else if (R.id.id_top == id) {
	// // strategy.bringBottomToFront(TAB_LEADBOARD);
	// // } else {
	// // strategy.bringBottomToFront(TAB_SEARCH);
	// // }
	// return;
	// }
	// if (mSearchview != null) {
	// mSearchview.ed.clearFocus();
	// }
	// switch (id) {
	// case R.id.id_recommend:
	// // if(mEntranceType == 1)
	// // {
	// // new RecommendView(BookShopActivity.this).bringSelfToFront();
	// // mEntranceType = 0;
	// // }
	// // else {
	// //
	// // }
	// if (strategy.getTabViewCount(TAB_RECOMMEND) == 0) {
	// RecommendView gv = new RecommendView(BookShopActivity.this);
	// gv.initializeIfNecessary();
	// strategy.switchToEmptyTab(TAB_RECOMMEND, gv);
	// } else {
	// strategy.switchToTab(TAB_RECOMMEND);
	// }
	// break;
	// case R.id.id_category:
	// if (strategy.getTabViewCount(TAB_CATEGORY) == 0) {
	// CategoryView av = new CategoryView(BookShopActivity.this);
	// av.initializeIfNecessary();
	// strategy.switchToEmptyTab(TAB_CATEGORY, av);
	// } else {
	// strategy.switchToTab(TAB_CATEGORY);
	// }
	// if (mSearchview != null) {
	// mSearchview.ed.clearFocus();
	// }
	// break;
	// case R.id.id_top:
	// if (strategy.getTabViewCount(TAB_LEADBOARD) == 0) {
	// LeadBoardView pv = new LeadBoardView(BookShopActivity.this);
	// pv.initializeIfNecessary();
	// strategy.switchToEmptyTab(TAB_LEADBOARD, pv);
	// } else {
	// strategy.switchToTab(TAB_LEADBOARD);
	// }
	// break;
	// case R.id.id_search:
	// if (strategy.getTabViewCount(TAB_SEARCH) == 0) {
	// SearchView pv = new SearchView(BookShopActivity.this);
	// pv.initializeIfNecessary();
	// strategy.switchToEmptyTab(TAB_SEARCH, pv);
	// mSearchview = pv;
	//
	// } else {
	// strategy.switchToTab(TAB_SEARCH);
	// }
	// break;
	// }
	//
	// if (null != mCurrentTab) {
	// mCurrentTab.setSelected(false);
	// }
	// v.setSelected(true);
	// mCurrentTab = v;
	// }
	// };

	public void makeToast(final String msg) {
		mToastHandler.post(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(BookShopActivity.this, msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	public void makeToast(final int id) {
		mToastHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(BookShopActivity.this, id, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mSearchview != null) {
			mSearchview.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater menuInflater = getMenuInflater();
	// menuInflater.inflate(R.menu.menu_file, menu); // 设置菜单显示的xml
	// // new com.idreamsky.ktouchread.menu.Menu(this,R.id.id_bookshop).init();
	// return true;
	// }

	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// if (item.getTitle().equals(getString(R.string.MyAccount))) // 我的账户
	// {
	// SwitchtoKPayAccount();
	//
	// } else if (item.getTitle().equals(getString(R.string.menu_more)))// 设置
	// {
	// Util.toActivity(this, Book_System_SettingAct.class);
	// }
	// return true;
	// }

	private void SwitchtoKPayAccount() {
		Intent intent = KPayAccount.GetUserIntent();
		startActivityForResult(intent,
				KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
		Book.Save();
	}

	public void GetUserInforError() {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					BookShopActivity.this);
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
							BookShopActivity.this
									.setResult(BookShelf.COLLECTREQUESTCOLDE);
							BookShopActivity.this.finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

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

	public boolean GetLongToken() {
		final String Token = KPayAccount.getLongtermToken();

		if (Token != null && Token.length() > 0) {
			UrlUtil.TokenTPL = Token;
			LogEx.Log_V("Token", "UrlUtil.TokenTPL :" + UrlUtil.TokenTPL);
			byte[] buffer = Base64.decodeBase64(UrlUtil.TokenTPL);
			if (buffer != null && buffer.length > 4) {
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(buffer[0]);
				stringBuffer.append(buffer[1]);
				stringBuffer.append(buffer[2]);
				stringBuffer.append(buffer[3]);
				UrlUtil.USERID = stringBuffer.toString();
				LogEx.Log_V("Token", "User ID :" + stringBuffer.toString());

			}
			return true;
		} else {
			if (UrlUtil.TokenT != null && UrlUtil.TokenT.length() > 0) {
				LogEx.Log_V("Token", "UrlUtil.TokenT :" + UrlUtil.TokenT);

				new Thread() {
					public void run() {
						if (new RequestGetToken().GetToken()) {
							KPayAccount.setLongtermToken(UrlUtil.TokenTPL);
							LogEx.Log_V("Token", "Add UrlUtil.TokenTPL :"
									+ UrlUtil.TokenTPL);

						} else {

							mHandler.sendEmptyMessage(10);
						}
					};
				}.start();
				return false;

			} else {
				// SwitchtoKPayAccount();
				return false;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_MYACCOUNT) {
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
		} else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						if (KPayAccount.getLongtermToken() == null
								|| KPayAccount.getLongtermToken().length() < 1) {
							UrlUtil.TokenTPL = null;

							UrlUtil.TokenT = extras.getString("token");
							LogEx.Log_V("Token", UrlUtil.TokenT);
							// if(UrlUtil.TokenT!= null &&
							// UrlUtil.TokenT.length() > 0)
							// {
							// GetLongToken();
							// }
							// else
							// {
							// GetUserInforError();
							// }
							this.setResult(BookShelf.COLLECTREQUESTCOLDE);
							this.finish();
						}

					} else {
						UrlUtil.TokenT = null;
						this.setResult(BookShelf.COLLECTREQUESTCOLDE);
						this.finish();
					}
				} else {
					UrlUtil.TokenT = null;
					this.setResult(BookShelf.COLLECTREQUESTCOLDE);
					this.finish();

				}

			} else {

				UrlUtil.TokenTPL = null;
				UrlUtil.TokenT = null;
				this.setResult(BookShelf.COLLECTREQUESTCOLDE);
				this.finish();
			}
		}
		// mViewStrategy.getCurrentView().UpdataUI();
		if (BookShelf.mCurrentBook != null) {
			BookShelf.mCurrentBook.CloseBook();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void InitTryDialog(String Msg) {
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					BookShopActivity.this);
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

//	View activity1 = null;
//	View activity2 = null;

	public void updateContent(String title, AbstractView abstractView) {
		int index = tabHost.getCurrentTab();

		switch (index) {
		case INDEX_LEADBOARD:
			isShowLeadBoardBack = true;
			leadBoardTitle = title;
			break;
		case INDEX_CATEGORYVIEW:
			isShowCategoryBack = true;
			categorTitle = title;
			break;
		default:
			return;
		}
		
		updateNavigationBar();

		abstractView.initializeIfNecessary();
		View activity1 = tabHost.getCurrentView();
		View activity2 = abstractView.getContentView();

		TabSpec tabSpec = this.getTabSpecBy(index);

		if (tabSpec != null) {
			tabSpec.setContent(activity2);
		}

//		Animation anim = null;
//		anim = AnimationUtils.loadAnimation(this,
//				android.R.anim.aui_activity_open_exit);
//		anim.setAnimationListener(new AnimationListenerImpl());
//		activity1.setAnimation(anim);
//		activity2.setAnimation(AnimationUtils.loadAnimation(this,
//				android.R.anim.aui_activity_open_enter));
//
//		activity1.invalidate();
//		activity2.invalidate();
		
		refreshTabView(index);

	}

	private TabSpec getTabSpecBy(int index) {
		TabSpec tabSpec = null;
		switch (index) {
		case INDEX_RECOMMEND:
			tabSpec = tabRecommend;
			break;
		case INDEX_LEADBOARD:
			tabSpec = tabLeadBoard;
			break;
		case INDEX_CATEGORYVIEW:
			tabSpec = tabCategory;
			break;
		default:
			break;
		}
		return tabSpec;
	}

	// 更新NavigationBar
	private void updateNavigationBar() {
		Builder builder = this.getNavigationBarBuilder();
		int index = tabHost.getCurrentTab();
		
		if(isShowLeadBoardBack && index == INDEX_LEADBOARD ){
			builder.showBackButton(true);
			builder.setTitle(leadBoardTitle);
		}else if(isShowCategoryBack && index == INDEX_CATEGORYVIEW){
			builder.showBackButton(true);
			builder.setTitle(categorTitle);
		}else{
			builder.showBackButton(false);
			builder.setTitle(R.string.toBookShop);
			return;
		}
		
		
		
		
	}

	// 动画监听
	private class AnimationListenerImpl implements AnimationListener {
		

		public void onAnimationEnd(Animation animation) {
//			activity1.setVisibility(View.GONE);
			Runnable runnable = new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					refreshTabView(tabHost.getCurrentTab());
				}};
			mHandler.postDelayed(runnable, 100);
			
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		}

	}

	@Override
	public void onTabChanged(int arg0, String arg1) {
		updateNavigationBar();		
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
