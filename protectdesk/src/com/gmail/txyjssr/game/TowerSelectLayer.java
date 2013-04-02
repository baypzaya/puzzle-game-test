package com.gmail.txyjssr.game;

import java.util.List;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.OnMoneyChangedListener;
import com.gmail.txyjssr.game.data.OnSelectedTowerListener;
import com.gmail.txyjssr.game.data.Tower;
import com.wiyun.engine.nodes.Menu;
import com.wiyun.engine.nodes.MenuItemSprite;
import com.wiyun.engine.nodes.Node;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.types.WYPoint;
import com.wiyun.engine.utils.TargetSelector;

public class TowerSelectLayer extends Menu{
	GameData mGameData = GameData.getInstance();
	WYPoint currentPoint;
	private float itemWidth;
	private float itemHeight;
	private float contentWidth;
	private OnSelectedTowerListener listener;

	public TowerSelectLayer(OnSelectedTowerListener listener) {
		this.listener = listener;
		Sprite titem = Sprite.make(R.drawable.tower1);
		itemWidth = titem.getWidth();
		itemHeight = titem.getHeight();		
	}

	public void showSeleteTower(WYPoint point) {
		removeAllChildren(true);
		int money = mGameData.getMoney();
		int[] towerTypes = mGameData.getCurrentUseableTower();

		for (int type : towerTypes) {
			MenuItemSprite item = null;
			TargetSelector ts = new TargetSelector(this, "selectedTower(int)", new Object[] { type });
			switch (type) {
			case 1:
				item = (MenuItemSprite.make(R.drawable.tower1, 0, ts));
				break;
			case 2:
				item = (MenuItemSprite.make(R.drawable.tower2, 0, ts));
				break;
			case 3:
				item = (MenuItemSprite.make(R.drawable.tower3, 0, ts));
				break;
			case 4:
				item = (MenuItemSprite.make(R.drawable.tower4, 0, ts));
				break;
			}
			
			int spend = Tower.getTowerSpend(type);
			item.setEnabled(money>=spend);
			item.setClickScale(1.5f);
			item.setTag(type);
			addChild(item);
		}
		alignItemsHorizontally();
		contentWidth = getChildCount() * itemWidth;
		setContentSize(contentWidth, itemHeight);
		
		WYPoint tpoint = mGameData.getDefenseLocation(point);
		setPosition(tpoint);
	}
	
	
	
	public void selectedTower(int type) {
		listener.onSelectedTower(type);
	}
	
	public void updateItemStatus(int money) {
		if(isVisible()){
			List<Node> children = getChildren();
			for(Node child:children){
				int type = child.getTag();
				int spend = Tower.getTowerSpend(type);
				child.setEnabled(money>=spend);
			}
		}
		
	}
}
