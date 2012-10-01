package com.gmail.txyjssr.game;

import java.util.ArrayList;

import com.gmail.txyjssr.R;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.SpriteBatchNode;
import com.wiyun.engine.nodes.SpriteEx;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.ZwoptexManager;

public class GameBackGroundLayer extends Layer {

	WYRect m_rect_free;
	WYRect m_rect_block;

	ArrayList<SpriteEx> m_bgs;

	public GameBackGroundLayer(float tileWidth, float tileHeight, int xCount, int yCount) {

		

		
//		SpriteBatchNode batchNode = SpriteBatchNode.make();
//		addChild(batchNode);

		m_rect_free = WYRect.make(0, tileHeight, tileHeight, tileHeight);
		m_rect_block = WYRect.make(tileHeight, 0, tileHeight, tileHeight);

		m_bgs = new ArrayList<SpriteEx>(xCount * yCount);
		for (int x = 0; x < xCount; x++) {
			for (int y = 0; y < yCount; y++) {
				SpriteEx sprite = ZwoptexManager.makeSpriteEx("tile_free.png");
				sprite.setPosition(x * tileWidth + tileWidth / 2, y * tileHeight + tileHeight / 2);
				addChild(sprite);
				m_bgs.add(sprite);
			}
		}

	}
}
