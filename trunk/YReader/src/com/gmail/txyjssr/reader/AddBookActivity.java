package com.gmail.txyjssr.reader;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gmail.txyjssr.reader.data.Book;
import com.gmail.txyjssr.reader.logic.BookLogic;
import com.gmail.txyjssr.reader.logic.FileLogic;

public class AddBookActivity extends BaseActivity implements OnItemClickListener{

	private ListView mListView;
	private final File rootFile = Environment.getExternalStorageDirectory();
	private FileLogic mLogic = new FileLogic();
	private File mCurrentFile;
	private FilesAdapter mFileAdapter;
	private BookLogic mBookLogic = new BookLogic();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_file_system);
		
		mListView = (ListView)findViewById(R.id.lv_files);
		mFileAdapter = new FilesAdapter(this);
		mListView.setAdapter(mFileAdapter);
		mListView.setOnItemClickListener(this);
		mCurrentFile = rootFile;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		openDirectory(mCurrentFile);
	}



	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		File file = (File) mListView.getAdapter().getItem(position);
		if(file.isFile()){
			openFile(file);
		}else if(file.isDirectory()){
			openDirectory(file);
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			if(mCurrentFile.equals(rootFile)){
				finish();
			}else{
				openDirectory(mCurrentFile.getParentFile());
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void openDirectory(File file) {
		mCurrentFile = file;
		List<File> fileList = mLogic.getAllFiles(file);
		mFileAdapter.setFiles(fileList);
	}
	
	private void openFile(File file) {
		Book book = mBookLogic.addBook(file);
		Intent intent = new Intent();
		intent.setClass(this, ReadBookActivity.class);
		intent.putExtra(ReadBookActivity.EXTRA_READ_BOOK, book);
		startActivity(intent);
		finish();
	}
	
	
}
