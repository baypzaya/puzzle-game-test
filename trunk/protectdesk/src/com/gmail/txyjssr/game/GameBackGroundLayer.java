package com.gmail.txyjssr.game;

import java.util.ArrayList;

import android.util.Log;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.GameData;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Label;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.SpriteEx;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.ResolutionIndependent;

public class GameBackGroundLayer extends Layer {
	
	static int TILE_WIDTH = 32;
	static int TILE_HEIGHT = 32;

	WYRect m_rect_free;
	WYRect m_rect_block;
	WYRect m_rect_path;

	ArrayList<SpriteEx> m_bgs;
	Texture2D texBackgroud;
	
	private Label moneyHintLabel;
	private Label moneyNumberLabel;
	
	private Label enemyHintLabel;
	private Label enemyNumberLabel;
	
	public GameBackGroundLayer(float tileWidth, float tileHeight, int xCount, int yCount) {
		float texTileWidth = ResolutionIndependent.resolveDp(TILE_WIDTH);
		float texTileHeight = ResolutionIndependent.resolveDp(TILE_HEIGHT);

		m_rect_free = WYRect.make(0, texTileHeight, texTileWidth,texTileHeight);
		m_rect_block = WYRect.make(texTileWidth, 0, texTileWidth,texTileHeight);
		m_rect_path = WYRect.make(0, 0, texTileWidth,texTileHeight);
		float scale = tileWidth/texTileWidth;
		texBackgroud = Texture2D.makePNG(R.drawable.astar);

		int[][] map = GameData.getInstance().getCurrentMap();
		m_bgs = new ArrayList<SpriteEx>(xCount * yCount);
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				SpriteEx sprite = null;
				if(map[y][x] == 0){
					sprite = SpriteEx.make(texBackgroud, m_rect_path);
				}else if(map[y][x]==1){
					sprite = SpriteEx.make(texBackgroud, m_rect_free);
				}else if(map[y][x]==2){
					sprite = SpriteEx.make(texBackgroud, m_rect_block);
				}
				sprite.setPosition(x * tileWidth + tileWidth / 2, y * tileHeight + tileHeight / 2);
				sprite.scale(scale);
				addChild(sprite);
				m_bgs.add(sprite);
			}
		}
		
		WYSize wySize = Director.getInstance().getWindowSize();
		moneyHintLabel = Label.make("money:", 20);
		moneyHintLabel.setAnchor(0,1);
		moneyHintLabel.setPosition(20, wySize.height -10);
		addChild(moneyHintLabel);
		
		moneyNumberLabel = Label.make("0", 20);
		moneyNumberLabel.setAnchor(0,1);
		moneyNumberLabel.setPosition(20+moneyHintLabel.getWidth(), wySize.height -10);
		addChild(moneyNumberLabel);
		
		enemyNumberLabel = Label.make("0   ", 20);
		enemyNumberLabel.setAnchor(1,1);
		enemyNumberLabel.setPosition(wySize.width - 20, wySize.height -10);
		addChild(enemyNumberLabel);
		
		enemyHintLabel = Label.make("ememies:", 20);
		enemyHintLabel.setAnchor(1,1);
		enemyHintLabel.setPosition(wySize.width - 20-enemyNumberLabel.getWidth(), wySize.height -10);
		addChild(enemyHintLabel);		
	}
	
	public void updateMoney(int money){
		Log.i("yujsh log","updateMoney:"+money);
		moneyNumberLabel.setText(""+money);
	}
	
	public void updateEnemy(int enemy){
		enemyNumberLabel.setText(""+enemy);
	}
}
