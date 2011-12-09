package com.idreamsky.ktouchread.bookshop;

import com.idreamsky.ktouchread.util.Configuration;
import com.idreamsky.ktouchread.util.ProcessDialog;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;



public abstract class AbstractView {
	private ViewGroup mParent;
	private boolean mIsEnriched = false;
	public Activity mContext;
	public AbstractView(Activity context) {
		mContext = context;

//
//
//		mProcessDialog = new Dialog(mContext, R.style.transparentdialog);
//		mProcessDialog.setContentView(R.layout.process);
//		mProcessDialog.setCancelable(true);
		

	
		final ViewGroup parent = returnParent(context);
		if (null == parent) {
			throw new NullPointerException(
					"getParent() must return a non-null ViewGroup.");
		}
		parent.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		mParent = parent;
		

	}

	public abstract void enrichContent(ViewGroup parent);
	public abstract void ReLoadData();
	
	protected ViewGroup returnParent(Context context) {
		return new RelativeLayout(context);
	}

	public final View getContentView() {
		return mParent;
	}
	
	public final void initializeIfNecessary() {
		if (mIsEnriched) {
			return;
		}
		enrichContent(mParent);
		onFinishInit();
//		new Thread(){
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				onFinishInit();
//			}
//		}.start();
		
		mIsEnriched = true;
	}

	/**
	 * Bring front this view to the activity's window.
	 */
	public final void bringSelfToFront() {
//		initializeIfNecessary();
//		((BookShopActivity)mContext).getViewStrategy().bringViewIn(this);
	}
	/**
	 * Called after you have enriched the view tree, this is the best place
	 * where you may send normal http request.
	 */
	protected void onFinishInit() {
	}
	/**
	 * @param bIn false表示out，true表示in
	 */
	
	protected void UpdataUI()
	{
		
	}
	protected void ChangeTab(boolean bIn)
	{
		
	}
	protected boolean ProcessBack()
	{
		return false;
	}
	/**
	 * Called when this view is detached from the current window.
	 */
	protected void onRemovedFromWindow() {
	}
	protected void onBackToView()
	{
		
	}
	public static final int FILL_PARENT = ViewGroup.LayoutParams.FILL_PARENT,
	WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	public static final TextView generateTextView(Context activity, int id,
			String text, float textSize, int gravity, int resBackground,
			View.OnClickListener listener) {
		TextView tv = new TextView(activity);
		if (id > 0) {
			tv.setId(id);
		}
		if (null != text && text.length() > 0) {
			tv.setText(text);
		}
		if (textSize > 0.0f) {
			tv.setTextSize(textSize);
		}
		if (gravity > 0) {
			tv.setGravity(gravity);
		}
		if (resBackground > 0) {
			tv.setBackgroundResource(resBackground);
		}
		if (null != listener) {
			tv.setOnClickListener(listener);
		}
		tv.setTextColor(0xFF404040);
		return tv;
	}
	
	protected RelativeLayout getListHeaderView() {
		final float density = Configuration.getDensity(mContext);

		RelativeLayout loadingLayout = new RelativeLayout(mContext);

		loadingLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		ProgressBar pb = new ProgressBar(mContext);
		pb.setId(309);
		RelativeLayout.LayoutParams pbParams = new RelativeLayout.LayoutParams(
				(int) (30 * density), (int) (30 * density));
		// pbParams.leftMargin = (int) (20 * density);
		pbParams.addRule(RelativeLayout.CENTER_VERTICAL);
		loadingLayout.addView(pb, pbParams);

		TextView tvMessage = new TextView(mContext);
		tvMessage.setText("");
		tvMessage.setTextSize(16.0f);
		tvMessage.setTextColor(Color.BLACK);
		RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		tvParams.leftMargin = (int) (5 * density);
		tvParams.addRule(RelativeLayout.CENTER_VERTICAL);
		tvParams.addRule(RelativeLayout.RIGHT_OF, 309);
		loadingLayout.addView(tvMessage, tvParams);

		return loadingLayout;
	}
	
//	public void ShowProcee()
//	{
//		((BookShopActivity) mContext).ShowProcess();
//	}
//	public void DismissProcess()
//	{
//		((BookShopActivity) mContext).DismissProcess();
//	}
	//public static boolean dialogFlag = false;
	public void ShowProcee()
	{
//		mProcessDialog = new Dialog(mContext, R.style.transparentdialog);
//		mProcessDialog.setContentView(R.layout.process);
//		mProcessDialog.setCancelable(true);
//		mProcessDialog.show();
		
		ProcessDialog.ShowProcess(mContext);
		return ; 
	}
	public void DismissProcess()
	{
		ProcessDialog.Dismiss();

//		mHandler.post(new Runnable() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub		
//			mProcessDialog.dismiss();
//			mProcessDialog = null;
//		}
//	});
	}
	
	
}

