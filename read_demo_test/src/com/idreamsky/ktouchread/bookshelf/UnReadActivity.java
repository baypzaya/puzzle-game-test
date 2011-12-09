package com.idreamsky.ktouchread.bookshelf;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.idreamsky.ktouchread.Adapter.DirectoryAdapter;
import com.idreamsky.ktouchread.bookread.BookReadActivity;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.data.Chapter;
import com.idreamsky.ktouchread.util.Util;

public class UnReadActivity extends Activity {
	
	private TextView tvSection;
	private Spinner spinnerSection;
	private ListView lvUnRead;
	private Book book;
	List<Chapter> chapters = null;
	List<Chapter> chapterCuts = null; //截取章节
	private int showSection = 100; //一次显示多少章节
	private DirectoryAdapter directoryAdapter;
	private Button btn_unRead_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.un_read);
		tvSection  = (TextView) this.findViewById(R.id.tvSection);
		spinnerSection = (Spinner) this.findViewById(R.id.spinnerSection);
		lvUnRead = (ListView) this.findViewById(R.id.lvUnRead);
		btn_unRead_back = (Button) this.findViewById(R.id.btn_unRead_back);
		btn_unRead_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UnReadActivity.this.finish();
			}
		});
		book =  BookShelf.mCurrentBook;//(Book) getIntent().getExtras().getSerializable(BookShelf.BOOKINFO);
		chapters = book.GetUnReadChapter();
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
	
		tvSection.setText(getString(R.string.unread_content)+chapters.size()+getString(R.string.unread_number));
		directoryAdapter = new DirectoryAdapter(this);
		directoryAdapter.setChapters(chapterCuts);
		lvUnRead.setAdapter(directoryAdapter);
		lvUnRead.setOnItemClickListener(onItemClickListener);
		initSpinner();
		
		
	}
	private int spinnerIndex = 0 ;
	public OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,long arg3) {
			int i = 0;
			if(spinnerIndex>=1){
				i  = spinnerIndex*showSection+index;
			}else
			{
				i  = index;
			}
			Chapter chapter = chapters.get(i);
			Intent intent = new Intent();
			intent.putExtra(Util.ENTRANCE,Util.UNREAD); //传章节id
			Bundle bundle = new Bundle();
			bundle.putSerializable(BookShelf.READBOOK, book);
			intent.putExtras(bundle);
			intent.putExtra(Util.UNREAD,chapter); 
			intent.setClass(UnReadActivity.this, BookReadActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivityForResult(intent, BookShelf.REFRESHCODE);
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==BookShelf.REFRESHCODE)
		{
			chapters.clear();
			chapters =  book.GetUnReadChapter();
			directoryAdapter.setChapters(chapters);
			directoryAdapter.notifyDataSetChanged();
			new Thread(){

				@Override
				public void run() {
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							initSpinner();
						}
					});
					
					super.run();
				}
				
			}.start();
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private Handler handler = new Handler();
	public void initSpinner()
	{
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
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spinnerShow);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		spinnerSection.setAdapter(adapter);
		spinnerSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
					directoryAdapter.setChapters(chapterCuts);
					directoryAdapter.notifyDataSetChanged();
					lvUnRead.setSelection(0);
				}else {
					chapterCuts.clear();
					for(int i=0;i<chapters.size();i++) //默认显示100章节
					{
						chapterCuts.add(chapters.get(i));
					}
					directoryAdapter.setChapters(chapterCuts);
					directoryAdapter.notifyDataSetChanged();
					lvUnRead.setSelection(0);
				}
				spinnerIndex = index;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
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
