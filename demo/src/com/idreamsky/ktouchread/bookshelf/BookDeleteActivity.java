package com.idreamsky.ktouchread.bookshelf;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.aliyun.aui.app.spirit.SpiritListActivity;
import com.aliyun.aui.widget.spirit.CustomButton;
import com.aliyun.aui.widget.spirit.NavigationBar;
import com.aliyun.aui.widget.spirit.SpiritMenu;
import com.idreamsky.ktouchread.data.Book;

public class BookDeleteActivity extends SpiritListActivity implements OnItemClickListener{
	
	protected boolean isSelectAll = false;	
	private BookSelectItemAdpater adpater;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置 NavigationBar
		final NavigationBar.Builder builder = getNavigationBarBuilder();
		builder.setTitle(R.string.deleteBook);
		builder.showBackButton(true);
		
		builder.setCommand(getString(R.string.select_all), new View.OnClickListener() {
			public void onClick(View v) {
				if(isSelectAll) {
					adpater.cancelSelectAll();
					builder.setCommandName(getString(R.string.select_all));
					isSelectAll = false;
				} else {
					adpater.selectAll();
					builder.setCommandName(getString(R.string.select_cancle));
					isSelectAll = true;
				}
			}
		});
		
		SpiritMenu.Builder menuBuilder = getSpiritMenuBuilder()
				.addButton(0, CustomButton.WARNING,R.string.delete)
				.addButton(1, CustomButton.NORMAL, R.string.cancel)
				.setOnItemClickListener(new SpiritMenu.OnItemClickListener(){
					public void onClick(int itemId) {
						switch (itemId) {
						case 0:
							finishSelectBooks();
							break;
						case 1:
							finish();
							break;
						default:
							break;
						}
					}
				});
		menuBuilder.show();
		
		
		
		
		List<Book> books = Book.GetBookList();
		ListView listView = getListView();
		listView.setPadding(0, 0, 0, menuBuilder.getSpiritMenuHeight());
		listView.setOnItemClickListener(this);
		adpater =  new BookSelectItemAdpater(this,books);
		listView.setAdapter(adpater);
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	

	private void finishSelectBooks() {
		boolean[] selectTags = adpater.getSelectedTags();
		List<String> bookIdNetList = new ArrayList<String>();
		for(int i=0;i<selectTags.length;i++){
			
			if(selectTags[i]){
				Book book = (Book) adpater.getItem(i);
				bookIdNetList.add(book.bookidNet);
			}
		}
		
		String[] results = new String[bookIdNetList.size()];
		for(int i=0;i<results.length;i++){
			results[i] = bookIdNetList.get(i);
		}

		Intent intent = new Intent();
		intent.putExtra(BookShelf.DELETE_BOOKS, results);
		setResult(RESULT_OK,intent);
		finish();
	}
	
	
}
