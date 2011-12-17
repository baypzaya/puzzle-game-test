package com.idreamsky.ktouchread.bookshelf;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.yunmi.AliyunResult;
import com.idreamsky.ktouchread.yunmi.YunmiUtils;

public class GetTokenActivity extends SpiritActivity {
	
	private static final String TAG = "GetTokenActivity";
	
	private int curGetTokenStatus = 0;

	public static final int STATUS_LOGIN_YUNMI = 1;
	public static final int STATUS_LOGIN_FAILDED = 2;
	public static final int STATUS_LOGIN_SUCCESS = 3;
	public static final int STATUS_GETTING_KTOUCH_TOKEN = 4;
	public static final int STATUS_GOT_KTOUCH_TOKEN = 5;
	public static final int STATUS_GETTING_TOKEN = 6;
	public static final int STATUS_GOT_TOKEN = 7;
	public static final int STATUS_NO_GOT_KTOUCH_TOKEN = 8;
	public static final int STATUS_NO_GOT_TOKEN = 9;
	
	public static final int UPDATE_GET_TOKEN_STATUS = 1;
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case UPDATE_GET_TOKEN_STATUS:
				int status = msg.arg1;
				onGetTokenStatusChange(status,msg.obj);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		this.setNavigationBarVisibility(false);
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.aliyun.xiaoyunmi.action.AYUN_LOGIN_BROADCAST");
		registerReceiver(yunmiLoginReceiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(yunmiLoginReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 当yunmi登陆失败 ，提醒用户获取用户信息失败
		if (curGetTokenStatus == STATUS_LOGIN_FAILDED) {
			this.GetUserInforError();
			return;
		}

		// 当yunmi登陆成功，则进行获取token流程
		if (curGetTokenStatus == STATUS_LOGIN_SUCCESS) {
			SwitchtoKPayAccount();
			return;
		}
	}
	
	
    //针对获取token状态变化处理
    private void onGetTokenStatusChange(int status,Object result) {
    	LogEx.Log_I(TAG,"curGetTokenStatus:curGetTokenStatus"+" changedStatus:"+status );
    	
		switch(status){
		case STATUS_LOGIN_YUNMI:
			curGetTokenStatus = STATUS_LOGIN_FAILDED;
			return;
		case STATUS_LOGIN_FAILDED:break;
		case STATUS_LOGIN_SUCCESS:break;
		case STATUS_GETTING_TOKEN:
			long delay = 20000;
			Runnable runnable = new Runnable(){
				
				@Override
				public void run() {
					if(curGetTokenStatus == STATUS_GETTING_TOKEN){
						GetUserInforError();
					}
				}				
			};
			handler.postDelayed(runnable, delay);
			break;
		case STATUS_GOT_TOKEN:
			AliyunResult ayResult = (AliyunResult)result;
			KPayAccount.getKTouchToken(ayResult,handler);			
			break;
		case STATUS_GETTING_KTOUCH_TOKEN:
			long delayKTouch = 20000;
			Runnable runnableKTouch = new Runnable(){
				
				@Override
				public void run() {
					if(curGetTokenStatus == STATUS_GETTING_KTOUCH_TOKEN){
						GetUserInforError();
					}
				}				
			};
			handler.postDelayed(runnableKTouch, delayKTouch);
			break;
		case STATUS_GOT_KTOUCH_TOKEN:
			UrlUtil.TokenT = (String)result;
			LogEx.Log_V("Token", UrlUtil.TokenT);
			if(UrlUtil.TokenT!= null && UrlUtil.TokenT.length() > 0)
			{
				this.finish();
			}else{
				GetUserInforError();
			}
			break;
		case STATUS_NO_GOT_KTOUCH_TOKEN:
			LogEx.Log_I(TAG, result.toString());
			GetUserInforError();
			break;
		case STATUS_NO_GOT_TOKEN:
			LogEx.Log_I(TAG, result.toString());
			GetUserInforError();
			break;
		default:break;
		}
		curGetTokenStatus = status;
	}
    
    public void GetUserInforError()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.splash_notify);
		builder.setMessage(R.string.splash_get_user_info_error);
		builder.setPositiveButton(getString(R.string.book_ol_network_agin),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						SwitchtoKPayAccount();

					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}
    

	private  void SwitchtoKPayAccount() {
		YunmiUtils.getAppToken(this,handler);
		
	}
	
	BroadcastReceiver yunmiLoginReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			boolean loginResult = false;
			if (intent.getAction().equals("com.aliyun.xiaoyunmi.action.AYUN_LOGIN_BROADCAST")) {
				Message msg = null;
				Bundle bd = intent.getExtras();
				if(bd != null)
				{
					loginResult = bd.getBoolean("aliyun_account_login_success");
					if(loginResult){
						msg = handler.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
						msg.arg1 = Book_SplashAct1.STATUS_LOGIN_SUCCESS;
						handler.sendMessage(msg);
						return;
					}
				} 
				
				msg = handler
						.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
				msg.arg1 = Book_SplashAct1.STATUS_LOGIN_FAILDED;
				handler.sendMessage(msg);
				
			}
		}
	};
    
}
