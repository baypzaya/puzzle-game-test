package com.idreamsky.ktouchread.bookshop.adapter;
import java.util.List;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.net.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryAdapter extends IdreamSkyBaseAdapter<Category>{
	
	private LayoutInflater layoutInflater;
	
	public CategoryAdapter(Context profile, List<Category> Tops) {
		super(profile, Tops);
		layoutInflater = LayoutInflater.from(profile);
	}

	@Override
	protected boolean isUpdatable() {
		return true;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Category category = getObject(position);
		ViewHolder holderl;
		if(null == convertView){
			holderl = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.book_onlie_category_item, null);
			holderl.name = (TextView) convertView.findViewById(R.id.category_name);
			convertView.setTag(holderl);
		}else{
			holderl = (ViewHolder) convertView.getTag();
		}
		if(category.categoryid.compareTo("-1") != 0)
		    holderl.name.setText(category.categoryname + "  ("+ category.bookcount + ")");
		return convertView;
	}
	final private class ViewHolder {
		TextView name;
	}
}