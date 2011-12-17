package com.idreamsky.ktouchread.bookshop.adapter;


import java.util.List;
import android.content.Context;
import android.widget.BaseAdapter;

public abstract class IdreamSkyBaseAdapter<T> extends BaseAdapter {

	private List<T> mList;
	protected Context mContext;

	public IdreamSkyBaseAdapter(Context context, List<T> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public T getObject(int position) {
		return mList.get(position);
	}

	public void setObject(int position, T t) {
		mList.set(position, t);
		notifyDataSetChanged();
	}

	public void appendMore(List<T> list) {
		if (!isUpdatable()) {
			return;
		}
		if (null == list || list.size() <= 0) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}
	
	public void AddNew(List<T> list) {
		if (!isUpdatable()) {
			return;
		}
		if (null == list || list.size() <= 0) {
			return;
		}
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}
	public void RemoveAll()
	{
		mList.clear();
		notifyDataSetChanged();
	}

	public void appendMoreFromTop(List<T> list) {
		if (!isUpdatable()) {
			return;
		}
		if (null == list || list.size() <= 0) {
			return;
		}
		mList.addAll(0, list);
		notifyDataSetChanged();
	}

	public void append(T t) {
		if (!isUpdatable()) {
			return;
		}
		if (null == t) {
			return;
		}
		mList.add(t);
		notifyDataSetChanged();
	}

	public void remove(T t) {
		if (!isUpdatable()) {
			return;
		}
		if (null == t) {
			return;
		}
		if (mList.remove(t)) {
			notifyDataSetChanged();
		}
	}

	public void remove(int position) {
		if (!isUpdatable()) {
			return;
		}
		int size = getCount();
		if (position < 0 || position > size - 1) {
			return;
		}
		mList.remove(position);
		notifyDataSetChanged();
	}

	public void removeOrThrow(int position) {
		if (!isUpdatable()) {
			return;
		}
		mList.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * Return true to make the attached data set updatable, false otherwise.
	 * <p>
	 * Default is <b>false</b>.
	 */
	protected boolean isUpdatable() {
		return false;
	}

}

