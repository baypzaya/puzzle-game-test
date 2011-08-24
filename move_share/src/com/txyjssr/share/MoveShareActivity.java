package com.txyjssr.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MoveShareActivity extends Activity implements
		View.OnClickListener, OnEditorActionListener,
		DialogInterface.OnClickListener {

	private static final int LOCAL_PICTURE_MENU = 1;

	private TextView mRemoteDevView;
	private TextView mLocalDevView;
	private ImageView mAddFileView;
	private ImageView mAddDevView;
	private EditText mEditText;
	private Dialog mSettingNetwork;

	private int mLocalIpAddress = -1;
	private int mLocalNetMask = -1;
	private TCPServer mServer;
	private Uri fileUri;
	private PopupWindow window;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		AppUtils.sActivity = this;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mServer != null) {
			mServer.closeServer();
		}

		System.exit(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (validate()) {
			if (mServer == null || !mServer.isStarted()) {
				mServer = new TCPServer();
				FileCommunicationServer cs = new FileCommunicationServer();
				mServer.setConmunication(cs);
				new Thread(mServer).start();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent returnIntent) {
		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == LOCAL_PICTURE_MENU) {
			fileUri = returnIntent.getData();
			ImageView view = new ImageView(this);
			view.setImageURI(fileUri);
			FrameLayout layout = (FrameLayout) findViewById(R.id.workspace_layout);
			layout.addView(view);
		}
	}

	private boolean validate() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		boolean result = wifiManager.isWifiEnabled();
		if (result) {
			mLocalIpAddress = wifiManager.getDhcpInfo().ipAddress;
			mLocalNetMask = wifiManager.getDhcpInfo().netmask;
			mLocalDevView.setText(Utils.converteIp(mLocalIpAddress));
		} else {
			settingNetwork();
		}

		return result;
	}

	private void initView() {
		mRemoteDevView = (TextView) findViewById(R.id.remote_dev);
		mLocalDevView = (TextView) findViewById(R.id.local_dev);
		mAddFileView = (ImageView) findViewById(R.id.add_file);
		mAddDevView = (ImageView) findViewById(R.id.add_dev);
		mEditText = (EditText) findViewById(R.id.remote_dev_input);

		// mRemoteDevView.setVisibility(View.INVISIBLE);
		mRemoteDevView.setOnClickListener(this);
		mLocalDevView.setOnClickListener(this);
		mAddFileView.setOnClickListener(this);
		mAddDevView.setOnClickListener(this);
		mEditText.setOnEditorActionListener(this);
	}

	private void settingNetwork() {
		mSettingNetwork = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.no_network))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setNeutralButton(R.string.retry_set_network, this).show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (dialog == mSettingNetwork) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_MAIN);
			intent.setClassName("com.android.settings",
					"com.android.settings.WirelessSettings");
			startActivity(intent);
		} else {
			finish();
		}

	}

	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		switch (viewId) {
		case R.id.add_dev:
			addDev();
			break;
		case R.id.add_file:
			addFile();
			break;
		case R.id.remote_dev:
			addRemoteDev();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void addFile() {
		// 弹出文件获取方式：文件浏览器、音乐、视频、图片
		String s = "android.intent.action.GET_CONTENT";
		Intent intent1 = new Intent(s);
		intent1.setType("image/*");
		Intent intent2 = Intent.createChooser(intent1, null);
		startActivityForResult(intent2, LOCAL_PICTURE_MENU);
		// popAwindow(findViewById(R.id.workspace_layout));
		// 获取设备

	}

	private void addDev() {
		// 验证设备是否已经开启wifi
		if (!AppUtils.isWiFiActive(this)) {
			settingNetwork();
		} else {
			// 搜索设备

			// 弹出匹配结果

			// test code
			FileCommunicationClient cc = new FileCommunicationClient(fileUri);
			final TCPClient client = new TCPClient("localhost",
					TCPServer.SERVER_PORT, cc);
			new Thread(new Runnable() {

				@Override
				public void run() {
					client.start();

				}
			}).start();

		}
	}

	// @Override
	// public void onAttachedToWindow() {
	// getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	// }

	private void popAwindow(View parent) {
		if (window == null) {
			LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = lay.inflate(R.layout.resource_type_select, null);

			window = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		// 设置整个popupwindow的样式。
		window.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.rounded_corners_pop));
		// 使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
		// 如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
		// listview的话就没有了作用。
		window.setFocusable(true);
		window.update();
		window.showAtLocation(parent, Gravity.BOTTOM | Gravity.LEFT, 0, 50);
	}

	private void addRemoteDev() {
		mRemoteDevView.setVisibility(View.GONE);
		mEditText.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {			
			String ipaddress = mEditText.getEditableText().toString();
			mRemoteDevView.setText(ipaddress);
			mRemoteDevView.setVisibility(View.VISIBLE);
			mEditText.setVisibility(View.GONE);
		}
		return false;
	}

}