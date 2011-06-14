package com.yujsh.android.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class GridViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.grid_view_layout);
		GridView gv = (GridView) findViewById(R.id.grid_view);
		
		List<HashMap<String,Object>> bitmapList=new ArrayList<HashMap<String,Object>>();
		 
//		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
		for(int i =0;i<40;i++){	
			HashMap<String,Object> testMap = new HashMap<String,Object>();
			testMap.put("1", R.drawable.icon);
			bitmapList.add(testMap);
		}
		
		String[] from = new String[]{"1"};
		int[] to = {R.id.image_view};
		SimpleAdapter adpater = new SimpleAdapter(this,bitmapList,R.layout.image_view_layout,from,to);
		
		gv.setAdapter(adpater);
		
		gv.setOnItemClickListener(new ItemClickListener());
		
	}
	
	class ItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position , long rowId) {
			Intent intent =new Intent(GridViewActivity.this,TestActivity.class);
			intent.putExtra("test_message", ""+position);
			GridViewActivity.this.startActivity(intent);
			
		}
	}

	
}
