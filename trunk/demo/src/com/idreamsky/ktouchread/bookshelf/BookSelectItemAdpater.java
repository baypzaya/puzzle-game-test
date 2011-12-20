package com.idreamsky.ktouchread.bookshelf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aliyun.aui.app.spirit.SpiritListActivity;
import com.aliyun.aui.widget.spirit.NavigationBar.Builder;
import com.idreamsky.ktouchread.data.Book;
import com.idreamsky.ktouchread.http.bitmapasync.AsyncImageLoader;
import com.idreamsky.ktouchread.util.ImgUtil;
import com.idreamsky.ktouchread.util.SDCardUtils;

public class BookSelectItemAdpater extends BaseAdapter implements OnCheckedChangeListener{
	private Context mContext;
	private List<Book> books = new ArrayList<Book>();
	private LayoutInflater mLayoutInflater;
	private boolean[] selectedTags;
	private AsyncImageLoader asyncImageLoader;
	private ListView listView;
//jiangbiao add 
	private ImageView myBookShelfLogo;
	private Button myBookShelfUpdateCount;
	private TextView myBookShelfBookName;
	private TextView myBookShelfAuthor;
	private TextView myBookShelfLatelyReadTitle;
	private TextView myBookShelfLatelyRead;
	private CheckBox myBookSelectCheckBox;
	public BookSelectItemAdpater(Context mContext,List<Book> books) {
		this.mContext = mContext;
		mLayoutInflater = LayoutInflater.from(mContext);
		this.books = books;
		selectedTags = new boolean[books.size()];
		asyncImageLoader = new AsyncImageLoader();
		listView =( (SpiritListActivity)mContext).getListView();
	}

	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int position) {
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//final ViewHolder v;
//jiangbiao modify
		View v = null;
		int count = 0;
		if (convertView == null) {
			v = mLayoutInflater.inflate(R.layout.book_select_item,
					null);
			/*v = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.book_select_item,
					null);
			v.myBookShelfLogo = (ImageView) convertView
					.findViewById(R.id.myBookShelfLogo);
			v.myBookShelfUpdateCount = (Button) convertView
					.findViewById(R.id.myBookShelfUpdateCount);
			v.myBookShelfBookName = (TextView) convertView
					.findViewById(R.id.myBookShelfBookName);
			v.myBookShelfAuthor = (TextView) convertView
					.findViewById(R.id.myBookShelfAuthor);
			v.myBookShelfLatelyReadTitle = (TextView) convertView
					.findViewById(R.id.myBookShelfLatelyReadTitle);
			v.myBookShelfLatelyRead = (TextView) convertView
					.findViewById(R.id.myBookShelfLatelyRead);
			v.myBookSelectCheckBox = (CheckBox) convertView
					.findViewById(R.id.checkbox_select);
			v.myBookSelectCheckBox.setOnCheckedChangeListener(this);
			convertView.setTag(v);*/
			
			
		} else {
			//v = (ViewHolder) convertView.getTag();
			v = convertView;

		}
//jiangbiao add
		v.setTag(position);
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("BookSelectItemAdapter","--------------->onClick");
				int pos = (Integer)v.getTag();
				if(selectedTags[pos]){
					selectedTags[pos] = false;
				}else{
					selectedTags[pos] = true;
				}
				BookSelectItemAdpater.this.notifyDataSetChanged();
			}
		});
		for(Boolean tag : selectedTags){			
			if(tag){
				count++;
			}
		}
		BookDeleteActivity activity = (BookDeleteActivity)mContext;
		Builder builder = activity.getNavigationBarBuilder();
		if(count != selectedTags.length){
			builder.setCommandName(activity.getString(R.string.select_all));
			activity.isSelectAll = false;
		}else{
			builder.setCommandName(activity.getString(R.string.select_cancle));
			activity.isSelectAll = true;
		}
		String buttonName = activity.getString(R.string.delete)+"("+count+")";
		activity.getSpiritMenuBuilder().setButtonText(0, buttonName);
		myBookShelfLogo = (ImageView) v
		.findViewById(R.id.myBookShelfLogo);
		myBookShelfUpdateCount = (Button) v
				.findViewById(R.id.myBookShelfUpdateCount);
		myBookShelfBookName = (TextView) v
				.findViewById(R.id.myBookShelfBookName);
		myBookShelfAuthor = (TextView) v
				.findViewById(R.id.myBookShelfAuthor);
		myBookShelfLatelyReadTitle = (TextView) v
				.findViewById(R.id.myBookShelfLatelyReadTitle);
		myBookShelfLatelyRead = (TextView) v
				.findViewById(R.id.myBookShelfLatelyRead);
		myBookSelectCheckBox = (CheckBox) v
				.findViewById(R.id.checkbox_select);
		final Book book = books.get(position);
		/*v.myBookShelfLogo.setScaleType(ImageView.ScaleType.FIT_XY);
		v.myBookShelfLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cover));
		v.myBookShelfBookName.setText(book.Book_Name);
		v.myBookShelfAuthor.setText(mContext.getString(R.string.bookshelf_author)+book.Author);
		v.myBookShelfLatelyRead.setText(book.Recent_Chapter_Name);*/
		myBookShelfLogo.setScaleType(ImageView.ScaleType.FIT_XY);
		myBookShelfLogo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cover));
		myBookShelfBookName.setText(book.Book_Name);
		myBookShelfAuthor.setText(mContext.getString(R.string.bookshelf_author)+book.Author);
		myBookShelfLatelyRead.setText(book.Recent_Chapter_Name);
		if (book.BookType == 0) {
			//v.myBookShelfLogo.setTag(book.bookidNet);
			myBookShelfLogo.setTag(book.bookidNet);

			final WeakReference<Bitmap> bitmapRef = ImgUtil.sBitmapPool
					.get(book.bookidNet);

			Bitmap coverBitmap = null;
			if (null != bitmapRef) {
				Bitmap bitmap = bitmapRef.get();
				if (null != bitmap) {
					if (bitmap.isRecycled()) {
						ImgUtil.sBitmapPool.remove(book.bookidNet);
					} else {
						coverBitmap = bitmap;
						
					}
				}
			}
			
			if (coverBitmap == null) {
				if (SDCardUtils.isSDCardEnable()) {
					coverBitmap = ImgUtil.getImageByPath(book.bookidNet);
					if (coverBitmap != null) {
						//v.myBookShelfLogo.setImageBitmap(coverBitmap);
						myBookShelfLogo.setImageBitmap(coverBitmap);
						ImgUtil.sBitmapPool.put(book.bookidNet,
								new WeakReference<Bitmap>(coverBitmap));

					}
				}
				if (coverBitmap == null) {
					//v.myBookShelfLogo.setImageResource(R.drawable.cover);
					myBookShelfLogo.setImageResource(R.drawable.cover);
					asyncImageLoader.loadDrawable(book.Pic_Url, book.bookidNet,
							new AsyncImageLoader.ImageCallback() {

								@Override
								public void imageLoaded(Bitmap imageDrawable,
										String bookidNet) {
									ImageView imageViewByTag = (ImageView) listView
											.findViewWithTag(bookidNet);
									if (imageViewByTag != null) {
										imageViewByTag
												.setImageBitmap(imageDrawable);
										ImgUtil.sBitmapPool.put(bookidNet, new WeakReference<Bitmap>(imageDrawable));
									}
					                 else
					                 {
					                	 imageDrawable.recycle();
					                	 imageDrawable = null;
					                 }

								}
							});
				}
			} else {
				//v.myBookShelfLogo.setImageBitmap(coverBitmap);
				myBookShelfLogo.setImageBitmap(coverBitmap);
			}
		}
		else {
			//v.myBookShelfLogo.setTag(book.pic_Path);
			myBookShelfLogo.setTag(book.pic_Path);

		}
			
		
//			v.myBookSelectCheckBox.setChecked(selectedTags[position]);
//			v.myBookSelectCheckBox.setTag(position);
			myBookSelectCheckBox.setChecked(selectedTags[position]);
			myBookSelectCheckBox.setTag(position);
		return v;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
		selectedTags = new boolean[books.size()];
		this.notifyDataSetChanged();
	}
	
	public class ViewHolder {
		public ImageView myBookShelfLogo;
		public Button myBookShelfUpdateCount;
		public TextView myBookShelfBookName;
		public TextView myBookShelfAuthor;
		public TextView myBookShelfLatelyReadTitle;
		public TextView myBookShelfLatelyRead;
		public CheckBox myBookSelectCheckBox;

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int postion = (Integer)buttonView.getTag();
		this.selectedTags[postion] = isChecked;
		int count = 0;
		for(Boolean tag : selectedTags){			
			if(tag){
				count++;
			}
		}
		BookDeleteActivity activity = (BookDeleteActivity)mContext;
		Builder builder = activity.getNavigationBarBuilder();
		if(count != selectedTags.length){
			builder.setCommandName(activity.getString(R.string.select_all));
			activity.isSelectAll = false;
		}else{
			builder.setCommandName(activity.getString(R.string.select_cancle));
			activity.isSelectAll = true;
		}
		String buttonName = activity.getString(R.string.delete)+"("+count+")";
		activity.getSpiritMenuBuilder().setButtonText(0, buttonName);
		if(count == 0){
			activity.getSpiritMenuBuilder().setButtonEnable(0, false);
		}
	}
	
	public boolean[] getSelectedTags(){
		return selectedTags;
	}
	
	public void selectAll(){
		for(int i =0 ;i<selectedTags.length;i++){
			selectedTags[i] = true;
		}
		this.notifyDataSetChanged();
	}
	
	public void cancelSelectAll(){
		for(int i =0 ;i<selectedTags.length;i++){
			selectedTags[i] = false;
		}
		this.notifyDataSetChanged();
	}

}
