package com.idreamsky.ktouchread.pay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.idreamsky.ktouchread.util.LogEx;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class KPayAccount {
	
	private static final String TAG = "Token";
	private static final String KPAY_HOST = "osatest01001.k-touch.cn:8012";	//用于第三方需要使用天宇测试环境时
//	private static final String KPAY_HOST = "bookstore.k-touch.cn:80";			//用于第三方需要连接天宇正式环境时
	
	private static final String KACCOUNT_PACKAGENAME = "com.osa.ktouchpay"; // 不可修改
	private static final String KACCOUNT_ENTRY_ACTIVITY = "com.osa.ktouchpay.PaymentSelect"; // 不可修改
	private static final String KACCOUNT_ENTRY_TYPE = "entry_type"; 

	
	private static final String KACCOUNT_ENTRY_PAYMENT_HOSTNAME 		= "hostname";
	private static final String KACCOUNT_ENTRY_PAYMENT_PRODUCTLINE 	= "productline";
	private static final String KACCOUNT_ENTRY_PAYMENT_PRODUCTID 	= "productId";
	private static final String KACCOUNT_ENTRY_PAYMENT_PRODUCTNAME 	= "productname";
	private static final String KACCOUNT_ENTRY_PAYMENT_PRICE 		= "price";
	
	
	// 调用startActivityForResult 的requestCode,预定义
	public static final int REQUESTCODE_FLAG_FOR_MYACCOUNT =1; // 启动到我的账户，可以不用
	public static final int REQUESTCODE_FLAG_FOR_PAYMENT = 111; // 启动到支付界面，必须使用，需要处理返回结果
	public static final int REQUESTCODE_FLAG_FOR_GETTOKEN = 112; // 获取token，建议使用，以便处理返回结果

	// 返回值预定义
	public static final int RESULT_PAYMENT_ERROR = -2;
	public static final int RESULT_PAYMENT_NOMONEY = -3;
	public static final int RESULT_PAYMENT_UPDATING = -4;
	/********** 需要预定义的参数 END ***********/
	
	public static final String KACCOUNT_ENTRY_PARA = "entry_para"; // caixf,
	// 2012-07-21，进入“我的账户”前判断token，如果不存在，则需要获取
public static final int KACCOUNT_ENTRY_PARA_GETTOKEN = 1; // caixf,
	
	public static Intent GetUserIntent()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setClassName(KACCOUNT_PACKAGENAME, KACCOUNT_ENTRY_ACTIVITY);
		intent.putExtra(KACCOUNT_ENTRY_TYPE, 1);
        intent.putExtra(KACCOUNT_ENTRY_PARA,KACCOUNT_ENTRY_PARA_GETTOKEN);



		return intent;
	}
	
	public static Intent GetPayIntent(String productID,String ProductName,String Price)
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setClassName(KACCOUNT_PACKAGENAME, KACCOUNT_ENTRY_ACTIVITY);
		intent.putExtra(KACCOUNT_ENTRY_TYPE, 2); // 进入支付流程
		intent.putExtra(KACCOUNT_ENTRY_PAYMENT_HOSTNAME, KPAY_HOST);
		intent.putExtra(KACCOUNT_ENTRY_PAYMENT_PRODUCTLINE, 2);
		intent.putExtra(KACCOUNT_ENTRY_PAYMENT_PRODUCTID,  productID); 
		intent.putExtra(KACCOUNT_ENTRY_PAYMENT_PRODUCTNAME, ProductName);
		intent.putExtra(KACCOUNT_ENTRY_PAYMENT_PRICE, Price);
		return intent;

	}
	
	public static Intent GetTokenIntent()
	{
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setClassName(KACCOUNT_PACKAGENAME, KACCOUNT_ENTRY_ACTIVITY);
		intent.putExtra(KACCOUNT_ENTRY_TYPE, 3);
		intent.putExtra(KACCOUNT_ENTRY_PARA, KACCOUNT_ENTRY_PARA_GETTOKEN);
		
		
//		Intent intent = new Intent(Intent.ACTION_MAIN, null);
//		intent.setClassName(KACCOUNT_PACKAGENAME, KACCOUNT_ENTRY_ACTIVITY);
//		intent.putExtra(KACCOUNT_ENTRY_TYPE, KACCOUNT_ENTRY_GETTOKEN);
//		startActivityForResult(intent, KACCOUNT_ENTRY_GETTOKEN);

		return intent;

	}


	public static final String OSA_LOCAL_SDCARD_ROOT = getExternalStorageDirectory();
	public static final String OSA_LOCAL_CONFIG_TOKEN_PATH = OSA_LOCAL_SDCARD_ROOT + "osa/config/";
	public static final String PREFERENCES_KSTORE_TOKEN = "kbook_token";

	public static String getExternalStorageDirectory() {
		String rootpath = Environment.getExternalStorageDirectory()
				.getPath();
		if (!rootpath.endsWith("/")) {
			rootpath += "/";
		}
		LogEx.Log_V(TAG, "getExternalStorageDirectory() path = " + rootpath);
		return rootpath;
	}

	public static String getLongtermToken(){
		String token = null;
		File tokenFile = null;
		FileReader fr  = null; 
		BufferedReader buf = null;
		
		try{
			
			tokenFile = new File(OSA_LOCAL_CONFIG_TOKEN_PATH + PREFERENCES_KSTORE_TOKEN);
			if (!tokenFile.exists()) {
				Log.d(TAG, "!!!file unexisted!!!" + token);
				return "";
			}
			fr = new FileReader(tokenFile);
			buf = new BufferedReader(fr);
			token = buf.readLine();
			LogEx.Log_V(TAG, "!!!file read a token:" + token);
			if(token == null)
			{
				LogEx.Log_V(TAG, "!!!file read a token but a null");
				return "";
			}
			return token;
		}
		catch(IOException e){
			LogEx.Log_V(TAG, "getLongtermToken IOException:" + e.toString());
		}
		finally{
			try { if (buf != null) {buf.close();}} catch (IOException ex) {} 
			try { if (fr != null) {fr.close();}} catch (IOException ex) {}
		}
		return "";
	}

	// 2011-7-23, caixf，保留永久token到配置文件，公共函数，需要在相应的Activity里调用
	/**
	 * 
	 * @param set long-term token
	 */
	public static void setLongtermToken(String token) {
		if(getLongtermToken() != ""){
			LogEx.Log_V(TAG, "already set by payaccount, token = " + token);
			return;
		}
		File tokenFile = null;
		FileWriter fw  = null; 
		
		try{
			File tokenD = new File(OSA_LOCAL_CONFIG_TOKEN_PATH);
			if(!tokenD.exists())
			{
				tokenD.mkdirs();
			}
			
			tokenFile = new File(OSA_LOCAL_CONFIG_TOKEN_PATH + PREFERENCES_KSTORE_TOKEN);
			if (!tokenFile.exists()) {
				tokenFile.createNewFile();
			}
			fw = new FileWriter(tokenFile);
			fw.write(token);
			LogEx.Log_V(TAG, "file write a token:" + token);
			return;
		}
		catch(IOException e){
			LogEx.Log_V(TAG, "setLongtermToken IOException:" + e.toString());
		}
		finally{
			try { if (fw != null) {fw.close();}} catch (IOException ex) {}
		}
		return;
	}

}
