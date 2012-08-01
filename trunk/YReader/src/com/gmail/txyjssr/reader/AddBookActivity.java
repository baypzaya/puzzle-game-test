package com.gmail.txyjssr.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AddBookActivity extends BaseActivity implements OnItemClickListener{

	private ListView mListView;
	private File rootFile = Environment.getExternalStorageDirectory();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_file_system);
		
		rootFile = Environment.getExternalStorageDirectory();
		List<File> fileList = getAllFiles(rootFile);
		
		mListView = (ListView)findViewById(R.id.lv_files);
		FilesAdapter adapter = new FilesAdapter(this, fileList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
	}

	private List<File> getAllFiles(File currentFile) {
		List<File> fileList = null;
		File[] files = currentFile.listFiles();
		if (files != null) {
			fileList = Arrays.asList(files);
		} else {
			fileList = new ArrayList<File>();
		}
		return fileList;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		File file = (File) mListView.getAdapter().getItem(position);
		if(file.isFile()){
			Book book = new Book();
			book.name = file.getName();
			book.path = file.getPath();
			book.progress = 0;
			book.total = file.length();
			BookDao.getInstance().addBook(book);
		}
		finish();
	}
}
