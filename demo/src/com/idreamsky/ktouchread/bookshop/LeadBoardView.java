package com.idreamsky.ktouchread.bookshop;

import java.util.ArrayList;
import java.util.List;


import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.adapter.TopAdapter;

import com.idreamsky.ktouchread.data.net.Top;

import android.app.Activity;

import android.graphics.Color;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class LeadBoardView extends AbstractView {

	private TopAdapter mTopAdapter = null;
	private ListView mListView;
	private List<Top> mTopList = null;


	public LeadBoardView(Activity context) {
		super(context);
		mTopAdapter = new TopAdapter(context, new ArrayList<Top>());

	}

	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;

		LinearLayout layoutBook = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.book_shop_categorylist, null);

//		Button btnBackButton = (Button) layoutBook
//				.findViewById(R.id.back_button);
//		btnBackButton.setText(R.string.toBookShelf);
//		btnBackButton.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				((BookShopActivity) mContext).Back();
//
//			}
//		});

		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
		iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(layoutBook, iconParams);

		mListView = (ListView) layoutBook.findViewById(R.id.list);
		mListView.setAdapter(mTopAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
		mListView.setCacheColorHint(Color.TRANSPARENT);
	}

	@Override
	public void onFinishInit() {

		LoadData();
	}

	public void LoadData()
	{
		ShowProcee();
		Top.GetTopList(new Top.GetTopListCallback() {

			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				DismissProcess();
				Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, msg);
				//((BookShopActivity) mContext).mHandler.sendMessage(message);
				if(BookShopActivity.mNetCheckNum == 0){
					((BookShopActivity) mContext).mHandler.sendMessage(message);
					BookShopActivity.mNetCheckNum++;
				}
				

			}

			@Override
			public void onSuccess(List<Top> topList,boolean bFalseData) {
				// TODO Auto-generated method stub
				mTopList = topList;
				mTopAdapter.appendMore(mTopList);
				if(!bFalseData)
				  DismissProcess();

			}

			@Override
			public void onUpdate(List<Top> topList) {
				// TODO Auto-generated method stub
				if (mTopList != null)
					mTopList.clear();
				mTopList = topList;
				mTopAdapter.AddNew(mTopList);
				DismissProcess();

			}
		});
	}
	@Override
	public void onRemovedFromWindow() {
		mTopAdapter.RemoveAll();
		if (mTopList != null)
			mTopList.clear();
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (0 <= position && position < mTopList.size()) {
				Top top = mTopList.get(position);
				if (top.toplistid.compareTo("-1") == 0)
					return;
//				ViewStrategy viewStrategy = ((BookShopActivity) mContext)
//						.getViewStrategy();
//				BookListView cv = (BookListView) viewStrategy.getView(
//						viewStrategy.getCurrentTab(), BookListView.class);
//				if (cv == null) {
//					BookListView bookListView = new BookListView(mContext, top);
//					bookListView.bringSelfToFront();
//				} else {
//					cv.SetTop(top);
//					cv.bringSelfToFront();
//				}
				
				BookShopActivity bookShopActivity = (BookShopActivity) mContext;
				BookListView bookListView = new BookListView(mContext, top);
				bookShopActivity.updateContent(top.toplistname,bookListView);
			}
		}
	};

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		LoadData();
		
	}

}