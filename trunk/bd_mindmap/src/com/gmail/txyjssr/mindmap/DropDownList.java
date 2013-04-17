package com.gmail.txyjssr.mindmap;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class DropDownList {

	private LayoutInflater mLayoutInflater;
	private ListView mListView;
	private View mView;
	private PopupWindow mPopupWindow;

	public DropDownList(Context context) {
		mLayoutInflater = LayoutInflater.from(context);
		mView = mLayoutInflater.inflate(R.layout.drop_down_list_layout,null);
		mListView = (ListView) mView.findViewById(R.id.drop_down_list);
		
		mPopupWindow = new PopupWindow(mView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(-00000);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
	}

	public void setAdapter(BaseAdapter adapter) {
		mListView.setAdapter(adapter);
	}

	public void show(View anchor) {
		mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		mPopupWindow.setWidth(mView.getMeasuredWidth());
		mPopupWindow.showAsDropDown(anchor,0,0);
	}

	public void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

}