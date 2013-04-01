package com.gmail.txyjssr.game;

import android.util.Log;
import android.view.MotionEvent;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.OnSelectedTowerListener;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Menu;
import com.wiyun.engine.nodes.MenuItemSprite;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.utils.TargetSelector;

public class TowerSelectLayer extends Menu {
	GameData mGameData = GameData.getInstance();
	WYPoint currentPoint;
	private float itemWidth;
	private float itemHeight;
	private float contentWidth;
	private OnSelectedTowerListener listener;

	public TowerSelectLayer(OnSelectedTowerListener listener) {
		this.listener = listener;
		Sprite titem = Sprite.make(R.drawable.tower);
		itemWidth = titem.getWidth() * 0.3f;
		itemHeight = titem.getHeight() * 0.3f;

		int[] towerTypes = mGameData.getCurrentUseableTower();

		for (int type : towerTypes) {
			switch (type) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			}
			TargetSelector ts = new TargetSelector(this, "selectedTower(int)", new Object[] { type });
			MenuItemSprite item = (MenuItemSprite.make(R.drawable.tower, 0, ts));
			item.setScale(0.3f);
			item.setClickScale(0.5f);
			addChild(item);

		}
		alignItemsHorizontally();
		contentWidth = getChildCount() * itemWidth;
		setContentSize(contentWidth, itemHeight);
	}

	public void showSeleteTower(WYPoint point) {		
		WYPoint tpoint = mGameData.getDefenseLocation(point);
		setPosition(tpoint);
	}
	
	public void selectedTower(int type) {
		Log.i("yujsh log", "selectType:" + type);
		listener.onSelectedTower(type);
	}
}
