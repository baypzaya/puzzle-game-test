package com.ktouch.pcsdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ktouch.pcs.IPCSManager;

public class MainActivity extends Activity implements OnClickListener {

	Handler handler = new Handler();

	IPCSManager mService;
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.i("yujsh log", "connect service");
			mService = IPCSManager.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			Log.i("yujsh log", "disconnect service");
			mService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent();
		intent.setClassName("com.ktouch.pcs", "com.ktouch.pcs.PCSService");
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		Button btnUpload = (Button) findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		try {
			int id = v.getId();
			switch (id) {
			case R.id.btn_upload:
				upload();
				break;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void upload() throws RemoteException {
		mService.upload("this is test path");
	}
}
