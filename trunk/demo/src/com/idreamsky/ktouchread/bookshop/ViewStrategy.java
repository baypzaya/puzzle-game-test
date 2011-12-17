package com.idreamsky.ktouchread.bookshop;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.idreamsky.ktouchread.bookshop.AbstractView;
import com.idreamsky.ktouchread.util.Util;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

 public class ViewStrategy {
	 
	// private Context mContext;
	 
	private static final String TAG = "ViewStrategy";

	private static final int MSG_REMOVE_OUT = 1, MSG_REMOVE_IN = 2;

	private static final int MSG_ADD_OUT = 3, MSG_ADD_IN = 4;

	private int mCurrentTab = -1;

	private boolean mBringInOver = true;

	private boolean mBackOver = true;
	private HashMap<Class<? extends AbstractView>, Integer> mCountMap;

	private HashMap<Integer, ArrayList<AbstractView>> mAbstactViewMap;
	private List<Integer> mTabList;
	
	/**
	 * For forward use.
	 */
	private Animation mRightIn, mLeftOut;

	/**
	 * For back use.
	 */
	private Animation mLeftIn, mRightOut;
	
	private ViewGroup mViewGroupContent;
	
	public ViewStrategy(Context context,ViewGroup viewgroup) {
	//	this.mContext= context;
		mViewGroupContent = viewgroup;
		mAbstactViewMap = new HashMap<Integer, ArrayList<AbstractView>>();
		mAbstactViewMap.put(-1, new ArrayList<AbstractView>());
		mCountMap = new HashMap<Class<? extends AbstractView>, Integer>();
		mTabList = new ArrayList<Integer>();
	}

	public  ViewGroup getContentFrame(){
		return mViewGroupContent;
	}
	
	public final void addTab(int tab) {
		if (tab == -1) {
			throw new IllegalArgumentException(
					"-1 is the default tab index, choose another.");
		}
		final HashMap<Integer, ArrayList<AbstractView>> map = mAbstactViewMap;
		if (map.containsKey(tab)) {
			throw new IllegalArgumentException("tab " + tab
					+ " already exists.");
		}
		map.put(tab, new ArrayList<AbstractView>());
		mTabList.add(tab);
	}

	public final int getTabViewCount(int tab) {
		return mAbstactViewMap.get(tab).size();
	}

	public final AbstractView getView(int tab,
			Class<? extends AbstractView> viewClass) {
		List<AbstractView> list = mAbstactViewMap.get(tab);
		for (AbstractView abstractView : list) {
			if (abstractView.getClass() == viewClass) {
				return abstractView;
			}
		}
		return null;
	}

	public final ArrayList<AbstractView> getAllViews(
			Class<? extends AbstractView> viewClass) {
		synchronized (this) {
			ArrayList<AbstractView> list = new ArrayList<AbstractView>();
			HashMap<Integer, ArrayList<AbstractView>> map = mAbstactViewMap;
			Set<Integer> set = map.keySet();
			for (Integer key : set) {
				ArrayList<AbstractView> viewList = map.get(key);
				int size = viewList.size();
				for (int i = 0; i < size; i++) {
					AbstractView abstractView = viewList.get(i);
					if (abstractView.getClass() == viewClass) {
						list.add(abstractView);
					}
				}
			}
			return list;
		}
	}

	public final void bringBottomToFront(int tab) {
		HashMap<Integer, ArrayList<AbstractView>> map = mAbstactViewMap;
		if (!map.containsKey(tab)) {
			return;
		}
		ArrayList<AbstractView> tabList = map.get(tab);
		int size = tabList.size();
		if (size <= 1) {
			return;
		}
		AbstractView av = tabList.get(0);
		tabList.clear();
		tabList.add(av);

		ViewGroup group = getContentFrame();
		group.removeAllViews();
		group.addView(av.getContentView());
	}

	public final void switchToEmptyTab(final int targetTab,
			final AbstractView targetView) {
		if (!isAnimationOver()) {
			return;
		}
		final int currTab = mCurrentTab;
		if (currTab == targetTab) {
			return;
		}
		final HashMap<Integer, ArrayList<AbstractView>> map = mAbstactViewMap;
		ArrayList<AbstractView> targetViews = map.get(targetTab);
		if (!map.containsKey(targetTab)) {
			throw new IllegalArgumentException("tab " + targetTab
					+ " does not exist.");
		}
		final int targetSize = targetViews.size();
		if (targetSize > 0) {
			throw new IllegalArgumentException("tab " + targetTab
					+ " is not empty");
		}
		if (null == targetView) {
			throw new NullPointerException("view is null.");
		}

		mCurrentTab = targetTab;
		mSwitchOver = false;

		ArrayList<AbstractView> currTabViews = map.get(currTab);
		final int currSize = currTabViews.size();
//		if (Configuration.DEBUG_VERSION) {
//			Log.e(TAG, "current tab views size is " + currSize);
//		}
		final AbstractView currView = currSize > 0 ? currTabViews
				.get(currSize - 1) : null;

		if (targetTab > currTab) {
			mSwitchHandler.post(new Runnable() {

				@Override
				public void run() {
					if (null != currView) {
						Animation outAnimation = checkLeftOutAnimation();
						long duration = outAnimation.getDuration();
						currView.getContentView().startAnimation(outAnimation);
						Message msg = Message.obtain();
						msg.what = MSG_SWITCH_LEFT_OUT;
						msg.obj = currView;
						mSwitchHandler.sendMessageAtTime(msg,
								SystemClock.uptimeMillis() + duration);
					}
					final View tView = targetView.getContentView();
					if (null == tView.getParent()) {
						getContentFrame().addView(tView);
					}

					Animation inAnimation = checkRightInAnimation();
					long duration = inAnimation.getDuration();
					tView.startAnimation(inAnimation);
					Message msg = Message.obtain();
					msg.what = MSG_SWITCH_RIGHT_IN;
					msg.arg1 = targetTab;
					msg.obj = targetView;
					mSwitchHandler.sendMessageAtTime(msg,
							SystemClock.uptimeMillis() + duration);
				}
			});
		} else {
			mSwitchHandler.post(new Runnable() {
				@Override
				public void run() {
					if (null != currView) {
						Animation outAnimation = checkRightOutAnimation();
						long duration = outAnimation.getDuration();
//						currView.getContentView().startAnimation(outAnimation);
						Message msg = Message.obtain();
						msg.what = MSG_SWITCH_RIGHT_OUT;
						msg.obj = currView;
						mSwitchHandler.sendMessageAtTime(msg,
								SystemClock.uptimeMillis() + duration);
					}

					final View tView = targetView.getContentView();
					if (null == tView.getParent()) {
						getContentFrame().addView(tView);
					}
					Animation inAnimation = checkLeftInAnimation();
					long duration = inAnimation.getDuration();
//					tView.startAnimation(inAnimation);
					Message msg = Message.obtain();
					msg.what = MSG_SWITCH_LEFT_IN;
					msg.arg1 = targetTab;
					msg.obj = targetView;
					mSwitchHandler.sendMessageAtTime(msg,
							SystemClock.uptimeMillis() + duration);
				}
			});
		}
	}

	private static final int MSG_SWITCH_LEFT_OUT = 0x10;
	private static final int MSG_SWITCH_RIGHT_IN = 0x11;

	private static final int MSG_SWITCH_RIGHT_OUT = 0x12;
	private static final int MSG_SWITCH_LEFT_IN = 0x13;

	private boolean mSwitchOver = true;

	private Handler mSwitchHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SWITCH_LEFT_OUT:
				AbstractView avLO = (AbstractView) msg.obj;
				View outView = avLO.getContentView();
				ViewGroup parent = (ViewGroup) outView.getParent();
				if (null != parent) {
					parent.removeView(outView);
				}
				if (!hasMessages(MSG_SWITCH_RIGHT_IN)) {
					mSwitchOver = true;
				}
				break;
			case MSG_SWITCH_RIGHT_IN:
				AbstractView avRI = (AbstractView) msg.obj;
				mAbstactViewMap.get(msg.arg1).add(avRI);
				if (!hasMessages(MSG_SWITCH_LEFT_OUT)) {
					mSwitchOver = true;
				}
				break;
			case MSG_SWITCH_RIGHT_OUT:
				AbstractView avRO = (AbstractView) msg.obj;
				View roView = avRO.getContentView();
				ViewGroup roParent = (ViewGroup) roView.getParent();
				if (null != roParent) {
					roParent.removeView(roView);
				}
				if (!hasMessages(MSG_SWITCH_LEFT_IN)) {
					mSwitchOver = true;
				}
				break;
			case MSG_SWITCH_LEFT_IN:
				AbstractView avLI = (AbstractView) msg.obj;
				mAbstactViewMap.get(msg.arg1).add(avLI);
				if (!hasMessages(MSG_SWITCH_RIGHT_OUT)) {
					mSwitchOver = true;
				}
				break;
			default:
				break;
			}
		}

	};

	public final void switchToTab(int targetTab) {
		if (!isAnimationOver()) {
			return;
		}
//		final int currTab = mCurrentTab;
//		if (currTab == targetTab) {
//			return;
//		}
//		final HashMap<Integer, ArrayList<AbstractView>> map = mAbstactViewMap;
//		ArrayList<AbstractView> targetViews = map.get(targetTab);
//		ArrayList<AbstractView> currentViews = map.get(currTab);
//		if (!map.containsKey(targetTab)) {
//			throw new IllegalArgumentException("tab " + targetTab
//					+ " does not exist.");
//		}
//
//		mCurrentTab = targetTab;
//		
//		final int sizeCurr = currentViews.size();
//		AbstractView vCurr = sizeCurr > 0 ? currentViews.get(sizeCurr - 1) : null;
//		if(vCurr != null)
//		{
//			vCurr.ChangeTab(false);
//		}
//		
//
//		final int size = targetViews.size();
//		AbstractView v = size > 0 ? targetViews.get(size - 1) : null;
//
//		final ViewGroup group = getContentFrame();
//		group.removeAllViews();
//		if (null != v) {
//			v.ChangeTab(true);
//			group.addView(v.getContentView());
//		}
	}

	/**
	 * Get whether if the strategy animation is over.
	 */
	public final boolean isAnimationOver() {
		return mBringInOver && mBackOver && mSwitchOver;
	}

	public final int getCurrentTab() {
		return mCurrentTab;
	}

	public final AbstractView getCurrentView() {
		int tab = mCurrentTab;
		ArrayList<AbstractView> list = mAbstactViewMap.get(tab);
		if (null == list) {
			return null;
		}
		int size = list.size();
		if (size <= 0) {
			return null;
		}
		return list.get(size - 1);
	}

	private static final long DURATION = 600;

	private Animation checkRightInAnimation() {
		Animation rightIn = mRightIn;
		if (null == rightIn) {
//			rightIn = Util.loadAnimation(mContext, android.R.anim.slide_out_right);
			rightIn = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
					1.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f,
					Animation.ABSOLUTE, 0.0f);
			rightIn.setDuration(DURATION);
			mRightIn = rightIn;
		}
		return rightIn;
	}

	private Animation checkLeftOutAnimation() {
		Animation leftOut = mLeftOut;
		if (null == leftOut) {
			leftOut = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.0f, Animation.ABSOLUTE,
					0.0f, Animation.ABSOLUTE, 0.0f);
			leftOut.setDuration(DURATION);
			mLeftOut = leftOut;
		}
		return leftOut;
	}

	private Animation checkLeftInAnimation() {
		Animation leftIn = mLeftIn;
		if (null == leftIn) {
//			leftIn = Util.loadAnimation(mContext, android.R.anim.slide_in_left);
			leftIn = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
					-1.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f,
					Animation.ABSOLUTE, 0.0f);
			leftIn.setDuration(DURATION);
			mLeftIn = leftIn;
		}
		return leftIn;
	}

	private Animation checkRightOutAnimation() {
		Animation rightOut = mRightOut;
		if (null == rightOut) {
			rightOut = new TranslateAnimation(Animation.ABSOLUTE, 0.0f,
					Animation.RELATIVE_TO_PARENT, 1.0f, Animation.ABSOLUTE,
					0.0f, Animation.ABSOLUTE, 0.0f);
			rightOut.setDuration(DURATION);
			mRightOut = rightOut;
		}
		return rightOut;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			final int tab = mCurrentTab;
			final int what = msg.what;
			if (MSG_REMOVE_OUT == what) {
				AbstractView abstractView = (AbstractView) msg.obj;
				View outView = abstractView.getContentView();

				ViewGroup parent = (ViewGroup) outView.getParent();
				if (null != parent) {
					parent.removeView(outView);
				}
				final ArrayList<AbstractView> viewList = mAbstactViewMap
						.get(tab);
				viewList.remove(abstractView);
				if (!hasMessages(MSG_REMOVE_IN)) {
					mBackOver = true;
				}
				abstractView.onRemovedFromWindow();
			} else if (MSG_REMOVE_IN == what) {
				if (!hasMessages(MSG_REMOVE_OUT)) {
					mBackOver = true;
				}
			} else if (MSG_ADD_OUT == what) {
				AbstractView abstractView = (AbstractView) msg.obj;
				View outView = abstractView.getContentView();
				ViewGroup parent = (ViewGroup) outView.getParent();
				if (null != parent) {
					parent.removeView(outView);
				}

				if (!hasMessages(MSG_ADD_IN)) {
					mBringInOver = true;
				}
			} else if (MSG_ADD_IN == what) {
				AbstractView abstractView = (AbstractView) msg.obj;
				final ArrayList<AbstractView> viewList = mAbstactViewMap
						.get(tab);
				final int size = viewList.size();
				final Class<? extends AbstractView> klass = abstractView
						.getClass();
				final int count = getMaxCountInQueue(klass);
				if (INFINITE_COUNT == count) {
//					if (Configuration.DEBUG_VERSION) {
//						Log.i(TAG, klass.getSimpleName()
//								+ " instance count has no limit.");
//					}
					viewList.add(abstractView);
				} else {
					int sum = 0;
					for (int i = 0; i < size; i++) {
						AbstractView av = viewList.get(i);
						if (av.getClass() == klass) {
							sum++;
						}
					}
					if (sum <= count - 1) {
						viewList.add(abstractView);
					} else {
						// Find the last AbstractView who's object instance
						// count is limited.
						int i = size - 1;
						for (; i >= 0; i--) {
							AbstractView av = viewList.get(i);
							if (av.getClass() == klass) {
								break;
							}
						}
						// Well, it's found! remove it, and add the new one.
						viewList.remove(i);
						viewList.add(abstractView);
					}
				}

				if (!hasMessages(MSG_ADD_OUT)) {
					mBringInOver = true;
				}
			}
		}
	};

	public void ClearTabView()
	{
		int tab = mCurrentTab;
		final ArrayList<AbstractView> viewList = mAbstactViewMap.get(tab);
		viewList.clear();
		mViewGroupContent.removeAllViews();
	}
	public void ClearAllView()
	{
		for(int i = 0 ; i < mTabList.size() ; i++)
		{
			int Tab = mTabList.get(i);
			if(mAbstactViewMap.containsKey(Tab) && Tab != mCurrentTab)
			{
				final ArrayList<AbstractView> viewList = mAbstactViewMap.get(Tab);
				if(viewList != null)
				{
					for(int j = 0 ; j < viewList.size() ; j ++ )
					{
						AbstractView view = viewList.get(j);
						view.onRemovedFromWindow();
					}
					viewList.clear();
				}
				mAbstactViewMap.remove(Tab);
			}
		}
	//	mViewGroupContent.removeAllViews();
		
	}
	/**
	 * Return true to prevent system handling KEY_BACK event, such as destroy
	 * the current foreground activity.
	 */
	public final boolean backToPreviousView() {
		if (!isAnimationOver()) {
			return true;
		}
		mBackOver = false;
		int tab = mCurrentTab;
		final ArrayList<AbstractView> viewList = mAbstactViewMap.get(tab);
		int size = viewList.size();
		if (size <= 1) {
			return false;
		}

		final AbstractView outView = viewList.get(size - 1);
		final AbstractView inView = viewList.get(size - 2);

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (null != outView) {
					Animation leftOut = checkLeftOutAnimation();
					long duration = leftOut.getDuration();
					outView.getContentView().startAnimation(
							checkRightOutAnimation());
					Message outMessage = Message.obtain();
					outMessage.what = MSG_REMOVE_OUT;
					outMessage.obj = outView;

					mHandler.sendMessageAtTime(outMessage,
							SystemClock.uptimeMillis() + duration);
				}
				if (null != inView) {
					if (null == inView.getContentView().getParent()) {
						getContentFrame().addView(inView.getContentView());
						inView.onBackToView();
					}
					Animation animation = checkRightInAnimation();
					long duration = animation.getDuration();

					Message inMessage = Message.obtain();
					inMessage.what = MSG_REMOVE_IN;
					inMessage.obj = inView;
					inView.getContentView().startAnimation(
							checkLeftInAnimation());
					mHandler.sendMessageAtTime(inMessage,
							SystemClock.uptimeMillis() + duration);
				}
			}
		});

		return true;
	}

	public final void bringViewIn(AbstractView target) {
		if (!isAnimationOver()) {
			return;
		}
		
		int tab = mCurrentTab;
		AbstractView currentView = null;
		final ArrayList<AbstractView> viewList = mAbstactViewMap.get(tab);
		int size = viewList.size();
		if (size > 0) {
			currentView = viewList.get(size - 1);
		}
		final AbstractView outView = currentView;
		final AbstractView inView = target;
		if(currentView == target)
		{
			return;
		}
		mBringInOver = false;
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (null != outView) {
					Animation leftOut = checkLeftOutAnimation();
					long duration = leftOut.getDuration();
					outView.getContentView().startAnimation(
							checkLeftOutAnimation());
					Message outMessage = Message.obtain();
					outMessage.what = MSG_ADD_OUT;
					outMessage.obj = outView;

					mHandler.sendMessageAtTime(outMessage,
							SystemClock.uptimeMillis() + duration);
				}
				if (null != inView) {
					if (null == inView.getContentView().getParent()) { 
						getContentFrame().addView(inView.getContentView());
					}
//					Animation animation = checkRightInAnimation();
//					long duration = animation.getDuration();

					Message inMessage = Message.obtain();
					inMessage.what = MSG_ADD_IN;
					inMessage.obj = inView;
					if(Util.isFirstOpenBookShop){
						inView.getContentView().startAnimation(checkRightInAnimation());
					}else{
						Util.isFirstOpenBookShop=true;
					}
					mHandler.sendMessageAtTime(inMessage,SystemClock.uptimeMillis() + 1000);
				}
			}
		});
	}

	/**
	 * A constant indicates that this view can has infinite instances in the
	 * queue. This is the default count.
	 * 
	 * @see {@link #setMaxCountInQueue(int)}.
	 */
	public static final int INFINITE_COUNT = -1;

	public final void setMaxCountInQueue(Class<? extends AbstractView> klass,
			int max) {
		if (max <= 0) {
			throw new IllegalArgumentException("max instance count " + max
					+ " is invalid");
		}
		mCountMap.put(klass, max);
	}

	public final int getMaxCountInQueue(Class<? extends AbstractView> klass) {
		final HashMap<Class<? extends AbstractView>, Integer> map = mCountMap;
		if (!map.containsKey(klass)) {
			return INFINITE_COUNT;
		}
		return mCountMap.get(klass);
	}
}

