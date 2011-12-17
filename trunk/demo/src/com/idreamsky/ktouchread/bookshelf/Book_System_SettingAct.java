package com.idreamsky.ktouchread.bookshelf;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.idreamsky.ktouchread.bookshelf.download.DownloadProgressListener;
import com.idreamsky.ktouchread.bookshelf.download.DownloadThread;
import com.idreamsky.ktouchread.bookshelf.download.FileDownloader;
import com.idreamsky.ktouchread.data.BackupThread;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.BackupThread.BackupCallback;
import com.idreamsky.ktouchread.data.SyncThread;
import com.idreamsky.ktouchread.data.SyncThread.SyncCallback;
import com.idreamsky.ktouchread.data.net.CheckInfo;
import com.idreamsky.ktouchread.data.net.CheckInfo.GetCheckinfoCallback;
import com.idreamsky.ktouchread.data.net.UrlUtil;
import com.idreamsky.ktouchread.pay.KPayAccount;
import com.idreamsky.ktouchread.service.Book_System_SettingService;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.NetUtil;

public class Book_System_SettingAct extends SpiritActivity {
	private ProgressDialog progressDialog;
	private ProgressDialog versionProgressDialog;
	private File filePath;
	private CheckInfo check;
	private SyncThread syncThread;
	private BackupThread backupThread;
	private Intent notifyIntent = null;
	private ProgressDialog syncDataDialog;
	private Runnable download;
	private boolean isDownload = true;

	private static final int THREADS = 3; // 下载开启的线程数

//	private ServiceConnection conn = new ServiceConnection() {
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			settingService = null;
//		}
//
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			settingService = ((Book_System_SettingService.MyBinder) service)
//					.getService();
//		}
//
//	};

	private static final int SYS_SETTING_UPDATE = 1;
	private static final int DOWNLOAD_LOADING = 2;
	private static final int DOWNLOAD_ERROR = -2;
	private static final int SYNC_DATA = 3;
	private static final int SUCCESS_DIALOG = 4;
	private static final int BACKUP_DATA = 5;
	private static final int BACKUP_SUCCESS = 6;

	final Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DOWNLOAD_LOADING:

				if (isDownload) {
					progressDialog
							.setProgress((msg.getData().getInt("size")) / 1024);// 把当前已经下载的数据长度设置为进度条的当前刻度

				
					if (check != null
							&& progressDialog.getProgress() == progressDialog
									.getMax()) {
						install();
					}
				}

				break;
			case DOWNLOAD_ERROR:
				Toast.makeText(Book_System_SettingAct.this,
						R.string.soft_update_fail, Toast.LENGTH_SHORT).show();
				break;

			case SYNC_DATA:
				syncDataDialog = new ProgressDialog(Book_System_SettingAct.this);
				syncDataDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				syncDataDialog.setTitle(R.string.sync_data);
				syncDataDialog.setMessage(getString(R.string.sync_data_loading));
				syncDataDialog.setIndeterminate(false);
				syncDataDialog.setIcon(R.drawable.icon);
				syncDataDialog.setCancelable(true);
				syncDataDialog.setButton(getString(R.string.setting_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								if (!syncThread.IsStop()) {
									syncThread.StopThread();
								}
								syncDataDialog.dismiss();
							}
						});
				syncDataDialog.show();

				break;
			case BACKUP_DATA:
			{
				syncDataDialog = new ProgressDialog(Book_System_SettingAct.this);
				syncDataDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				syncDataDialog.setTitle(R.string.backup_data);
				syncDataDialog.setMessage(getString(R.string.backup_data_loading));
				syncDataDialog.setIndeterminate(false);
				syncDataDialog.setIcon(R.drawable.icon);
				syncDataDialog.setCancelable(true);
				syncDataDialog.setButton(getString(R.string.setting_cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								if (!backupThread.IsStop()) {
									backupThread.StopThread();
								}
								syncDataDialog.dismiss();
							}
						});
				syncDataDialog.show();
			}
			break;
			case SUCCESS_DIALOG:
				try{
					new AlertDialog.Builder(Book_System_SettingAct.this)
					.setTitle(R.string.sync_data_update)
					.setMessage(R.string.sync_data_update_complete)
					.setNegativeButton(R.string.setting_enter, null)
					.create().show();
			        syncDataDialog.dismiss();
				}catch (Exception e) {
					// TODO: handle exception
				}

				break;
			case BACKUP_SUCCESS:
			{
				try{
					new AlertDialog.Builder(Book_System_SettingAct.this)
					.setTitle(R.string.backup_data_update)
					.setMessage(R.string.backup_data_update_complete)
					.setNegativeButton(R.string.setting_enter, null)
					.create().show();
			        syncDataDialog.dismiss();
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			break;
			case SYS_SETTING_UPDATE:
				boolean netState = NetUtil
						.checkNetwork(Book_System_SettingAct.this);
				if (netState) {
					initVersion();
				} else {
					failDialogTip();
				}
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//设置 NavigationBar
		NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.book_system_setting);
		builder.showBackButton(false);
		
				
		setContentView(R.layout.book_system_setting);

//		btn = (ImageButton) findViewById(R.id.sys_setting_notice);
		notifyIntent = new Intent(Book_System_SettingAct.this,
				Book_System_SettingService.class);

		
		
		/* 控制通知设置 */
		// mSettingUtils = new SettingUtils(this, "notifySetting",
		// Context.MODE_PRIVATE);
		// String key = mSettingUtils.getString("notify", null);
		// if (key == null) {
		// btn.setBackgroundResource(R.drawable.close);
		// mSettingUtils.putString("notify", "true");
		// mSettingUtils.commitOperate();
		// } else {
		// if (key.equals("true")) {
		// bindService(notifyIntent, conn, Context.BIND_AUTO_CREATE);
		// btn.setBackgroundResource(R.drawable.open);
		// } else {
		// btn.setBackgroundResource(R.drawable.close);
		// }
		// }

		UrlUtil.Init(this);
		progressDialog = new ProgressDialog(Book_System_SettingAct.this);
	}

	/**
	 * @param 处理按钮点击事件
	 */
	public void sysSettingClick(View view) {
		switch (view.getId()) {
//		case R.id.sys_setting_notice:
//			// String key = mSettingUtils.getString("notify", null);
//			// IsNotify(key);
//
			
//			break;
		case R.id.my_account:
			SwitchtoKPayAccount();
			break;
		case R.id.sys_setting_update:
		case R.id.sys_setting_update_layout:

			new Thread(new Runnable() {

				@Override
				public void run() {
					handler.sendEmptyMessage(SYS_SETTING_UPDATE);
				}
			}).start();
			break;
		case R.id.sys_setting_sync:
		case R.id.sys_setting_sync_layout:
			if (NetUtil.checkNetwork(this)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						syncThread = new SyncThread(new SyncCallBack());
						if (syncThread.IsStop()) {
							syncThread.StartThread();
						}
						handler.sendEmptyMessage(SYNC_DATA);
					}
				}).start();
			}else{
				failDialogTip();
			}
		break;
		case R.id.sys_setting_backup:
		case R.id.sys_setting_backup_layout:
		{
			if (NetUtil.checkNetwork(this)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						backupThread = new BackupThread(new BackupCallBack());
						if (backupThread.IsStop()) {
							backupThread.StartThread();
						}
						handler.sendEmptyMessage(BACKUP_DATA);
					}
				}).start();
			}else{
				failDialogTip();
			}
		}
		break;
		case R.id.sys_setting_help:
			Intent intent = new Intent(Book_System_SettingAct.this,
					GuideActivity.class);
			startActivity(intent);
			break;
		}

	}

	/* 成功返回的结果码 */
	private static final int RESULT_SUCCESS = 1;

	private final class SyncCallBack implements SyncCallback {

		@Override
		public void onSuccess() {
			Intent data = new Intent();
			data.putExtra("newArticle", "true");
			Book_System_SettingAct.this.setResult(RESULT_SUCCESS, data);
			progressDialog.dismiss();
			handler.sendEmptyMessage(SUCCESS_DIALOG);
		}

		@Override
		public void onFail() {

		}

	}
	private final class BackupCallBack implements BackupCallback {

		@Override
		public void onSuccess() {
//			Intent data = new Intent();
//			data.putExtra("newArticle", "true");
//			Book_System_SettingAct.this.setResult(RESULT_SUCCESS, data);
			progressDialog.dismiss();
			handler.sendEmptyMessage(BACKUP_SUCCESS);
		}

		@Override
		public void onFail() {

		}

	}

	// startActivityForResult(new
	// Intent(Book_System.SettingAct.this,TwoAct.class),1);
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// super.onActivityResult(requestCode, resultCode, data);
	// }

	/* 与通知相关的 */

//	private void IsNotify(String key) {
//		if (key.equals("true")) { // 關閉服務
//			if (settingService != null) {
//				unbindService(conn);
//			}
//			btn.setBackgroundResource(R.drawable.close);
//			mSettingUtils.putString("notify", "false");
//			mSettingUtils.commitOperate();
//		} else { // 開啟服務
//			bindService(notifyIntent, conn, Context.BIND_AUTO_CREATE);
//			btn.setBackgroundResource(R.drawable.open);
//			mSettingUtils.putString("notify", "true");
//			mSettingUtils.commitOperate();
//		}
//	}

	private void VersionProgressDialog() {
		versionProgressDialog = new ProgressDialog(Book_System_SettingAct.this);
		versionProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		versionProgressDialog.setTitle(R.string.setting_check_version);
		versionProgressDialog.setMessage(getString(R.string.setting_get_version));
		versionProgressDialog.setIndeterminate(false);
		versionProgressDialog.setIcon(R.drawable.icon);
		versionProgressDialog.setCancelable(true);
		versionProgressDialog.show();
	}

	private void ShowProgressDialog() {
		progressDialog = new ProgressDialog(Book_System_SettingAct.this);
		progressDialog.setButton(getString(R.string.splash_close), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownloadThread.Threadlf = false;
				isDownload = false;
				handler.removeCallbacks(download);
				((Dialog) dialog).setCancelable(false);
				progressDialog.dismiss();
				Book_System_SettingAct.this.finish();
				Intent intent = new Intent(Book_System_SettingAct.this,
						Book_System_SettingAct.class);
				startActivity(intent);
			}
		});
		progressDialog.setTitle(R.string.soft_update_downloading);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage(getString(R.string.soft_update_downloading));
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);
		progressDialog.show();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				exitApplication();
			
		}
		return false;
	}

	/**
	 * 退出应用程序
	 */
	public void exitApplication() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.bookshelf_exit);
		builder.setMessage(R.string.bookshelf_enter);
		builder.setPositiveButton(getString(R.string.confirm),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		builder.setNegativeButton(getString(R.string.cancel), null);
		builder.create().show();
	}
	private void failDialogTip() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				Book_System_SettingAct.this);
		dialog.setTitle(R.string.sync_data_net_setting_tip);
		dialog.setMessage(R.string.sync_data_net_setting_msg);
		dialog.setPositiveButton(R.string.setting_enter, null);
		dialog.show();
	}

	private void initVersion() {
		VersionProgressDialog();
		CheckInfo.GetCheckInfo(new GetCheckinfoCallback() {

			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub

				versionProgressDialog.dismiss();
				failDialogTip(); // 网络失败。

			}

			@Override
			public void onSuccess(CheckInfo checkinfo) {
				// TODO Auto-generated method stub

				check = checkinfo;
				versionProgressDialog.dismiss();

				String localVersion = getLocalVersion(); // 本地版本

				if (check.LatestVer.equals(localVersion)) { // 服务器版本与本地的版本号相同
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							Book_System_SettingAct.this);
					dialog.setTitle(R.string.soft_update);
					dialog.setMessage(R.string.soft_update_new_msg);
					dialog.setNegativeButton(R.string.setting_enter, null);
					dialog.create().show();
				}

				else { // 检查是否可以升级
					if (check.IfUpdateble == 1) { // 检查是否可以升级

						AlertDialog.Builder dialog = new AlertDialog.Builder(
								Book_System_SettingAct.this);

						StringBuffer buffer = new StringBuffer();
						buffer.append(getString(R.string.setting_current_version)).append(localVersion)
								.append(getString(R.string.setting_new_version)).append(check.LatestVer)
								.append(getString(R.string.setting_update));

						dialog.setTitle(R.string.soft_update);
						dialog.setMessage(buffer.toString());

						dialog.setPositiveButton(getString(R.string.splash_prompt_yes),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										downloadPath();
									}
								});
						dialog.setNegativeButton(getString(R.string.splash_prompt_no),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						dialog.create().show();

					}
					else{
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								Book_System_SettingAct.this);
						dialog.setTitle(R.string.soft_update);
						dialog.setMessage(R.string.soft_update_new_msg);
						dialog.setNegativeButton(R.string.setting_enter, null);
						dialog.create().show();
					}
				}

			}

		});

	}

	private void downloadPath() {
		ShowProgressDialog(); // 显示进度条
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

	private void download(final String path, final File saveDir) {
		download = new Runnable() {
			@Override
			public void run() {
				try {
					DownloadThread.Threadlf = true;
					isDownload = true;
					FileDownloader loader = new FileDownloader(
							Book_System_SettingAct.this, path, saveDir, THREADS);

					savePath(loader);

					progressDialog.setMax(loader.getFileSize() / 1024);// 设置进度条的最大刻度为文件的大小
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
		String fileName = loader.filename;
		if (Environment.getExternalStorageState().equals( // 如果存储卡存在，就把文件下载到存储卡
				Environment.MEDIA_MOUNTED)) {
			filePath = new File(Environment.getExternalStorageDirectory(),
					fileName);
		} else { // 下载到应用包下面
			String cmd = "chmod +x " + getFilesDir().getPath();
			try {
				Runtime.getRuntime().exec(cmd);
				filePath = new File(getFilesDir(), fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 打开应用程序
	private void install() {
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.setDataAndType(Uri.fromFile(filePath),
				"application/vnd.android.package-archive");
		startActivity(installIntent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void SwitchtoKPayAccount() {
		 Book.Save();
//		 Intent intent = KPayAccount.GetUserIntent();
//		 startActivityForResult(intent,
//		 KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
		 
		 Intent intent = new Intent("com.aliyun.xiaoyunmi.action.AYUN_ACCOUNT_MAIN",Uri.parse( "yunmi://account_main/"));
		 this.startActivity(intent);

	}

}
