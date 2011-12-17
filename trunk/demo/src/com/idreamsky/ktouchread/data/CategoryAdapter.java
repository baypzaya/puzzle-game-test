package com.idreamsky.ktouchread.data;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.bookshop.AbstractView;
import com.idreamsky.ktouchread.data.net.Category;

public final class CategoryAdapter extends IdreamSkyBaseAdapter<Category> {
	
	private static final int ID_NAME = 0x41;
	private static final int ID_ARROW = 0x41;
	
	public CategoryAdapter(Context context, List<Category> categorys) {
		super(context, categorys);
	}

	@Override
	protected boolean isUpdatable() {
		return true;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final Context context = mContext;
		final Category category = getObject(position);
		if(null == convertView){
			final float density = 1;// Configuration.getDensity(p);
			final RelativeLayout layout = new RelativeLayout(context);
			
			
//			final ImageView icon = new ImageView(context);
//			icon.setId(ID_ICON);
//			icon.setBackgroundResource(R.drawable.icon);
//			RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
//					(int) (58 * density), (int) (58 * density));
//			iconParams.leftMargin = (int) (10 * density);
//			iconParams.addRule(RelativeLayout.CENTER_VERTICAL);
//			iconParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			layout.addView(icon, iconParams);
			//BitmapRequest.fillImageView(Game.getSmallIconUrl(game), icon);
			
			
			final ImageView arrow = new ImageView(context);
			arrow.setBackgroundResource(R.drawable.arrow_right);
			arrow.setId(ID_ARROW);
			arrow.setBackgroundResource(R.drawable.arrow_right);
			RelativeLayout.LayoutParams arrowParams = new RelativeLayout.LayoutParams(
					(int) (23 * density), (int) (23 * density));
			arrowParams.rightMargin = (int) (10 * density);
			arrowParams.addRule(RelativeLayout.CENTER_VERTICAL);
			arrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			layout.addView(arrow, arrowParams);
			
			
			final TextView tvName = AbstractView.generateTextView(context, ID_NAME, category.categoryname,
					20.0f, -1, -1, null);
			RelativeLayout.LayoutParams nickParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			nickParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			nickParams.addRule(RelativeLayout.CENTER_VERTICAL);
			nickParams.leftMargin = (int) (10 * density);
			nickParams.topMargin = (int) (10 * density);
			layout.addView(tvName, nickParams);
			
			ViewHolder holder = new ViewHolder();
			//holder.icon = icon;
			holder.name = tvName;
			layout.setTag(holder);
			layout.setLayoutParams(new ListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			convertView = layout;
			
		}else{
			
			ViewHolder vh = (ViewHolder) convertView.getTag();
			//ImageView icon = vh.icon;
			//icon.setBackgroundResource(R.drawable.icon);
			//BitmapRequest.fillImageView(game.icon_url, icon);
			vh.name.setText(category.categoryname);	
			
		}
		
		return convertView;
	}
	final private class ViewHolder {

		TextView name;

	}
}
