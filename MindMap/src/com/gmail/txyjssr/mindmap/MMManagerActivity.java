package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MMManagerActivity extends ListActivity implements OnItemClickListener {
	public static final int RESULT_CODE_NEW = 1001;
	public static final int RESULT_CODE_OPEN = 1002;
	
	public static final String EXTRA_OPEN_MINDMAP_ID = "EXTRA_OPEN_MINDMAP_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_manager_activity);

		MindMapDao dao = new MindMapDao();
		List<TabMindMap> tmmList = dao.getAllMindMap();

		MindMapAdapter adapter = new MindMapAdapter(this, tmmList);
		getListView().setAdapter(adapter);
		
		getListView().setOnItemClickListener(this);
	}

	class MindMapAdapter extends BaseAdapter {
		private Context context;
		private List<TabMindMap> tmmList;

		public MindMapAdapter(Context context, List<TabMindMap> tmmList) {
			this.tmmList = tmmList;
			this.context = context;
		}

		@Override
		public int getCount() {
			return tmmList.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return null;
			}
			return tmmList.get(position - 1);
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
			if (position == 0) {
				tvName.setText("NEW MindMap");
			} else {
				TabMindMap tmm = tmmList.get(position-1);
				tvName.setText(tmm.name);
			}

			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long index) {
		if(position == 0){
			setResult(RESULT_CODE_NEW);
		}else{
			TabMindMap tmm = (TabMindMap) getListView().getAdapter().getItem(position);
			Intent intent = new Intent(this,MindMapActivity.class);
			intent.putExtra(EXTRA_OPEN_MINDMAP_ID, tmm._id);
			setResult(RESULT_CODE_OPEN,intent);
		}
		
		finish();
	}
}
