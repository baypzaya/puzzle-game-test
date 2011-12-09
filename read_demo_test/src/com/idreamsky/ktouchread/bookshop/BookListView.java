package com.idreamsky.ktouchread.bookshop;


import java.util.ArrayList;
import java.util.List;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.adapter.BookAdapter;
import com.idreamsky.ktouchread.data.net.Category;
import com.idreamsky.ktouchread.data.net.NetBook;
import com.idreamsky.ktouchread.data.net.Top;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class BookListView extends AbstractView {
	private Category mCategory;

	private ListView mListView;
	private List<NetBook> mBookList = null;
	private List<NetBook> mBookListInit = null;
	private BookAdapter mBookAdapter;
	private Top mTop;
	
	private int mType = 0; //0:category,1:top
	
	private TextView mTextTitle;
	public int PageIndex = 0;
	private boolean HasLoadData = false;
	
	public BookListView(Activity context) {
		super(context);
		PageIndex=0;
		mBookListInit = new ArrayList<NetBook>();
		for(int i = 0 ; i < 5 ;i++)
		{
			NetBook book = new NetBook();
			book.bookid = "-1";
			mBookListInit.add(book);
			
		}
		mBookAdapter = new BookAdapter(mContext,mBookListInit);
	}
	public BookListView(Activity context,Category category) {
		super(context);
		mBookList = new ArrayList<NetBook>();
		for(int i = 0 ; i < 5 ;i++)
		{
			NetBook book = new NetBook();
			book.bookid = "-1";
			mBookList.add(book);
			
		}
		mBookAdapter = new BookAdapter(mContext,mBookList);
		mCategory = category;
		mType = 0;
	}

	public BookListView(Activity context,Top top) {
		super(context);
		mBookList = new ArrayList<NetBook>();
		for(int i = 0 ; i < 5 ;i++)
		{
			NetBook book = new NetBook();
			book.bookid = "-1";
			mBookList.add(book);
			
		}
		mBookAdapter = new BookAdapter(mContext,mBookList);
		mType = 1;
		mTop = top;
	}

	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;
		  
		LinearLayout layoutBook = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.book_shop_booklist, null);
		
//        Button btnBackButton = (Button)layoutBook.findViewById(R.id.back_button);
//        btnBackButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((BookShopActivity) mContext).Back();
//				
//			}
//		});
        
        mTextTitle = (TextView)layoutBook.findViewById(R.id.textview_head_title);
        if(mType == 0)
        {
        	mTextTitle.setText(mCategory.categoryname);
        }
        else {
        	mTextTitle.setText(mTop.toplistname);
		}

		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
		iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(layoutBook, iconParams);
		
//		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
//		View v =  layoutInflater.inflate(R.layout.page, null);
//		pre = (Button) v.findViewById(R.id.per);
//		next = (Button) v.findViewById(R.id.next);
//		pre.setOnClickListener(pageClickListener);
//		next.setOnClickListener(pageClickListener);
//		pre.setEnabled(false);
//		next.setEnabled(false);
		
		mListView = (ListView) layoutBook.findViewById(R.id.booklist);
//		mListView.addFooterView(v);
		mListView.setAdapter(mBookAdapter);
		
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mBookAdapter.SetListView(mListView);
//		
//		mListView.setOnScrollListener(new OnScrollListener() {
//			
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//                LogEx.Log_V("test" , "Scroll>>>first: " + firstVisibleItem + ", visible: " + visibleItemCount + ", total: " + totalItemCount); 
//                int lastItem = firstVisibleItem + visibleItemCount - 1; 
//              //  LogEx.Log_V("test" , "Scroll>>>lastItem:" + lastItem);
//                if(Math.abs(lastItem - mlastItem) > 50)
//                {
//                	mlastItem = lastItem;
//                	ShowProcee();
//                	System.gc();
//                	new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//							DismissProcess();
//							
//						}
//					}, 2000);
//                }
//                 
//				
//			}
//		});
		
	
	}
	
//	public OnClickListener pageClickListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			
//			if(v.getId()==R.id.next) //下一页
//			{
//				PageIndex++;
//				ChangeData(true);
//				
//			}else
//			{
//				PageIndex--;
//				ChangeData(false);
//			}
//			mListView.setSelection(0);
//		}
//	};

	@Override
	public void onFinishInit() {

		LoadData();

	}

	public void LoadData()
	{
		ShowProcee();
		new Thread(){
			public void run() {
				if(mType == 0)
				{
					GetCategoryBookList();
				}
				else {
					GetTopBookList();
				}
			};
		}.start();
	}
	@Override
	public void onRemovedFromWindow() {
		
		mBookAdapter.RemoveAll();
		if(mBookList != null)
			mBookList.clear();	
	}

	public void SetCategory(Category category) {
		ShowProcee();
		mCategory = category;
		mType = 0;
		GetCategoryBookList();
	}
	public void SetTop(Top top)
	{
		ShowProcee();
		mTop = top;
		mType = 1;
		GetTopBookList();
	}

	private void GetCategoryBookList() {
		NetBook.GetCategoryBookList(mCategory,1,99999,
				new NetBook.GetNetBookListCallback() {

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						DismissProcess();
						Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, msg);
						((BookShopActivity) mContext).mHandler.sendMessage(message);
					}

					@Override
					public void onSuccess(List<NetBook> bookList) {
						//mBookList.clear();
						mBookList = bookList;
						mBookAdapter.AddNew(mBookList);
//						totalSize  = mBookList.size();
//						PageNum = totalSize/PageBookNum;
//						if(totalSize % PageBookNum != 0 )
//							PageNum++;
//						ChangeData(false);
						
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								HasLoadData = true;
								mListView.setOnItemClickListener(mItemClickListener);
							}
						}, 1000);
						

						//mBookAdapter.AddNew(mBookList);
						DismissProcess();

					}
				});
	}
	int totalSize = 0;
	private void GetTopBookList()
	{
		Log.i("yujsh log","GetTopBookList()");
		NetBook.GetTopBookList(mTop, new NetBook.GetNetBookListCallback() {
			
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				Log.i("yujsh log","onFail");
				DismissProcess();
				Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, msg);
				((BookShopActivity) mContext).mHandler.sendMessage(message);
			}
			
			@Override
			public void onSuccess(List<NetBook> bookList) {
				Log.i("yujsh log","onSuccess");
				// TODO Auto-generated method stub
			//	mBookList.clear();
				mBookList = bookList;
				mBookAdapter.AddNew(mBookList);
//				totalSize  = mBookList.size();
//				PageNum = totalSize/PageBookNum;
//				if(totalSize % PageBookNum != 0 )
//					PageNum++;
//				ChangeData(true);
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						HasLoadData = true;
						mListView.setOnItemClickListener(mItemClickListener);
					}
				}, 1000);
//				mBookAdapter.AddNew(mBookList);
				DismissProcess();
				
			}
		});
	}
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(!HasLoadData){
				return;
			}else
			{
				int pos = position;//PageIndex*PageBookNum + position;
				if(0 <= pos && mBookList.size() > pos)
				{
					
					NetBook book = mBookList.get(pos);
					if(book.bookid.compareTo("-1") == 0)
						return;
//					ViewStrategy viewStrategy = ((BookShopActivity) mContext).getViewStrategy();
//			        BookDetailView cv = (BookDetailView) viewStrategy.getView(viewStrategy.getCurrentTab(), BookDetailView.class);
//			        if (cv == null) {
//				        new BookDetailView(mContext,book).bringSelfToFront();
//			         } else 
//			         {
//			        	 cv.SetBook(book);
//				         cv.bringSelfToFront();
//			         }
				}
			}
		}
	};
//	public void ChangeData(boolean bShowProcess)
//	{
//
//		System.gc();
//		if(PageNum == 1)
//		{
//			pre.setEnabled(false);
//			next.setEnabled(false);
//		}
//		else if(PageIndex<=0)
//		{
//			PageIndex=0;
//			pre.setEnabled(false);
//			next.setEnabled(true);
//		}else if( (PageIndex+1)>=PageNum )
//		{
//			PageIndex = PageNum - 1;
//			pre.setEnabled(true);
//			next.setEnabled(false);
//		}
//		else
//		{
//			pre.setEnabled(true);
//			next.setEnabled(true);
//		}
//		
//		if(bShowProcess)
//		{
//	    	ShowProcee();
//	    	
//	    	new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					System.gc();
//					
//					mBookAdapter.RemoveAll();
//					//mBookAdapter.ClearBitmapPool();
//					
//					int firstItem = PageIndex * PageBookNum;
//					int lastItem = PageIndex * PageBookNum + PageBookNum -1;
//					if(lastItem >= totalSize)
//					{
//						lastItem = totalSize-1;
//					}
//					mBookAdapter.AddNew(mBookList.subList(firstItem, lastItem));
//					pre.setVisibility(View.VISIBLE);
//					next.setVisibility(View.VISIBLE);
//					//Toast.makeText(mContext, "pageIndex is "+PageIndex+"  pageNum is "+ PageNum, 1).show();
//					
//					DismissProcess();
//					
//				}
//			}, 2000);
//		}
//		else
//		{
//			System.gc();
//			
//			mBookAdapter.RemoveAll();
//			//mBookAdapter.ClearBitmapPool();
//			
//			int firstItem = PageIndex * PageBookNum;
//			int lastItem = PageIndex * PageBookNum + PageBookNum -1;
//			if(lastItem >= totalSize)
//			{
//				lastItem = totalSize-1;
//			}
//			mBookAdapter.AddNew(mBookList.subList(firstItem, lastItem));
//			pre.setVisibility(View.VISIBLE);
//			next.setVisibility(View.VISIBLE);
//		}
//
//
//
//		
//	}
	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		LoadData();
	}
	
}