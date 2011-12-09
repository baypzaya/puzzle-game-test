package com.idreamsky.ktouchread.bookshelf;

import java.io.File;

import java.util.ArrayList;
import java.util.List;



import com.idreamsky.ktouchread.Adapter.AddFileAdapter;

import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.Chapter;

import com.idreamsky.ktouchread.data.SearchData;
import com.idreamsky.ktouchread.service.AddBookService;

import com.idreamsky.ktouchread.util.SDCardUtils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddFile extends Activity {
	private TextView path; 
	private Button scanning; 
	private LinearLayout back;
	private ListView lvContent;
	private List<SearchData> searchDatas;
	public static String rootPath = SDCardUtils.GetPath(); //sd卡路径
	public AddFileAdapter adapter;
	public String perPath= ""; //上一级目录路径
	public String currentPath = ""; //当前目录路径
	private File currentFile ; 
	private int postion = 0;
	private Dialog mProcessDialog = null;
	public boolean isScanning = false; //是否自动扫描
	private int scanningCount = 0;
	LoadTask lt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_add);
		path = (TextView) this.findViewById(R.id.path);
		scanning = (Button) this.findViewById(R.id.scanning);
		back = (LinearLayout) this.findViewById(R.id.back);
		lvContent  = (ListView) this.findViewById(R.id.lvContent);
		adapter = new AddFileAdapter(AddFile.this);	
		getFileDir(SDCardUtils.GetPath());
		
		
		
	
		lvContent.setOnItemClickListener(onItemClickListener);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String path =currentFile.getParent();
				getFileDir(path);
			}
		});
		
		scanning.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isScanning){
					File file = new File(path.getText().toString());
					currentFile = file;
				}
				if(lt!=null)
				{
					lt.flag  = false;
				}
				searchDatas.clear();
				adapter.setSearchDatas(searchDatas);
				adapter.notifyDataSetChanged();
				scanningCount = 0;
				isScanning = true;
				mProcessDialog = new Dialog(AddFile.this, R.style.transparentdialog);
				mProcessDialog.setContentView(R.layout.process);
				mProcessDialog.setCancelable(true);
				mProcessDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						lt.flag  = false;
						Toast.makeText(AddFile.this, "扫描取消", 1).show();
					}
				});
				mProcessDialog.show();
				
				lt = new LoadTask(mHandler);
				lt.execute(null);
			}
		});
	}
	/**
	 *  得到当前路径的所有文件
	 * @param path
	 */
	public void getFileDir(String path)
	{
//		currentPath = path;
		this.path.setText(path);
		// 获取当前路径下的文件 
		if(searchDatas!=null && searchDatas.size()!=0)
			searchDatas.clear();
		
		searchDatas = new ArrayList<SearchData>();
		
		File presentFile = new  File(path); 
		currentFile  = presentFile;
		File[] files = presentFile.listFiles();  
		if(path.equals(SDCardUtils.GetPath()))
		{
			back.setVisibility(View.GONE);
		}else {
			back.setVisibility(View.VISIBLE);
		}
		
		//添加当前路径下的所有的文件名和路径  
		for(File f: files)
		{
			SearchData searchData = new SearchData();
			int index = f.getName().indexOf(".");
			if(f.isDirectory() && index!=0) //如果是目录 并排除隐藏文件
			{
				searchData.setDirectoryOrFile(true); 
				searchData.setFile(f);
				searchDatas.add(searchData);
			}else if(f.isFile() && index!=0)
			{
				String format = SDCardUtils.getFileExtension(f.getAbsolutePath());
				if(format!=null)
				{
					if(format.equals("txt")){
						searchData.setDirectoryOrFile(false); 
						searchData.setFile(f);
						searchDatas.add(searchData);
					}
				}
			}else {
				
			}
		}
		
		adapter.setSearchDatas(searchDatas);
		lvContent.setAdapter(adapter);
		lvContent.setSelection(postion);
	}
	String name;
	public boolean flag = true;
	public String line="";
	public Book book;
	public List<Chapter> ChapterList = null;
	public Chapter chapter;
	public StringBuffer sb;
	int  count = 0;
	int chapterName = 1;
	public boolean isSuccessAddChapterFlag = true;
	public static String action = "android.add.action";
	public boolean flagAction = false;
	private OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int index,long arg3) {
			postion = index;
			final SearchData searchData = searchDatas.get(index);
			if(searchData.getFile().isDirectory()) //如果是目录
			{
				getFileDir(searchData.getFile().toString());
			}else
			{
				String format = SDCardUtils.getFileExtension(searchData.getFile().getAbsolutePath());
				if(searchData.getFile().isFile())
				{
					if(format.equals("txt")){
						name = searchData.getFile().getName();
						name = name.substring(0, name.lastIndexOf("."));
						AlertDialog.Builder builder = new AlertDialog.Builder(AddFile.this);
						builder.setTitle(getString(R.string.addBook));
						builder.setMessage(getString(R.string.isAddBook)+" '"+name+"' "+getString(R.string.toBookShelf1)+"?");
						builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if(Book.IsExistByName(name))
								{
									Toast.makeText(AddFile.this, "该书籍已存在!", 1).show();
								}else
								{
									mProcessDialog = new Dialog(AddFile.this,R.style.transparentdialog);
									mProcessDialog.setContentView(R.layout.process);
									mProcessDialog.setCancelable(true);
									mProcessDialog.show();
//									addBook(searchData);
									book = new Book();
									book.Book_Name = name;
									book.bookidNet = String.valueOf(System.currentTimeMillis());
									book.pic_Path = searchData.getFile().getAbsolutePath();
									book.BookType = 1;
									book.bUpdate = 2;
									book.bFree = 3;
									Book.AddBook(book);
									
									Intent intent = new Intent(AddFile.this,AddBookService.class);
									intent.putExtra("bookId", book.bookidNet);
//									AddBookService.book = book;
									intent.putExtra("searchData", searchData);
//									AddBookService.searchData = ;
									startService(intent);
									
									
									IntentFilter filter = new IntentFilter(action);
									registerReceiver(broadcastReceiver, filter);
									flagAction = true;
//									new Thread()
//									{
//
//										@Override
//										public void run() {
//											while (isSuccessAddChapterFlag) {
//												if(AddBookService.isSuccessAddChapter)
//												{
//													proHandler.sendEmptyMessage(0);
//													break;
//												}
//												try {
//													Thread.sleep(1000);
//												} catch (InterruptedException e) {
//													e.printStackTrace();
//												}
//											}
//											super.run();
//										}
//										
//									}.start();
								}
							}
						});
						
						builder.setNegativeButton(getString(R.string.cancel),null);
						builder.create().show();
					}
				}
			}
		}
	};
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(isScanning)
			{
				isScanning = false;
				path.setText(currentFile.toString());
				getFileDir(currentFile.toString());
			}else
			{
				if(path.getText().equals(SDCardUtils.GetPath())) //如果是根目录
				{
					setResult(BookShelf.REFRESHCODE);
					finish();
				}else
				{
					getFileDir(currentFile.getParent());
				}
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		if(flagAction)
		{
			unregisterReceiver(broadcastReceiver);
			flagAction = false;
		}
		super.onDestroy();
	}
	public Handler proHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			if(mProcessDialog!=null && mProcessDialog.isShowing())
				mProcessDialog.dismiss();
			Toast.makeText(AddFile.this, "添加成功!", 0).show();
			AddBookService.isSuccessAddChapter = false;
//			AlertDialog.Builder builder = new AlertDialog.Builder(AddFile.this);
//			builder.setTitle("提示");
//			builder.setMessage("添加成功!,是否开始阅读?");
//			builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					BookShelf.mCurrentBook = book;
//					Util.toActivity(AddFile.this, BookReadActivity.class);
//					AddFile.this.finish();
//				}
//				
//			});
//			builder.setNegativeButton(getString(R.string.cancel), null);
//			builder.create().show();
			super.handleMessage(msg);
		}
		
	};
	
	private Handler mHandler  = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			
			Object object = msg.obj;
			if(object!=null){
				SearchData searchData = new SearchData();
				searchData.setDirectoryOrFile(false); 
				searchData.setFile((File)object);
				searchDatas.add(searchData);
			}
			adapter.setSearchDatas(searchDatas);
			adapter.notifyDataSetChanged();
			back.setVisibility(View.GONE);
			lvContent.setSelection(searchDatas.size());
			path.setText("扫描结果"+String.valueOf(scanningCount)+"本");
			super.handleMessage(msg);
		}
		
	};
	
	 private class LoadTask extends AsyncTask<Void, Void, Void> 
	    {
		   private boolean flag  = true;
	    	Handler mHandler;
	    	public LoadTask(Handler handler)
	    	{
	    		mHandler = handler;
	    	}
			protected void onPreExecute() {
				super.onPreExecute();
			}
			private void LoadApk(File FileApk,int nDepth)
			{
				int mDepth = nDepth;
				File[] fileLists = FileApk.listFiles();
				if(fileLists == null)
				{
					return;
				}
		        
		        for(int i = 0;i< fileLists.length;i++)
		        {
		        	if(flag)
		        	{
			        	File file = fileLists[i];
			        	if(file !=null)
			        	{
			        		String extension = SDCardUtils.getFileExtension(file.getAbsolutePath());
				        	if(file.isFile() && extension != null &&  extension.compareTo("txt") == 0)
				        	{
				        		scanningCount++;
				        		mHandler.sendMessage(mHandler.obtainMessage(0,file));
				        	} 
				        	else if(file.isDirectory() && mDepth < 4)
				        	{
				        		LoadApk(file,(mDepth+1));
				        	}
			        	}
		        	}else
		        	{
		        		break;
		        	}
		        }
			}
			
			@Override
			protected void onCancelled() {
				super.onCancelled();
				if(mProcessDialog.isShowing())
					mProcessDialog.dismiss();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				scanningCount = 0;
				if(searchDatas!=null && searchDatas.size()!=0){
					searchDatas.clear();
				}
		        String path =  SDCardUtils.GetPath();
		        File FileApk = new File(path);
		        LoadApk(FileApk,0);
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(mProcessDialog.isShowing())
					mProcessDialog.dismiss();
				
				mHandler.sendMessage(mHandler.obtainMessage(0,null));
				
			}
			
	    }
	 
	 public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			proHandler.sendEmptyMessage(0);
		}
	};
}
