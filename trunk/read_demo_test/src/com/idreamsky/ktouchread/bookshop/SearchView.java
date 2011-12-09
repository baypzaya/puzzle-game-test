package com.idreamsky.ktouchread.bookshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.adapter.BookAdapterEx;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.NetBook.GetNetBookListCallback;
import com.idreamsky.ktouchread.data.net.SearchKey;
import com.idreamsky.ktouchread.data.net.SearchKey.GetKeyCallback;

public class SearchView extends AbstractView {

	public static String DefaultKey = null;
	private BookAdapterEx mBookAdapter;
	private ListView mListView = null;
	public EditText ed;
	private Button bt;
	private List<NetBook> mBookList;

	private AbsoluteLayout absoluteLayout;
	private FrameLayout frameLayout;
	private RelativeLayout layout;
	private View view;

	private int bgW;
	private int bgH;
	private int diffW;
	private int diffH;
	private LinearLayout mLinearLayoutInput;

	private List<String> searchKey = null;

	private PointF mTouch = new PointF(); // 拖拽点
	private String keyAuthor = null;
//	private Button btnBackButton = null;
	private String author;

	private InputMethodManager mInputMethodManager;

	public static interface SearchKeyCallBack {
		public void ClickKey(String Key);
	}

	public SearchView(Activity context) {
		super(context);
		mBookAdapter = new BookAdapterEx(context, new ArrayList<NetBook>());
	}

	public SearchView(Activity context, String Author) {
		super(context);
		keyAuthor = Author;
		mBookAdapter = new BookAdapterEx(context, new ArrayList<NetBook>());
	}

	@Override
	public void enrichContent(ViewGroup parent) {
		layout = (RelativeLayout) parent;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.book_onlie_search, null);
		layout.addView(view);
		
		mLinearLayoutInput =  (LinearLayout)layout.findViewById(R.id.layout_search);
		frameLayout = (FrameLayout) layout.findViewById(R.id.id_frame);
		absoluteLayout = (AbsoluteLayout) frameLayout.findViewById(R.id.layout);
		

	
	    DisplayMetrics metrics=new DisplayMetrics();
	    mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	   
        LinearLayout layout_search=(LinearLayout)view.findViewById(R.id.layout_search);
        int layout_search_height=layout_search.getLayoutParams().height;
        
        RelativeLayout layout_title=(RelativeLayout)view.findViewById(R.id.layout_book_head);
        int layout_title_height=layout_title.getLayoutParams().height;
       
	    bgH = metrics.heightPixels-(layout_title_height+layout_search_height);
		bgW = metrics.widthPixels;
		

//		btnBackButton = (Button) layout.findViewById(R.id.back_button);
//		btnBackButton.setText(R.string.toBookShelf);
//		//btnBackButton.setOnClickListener(this);
//		btnBackButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//			    if(BookShelf.backFlag){
//			    	
////					if (mListView != null && mListView.isShown()) {
////						 mListView.setVisibility(View.INVISIBLE);
////						 absoluteLayout.setVisibility(View.VISIBLE);
////						 btnBackButton.setText(R.string.toBookShelf);
////						 ed.setText(mContext.getString(R.string.reservation_keyWord));
////					}
////					else
////					{
//				    	mContext.finish();
//				    	BookShelf.backFlag=false;
////					}
//
//			    }else{
//				((BookShopActivity) mContext).Back();
//			    }
//			}
//		});
		
		

		ed = (EditText) view.findViewById(R.id.input_search);
		ed.setOnClickListener(new SearchClcikListener());
		bt = (Button) view.findViewById(R.id.click_search);
		bt.setOnClickListener(new SearchClcikListener());
        
		mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		mListView = (ListView) frameLayout.findViewById(R.id.search_listview);
		mListView.setAdapter(mBookAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		mListView.setCacheColorHint(Color.TRANSPARENT);
	
		SearchKey.GetSearchKey(new GetKeyCallback() {

			@Override
			public void onFail(String msg){
				((BookShopActivity) mContext).makeToast(msg);
			}

			@Override
			public void onUpdate(List<String> keyList) {
			    searchKey=keyList; 
				if(DefaultKey != null)
				{
					ed.setText(DefaultKey);
				}
				else
				{
					ed.setText(R.string.reservation_keyWord);
				}
			}

			@Override
			public void onSuccess(List<String> keyList) {
				searchKey = keyList;
				if(DefaultKey != null)
				{
					ed.setText(DefaultKey);
				}
				else
				{
					ed.setText(R.string.reservation_keyWord);
				}
			}
		});

		
		
//		ed.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				
//				
//				if(!hasFocus)
//				{
//					mInputMethodManager.showSoftInput(v, 0);
//				}
//			}
//		});
		

		
		
		
		
	    if(keyAuthor!=null&&keyAuthor.length()>0){
	    	ShowProcee();
	    	//java.net.URLEncoder.encode(keyAuthor.trim())
			NetBook.Search(keyAuthor.trim(),
					new GetNetBookListCallback() {

						@Override
						public void onFail(String msg) {
							Toast.makeText(mContext,"连接服务器失败",Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(List<NetBook> bookList) {
							searchSuccess(bookList);
						}

					});
	    }else{
	    	mListView.setVisibility(View.INVISIBLE);
	    }
	    
	   
		PlayAnim(absoluteLayout);
		
	   Intent intent=mContext.getIntent();
	   if(intent!=null&&intent.getExtras().get("Author")!=null)
	   {
		   author = intent.getExtras().getString("Author");
		   ed.setText((CharSequence) intent.getExtras().get("Author"));
	   }
	   
	    
	}

	private final class SearchClcikListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.click_search) {
				if (ed.getText().toString().length() < 1) {
					return;
				}
//				java.net.URLEncoder.encode(ed.getText().toString()
//						.trim())
				ShowProcee();
				NetBook.Search(ed.getText().toString().trim()
						, new GetNetBookListCallback() {

							@Override
							public void onFail(String msg) {

							}

							@Override
							public void onSuccess(List<NetBook> bookList) {
								searchSuccess(bookList);
							}

						});
			}
			// else if(v.getId()==R.id.input_search){
			// final InputMethodManager imm =
			// (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
			// }

		}

	}

	@Override
	protected void onFinishInit() {
		super.onFinishInit();
	}

	@Override
	public void onRemovedFromWindow() {

		mBookAdapter.RemoveAll();
		if (mBookList != null)
			mBookList.clear();

		if (searchKey != null)
			searchKey.clear();

	}

	public class KeyItem implements View.OnClickListener {
		float aboveFromXDelta;
		float aboveFromYDelta;
		float aboveToXDelta;
		float aboveToYDelta;
		TranslateAnimation translateAnimation;
		String name;
		AbsoluteLayout view;
		int time;
		Context context;
		TextView mTextView;
		int id;
		boolean visiable;

		SearchKeyCallBack mSearchKeyCallBack;

		/**
		 * @param aboveFromXDelta
		 *            动画起始的时候物件对象的X轴的坐标
		 * @param aboveFromYDelta
		 *            动画起始的时候物件对象的Y轴的坐标
		 * @param aboveToXDelta
		 *            动画结束时物件对象的X轴坐标
		 * @param aboveToYDelta
		 *            动画结束时物件对象的Y轴坐标
		 * @param context
		 *            ...
		 * @param name
		 *            ...
		 * @param view
		 *            ...
		 * @param time
		 *            动画间隔时间
		 */
		KeyItem(float aboveFromXDelta, float aboveFromYDelta,
				float aboveToXDelta, float aboveToYDelta, Context context,
				String name, AbsoluteLayout view, int time, int id) {
			this.aboveFromXDelta = aboveFromXDelta;
			this.aboveFromYDelta = aboveFromYDelta;
			this.aboveToXDelta = aboveToXDelta;
			this.aboveToYDelta = aboveToYDelta;
			this.name = name;
			this.view = view;
			this.time = time;
			this.context = context;
			this.id = id;
		}

		public void startAnimation() {
			translateAnimation = new TranslateAnimation(aboveFromXDelta,
					aboveToXDelta, aboveFromYDelta, aboveToYDelta);

			mTextView = (TextView) view.findViewById(id);
			mTextView.setText(name);
			mTextView.setAnimation(translateAnimation);
			mTextView.setOnClickListener(this);

			translateAnimation.setDuration(time);
			translateAnimation.start();

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String key = mTextView.getText().toString();
			// Toast.makeText(mContext, key, 50).show();
			ed.setText(key);
			if (key != null && key.length() > 0) {
				ShowProcee();
				NetBook.Search(key,
						new GetNetBookListCallback() {

							@Override
							public void onFail(String msg) {
								searchFail(msg);
								if(DefaultKey != null)
								{
									ed.setText(DefaultKey);
								}
								else
								{
									ed.setText(R.string.reservation_keyWord);
								}
								
							}

							@Override
							public void onSuccess(List<NetBook> bookList) {
								searchSuccess(bookList);
							}
						});
			}

		}

	}

	private void searchFail(String msg) {

		DismissProcess();
		((BookShopActivity) mContext).makeToast(R.string.search_fail);
		mListView.setVisibility(View.INVISIBLE);
		absoluteLayout.setVisibility(View.VISIBLE);
	}

	private void searchSuccess(List<NetBook> bookList) {
		
		if (bookList.size() < 1) {
			// ((BookShopActivity) mContext).Back();
			Toast.makeText(mContext, mContext.getString(R.string.search_fail),
					Toast.LENGTH_LONG).show();
			DismissProcess();
			return;

		}
		mBookList = bookList;
		mBookAdapter = new BookAdapterEx(mContext, new ArrayList<NetBook>());
		mListView.setAdapter(mBookAdapter);
		mBookAdapter.SetListView(mListView);
		mBookAdapter.AddNew(mBookList);
		DismissProcess();
		absoluteLayout.setVisibility(View.INVISIBLE);
		mListView.setVisibility(View.VISIBLE);
//		btnBackButton.setText(R.string.back);

//		if (BookShelf.backFlag) {
//			ed.setText(mBookList.get(0).authorname);
//		}

	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mBookList != null && position >= 0) {
				NetBook book = mBookList.get(position);
//				ViewStrategy viewStrategy = ((BookShopActivity) mContext)
//						.getViewStrategy();
//				BookDetailView cv = (BookDetailView) viewStrategy.getView(
//						viewStrategy.getCurrentTab(), BookDetailView.class);
//				if (cv == null) {
//					new BookDetailView(mContext, book).bringSelfToFront();
//				} else {
//					cv.SetBook(book);
//					cv.bringSelfToFront();
//				}
			}
		}
	};

	protected boolean ProcessBack() {
		// TODO 处理返回键事件：如果有列表，返回到搜索页面，返回true。如果没有搜索列表，返回到主页面，返回false

		if(BookShelf.backFlag)
			return false;
		if (mListView != null && mListView.isShown()) {
			mListView.setVisibility(View.INVISIBLE);
			absoluteLayout.setVisibility(View.VISIBLE);
//			btnBackButton.setText(R.string.toBookShelf);
			
			if(DefaultKey != null)
			{
				ed.setText(DefaultKey);
			}
			else
			{
				ed.setText(R.string.reservation_keyWord);
			}
			return true;
		}

		return false;
	}

	protected void onBackToView() {
		mLinearLayoutInput.removeAllViews();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		view = inflater.inflate(R.layout.book_onlie_search_input, null);
		mLinearLayoutInput.addView(view);

		ed = (EditText) view.findViewById(R.id.input_search);
		ed.setOnClickListener(new SearchClcikListener());
		
		if (BookShelf.backFlag && author != null) {
		   ed.setText(author);
	     }
		bt = (Button) view.findViewById(R.id.click_search);
		bt.setOnClickListener(new SearchClcikListener());

	}

	protected void ChangeTab(boolean bIn) {

		if (bIn) {
			((BookShopActivity) mContext).mSearchview = this;
		} else {
			((BookShopActivity) mContext).mSearchview = null;
		}

	}

	public void onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mTouch.x = event.getX();
			mTouch.y = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			if (Math.abs(event.getX() - mTouch.x) > 100) {

				if (absoluteLayout != null) {
					PlayAnim(absoluteLayout);
				}

			}
		}

	}

	int textIds[] = { R.id.KEY_TYPE_TOP_CENTET, R.id.KEY_TYPE_TOP_SECEND_LEFT,
			R.id.KEY_TYPE_TOP_SENEND_RIGHT, R.id.KEY_TYPE_TOP_THIRD_LEFT,
			R.id.KEY_TYPE_TOP_THIRD_RIGHT, R.id.KEY_TYPE_CENTER_LEFT,
			R.id.KEY_TYPE_CENTER_Center, R.id.KEY_TYPE_CENTER_RIGHT,
			R.id.KEY_TYPE_BOTTOM_SECEND_LEFT,
			R.id.KEY_TYPE_BOTTOM_SECEND_RIGHT, R.id.KEY_TYPE_BOTTOM_THIRD_LEFT,
			R.id.KEY_TYPE_BOTTOM_THIRD_RIGHT, R.id.KEY_TYPE_BOTTOM_CENTER };

	private void PlayAnim(AbsoluteLayout layout) {

		if (searchKey != null) {
			Collections.shuffle(searchKey);
			int size = searchKey.size();

			if (size < 13) {
				for (int i = 0; i < size; i++) {
					KeyItem keyItem = new KeyItem(new Random().nextInt(bgW),
							new Random().nextInt(bgH), 0, 0,
							layout.getContext(), searchKey.get(i),
							absoluteLayout, 2000, textIds[i]);
					keyItem.startAnimation();
				}
			} else {

				/* 上面5个 */
				KeyItem KEY_TYPE_TOP_CENTET = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(0),
						absoluteLayout, 2000, R.id.KEY_TYPE_TOP_CENTET);
				KEY_TYPE_TOP_CENTET.startAnimation();

				KeyItem KEY_TYPE_TOP_SECEND_LEFT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(1), layout,
						2000, R.id.KEY_TYPE_TOP_SECEND_LEFT);
				KEY_TYPE_TOP_SECEND_LEFT.startAnimation();

				KeyItem KEY_TYPE_TOP_SENEND_RIGHT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(2), layout,
						2000, R.id.KEY_TYPE_TOP_SENEND_RIGHT);
				KEY_TYPE_TOP_SENEND_RIGHT.startAnimation();

				KeyItem KEY_TYPE_TOP_THIRD_LEFT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(3), layout,
						2000, R.id.KEY_TYPE_TOP_THIRD_LEFT);
				KEY_TYPE_TOP_THIRD_LEFT.startAnimation();

				KeyItem KEY_TYPE_TOP_THIRD_RIGHT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(4), layout,
						2000, R.id.KEY_TYPE_TOP_THIRD_RIGHT);
				KEY_TYPE_TOP_THIRD_RIGHT.startAnimation();

				/* 中间3个 */
				KeyItem KEY_TYPE_CENTER_LEFT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(5), layout,
						2000, R.id.KEY_TYPE_CENTER_LEFT);
				KEY_TYPE_CENTER_LEFT.startAnimation();

				KeyItem KEY_TYPE_CENTER_Center = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(6), layout,
						2000, R.id.KEY_TYPE_CENTER_Center);
				KEY_TYPE_CENTER_Center.startAnimation();

				KeyItem KEY_TYPE_CENTER_RIGHT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(7), layout,
						2000, R.id.KEY_TYPE_CENTER_RIGHT);
				KEY_TYPE_CENTER_RIGHT.startAnimation();

				/* 下面5个 */
				KeyItem KEY_TYPE_BOTTOM_SECEND_LEFT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(8), layout,
						2000, R.id.KEY_TYPE_BOTTOM_SECEND_LEFT);
				KEY_TYPE_BOTTOM_SECEND_LEFT.startAnimation();

				KeyItem KEY_TYPE_BOTTOM_SECEND_RIGHT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(9), layout,
						2000, R.id.KEY_TYPE_BOTTOM_SECEND_RIGHT);
				KEY_TYPE_BOTTOM_SECEND_RIGHT.startAnimation();

				KeyItem KEY_TYPE_BOTTOM_THIRD_LEFT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(10), layout,
						2000, R.id.KEY_TYPE_BOTTOM_THIRD_LEFT);
				KEY_TYPE_BOTTOM_THIRD_LEFT.startAnimation();

				KeyItem KEY_TYPE_BOTTOM_THIRD_RIGHT = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(11), layout,
						2000, R.id.KEY_TYPE_BOTTOM_THIRD_RIGHT);
				KEY_TYPE_BOTTOM_THIRD_RIGHT.startAnimation();

				KeyItem KEY_TYPE_BOTTOM_CENTER = new KeyItem(
						new Random().nextInt(bgW), new Random().nextInt(bgH),
						0, 0, layout.getContext(), searchKey.get(12), layout,
						2000, R.id.KEY_TYPE_BOTTOM_CENTER);
				KEY_TYPE_BOTTOM_CENTER.startAnimation();

			}
		}

	}

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		
	}

}
