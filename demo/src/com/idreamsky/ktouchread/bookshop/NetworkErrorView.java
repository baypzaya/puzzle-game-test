package com.idreamsky.ktouchread.bookshop;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.util.NetUtil;


public class NetworkErrorView extends AbstractView{
	
	private Button btnSetting;
	private Button btnAgin;
	
	public NetworkErrorView(Activity context) {
		super(context);
	}
	
	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;
		
		LinearLayout layoutNetWork = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.book_shop_network_setting,null);
		
		layout.addView(layoutNetWork);
		
		btnSetting=(Button)layoutNetWork.findViewById(R.id.book_ol_network_setting);
		btnAgin=(Button)layoutNetWork.findViewById(R.id.book_ol_network_agin);
	}
	
	
	
	@Override
	public void onFinishInit() {
	     btnSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				mContext.startActivity(intent);
			}
		});
	     
	     btnAgin.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(NetUtil.checkNetwork(mContext)){
//						((BookShopActivity) mContext).SwitchView();
					}else{
						Toast.makeText(mContext,R.string.network_error,2000).show();
					}
				}
			});
	}

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		
	}
	
}
