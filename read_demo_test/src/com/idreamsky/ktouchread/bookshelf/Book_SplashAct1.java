package com.idreamsky.ktouchread.bookshelf;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.idreamsky.ktouchread.bookshelf.download.DownloadProgressListener;
import com.idreamsky.ktouchread.bookshelf.download.DownloadThread;
import com.idreamsky.ktouchread.bookshelf.download.FileDownloader;
import com.idreamsky.ktouchread.data.net.CheckInfo;
import com.idreamsky.ktouchread.data.net.CheckInfo.GetCheckinfoCallback;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;
import com.idreamsky.ktouchread.util.SDCardUtils;

/**
 * @author lewis
 */
public class Book_SplashAct1 extends Activity {

	
	private static final String TAG="Book_SplashAct";
	
	private ProgressDialog progressDialog;
	private ImageView icon_splash_progress;
	private AnimationDrawable animationDrawable;
	private File filePath;
	private CheckInfo check;
	private AlertDialog.Builder chooseDialog;
	private AlertDialog.Builder forceDialog;
	private AlertDialog.Builder alertDialog;

	private static final int THREADS = 3; // 下载开启的线程数
	public static final int REQUESTCODE_FOR_NETWORK_SET = 10;

	private static final int DOWNLOAD_LOADING = 2;
	private static final int DOWNLOAD_ERROR = -2;
	private boolean isDownload = true; 
	public static boolean jumpFlag= false;
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_LOADING:
				
				if(isDownload){
				progressDialog.setProgress((msg.getData().getInt("size"))/1024);// 把当前已经下载的数据长度设置为进度条的当前刻度
				DownloadComplete();
				}
				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(Book_SplashAct1.this, R.string.soft_update_fail,
						Toast.LENGTH_SHORT).show();
				break;
			}

			super.handleMessage(msg);
		}

		private void DownloadComplete() {
			if (progressDialog.getProgress() == progressDialog.getMax()) {
				install();
			}
		}
	};

//	private Timer timer;
//	private TimerTask task = new TimerTask() {
//
//		@Override
//		public void run() {
//			SwithcTo();
//		}
//	};

	public void GetUserInforError()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(Book_SplashAct1.this);
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
						Book_SplashAct1.this.finish();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}
	private void SwithcTo()
	{
		final String Token = KPayAccount.getLongtermToken();
		if (Token != null && Token.length() >0) {
			UrlUtil.TokenTPL = Token;
			SwitchToBookShop();
		} else {
//			SwitchToBookShop();
			SwitchtoKPayAccount();
		}
	}
	private void SwitchToBookShop() {
//		Intent intent = new Intent(Book_SplashAct1.this, BookShelf.class);
		Intent intent = new Intent(Book_SplashAct1.this, ReadTabActivity.class);
		startActivity(intent);
		finish();
	}

	private void SwitchtoKPayAccount() {
		 Intent intent = KPayAccount.GetTokenIntent();
		 startActivityForResult(intent,
		 KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_shelf_splash);
		
		initView();
		if(SDCardUtils.isSDCardEnable())
		{
			checkNetWork();
		}
		else {
			Toast.makeText(this, "SD卡不存在，请插入SD卡！", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Book_SplashAct1.this.finish();
				}
			},  2000);
		}
		
	}

	protected void onResume() {
		super.onResume();
		playAnim();
	
		
	}
	
	private void initView() { // 初始化UI
		icon_splash_progress = (ImageView) findViewById(R.id.splash_progress);
		icon_splash_progress.setBackgroundResource(R.anim.splash_loading);
		animationDrawable = (AnimationDrawable) icon_splash_progress
				.getBackground();
		alertDialog = new AlertDialog.Builder(this);
	}

	private void playAnim() { // 播放动画
		handler.postDelayed(new Runnable() {
			public void run() {
				animationDrawable.start();
			}
		}, 500);
	}

	public void checkNetWork() { // 检测网络
		boolean netState = NetUtil.checkNetwork(this);
		if (!netState) { // 提示设置网络
			alertNetError();
		}else{
			initVersion();
		}
	}
	
	@Override
	protected void onStop() {
		if(alertDialog!=null){
			alertDialog.create().dismiss();
		}
		super.onStop();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				return true;
			
		}
		return false;
	}
	private void alertNetError() { // 提示设置网络
		alertDialog.setTitle(R.string.splash_network_setting)
				.setMessage(R.string.splash_prompt_content)
				.setPositiveButton(R.string.splash_prompt_OK,
						new DialogInterface.OnClickListener() { // 跳转到网络设置界面
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								alertDialog.create().dismiss();
								Intent intent = new Intent(
										android.provider.Settings.ACTION_WIRELESS_SETTINGS);
								startActivity(intent);
								startActivityForResult(intent, REQUESTCODE_FOR_NETWORK_SET);
							}
						})

				.setNeutralButton(R.string.splash_prompt_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
                                SwithcTo();
							}
						}).create().show();
	}

	private void initVersion() { // 检测是否需要更新
		try {
			CheckInfo.GetCheckInfo(new GetCheckinfoCallback() {

				@Override
				public void onFail(String msg) {
					// TODO Auto-generated method stub

					LogEx.Log_I("network fail", msg.toString()+" fail");
					SwithcTo();
//					timer=new Timer();
//					timer.schedule(task,100);

				}

				@Override
				public void onSuccess(CheckInfo checkinfo) {
					// TODO Auto-generated method stub
					LogEx.Log_I("network fail", "success");
					check = checkinfo;
					
					String localVersion = getLocalVersion(); // 本地版本
					if (!check.LatestVer.equals(localVersion)) {
						if (check.IfUpdateble == 1) {
							if (check.IfForceUpdate == 1) {
								forceUpdate();
							} else {
								chooseUpdate();
							}
						} else {
							SwithcTo();
						}
					} else {
						SwithcTo();
						// timer = new Timer();
						// timer.schedule(task,100);
					}

				}
			});
		} catch (Exception e1) {
			LogEx.Log_I(TAG,e1.toString());
		}

	}

	private void forceUpdate() { // 强制升级
		forceDialog = new AlertDialog.Builder(this);
		forceDialog.setTitle(R.string.soft_update);
		forceDialog.setMessage(R.string.soft_update_old_version);
		forceDialog.setCancelable(false);
		forceDialog.setPositiveButton(R.string.soft_update_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadPath();
					}
				});
		forceDialog.setNeutralButton(R.string.soft_update_exit, // 暂不升级
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		forceDialog.create().show();
	}

	private void chooseUpdate() { // 可选择升级
		chooseDialog = new AlertDialog.Builder(this);
		chooseDialog.setTitle(R.string.soft_update);
		chooseDialog.setMessage(R.string.soft_update_msg);
		chooseDialog.setCancelable(false);
		chooseDialog.setPositiveButton(R.string.soft_update_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloadPath();
						dialog.dismiss();
					}

				});
		chooseDialog.setNeutralButton(R.string.soft_update_no, // 暂不升级
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
//						timer = new Timer();
//						timer.schedule(task, 1000);
						SwithcTo();
					}
				});
		chooseDialog.create().show();
	}

	private void downloadPath() {

		showProgressDialog(); // 显示进度条
		dffDownUrl(); // 区分不同的存储路径
	}
	
	private String getLocalVersion() {
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			LogEx.Log_I("version name is not found", e.toString());
		}
		return versionName;
	}


	private void dffDownUrl() {
		String path = check.DownUrl;
		if (Environment.getExternalStorageState().equals( // 如果存储卡存在，就把文件下载到存储卡
				Environment.MEDIA_MOUNTED)) {
			download(path, Environment.getExternalStorageDirectory());
		} else { // 下载到应用包下面
			
			String cmd = "chmod +x " + getFilesDir().getPath();
			try {
				Runtime.getRuntime().exec(cmd);
				download(path, getFilesDir());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(Book_SplashAct1.this);
		progressDialog.setButton(getString(R.string.splash_close), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (check.IfUpdateble == 1) {
					if (check.IfForceUpdate == 1) {
						android.os.Process.killProcess(android.os.Process.myPid());
					} else if (check.IfForceUpdate == 0) {
						DownloadThread.Threadlf = false;
						isDownload=false;
						handler.removeCallbacks(download);
						progressDialog.dismiss();
//						timer = new Timer();
//						timer.schedule(task, 1000);
						SwithcTo();
					}
				}
			}
		});
		progressDialog.setTitle(R.string.soft_update_downloading);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage(getString(R.string.soft_update_downloading));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();


	}

	private Runnable download;
	
	private void download(final String path, final File saveDir) {
	
		download=new Runnable() {
			
			@Override
			public void run() {
				try {
					DownloadThread.Threadlf = true;
					isDownload=true;
					FileDownloader loader = new FileDownloader(
							Book_SplashAct1.this, path, saveDir, THREADS);
					
					savePath(loader);
					
					progressDialog.setMax(loader.getFileSize()/1024);// 设置进度条的最大刻度为文件的大小
					
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {
							Message msg = new Message();
							msg.what = DOWNLOAD_LOADING;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);
						}
					});
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWNLOAD_ERROR;
					handler.sendMessage(msg);
				}
			}

	
		};
		
		
		new Thread(download).start();
		
		
	}
	
	private void savePath(FileDownloader loader) {
		String fileName=loader.filename;
		if (Environment.getExternalStorageState().equals( // 如果存储卡存在，就把文件下载到存储卡
				Environment.MEDIA_MOUNTED)) {
			filePath=new File(Environment.getExternalStorageDirectory(),fileName);
		} else { // 下载到应用包下面
			String cmd = "chmod +x " + getFilesDir().getPath();
			try {
				Runtime.getRuntime().exec(cmd);
				filePath=new File(getFilesDir(),fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
		return true;
	}

	// 打开应用程序
	private void install() {
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.fromFile(filePath),
				"application/vnd.android.package-archive");
		startActivity(installIntent);
		android.os.Process.killProcess(android.os.Process.myPid());
//		
//		
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(Uri.fromFile(getFile(fileName)), "application/vnd.android.package-archive");
//		mContext.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						UrlUtil.TokenT = extras.getString("token");
						LogEx.Log_V("Token", UrlUtil.TokenT);
						if(UrlUtil.TokenT!= null && UrlUtil.TokenT.length() > 0)
						{
							MakeToast("UrlUtil.TokenT" + UrlUtil.TokenT);
							SwitchToBookShop();
						}
						else
						{
							GetUserInforError();
						}
					}
					else {
						GetUserInforError();
					}
				}
				else {
					GetUserInforError();
				}

			}
			else
			{
				GetUserInforError();
			}
		} else if (requestCode == KPayAccount.REQUESTCODE_FLAG_FOR_MYACCOUNT) {
			if (resultCode == RESULT_OK) {
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						UrlUtil.TokenT = extras.getString("token");
					}
				}

			}
			SwitchToBookShop();
		}
		else if(REQUESTCODE_FOR_NETWORK_SET == requestCode)
		{
			checkNetWork();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
    public void MakeToast(final String msg)
    {
    	if(Configuration.DEBUG_VERSION)
    	{
        	this.handler.post(new Runnable() {
    			
    			@Override
    			public void run() {
    				// TODO Auto-generated method stub
    				Toast.makeText(Book_SplashAct1.this, msg , Toast.LENGTH_SHORT);
    				
    			}
    		});
    	}

    }

}
