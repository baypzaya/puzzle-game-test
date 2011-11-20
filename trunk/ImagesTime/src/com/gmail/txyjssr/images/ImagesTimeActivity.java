package com.gmail.txyjssr.images;

import android.app.ListActivity;
import android.os.Bundle;

public class ImagesTimeActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ImageGroupAdpater adpater = new ImageGroupAdpater(this);
		getListView().setAdapter(adpater);
	}
}