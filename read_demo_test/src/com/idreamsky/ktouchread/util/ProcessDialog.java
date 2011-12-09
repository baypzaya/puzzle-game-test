package com.idreamsky.ktouchread.util;

import com.idreamsky.ktouchread.bookshelf.R;

import android.app.Dialog;
import android.content.Context;

public class ProcessDialog {
	public static Dialog mProcessDialog = null;
	
	
	
	static public void ShowProcess(Context context) {
		try {
			if(mProcessDialog == null)
			{
				mProcessDialog = new Dialog(context, R.style.transparentdialog);
				mProcessDialog.setContentView(R.layout.process);
				mProcessDialog.setCancelable(true);
				mProcessDialog.show();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	static public void ShowProcess(Context context,boolean bCancel) {
		try {
			if(mProcessDialog == null)
			{
				mProcessDialog = new Dialog(context, R.style.transparentdialog);
				mProcessDialog.setContentView(R.layout.process);
				mProcessDialog.setCancelable(bCancel);
				mProcessDialog.show();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	static public void Dismiss() {
		try {
			if (mProcessDialog != null && mProcessDialog.isShowing()) {
				mProcessDialog.dismiss();
				mProcessDialog = null;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
