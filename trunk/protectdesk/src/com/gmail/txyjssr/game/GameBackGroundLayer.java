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
	static int TILE_WIDTH = 32;
	static int TILE_HEIGHT = 32;
	
	float m_tile_width;
	float m_tile_height;
	int m_tile_x;
	int m_tile_y;

	WYRect m_rect_free;
	WYRect m_rect_block;
	WYRect m_rect_r1;
	
	ArrayList<SpriteEx> m_bgs;
	
	

	public GameBackGroundLayer() {

		m_tile_width = ResolutionIndependent.resolveDp(TILE_WIDTH);
		m_tile_height = ResolutionIndependent.resolveDp(TILE_HEIGHT);

		ZwoptexManager.addZwoptex("astar", R.raw.astar);

		Texture2D tex = Texture2D.makePNG(R.drawable.astar);
		SpriteBatchNode batchNode = SpriteBatchNode.make(tex);
		addChild(batchNode);

		m_tile_x = (int) (Director.getInstance().getWindowSize().width / m_tile_width);
		m_tile_y = (int) (Director.getInstance().getWindowSize().height / m_tile_height);

		m_rect_free = WYRect.make(0, m_tile_height, m_tile_height, m_tile_height);
		m_rect_block = WYRect.make(m_tile_height, 0, m_tile_height, m_tile_height);
		m_rect_r1 = WYRect.make(0, 0, m_tile_height, m_tile_height);
		
		m_bgs = new ArrayList<SpriteEx>(m_tile_x * m_tile_y);
		for (int x = 0; x < m_tile_x; x++) {
			for (int y = 0; y < m_tile_y; y++) {
				SpriteEx sprite = ZwoptexManager.makeSpriteEx("astar", "tile_free.png", tex);
				sprite.setPosition(x * m_tile_width + m_tile_width / 2, y
						* m_tile_height + m_tile_height / 2);
				batchNode.addChild(sprite);
				m_bgs.add(sprite);
			}
		}

	}
}
