package com.gmail.txyjssr.reader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Light_NoTitleBar);
	}
	
	protected void showToast(final String msg){
		Runnable r= new Runnable(){

			@Override
			public void run() {
				Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG).show();
				
			}};
		
		runOnUiThread(r);
	}
}
