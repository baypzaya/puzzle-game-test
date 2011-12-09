package com.idreamsky.ktouchread.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.idreamsky.ktouchread.bookshelf.AddFile;
import com.idreamsky.ktouchread.bookshelf.BookShelf;
import com.idreamsky.ktouchread.bookshelf.Book_System_SettingAct;
import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.pay.KPayAccount;

public class Menu {
	private Activity context;
	private LinearLayout menu_account;
	private LinearLayout phoneBook;
	private LinearLayout menu_setting;
	private PopupWindow pw;
	private int showParentId;

	public Menu(Activity context, int showParentId) {
		this.context = context;
		this.showParentId = showParentId;
	}

	public void init() {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.system_menu, null);
		menu_account = (LinearLayout) view.findViewById(R.id.menu_account);
		phoneBook = (LinearLayout) view.findViewById(R.id.phoneBook);
		menu_setting = (LinearLayout) view.findViewById(R.id.menu_setting);
		pw = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		ColorDrawable dw = new ColorDrawable(-00000);
		pw.setBackgroundDrawable(dw);
		pw.setAnimationStyle(R.style.PopupMenuAnimation);
		pw.showAtLocation(context.findViewById(showParentId), Gravity.BOTTOM,
				0, 0);
		pw.update();

		menu_account.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = KPayAccount.GetUserIntent();
				context.startActivityForResult(intent,
						KPayAccount.REQUESTCODE_FLAG_FOR_GETTOKEN);
			}
		});
		phoneBook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent();
//				Bundle bundle = new Bundle(); 
//				bundle.putSerializable(BookShelf.READBOOK, book);
//				intent.putExtras(bundle);
				intent.setClass(context,  AddFile.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				context.startActivityForResult(intent, BookShelf.REFRESHCODE);
			}
		});
		menu_setting.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
//				Util.toActivity(context, Book_System_SettingAct.class);
				
				Intent intent = new Intent();
				intent.setClass(context, Book_System_SettingAct.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				context.startActivityForResult(intent, BookShelf.REFRESHCODE);
				
				
			}
		});
	}

	public void dismiss() {
		if (pw.isShowing())
			pw.dismiss();
	}
}
