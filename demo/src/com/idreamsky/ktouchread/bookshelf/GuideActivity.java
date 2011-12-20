package com.idreamsky.ktouchread.bookshelf;

import java.util.Timer;
import java.util.TimerTask;

import com.aliyun.aui.app.spirit.SpiritActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class GuideActivity extends SpiritActivity {

	private Intent intent;
	private String flag = "";
	private String read_flag = "";
	private int index = 0; // 避免用户多次点击屏幕出现activity重复

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.guide);

		intent = getIntent();

		if (intent != null) {
			if (Book_SplashAct1.jumpFlag) {
				flag = intent.getExtras().getString("flag");
				Book_SplashAct1.jumpFlag = false;

			} else if (BookShelf.jumpFlag) {
				read_flag = intent.getExtras().getString("read_flag");
				LinearLayout guide = (LinearLayout) findViewById(R.id.guide);
				guide.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.read_guide));
				BookShelf.jumpFlag = false;
			}

		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				if (read_flag.equals("true")) {
					setResult(BookShelf.SHOWGUIDE);
					finish();
				} else {
					index++;
					if (flag.equals("true") && index == 1) {
						setResult(BookShelf.SHOWGUIDE);
						finish();
					} else {
						setResult(BookShelf.SHOWGUIDE);
						finish();
					}
				}

			}
		}, 5000);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (read_flag.equals("true")) {
			this.finish();
		} else {
			index++;
			if (flag.equals("true") && index == 1) {
				this.finish();
			} else {
				this.finish();
			}
		}
		return super.onTouchEvent(event);
	}

}
