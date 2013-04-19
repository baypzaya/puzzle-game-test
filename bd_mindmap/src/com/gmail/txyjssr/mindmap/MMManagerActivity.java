package com.gmail.txyjssr.mindmap;

import java.util.Formatter;
import java.util.List;

import com.baidu.mobstat.StatService;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MMManagerActivity extends ListActivity implements OnItemClickListener, OnClickListener {
	public static final int RESULT_CODE_NEW = 1001;
	public static final int RESULT_CODE_OPEN = 1002;

	public static final String EXTRA_OPEN_MINDMAP_ID = "EXTRA_OPEN_MINDMAP_ID";
	public static final String EXTRA_NEW_MINDMAP_NAME = "EXTRA_NEW_MINDMAP_NAME";
	MindMapDao midMapDao = new MindMapDao();
	NodeDao nodeDao = new NodeDao();
	private MindMapAdapter adapter;
	private ImageView ivdelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_manager_activity);

		List<TabMindMap> tmmList = midMapDao.getAllMindMap();

		adapter = new MindMapAdapter(this, tmmList);
		getListView().setAdapter(adapter);

		getListView().setOnItemClickListener(this);

		ImageView ivBack = (ImageView) findViewById(R.id.iv_arrow_back);
		ivBack.setOnClickListener(this);

		ImageView ivAdd = (ImageView) findViewById(R.id.iv_mm_menu_add);
		ivAdd.setOnClickListener(this);

		ivdelete = (ImageView) findViewById(R.id.iv_mm_menu_delete);
		ivdelete.setOnClickListener(this);

		TextView tvSavePath = (TextView) findViewById(R.id.tv_image_save_path);
		Formatter formatter = new Formatter();

		String message = formatter.format(getString(R.string.hint_image_save_path), MindMapActivity.MM_CACHE_PATH).toString();
		tvSavePath.setText(message);
	}
	
	public void onResume() {
		super.onResume();
		// baidu code start
		StatService.onResume(this);
		// baidu code end
	}

	public void onPause() {
		super.onPause();
		// baidu code start
		StatService.onPause(this);
		// baidu code end
	}

	class MindMapAdapter extends BaseAdapter {
		public static final int TYPE_NORMAL = 0;
		public static final int TYPE_EDIT = 1;

		private Context context;
		private List<TabMindMap> tmmList;
		private int currentType = TYPE_NORMAL;

		public MindMapAdapter(Context context, List<TabMindMap> tmmList) {
			this.tmmList = tmmList;
			this.context = context;
		}

		@Override
		public int getCount() {
			return tmmList.size();
		}

		@Override
		public Object getItem(int position) {
			return tmmList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.mind_map_adapter, null);
			}
			TextView tvName = (TextView) convertView.findViewById(R.id.tv_mm_name);

			TabMindMap tmm = tmmList.get(position);
			tvName.setText(tmm.name);

			ImageView ivIcon = (ImageView) convertView.findViewById(R.id.iv_mm_icon);
			ImageView ivDelete = (ImageView) convertView.findViewById(R.id.iv_mm_delete);
			ivDelete.setTag(position);
			if (currentType == TYPE_EDIT) {
				ivIcon.setVisibility(View.GONE);
				ivDelete.setVisibility(View.VISIBLE);
			} else {
				ivIcon.setVisibility(View.VISIBLE);
				ivDelete.setVisibility(View.GONE);
				ivDelete.setOnClickListener(MMManagerActivity.this);
			}

			return convertView;
		}

		public void setCurrentType(int type) {
			this.currentType = type;
			notifyDataSetChanged();
		}

		public int getCurrentType() {
			return currentType;
		}

		public void removeItem(int position) {
			tmmList.remove(position);
			notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {

		TabMindMap tmm = (TabMindMap) getListView().getAdapter().getItem(position);
		Intent intent = new Intent();
		intent.putExtra(EXTRA_OPEN_MINDMAP_ID, tmm._id);
		setResult(RESULT_CODE_OPEN, intent);

		finish();
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.iv_arrow_back:
			finish();
			break;
		case R.id.iv_mm_delete:
			int position = (Integer) view.getTag();
			TabMindMap tmm = (TabMindMap) getListView().getAdapter().getItem(position);
			nodeDao.deleteNodesBy(tmm._id);
			midMapDao.deleteTabMindMap(tmm);
			adapter.removeItem(position);
			break;
		case R.id.iv_mm_menu_add:
			DialogUtils.showInputDialog(this, getString(R.string.create_new_mm), null, new InputListener() {

				@Override
				public void onInputCompleted(String inputStr) {
					if (!TextUtils.isEmpty(inputStr)) {
						Intent intent = new Intent();
						intent.putExtra(EXTRA_NEW_MINDMAP_NAME, inputStr);
						setResult(RESULT_CODE_NEW, intent);
						finish();
					}
				}
			});
			break;
		case R.id.iv_mm_menu_delete:
			if (adapter.getCurrentType() == MindMapAdapter.TYPE_NORMAL) {
				adapter.setCurrentType(MindMapAdapter.TYPE_EDIT);
				ivdelete.setImageResource(R.drawable.mm_menu_finish);
			} else {
				adapter.setCurrentType(MindMapAdapter.TYPE_NORMAL);
				ivdelete.setImageResource(R.drawable.mm_menu_delete);
			}
			break;
		}

	}
}
