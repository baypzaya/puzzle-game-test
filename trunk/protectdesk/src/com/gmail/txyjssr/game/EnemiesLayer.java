package com.gmail.txyjssr.game;

import com.gmail.txyjssr.R;
import com.gmail.txyjssr.game.data.Enemy;
import com.gmail.txyjssr.game.data.GameData;
import com.gmail.txyjssr.game.data.OnEnemyStateChangedListener;
import com.gmail.txyjssr.game.data.Path;
import com.wiyun.engine.actions.Action;
import com.wiyun.engine.actions.Action.Callback;
import com.wiyun.engine.actions.Animate;
import com.wiyun.engine.actions.IntervalAction;
import com.wiyun.engine.actions.MoveTo;
import com.wiyun.engine.actions.RepeatForever;
import com.wiyun.engine.actions.ScaleBy;
import com.wiyun.engine.nodes.Animation;
import com.wiyun.engine.nodes.Director;
import com.wiyun.engine.nodes.Label;
import com.wiyun.engine.nodes.Layer;
import com.wiyun.engine.nodes.Node;
import com.wiyun.engine.opengl.Texture2D;
import com.wiyun.engine.types.WYRect;
import com.wiyun.engine.types.WYSize;
import com.wiyun.engine.utils.ResolutionIndependent;
import com.wiyun.engine.utils.TargetSelector;

public class EnemiesLayer extends Layer implements Callback {

	float ITEM_WIDTH;
	float ITEM_HEIGHT;

	Animation mAnimDown;
	IntervalAction anim;

	Texture2D texEnemy;
	WYSize s;
	float mTileWidth;
	float mTileHeight;
	Path path;

	private OnEnemyStateChangedListener listener;

	public EnemiesLayer(float tileWidth, float tileHeight, OnEnemyStateChangedListener listener) {

		ITEM_WIDTH = ResolutionIndependent.resolveDp(24);
		ITEM_HEIGHT = ResolutionIndependent.resolveDp(32);
		mTileWidth = tileWidth;
		mTileHeight = tileHeight;

		// add player
		s = Director.getInstance().getWindowSize();
		texEnemy = Texture2D.makePNG(R.drawable.player);
		this.listener = listener;

		int[] pathArray = GameData.getInstance().getCurrentPath();
		path = new Path(tileWidth, tileHeight, pathArray);

		mAnimDown = new Animation(1);
		mAnimDown.addFrame(0.3f, frameAt(0, 2), frameAt(2, 2));
		schedule(new TargetSelector(this, "addEnemies", new Object[] {}), 1);

	}

	public Enemy createEnemy() {
		// Random random = new Random();
		// Integer x = random.nextInt(480);
		Enemy enemy = new Enemy(texEnemy, WYRect.make(0, ITEM_HEIGHT * 2, ITEM_WIDTH, ITEM_HEIGHT));
		enemy.setPosition(path.getFirstLocal());
		return enemy;
	}

	public void addEnemies() {
		Enemy enemy = createEnemy();
		addChild(enemy);

		anim = (IntervalAction) Animate.make(mAnimDown, true).autoRelease();
		enemy.runAction((Action) RepeatForever.make(anim).autoRelease());
		enemy.setPathIndex(1);
		IntervalAction moveTo = (IntervalAction) MoveTo.make(1, enemy.getPositionX(), enemy.getPositionY(),
				path.getNextLocal(0).x, path.getNextLocal(0).y).autoRelease();
		enemy.runAction(moveTo);
		moveTo.setCallback(this);
		enemy.setLifeChangedListener(listener);
		GameData.getInstance().addEnemy(enemy.getPointer(), enemy);
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
		// node.stopAllActions();
		Enemy enemy = GameData.getInstance().getEnemyMap().get(node.getPointer());
		if (enemy != null) {
			int index = enemy.getPathIndex();
			if (path.hasNext(index)) {
				enemy.setPathIndex(index + 1);
				IntervalAction moveTo = (IntervalAction) MoveTo.make(1, enemy.getPositionX(), enemy.getPositionY(),
						path.getNextLocal(index).x, path.getNextLocal(index).y).autoRelease();
				node.runAction(moveTo);
				moveTo.setCallback(this);
			} else {
				node.stopAllActions();
				removeChild(node, true);
				listener.onCrossed(enemy);
			}
		}else{
			node.stopAllActions();
			removeChild(node, true);
		}

	}

	@Override
	public void onUpdate(int arg0, float arg1) {
	}

	public void updateEnemyState(Enemy enemy, long life) {
		if (life <= 0) {
			enemy.stopAllActions();
			removeChild(enemy, true);
			GameData.getInstance().removeEnemy(enemy.getPointer());
			GameData.getInstance().addMoney(enemy.cost);
			enemy = null;
		} else {
			Label label = Label.make("" + life);
			label.setScale(0.2f);
			addChild(label);
			label.setPosition(enemy.getPositionX(), enemy.getPositionY());
			ScaleBy scaleBy = ScaleBy.make(0.5f, 2);
			label.runAction(scaleBy);
			scaleBy.setCallback(this);
		}
	}
}
