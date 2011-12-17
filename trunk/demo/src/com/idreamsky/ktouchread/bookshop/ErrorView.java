package com.idreamsky.ktouchread.bookshop;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.idreamsky.ktouchread.bookshelf.R;
public class ErrorView extends AbstractView{
	private Button btnTry;
	private Button btnCancel;
	private String Msg;
	private TextView mTextView;
	
	public ErrorView(Activity context,String msg) {
		
		super(context);
		Msg = msg;
	}
	
	@Override
	public void enrichContent(ViewGroup parent) {
		final RelativeLayout layout = (RelativeLayout) parent;
		
		LinearLayout layoutNetWork = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.book_shop_error,null);
		
		layout.addView(layoutNetWork);
		
		btnTry=(Button)layoutNetWork.findViewById(R.id.book_ol_network_try);
		btnCancel=(Button)layoutNetWork.findViewById(R.id.book_ol_network_cancel);
		mTextView = (TextView)layoutNetWork.findViewById(R.id.book_shop_network_text);
		
		mTextView.setText(Msg);
	}
	
	
	
	@Override
	public void onFinishInit() {
		btnTry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(
//						android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//				mContext.startActivity(intent);
				((BookShopActivity) mContext).mHandler.sendEmptyMessage(13);
			}
		});
	     
		btnCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					((BookShopActivity) mContext).Back();
				}
			});
	}

	@Override
	public void ReLoadData() {
		// TODO Auto-generated method stub
		
	}
	
}
