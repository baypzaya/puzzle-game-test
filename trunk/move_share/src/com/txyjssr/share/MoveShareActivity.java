package com.txyjssr.share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MoveShareActivity extends Activity implements
		View.OnClickListener, DialogInterface.OnClickListener {

	private TextView mRemoteDevView;
	private TextView mLocalDevView;
	private TextView mAddFileView;
	private TextView mAddDevView;
	private Dialog mSettingNetwork;
	
	private int mLocalIpAddress = -1;
	private int mLocalNetMask = -1;
	TCPServer mServer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		
		mServer = new TCPServer();
		new Thread(mServer).start();
	}

	@Override
	protected void onDestroy() {		
		super.onDestroy();
		if(mServer!=null){
			mServer.closeServer();
		}
		
		System.exit(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		validate();
	}

	private void validate() {		
		WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		
		if(wifiManager.isWifiEnabled()){
			mLocalIpAddress = wifiManager.getDhcpInfo().ipAddress;		
			mLocalNetMask = wifiManager.getDhcpInfo().netmask;
			mLocalDevView.setText(intToIp(mLocalIpAddress));			
		}else {
			settingNetwork();
		}
		
	}

	private void initView() {
		mRemoteDevView = (TextView) findViewById(R.id.remote_dev);
		mLocalDevView = (TextView) findViewById(R.id.local_dev);
		mAddFileView = (TextView) findViewById(R.id.add_file);
		mAddDevView = (TextView) findViewById(R.id.add_dev);
		
		mRemoteDevView.setVisibility(View.INVISIBLE);		
		mAddFileView.setOnClickListener(this);
		mAddDevView.setOnClickListener(this);
	}

	public boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);		
		return wifiManager.isWifiEnabled();
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

		// 获取设备

	}

	private void addDev() {
		// 验证设备是否已经开启wifi
		if (!isWiFiActive(this)) {
			settingNetwork();
		} else {
			// 搜索设备
			
			// 弹出匹配结果
			
			//test code
			TCPClient client = new TCPClient("192.168.129.213",TCPServer.SERVER_PORT,null);
			client.start();
		}
	}

	@Override
	public void onAttachedToWindow() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}

	private String intToIp(int i) {
		String ipAdress = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ipAdress;
	}
}