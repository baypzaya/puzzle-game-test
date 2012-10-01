package com.gmail.txyjssr.game;

import com.gmail.txyjssr.R;
import com.wiyun.engine.actions.Action;
import com.wiyun.engine.actions.Animate;
import com.wiyun.engine.actions.IntervalAction;
import com.wiyun.engine.actions.RepeatForever;
import com.wiyun.engine.nodes.Animation;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.ResolutionIndependent;

public class EnemiesLayer extends Layer {

	float ITEM_WIDTH;
	float ITEM_HEIGHT;

	Sprite m_moveSprite;
	float m_findStartx;
	float m_findStarty;
	Animation mAnimDown;
	Sprite mPlayer;

	public EnemiesLayer(float tileWidth, float tileHeight) {

		ITEM_WIDTH = ResolutionIndependent.resolveDp(24);
		ITEM_HEIGHT = ResolutionIndependent.resolveDp(32);

		// add player
		WYSize s = Director.getInstance().getWindowSize();
		Texture2D tex = Texture2D.makePNG(R.drawable.player);
		mPlayer = Sprite.make(tex, WYRect.make(0, ITEM_HEIGHT * 2, ITEM_WIDTH, ITEM_HEIGHT));
		mPlayer.setPosition(s.width / 2, s.height / 2);
		addChild(mPlayer);

		mAnimDown = new Animation(1);
		mAnimDown.addFrame(0.3f, frameAt(0, 2), frameAt(2, 2));

		IntervalAction anim = (IntervalAction) Animate.make(mAnimDown, true).autoRelease();
		mPlayer.runAction((Action) RepeatForever.make(anim).autoRelease());

	}

	private WYRect frameAt(int x, int y) {
		return WYRect.make(x * ITEM_WIDTH, y * ITEM_HEIGHT, ITEM_WIDTH, ITEM_HEIGHT);
	}

}
