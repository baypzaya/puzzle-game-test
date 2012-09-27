package com.gmail.txyjssr.images;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabViewActivity extends TabActivity {
	// 声明TabHost对象
	TabHost mTabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_view_layout);

		// 取得TabHost对象
		mTabHost = getTabHost();

		/* 为TabHost添加标签 */
		Intent testIntent1 = new Intent(this, ImagesTimeActivity.class);
		testIntent1.putExtra("test_message", "1");
		Intent testIntent2 = new Intent(this, ImagesTimeActivity.class);
		testIntent2.putExtra("test_message", "2");
		// Intent testIntent3 = new Intent(this, TestActivity.class);
		// testIntent3.putExtra("test_message", "3");

		String tabTileRecently = getString(R.string.tab_title_recently);
		String tabTileAllBooks = getString(R.string.tab_title_all_book);
		
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
				.setIndicator(tabTileRecently).setContent(testIntent1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
				.setIndicator(tabTileAllBooks).setContent(testIntent2));

		// 设置TabHost的背景颜色
		mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));

		// 设置当前显示哪一个标签
		mTabHost.setCurrentTab(0);

		// 标签切换事件处理，setOnTabChangedListener
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
			}
		});
	}
}
