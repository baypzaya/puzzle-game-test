package com.yujsh.android.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		String text = getIntent().getStringExtra("test_message");
		TextView tv = new TextView(this);
		tv.setText("this is a test activity "+text);
		setContentView(tv);
	}
}
