package com.idreamsky.ktouchread.bookshelf;

import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.LogEx;

import android.app.AlertDialog;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class KTouchReadApplication extends Application {
	
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = this.getApplicationContext();
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.aliyun.xiaoyunmi.action.AYUN_LOGOUT_BROADCASET");
		intentFilter.addAction("com.aliyun.xiaoyunmi.action.AYUN_DELETE_BROADCASET");
		try{
			registerReceiver(yunmiLoginReceiver, intentFilter);
		}catch(Exception e){
			e.printStackTrace();
			LogEx.Log_I("KTouchReadApplication", "exception:"+e.getMessage());
		}
	}
	
	BroadcastReceiver yunmiLoginReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action  = intent.getAction();
			if (action.equals("com.aliyun.xiaoyunmi.action.AYUN_LOGOUT_BROADCASET")) {
				LogEx.Log_I("yunmiLoginReceiver", "receive xiaoyunmi logout action");
				boolean logoutResult = intent.getBooleanExtra("aliyun_account_login_success",false);
				if(logoutResult){
					Toast.makeText(context, "xiaoyunmi logout successfully!", Toast.LENGTH_SHORT).show();
					clearToken();
				}	
			}
			
			if(action.equals("com.aliyun.xiaoyunmi.action.AYUN_DELETE_BROADCASET")){
				LogEx.Log_I("yunmiLoginReceiver", "receive xiaoyunmi delete action");
				boolean deleteResult = intent.getBooleanExtra("aliyun_account_delete_success",false);
				if(deleteResult){
					Toast.makeText(context, "xiaoyunmi delete successfully!", Toast.LENGTH_SHORT).show();
					clearToken();
				}
			}
		}
	};
	
	private void clearToken(){
		UrlUtil.TokenT = null;
		UrlUtil.TokenTPL = null;
		KPayAccount.setLongtermToken("");
//		SwitchtoKPayAccount();
		
	}
	
	private void SwitchtoKPayAccount() {
		Intent intent = KPayAccount.GetUserIntent();
		sContext.startActivity(intent);
	}
	
	
}
