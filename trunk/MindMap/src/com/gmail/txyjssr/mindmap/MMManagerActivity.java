package com.gmail.txyjssr.mindmap;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MMManagerActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mind_map_manager_activity);
		
		MindMapDao dao = new MindMapDao();
		List<TabMindMap> tmmList = dao.getAllMindMap();
		
		MindMapAdapter adapter = new MindMapAdapter(this,tmmList);
		getListView().setAdapter(adapter);
	}

	class MindMapAdapter extends BaseAdapter {
		private Context context;
		private List<TabMindMap> tmmList;
		
		public MindMapAdapter(Context context,List<TabMindMap> tmmList){
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
			if(convertView==null){
				convertView = LayoutInflater.from(context).inflate(R.layout.mind_map_adapter, null);
			}
			TabMindMap tmm = tmmList.get(position);
			TextView tvName = (TextView)convertView.findViewById(R.id.tv_mm_name);
			tvName.setText(tmm.name);
			
			return convertView;
		}

	}
}
