package com.idreamsky.ktouchread.common;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.idreamsky.ktouchread.bookshelf.R;
/**
 * 
 * @author encore.liang
 * Floating tip layer 
 * Floating tip layer 1.5 seconds off
 */
public class CustomPopup {
	private Activity curActivity;
	private PopupWindow popupWindow;
	private LayoutInflater mLayoutInflater;
	private Timer timer;
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			dismiss();
			
		}
	};
	public CustomPopup(Activity curActivity)
	{
		this.curActivity = curActivity;
		mLayoutInflater = LayoutInflater.from(curActivity);
		
	}
	/**
	 * @author encore.liang
	 * @param parentResourceId 要显示的父级资源id
	 * @param showContent 显示内容
	 */
	public void show(View v,String showContent)
	{
		View view = mLayoutInflater.inflate(R.layout.popup_window, null);
		TextView popupContent = (TextView) view.findViewById(R.id.popupContent);
		popupContent.setText(showContent);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		popupWindow.update();
		timer = new Timer();
		timer.schedule(task, 1500);

		
	}
	
	public void show(int id,String showContent)
	{
		View view = mLayoutInflater.inflate(R.layout.popup_window, null);
		TextView popupContent = (TextView) view.findViewById(R.id.popupContent);
		popupContent.setText(showContent);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
		popupWindow.showAtLocation(curActivity.findViewById(id), Gravity.CENTER, 0, 0);
		popupWindow.update();
		timer = new Timer();
		timer.schedule(task, 1500);

		
	}
	/**
	 * 关闭
	 */
	private void dismiss()
	{
		if(popupWindow.isShowing())
			popupWindow.dismiss();
	}
}
