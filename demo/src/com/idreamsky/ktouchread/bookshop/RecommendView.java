package com.idreamsky.ktouchread.bookshop;

import java.util.ArrayList;
import java.util.List;

import com.idreamsky.ktouchread.bookshelf.Poster;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.adapter.BookAdapterEx;
import com.idreamsky.ktouchread.data.net.Advert;
import com.idreamsky.ktouchread.data.net.NetBook;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;



public class RecommendView extends AbstractView {
	private List<NetBook> mBookList = null;
	private BookAdapterEx mBookAdapter;
	private int TitleID = 0x52;
	private ListView mListView = null;
	private Poster mPoster;
	  private ImageView fenge;
	  private ImageView fenge1;
	public RecommendView(Activity context) {
		super(context);
		mBookList = new ArrayList<NetBook>();
		for(int i = 0 ; i < 4 ;i++)
		{
			NetBook book = new NetBook();
			book.bookid = "-1";
			mBookList.add(book);
			
		}
		mBookAdapter = new BookAdapterEx(mContext,mBookList);
	}

	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;
		
		LinearLayout layoutBook = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.book_onlie_recommend, null);
        
//        Button btnBackButton = (Button)layoutBook.findViewById(R.id.back_button);
//        btnBackButton.setText(R.string.toBookShelf);
//        btnBackButton.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((BookShopActivity) mContext).Back();
//				
//			}
//		});
        fenge = (ImageView) layoutBook.findViewById(R.id.fenge);
        fenge.getBackground().setAlpha(75);
        fenge.invalidate();
        fenge1 = (ImageView) layoutBook.findViewById(R.id.fenge1);
        fenge1.getBackground().setAlpha(75);
        fenge1.invalidate();


		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
		iconParams.addRule(RelativeLayout.BELOW,TitleID);
		iconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout.addView(layoutBook, iconParams);
		
		mListView = (ListView) layout.findViewById(R.id.lvMyBookShelf);
		mListView.setAdapter(mBookAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mBookAdapter.SetListView(mListView);
		
		//mPoster = new Poster(mContext,layoutBook,Poster.bookShop);
		/*
		mPoster.SetCallback(new Poster.clickAdvertDataCallBack() {
			
			@Override
			public void getAdverDataCallBack(Advert advert) {
				// TODO Auto-generated method stub
				
//				ViewStrategy viewStrategy = ((BookShopActivity) mContext).getViewStrategy();
//				BookDetailView cv = (BookDetailView) viewStrategy.getView(
//						viewStrategy.getCurrentTab(), BookDetailView.class);
//				if (cv == null) {
//					new BookDetailView(mContext,advert).bringSelfToFront();
//				} else {
//					cv.SetAdvert(advert);
//					cv.bringSelfToFront();
//				}
				
			}
		});
		mPoster.showPosterList();
		*/
	}

	@Override
	public void onFinishInit() {	       		

		LoadData();
	}

	private void LoadData()
	{
		   ShowProcee();
		    NetBook.GetRecommenBookList(new NetBook.GetNetBookListCallback() {
				
				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub
					DismissProcess();
					//((BookShopActivity) mContext).makeToast(msg);
					Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, msg);
					//((BookShopActivity) mContext).mHandler.sendMessage(message);
					if(BookShopActivity.mNetCheckNum == 0){
						((BookShopActivity) mContext).mHandler.sendMessage(message);
						BookShopActivity.mNetCheckNum++;
					}
				}				
				@Override
				public void onSuccess(List<NetBook> bookList) {
					// TODO Auto-generated method stub
					mBookList.clear();
					mBookList = bookList;
					mBookAdapter.AddNew(mBookList);
					DismissProcess();
					
//					Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, "网络超时");
//					((BookShopActivity) mContext).mHandler.sendMessage(message);

				}
			});
	}
	@Override
	public void onRemovedFromWindow() {
		/*
		if(mPoster!=null)
		{
			mPoster.stopPoster();
		}
		*/
		mBookAdapter.RemoveAll();
		if(mBookList != null)
		     mBookList.clear();
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (0 <= position && position < mBookList.size()) {
				NetBook book = mBookList.get(position);
				if(book.bookid.compareTo("-1") == 0)
					return;
//				ViewStrategy viewStrategy = ((BookShopActivity) mContext)
//						.getViewStrategy();
//				BookDetailView cv = (BookDetailView) viewStrategy.getView(
//						viewStrategy.getCurrentTab(), BookDetailView.class);
//				if (cv == null) {
//					new BookDetailView(mContext,book).bringSelfToFront();
//				} else {
//					cv.bringSelfToFront();
//				}
				Intent intent = new Intent(mContext,BookDetailActivity.class);
				intent.putExtra(BookDetailActivity.EXTRA_SCR_TYPE, BookDetailActivity.TYPE_BOOK);
				intent.putExtra(BookDetailActivity.EXTRA_BOOKID, book.bookid);
				intent.putExtra(BookDetailActivity.EXTRA_CPCODE, book.cpcode);
				intent.putExtra(BookDetailActivity.EXTRA_RPID, book.rpid);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);

			}
		}
	};
	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		LoadData();
	}

}