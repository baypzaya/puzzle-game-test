package com.idreamsky.ktouchread.directoy;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aliyun.aui.app.spirit.SpiritActivity;
import com.idreamsky.ktouchread.Adapter.BookMarkAdapter;
import com.idreamsky.ktouchread.Adapter.DirectoryAdapter;
import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.BookMark;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.service.AddBookService;
import com.idreamsky.ktouchread.util.LogEx;
import com.idreamsky.ktouchread.util.Util;

public class DirectoryActivity extends SpiritActivity  {
	private int directory = 1; //目录
	private int bookMark = 2; //书签
	private int curSelect = directory ;//当前选中 默认目录
	private Button btn_directory; 
	private Button btn_bookMark;
	private ListView lvDirectory;
	private LinearLayout linDirectoryTitle; //目录工具栏
	private TextView tvSection; //显示总章节
	private Spinner spinnerSection;
	List<Chapter> chapters = null;
	List<Chapter> chapterCuts = null; //截取章节
	List<BookMark> bookMarks  = null;
	private int showSection = 100; //一次显示多少章节
	private DirectoryAdapter directoryAdapter;
	private BookMarkAdapter bookMarkAdapter;
	private Book book;
	public static int directoryCode = 333;
	private int spinnerIndex = 0 ;
	String entrance = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directory_bookmark);
		btn_directory = (Button) this.findViewById(R.id.btn_directory); 
		btn_bookMark = (Button) this.findViewById(R.id.btn_bookMark);
		lvDirectory  = (ListView) this.findViewById(R.id.lvDirectory);
		linDirectoryTitle  = (LinearLayout) this.findViewById(R.id.linDirectoryTitle);
		tvSection = (TextView) this.findViewById(R.id.tvSection);
		spinnerSection  =(Spinner) this.findViewById(R.id.spinnerSection);
		Button btnBack = (Button) this.findViewById(R.id.btn_director_back); 
		btnBack.setOnClickListener(btnClickListener);
		
		btn_directory.setOnClickListener(btnClickListener);
		btn_bookMark.setOnClickListener(btnClickListener);
		entrance = getIntent().getStringExtra(Util.ENTRANCE); //入口
		if(entrance!=null)
		{
			
		}
	
		book = BookShelf.mCurrentBook;//(Book) getIntent().getExtras().getSerializable(BookShelf.BOOKINFO);
		book.OpenBook();
		chapters = book.GetChapterList();
		bookMarks = book.GetBookMarkList();
		
		lvDirectory.setOnItemLongClickListener(onItemLongClickListener);
		tvSection.setText(getString(R.string.directory_book)+chapters.size()+getString(R.string.unread_number));
		new Thread(){

			@Override
			public void run() {
				chapterCuts = new ArrayList<Chapter>();
				if(chapters.size()>100){
					for(int i=0;i<showSection;i++) //默认显示100章节 
					{
						chapterCuts.add(chapters.get(i));
					}
				}else {
					for(int i=0;i<chapters.size();i++) //默认显示100章节
					{
						chapterCuts.add(chapters.get(i));
					}
				}
				handler.sendEmptyMessage(0);
				super.run();
			}
			
		}.start();
		
				
		directoryAdapter = new DirectoryAdapter(DirectoryActivity.this);
		resetSpinnerContent();

		
		bookMarkAdapter = new BookMarkAdapter(this);
	}
	
	public void resetSpinnerContent()
	{
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				int count = 0;
				float count1 = 0.0f;
				if(chapters.size()>100) 
					count1 = (float)chapters.size()/(float)showSection; //计算下拉框显示的内容 
				else
					count1 = 1; //默认1 
				count = (int) Math.ceil(count1);
				String[] spinnerShow = new String[count]; 
				int smallPage = 0;
				for(int j=1;j<=count;j++)
				{
					int bigNumber = j*showSection;
					if(j==1) //第一个
					{
						smallPage = j;
					}
					spinnerShow[j-1] = smallPage+" - "+bigNumber+getString(R.string.unread_number);
					smallPage+=showSection;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(DirectoryActivity.this,android.R.layout.simple_spinner_item,spinnerShow);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
				spinnerSection.setAdapter(adapter);
				spinnerSection.setOnItemSelectedListener(onItemSelectedListener);
			}
		});
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				directoryAdapter.setChapters(chapterCuts);
				lvDirectory.setAdapter(directoryAdapter);
				lvDirectory.setOnItemClickListener(onItemClickListener);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	public OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int index, long arg3) {
			if(chapters.size()>100){
				int bigCount = index+1;
				bigCount = bigCount*showSection; //计算要显示到显示到哪个章节
				int smallCount = bigCount - showSection; 
				if(bigCount>=chapters.size())
				{
					bigCount = chapters.size();
				}
				chapterCuts.clear();
				for(int i=smallCount;i<bigCount;i++) //默认显示的章节
				{
					chapterCuts.add(chapters.get(i));
				}
				directoryAdapter.notifyDataSetChanged();
				lvDirectory.setSelection(0);
			}else {
				chapterCuts.clear();
				for(int i=0;i<chapters.size();i++) //默认显示100章节
				{
					chapterCuts.add(chapters.get(i));
				}
				directoryAdapter.notifyDataSetChanged();
				lvDirectory.setSelection(0);
			}
			spinnerIndex = index;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	};
	
	public AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			if(curSelect == bookMark)
			{
				if(arg2 >=0 && arg2 < bookMarks.size())
				{
				   final	BookMark bookMark = bookMarks.get(arg2);
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DirectoryActivity.this);
					builder.setTitle(R.string.splash_notify);
					builder.setMessage(R.string.delete_bookmark);
					builder.setPositiveButton(getString(R.string.confirm),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									book.DeleteBookMark(bookMark);
									bookMarks.remove(bookMark);
//									bookMarks = book.GetBookMarkList();
//									bookMarkAdapter.setBookMarks(bookMarks);
									bookMarkAdapter.notifyDataSetChanged();

								}
							});
					builder.setNegativeButton(getString(R.string.cancel),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});
					builder.create().show();
					
				}
			}
			return false;
		}
	};
	
	public OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
			if(curSelect==directory) //目录
			{
				directoryClick(index);
			}else
			{
				bookMarkClick(index);
			}
			
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==BookShelf.REFRESHCODE)
		{
			bookMarks.clear();
			bookMarks = book.GetBookMarkList();
			bookMarkAdapter.setBookMarks(bookMarks);
			bookMarkAdapter.notifyDataSetChanged();
		}
		
		if(chapterCuts!=null)
		{
			if(AddBookService.isAddChapter){
				int oldCount = chapters.size();
				chapters = book.GetChapterList();
				if(oldCount==chapters.size())
				{
					return;
				}
//				chapterCuts.clear();
//				chapterCuts = new ArrayList<Chapter>();
				if(chapters.size()>100){
//					chapterCuts.addAll(chapters.subList(0, 99));
//					for(int i=0;i<showSection;i++) //默认显示100章节 
//					{
//						chapterCuts.add(chapters.get(i));
//					}
					int bigCount = spinnerIndex+1; //2
					bigCount = bigCount*showSection; //计算要显示到显示到哪个章节  200
					int smallCount = bigCount - showSection; //200-100 
					if(bigCount>=chapters.size())
					{
						bigCount = chapters.size();
					}
					chapterCuts.clear();
					for(int i=smallCount;i<bigCount;i++) //默认显示的章节
					{
						chapterCuts.add(chapters.get(i));
					}
				}else {
					chapterCuts.clear();
					chapterCuts.addAll(chapters);
				}
				tvSection.setText(getString(R.string.directory_book)+chapters.size()+getString(R.string.unread_number));
				directoryAdapter.notifyDataSetChanged();
				lvDirectory.setSelection(0);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 目录项点击事件
	 */
	public void directoryClick(int index)
	{
		int i = 0;
		if(spinnerIndex>=1){
			i  = spinnerIndex*showSection+index;
		}else
		{
			i  = index;
		}
		Intent intent = new Intent();
		intent.putExtra(Util.ENTRANCE,Util.CHAPTER); //传章节id
		if(entrance!=null)
			intent.putExtra(Util.ENTRANCEBOOKDETAIL,BookShelf.BOOKDETAIL);
//		Bundle bundle = new Bundle();
//		bundle.putSerializable(BookShelf.READBOOK, book);
//		intent.putExtras(bundle);
		intent.putExtra(Util.CHAPTER,i); 
		intent.setClass(this, BookReadActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		if(book.BookType==1)
		{
			startActivityForResult(intent,BookShelf.REFRESHCODE);
		}else
		{
			startActivityForResult(intent,BookShelf.REFRESHCODE);
		}
		
	}
	public static Bitmap bgBitmap;
	public Bitmap bgBitmap1;
	/**
	 * 书签点击
	 */
	public void bookMarkClick(int index)
	{
		BookMark bookMark = bookMarks.get(index);
		LogEx.Log_I("operate", "onSingleTapUp touch off 1");
//		Util.toActivity(mActivity, BookReadActivity.class);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(BookShelf.READBOOK, book);
		bundle.putSerializable(BookShelf.DIRECTORY, bookMark);
		intent.putExtra(Util.ENTRANCE, Util.BOOKMARK);
		intent.putExtras(bundle);
		intent.setClass(this,  BookReadActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivityForResult(intent,BookShelf.REFRESHCODE);
	}
	
	
	
	private OnClickListener btnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.btn_directory:
					curSelect = directory;
					changeCuSelect();
					if(chapterCuts.size()==0)
					{
//						int size = chapterCuts.size();
//						for(int i=0;i<size;i++) //默认显示的章节
//						{
//							chapterCuts.add(chapters.get(i)); 
//						}
						if(chapters.size()>100){
							for(int i=0;i<showSection;i++) //默认显示100章节
							{
								chapterCuts.add(chapters.get(i));
							}
						}else {
							for(int i=0;i<chapters.size();i++) 
							{
								chapterCuts.add(chapters.get(i));
							}
						}
					}
					directoryAdapter.setChapters(chapterCuts);
					lvDirectory.setAdapter(directoryAdapter);
					directoryAdapter.notifyDataSetInvalidated();
					break;
				case R.id.btn_bookMark:
					curSelect = bookMark;
					changeCuSelect();
					bookMarks = book.GetBookMarkList();
					bookMarkAdapter.setBookMarks(bookMarks);
					lvDirectory.setAdapter(bookMarkAdapter);
					bookMarkAdapter.notifyDataSetChanged();
					break;
				case R.id.btn_director_back:
				{
					back();
				}
				break;
			}
			
		}
	};
	private void back()
	{
		this.finish();
	}
	public void changeCuSelect() //改变当前选中的
	{
		if(curSelect==directory) //如果目录
		{
			btn_directory.setBackgroundResource(R.drawable.bar_bg_8); //选中的背景
			btn_directory.setTextColor(Color.WHITE);
			btn_bookMark.setBackgroundResource(R.drawable.bar_bg_7); //书签改未选中
			btn_bookMark.setTextColor(Color.BLACK);
			linDirectoryTitle.setVisibility(View.VISIBLE);
		}else{
			btn_directory.setBackgroundResource(R.drawable.bar_bg_7); //选中的背景
			btn_directory.setTextColor(Color.BLACK);
			btn_bookMark.setBackgroundResource(R.drawable.bar_bg_8); //书签改未选中
			btn_bookMark.setTextColor(Color.WHITE);
			linDirectoryTitle.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			this.setResult(BookShelf.REFRESHCODE);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
