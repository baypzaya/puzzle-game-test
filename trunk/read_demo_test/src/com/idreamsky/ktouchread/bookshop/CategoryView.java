package com.idreamsky.ktouchread.bookshop;

import java.util.ArrayList;
import java.util.List;
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

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.adapter.CategoryAdapter;
import com.idreamsky.ktouchread.data.net.Category;


public class CategoryView extends AbstractView {

	private List<Category> mCategoryList = null;
	
	private CategoryAdapter mCategoryAdapter = null;
	private ListView mListView;
	
//	private TextView category_name = null;
//	private TextView book_ol_all_book;

	public CategoryView(Activity context) {
		super(context);
		
		mCategoryAdapter = new CategoryAdapter(context,new ArrayList<Category>());
	}

	@Override
	public void enrichContent(ViewGroup parent) {		
		final RelativeLayout layout = (RelativeLayout) parent;
		        
		LinearLayout layoutBook = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.book_shop_categorylist, null);
		
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

		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
		iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		layout.addView(layoutBook, iconParams);
		
		mListView = (ListView) layoutBook.findViewById(R.id.list);
		mListView.setAdapter(mCategoryAdapter);
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
		Category.GetCategoryList(new Category.GetCategoryListCallback() {

			@Override
			public void onFail(String msg) {
				DismissProcess();
				
				Message message = ((BookShopActivity) mContext).mHandler.obtainMessage(12, msg);
				((BookShopActivity) mContext).mHandler.sendMessage(message);
			}

			@Override
			public void onSuccess(List<Category> categorylist,boolean bFalseData) {
				mCategoryList = categorylist;
				mCategoryAdapter.AddNew(mCategoryList);	
				if(!bFalseData)
				   DismissProcess();
			}
			@Override
			public void onUpdate(List<Category> categorylist){
				if(mCategoryList != null)
				    mCategoryList.clear();
				mCategoryList = categorylist;
				mCategoryAdapter.AddNew(categorylist);
				DismissProcess();
			}
		});
	}
	@Override
	public void onRemovedFromWindow() {
		mCategoryAdapter.RemoveAll();
		if(mCategoryList != null)
		      mCategoryList.clear();
	}

	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		       		 
			if (mCategoryList != null) {
				final Category category = mCategoryList.get(position);
				if(category.categoryid.compareTo("-1") == 0)
					return;
//				ViewStrategy viewStrategy = ((BookShopActivity) mContext).getViewStrategy();
//		        BookListView cv = (BookListView) viewStrategy.getView(viewStrategy.getCurrentTab(),BookListView.class);
//		        if (cv == null) {
//			       BookListView bookListView = new BookListView(mContext,category);
//			       bookListView.bringSelfToFront();
//		        } else {
//			       cv.SetCategory(category);
//			       cv.bringSelfToFront();
//		         }
				
				BookListView bookListView = new BookListView(mContext,category);
				((BookShopActivity) mContext).updateContent(category.categoryname, bookListView);
			}
		}
	};

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		LoadData();
	}
}