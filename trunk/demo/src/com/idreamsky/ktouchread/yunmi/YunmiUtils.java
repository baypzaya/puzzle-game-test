package com.idreamsky.ktouchread.yunmi;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.aliyun.ams.tyid.TYIDConstants;
import com.aliyun.ams.tyid.TYIDException;
import com.aliyun.ams.tyid.TYIDManager;
import com.aliyun.ams.tyid.TYIDManagerFuture;
import com.idreamsky.ktouchread.bookshelf.Book_SplashAct1;
import com.idreamsky.ktouchread.util.LogEx;

public class YunmiUtils{
	private static final String TAG = "YunmiUtils";
	

	private static final String APP_KAY ="YzuishukIW9SPkRE";
	
	public static String getTYID(Context context) throws TYIDException {
		String tyId = null;
		
		TYIDManager tyIdManager = TYIDManager.get(context);
		if (tyIdManager != null) {
			String tyidId = tyIdManager.getTYID();
			if (tyidId == null) {				
				Intent intent = new Intent("com.aliyun.xiaoyunmi.action.AYUN_LOGIN",Uri.parse("yunmi://login/"));
				context.startActivity(intent);
			}
		}
		
		return tyId;
	}
	
	/*
	 * AppKey: YzuishukIW9SPkRE Token: 483803315zY3siukuh9WIkPScER16E16812
	 * Secret: Kx4k5cUL0U Kp: 1861123843085133 Nickname: yujsh AliyunID:
	 * yujsh@aliyun.com
	 */
	
	//tianyu
	//81570555
	
	public static void getAppToken(final Context context,final Handler handler ) {
		
		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				Message msg = handler
						.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
				msg.arg1 = Book_SplashAct1.STATUS_GETTING_TOKEN;
				handler.sendMessage(msg);
				
				String errorStr = "";
				
				try{
//					getTYID(context);
					TYIDManager tyIdManager = TYIDManager.get(context);
					//"caprzgMDrmo6h3Ld"
					TYIDManagerFuture<Bundle>  bundleFuture = tyIdManager.getToken(TYIDConstants.SERVICE_ALIYUN,APP_KAY, null, null);
					Bundle bundle = bundleFuture.getResult();
					if(bundle != null){
						int code = bundle.getInt(TYIDConstants.KEY_CODE);
						if(code == TYIDConstants.CODE_TOKEN_MISS){ //TYIDConstants.CODE_TOKEN_MISS, 有账号, 但token缺失.
							//有账号, 但token缺失, 需要重新登录
							Intent intent = new Intent("com.aliyun.xiaoyunmi.action.AYUN_LOGIN",Uri.parse( "yunmi://login/"));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);							
							msg = handler
									.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
							msg.arg1 = Book_SplashAct1.STATUS_LOGIN_YUNMI;
							handler.sendMessage(msg);
							
							return;
						}else if(code == TYIDConstants.CODE_OK){
							AliyunResult result = new AliyunResult();
							result.kp = bundle.getString(TYIDConstants.KEY_KP);
							result.token = bundle.getString(TYIDConstants.KEY_OAUTH_TOKEN);
							result.secret = bundle.getString(TYIDConstants.KEY_OAUTH_SECRET);
							result.aliyunId = bundle.getString(TYIDConstants.KEY_ALIYUNID);
							result.nickName = bundle.getString(TYIDConstants.KEY_NICKNAME);
							
							msg = handler
									.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
							msg.arg1 = Book_SplashAct1.STATUS_GOT_TOKEN;
							msg.obj = result;
							handler.sendMessage(msg);
							
							return;
						}else {
							LogEx.Log_I(TAG, "getAppToken unkown code:"+code);
							errorStr = "getAppToken unkown code:"+code;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					errorStr = "exception :"+e.getMessage();
				}
				
				msg = handler
						.obtainMessage(Book_SplashAct1.UPDATE_GET_TOKEN_STATUS);
				msg.arg1 = Book_SplashAct1.STATUS_NO_GOT_TOKEN;
				msg.obj = errorStr;
				handler.sendMessage(msg);
				
			}
			
		};
		
		new Thread(runnable).start();
	}

}
