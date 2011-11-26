package com.gmail.txyjssr.images;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import com.gmail.txyjssr.images.ImagesCacheManager.ICacheChangeCallBack;

public class ImagesTimeActivity extends ListActivity implements
		ICacheChangeCallBack {

	private static final int MSG_UPDATE_LISTVIER = 0;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			int what = msg.what;

			switch (what) {
			case MSG_UPDATE_LISTVIER:
				BaseAdapter ba = (BaseAdapter) getListView().getAdapter();
				ba.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ImagesCacheManager imagesCacheMananger = ImagesCacheManager.getInstance();
		imagesCacheMananger.setCallBack(this);
		ImageGroupAdpater adpater = new ImageGroupAdpater(this);
		getListView().setAdapter(adpater);
	}

	@Override
	public void runCallBack() {
		Message msg = mHandler.obtainMessage(MSG_UPDATE_LISTVIER);
		msg.sendToTarget();
	}
}