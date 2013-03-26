package com.gmail.txyjssr.mindmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogUtils {

	public static void showInputDialog(Context context, String title, String inputStr,
			final InputListener inputListener) {

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		final View view = layoutInflater.inflate(R.layout.dialog_input, null);

		OnClickListener listener = new OnClickListener() {
			private String lInputStr = "";

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					lInputStr = ((EditText) view.findViewById(R.id.et_input)).getEditableText().toString().trim();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
				inputListener.onInputCompleted(lInputStr);
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setView(view);
		builder.setCancelable(false);
		if (!TextUtils.isEmpty(inputStr)) {
			((EditText) view.findViewById(R.id.et_input)).setText(inputStr);
		}
		builder.setPositiveButton(R.string.entry, listener);
		builder.setNegativeButton(R.string.cancel, listener);

		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	public static void showHintDilog(Context context, String message, String postiveButtonName,
			String negativeButtonName, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.dialog_title_hint);
		builder.setMessage(message);
		builder.setCancelable(false);
		if (!TextUtils.isEmpty(postiveButtonName)) {
			builder.setPositiveButton(postiveButtonName, listener);
		}
		if (!TextUtils.isEmpty(negativeButtonName)) {
			builder.setNegativeButton(negativeButtonName, listener);
		}
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}
