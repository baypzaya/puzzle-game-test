package com.idreamsky.ktouchread.bookmarkfactory;

import java.util.List;

import android.content.Context;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.idreamsky.ktouchread.bookshelf.R;
import com.idreamsky.ktouchread.data.net.BookMarkFactory;
import com.idreamsky.ktouchread.util.NetUtil;

public class BookMarkFactoryThem extends SimpleOnGestureListener {
	private static final int FLING_MIN_DISTANCE = 150;
	private static final int FLING_MIN_VELOCITY = 250;
	private Context mContext;
	private ViewFlipper bookmark_flipper;
	private List<BookMarkFactory.BookMarkPIC> BookMarkPICList = null;

	public BookMarkFactoryThem(Context mContext, ViewFlipper bookmark_flipper,
			List<BookMarkFactory.BookMarkPIC> BookMarkPICList) {
		this.mContext = mContext;
		this.bookmark_flipper = bookmark_flipper;
		this.BookMarkPICList = BookMarkPICList;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		
		
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // 往左滑动
			if (NetUtil.checkNetwork(mContext) && BookMarkPICList != null && BookMarkPICList.size()>4) {
				this.bookmark_flipper.setInAnimation(AnimationUtils
						.loadAnimation(mContext, R.anim.left_in));
				this.bookmark_flipper.setOutAnimation(AnimationUtils
						.loadAnimation(mContext, R.anim.left_out));
				bookmark_flipper.showNext();
				return true;
			} else {
				Toast.makeText(mContext,
						mContext.getString(R.string.page_first),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // 往右滑动
			if (NetUtil.checkNetwork(mContext) && BookMarkPICList != null && BookMarkPICList.size()>4) {
				this.bookmark_flipper.setInAnimation(AnimationUtils
						.loadAnimation(mContext, R.anim.right_in));
				this.bookmark_flipper.setOutAnimation(AnimationUtils
						.loadAnimation(mContext, R.anim.right_out));
				bookmark_flipper.showPrevious();

				return true;
			} else {
				Toast.makeText(mContext,
						mContext.getString(R.string.page_last),
						Toast.LENGTH_SHORT).show();
				return true;
			}

		}
		return true;

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return super.onSingleTapUp(e);
	}

	
	
	
	
	
}
