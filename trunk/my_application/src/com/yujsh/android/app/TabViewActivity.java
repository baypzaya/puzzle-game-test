package com.yujsh.android.app;


import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabViewActivity extends TabActivity {
	// ����TabHost����
	TabHost mTabHost;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.mytest);
		setContentView(R.layout.tab_view_layout);

		// ȡ��TabHost����
		mTabHost = getTabHost();

		/* ΪTabHost��ӱ�ǩ */
		// �½�һ��newTabSpec(newTabSpec)
		// �������ǩ��ͼ��(setIndicator)
		// ��������(setContent)
		Intent testIntent1 = new Intent(this, GridViewActivity.class);
		testIntent1.putExtra("test_message", "1");
		Intent testIntent2 = new Intent(this, CustormButtonActivity.class);
		testIntent2.putExtra("test_message", "2");
		Intent testIntent3 = new Intent(this, TestActivity.class);
		testIntent3.putExtra("test_message", "3");
		
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("TAB 1")
				.setContent(testIntent1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("TAB 2")
				.setContent(testIntent2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("TAB 3")
				.setContent(testIntent3));

		// ����TabHost�ı�����ɫ
		mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		// ����TabHost�ı���ͼƬ��Դ
		// mTabHost.setBackgroundResource(R.drawable.bg0);

		// ���õ�ǰ��ʾ��һ����ǩ
		mTabHost.setCurrentTab(0);

		// ��ǩ�л��¼�����setOnTabChangedListener
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
			}
		});
	}
}
