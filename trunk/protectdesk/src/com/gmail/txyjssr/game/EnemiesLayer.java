package com.gmail.txyjssr.game;

import java.util.List;
import java.util.Random;

import android.util.Log;

import com.gmail.txyjssr.R;
import com.wiyun.engine.actions.Action;
import com.wiyun.engine.actions.Action.Callback;
import com.wiyun.engine.actions.Animate;
import com.wiyun.engine.actions.IntervalAction;
import com.wiyun.engine.actions.MoveTo;
import com.wiyun.engine.actions.RepeatForever;
import com.wiyun.engine.nodes.Animation;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Node;
import com.wiyun.engine.nodes.Sprite;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.TargetSelector;

public class EnemiesLayer extends Layer implements Callback{

	float ITEM_WIDTH;
	float ITEM_HEIGHT;
	
	Animation mAnimDown;
	IntervalAction anim;
	
	Texture2D texEnemy;
	WYSize s;

	public EnemiesLayer(float tileWidth, float tileHeight) {

		ITEM_WIDTH = ResolutionIndependent.resolveDp(24);
		ITEM_HEIGHT = ResolutionIndependent.resolveDp(32);

		// add player
		s = Director.getInstance().getWindowSize();
		texEnemy = Texture2D.makePNG(R.drawable.player);

		mAnimDown = new Animation(1);
		mAnimDown.addFrame(0.3f, frameAt(0, 2), frameAt(2, 2));
		schedule(new TargetSelector(this, "addEnemies", new Object[] {}),2);
	}
	
	public Sprite createEnemy(){
		Random random = new Random();
		Integer x = random.nextInt(480);
		Sprite enemy = Sprite.make(texEnemy, WYRect.make(0, ITEM_HEIGHT * 2, ITEM_WIDTH, ITEM_HEIGHT));
		enemy.setPosition(Math.abs(x), s.height);
		return enemy;
	}
	
	public void addEnemies(){
		Sprite enemy = createEnemy();
		addChild(enemy);
		
		anim = (IntervalAction) Animate.make(mAnimDown, true).autoRelease();
		enemy.runAction((Action) RepeatForever.make(anim).autoRelease());

		IntervalAction moveTo =(IntervalAction) MoveTo.make(10, enemy.getPositionX(), s.height, enemy.getPositionX(), 0).autoRelease();
		enemy.runAction(moveTo);
		moveTo.setCallback(this);
		
		GameData.getInstance().addEnemy(enemy);
		
	}

	private WYRect frameAt(int x, int y) {
		return WYRect.make(x * ITEM_WIDTH, y * ITEM_HEIGHT, ITEM_WIDTH, ITEM_HEIGHT);
	}

	@Override
	public void onStart(int arg0) {
	}

	@Override
	public void onStop(int arg0) {
		Action action = Action.from(arg0);
		Node node = action.getTarget();
		node.stopAllActions();
		node.setVisible(false);
	}

	@Override
	public void onUpdate(int arg0, float arg1) {
	}
	


}
