package com.idreamsky.ktouchread.bookshelf;

import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aliyun.aui.app.spirit.SlideTabActivity;
import com.aliyun.aui.widget.spirit.SlideTabHost;
import com.aliyun.aui.widget.spirit.SlideTabHost.OnTabChangeListener;
import com.aliyun.aui.widget.spirit.SlideTabHost.TabSpec;
import com.idreamsky.ktouchread.bookmarkfactory.BookMarkFactoryActivity;
import com.idreamsky.ktouchread.bookshop.BookShopActivity;

public class ReadTabActivity extends SlideTabActivity implements
		OnTabChangeListener {

	public static final int INDEX_BOOK_SHELF = 0;
	public static final int INDEX_BOOK_SHOP = 1;
	public static final int INDEX_BOOK_MARK_FACTORY = 2;
	public static final int INDEX_BOOK_SYSTEM_SETTING = 3;

	public static ReadTabActivity sReadTabActivity;

	private SlideTabHost mTabHost;
	private TabSpec tabSpecBookShelf;
	private TabSpec tabSpecBookShop;
	private TabSpec tabSpecBookMarkFactory;
	private TabSpec tabSpecBook_System_SettingAct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sReadTabActivity = this;

		mTabHost = super.getTabHost();

		tabSpecBookShelf = mTabHost.newTabSpec("书架");
		tabSpecBookShop = mTabHost.newTabSpec("书城");
		tabSpecBookMarkFactory = mTabHost.newTabSpec("书签");
		tabSpecBook_System_SettingAct = mTabHost.newTabSpec("管理");
		
		resetAllTabSpec();
		
		mTabHost.addTab(tabSpecBookShelf);
		mTabHost.addTab(tabSpecBookShop);
		mTabHost.addTab(tabSpecBookMarkFactory);
		mTabHost.addTab(tabSpecBook_System_SettingAct);

		this.setDefaultTab("书架");

		mTabHost.setOnTabChangedListener(this);
	}

	private void resetAllTabSpec() {
		for(int i = 0;i<4;i++){
			resetTabSpecBy(i);
		}
	}

	private void changeTab(int index, Intent intent) {
		//恢复当前标签页的content
		int curIndex = mTabHost.getCurrentTab();
		resetTabSpecBy(curIndex);
		
		//修正目标标签页的content，并打开目标标签页
		TabSpec tabSpec = getTabSpecBy(index);
		if (tabSpec != null) {
			tabSpec.setContent(intent);
			mTabHost.setCurrentTabByTag(tabSpec.getTag());
		}
	}

	/**
	 * 获取指定index的Tabspec
	 * @param index
	 * @return
	 */
	private TabSpec getTabSpecBy(int index) {
		TabSpec tabSpec = null;
		switch (index) {
		case INDEX_BOOK_SHELF:
			tabSpec = tabSpecBookShelf;
			break;
		case INDEX_BOOK_SHOP:
			tabSpec = tabSpecBookShop;
			break;
		case INDEX_BOOK_MARK_FACTORY:
			tabSpec = tabSpecBookMarkFactory;
			break;
		case INDEX_BOOK_SYSTEM_SETTING:
			tabSpec = tabSpecBook_System_SettingAct;
			break;
		default:
			break;
		}

		return tabSpec;
	}

	@Override
	public void onTabChanged(int index, String tag) {
//		resetTabSpecBy(index);
	}

	private void resetTabSpecBy(int index) {
		Intent intent = new Intent();
		TabSpec tabSpec = getTabSpecBy(index);

		switch (index) {
		case INDEX_BOOK_SHELF:
			intent = this.getIntent();
			if (intent == null) {
				intent = new Intent();
			}
			intent.setClass(this, BookShelf.class);
			break;
		case INDEX_BOOK_SHOP:
			intent.putExtra(BookShelf.ENTRANCE, BookShelf.MYBOOKSHELF);
			intent.setClass(this, BookShopActivity.class);
			break;
		case INDEX_BOOK_MARK_FACTORY:
			intent.setClass(this, BookMarkFactoryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		case INDEX_BOOK_SYSTEM_SETTING:
			intent.setClass(this, Book_System_SettingAct.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		default:
			intent = null;
			break;
		}

		if (tabSpec != null && intent != null) {
			Log.i("yujsh log","set content :"+index+" tag:"+tabSpec.getTag());
			tabSpec.setContent(intent);
		}

	}

	public static void changeTabBy(int index, Intent intent) {
		sReadTabActivity.changeTab(index, intent);
	}

}