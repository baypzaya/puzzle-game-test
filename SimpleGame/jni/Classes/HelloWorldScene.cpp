#include "HelloWorldScene.h"
#include "GameOverScene.h"
#include "SimpleAudioEngine.h"
#include "GameData.h"
#include "Enimy.h"
#include "DefenceTower.h"
#include "Bullet.h"

using namespace cocos2d;

HelloWorld::~HelloWorld() {
	// cpp don't need to call super dealloc
	// virtual destructor will do this
	delete mGameData;
}

HelloWorld::HelloWorld() {
}

CCScene* HelloWorld::scene() {
	CCScene * scene = NULL;
	do {
		// 'scene' is an autorelease object
		scene = CCScene::create();
		CC_BREAK_IF(! scene);

		// 'layer' is an autorelease object
		HelloWorld *layer = HelloWorld::create();
		CC_BREAK_IF(! layer);

		// add layer as a child to scene
		scene->addChild(layer);
	} while (0);

	// return the scene
	return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init() {
	bool bRet = false;
	do {
		//////////////////////////////////////////////////////////////////////////
		// super init first
		//////////////////////////////////////////////////////////////////////////
		CC_BREAK_IF(! CCLayerColor::initWithColor( ccc4(255,255,255,255) ) );

		initResource();
		CCLog("mGameData getInstance");
		mGameData = GameData::getInstance();

		CCSprite *backgroup = CCSprite::create("map1.png");
		backgroup->setAnchorPoint(ccp(0,0));
		backgroup->setPosition(CCPointZero);
		addChild(backgroup);

		//////////////////////////////////////////////////////////////////////////
		// add your codes below...
		//////////////////////////////////////////////////////////////////////////

		// 1. Add a menu item with "X" image, which is clicked to quit the program.

		// Create a "close" menu item with close icon, it's an auto release object.
		CCMenuItemImage *pCloseItem = CCMenuItemImage::create(
				"CloseNormal.png", "CloseSelected.png", this,
				menu_selector(HelloWorld::menuCloseCallback));
		CC_BREAK_IF(! pCloseItem);

		// Place the menu item bottom-right conner.
		CCSize visibleSize = CCDirector::sharedDirector()->getVisibleSize();
		CCPoint origin = CCDirector::sharedDirector()->getVisibleOrigin();

		pCloseItem->setPosition(
				ccp(origin.x + visibleSize.width - pCloseItem->getContentSize().width/2,
						origin.y + pCloseItem->getContentSize().height/2));

		// Create a menu with the "close" menu item, it's an auto release object.
		CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
		pMenu->setPosition(CCPointZero);
		CC_BREAK_IF(! pMenu);

		// Add the menu to HelloWorld layer as a child layer.
		this->addChild(pMenu, 1);

		//money label
		CCLabelTTF* moneyLabel = CCLabelTTF::create("0",
				"fonts/American Typewriter.ttf", 45);
		moneyLabel->setAnchorPoint(ccp(0,1));
		moneyLabel->setTag(10);
		moneyLabel->setPosition(ccp(0,visibleSize.height));
		addChild(moneyLabel);

		//power label
		CCLabelTTF* powerLabel = CCLabelTTF::create("0",
				"fonts/American Typewriter.ttf", 45);
		powerLabel->setAnchorPoint(ccp(1,1));
		powerLabel->setTag(11);
		powerLabel->setPosition(ccp(visibleSize.width,visibleSize.height));
		addChild(powerLabel);

		this->schedule(schedule_selector(HelloWorld::shoot), 0.5);
		this->schedule(schedule_selector(HelloWorld::gameLogic), 0.5);
		this->schedule(schedule_selector(HelloWorld::updateGame));

		this->setTouchEnabled(true);
		this->setKeypadEnabled(true);

		CocosDenshion::SimpleAudioEngine::sharedEngine()->playBackgroundMusic(
				"background-music-aac.wav", true);

		bRet = true;
	} while (0);

	return bRet;
}

void HelloWorld::menuCloseCallback(CCObject* pSender) {
	// "close" menu item clicked
	CCDirector::sharedDirector()->end();
}

// cpp with cocos2d-x
void HelloWorld::addTarget() {
	if (mGameData->hasNextEnimy() && mGameData->getNextEnimyType() != -1) {
		Enimy* enimy = new Enimy();
		enimy->addToScenne(this);
		mGameData->_enimies->addObject(enimy);
	}
}

void HelloWorld::shoot(float dt) {
	CCLog("-------------------start shoot-------------------");
	CCObject* enimyO;
	CCObject* towerO;
	CCARRAY_FOREACH( mGameData->_towers, towerO) {
			DefenceTower *tower = dynamic_cast<DefenceTower*> (towerO);
			bool hasTarget = tower->hasFireTarget();
			Bullet* bullet = NULL;
			if (hasTarget) {
				CCLog("hasTarget");
				bullet = tower->fireCurrentTarget();
			} else {
				CCLog("no hasTarget");
				CCARRAY_FOREACH( mGameData->_enimies, enimyO) {
						Enimy *enimy = dynamic_cast<Enimy*> (enimyO);
						if (enimy->life <= 0) {
							continue;
						}

						if (tower->canFire(enimyO)) {
							tower->setCurrentTarget(enimyO);
							bullet = tower->fire(enimyO);
							break;
						}
					}
			}
			if (bullet) {
				addChild(bullet->bulletSprite);
				bullet->flyToTarget(tower->getCurrentTarget());
				mGameData->_bullets->addObject(bullet);
			}
		}
	CCLog("-------------------end shoot-------------------");
}

void HelloWorld::gameLogic(float dt) {
	this->addTarget();
}

bool HelloWorld::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent) {
	CCLog("my touch began test");
	return true;
}

// cpp with cocos2d-x
void HelloWorld::ccTouchesEnded(CCSet* touches, CCEvent* event) {

	CCTouch* touch = (CCTouch*) (touches->anyObject());
	CCPoint location = touch->getLocation();

	float x = location.x;
	float y = location.y;
	if (mGameData->canLocationTower(x, y)) {
		CCPoint point = mGameData->converToCellPoint(x, y);

		CCMenuItemImage *item1 = CCMenuItemImage::create("tower1.png", "tower1.png",
				this, menu_selector( HelloWorld::buildDenfenceTower));
		CCMenuItemImage *item2 = CCMenuItemImage::create("tower2.png", "tower2.png",
				this, menu_selector( HelloWorld::buildDenfenceTower));
		CCMenuItemImage *item3 = CCMenuItemImage::create("tower3.png", "tower3.png",
				this, menu_selector( HelloWorld::buildDenfenceTower));

		item1->setTag(101);
		item2->setTag(102);
		item3->setTag(103);

		CCSize menuContentSize;
		menuContentSize.setSize(item1->getContentSize().width * 3,
				item1->getContentSize().height);

		CCMenu *menu = CCMenu::create(item1, item2, item3, NULL);
		menu->alignItemsHorizontally();
		menu->setPosition(point);
		menu->setContentSize(menuContentSize);

		addChild(menu);

//		DefenceTower* tower = new DefenceTower(point.x, point.y);
//		addChild(tower->spriteTower);
//		mGameData->_towers->addObject(tower);
	}
}

void HelloWorld::updateGame(float dt) {
	int gameState = mGameData->currentGameState;
	if (gameState == GameData::STATE_START) {

		//update enimy
		CCObject* enimyO;
		CCArray* deathEnimy = CCArray::create();
		CCARRAY_FOREACH( mGameData->_enimies, enimyO) {
				Enimy *enimy = dynamic_cast<Enimy*> (enimyO);
				if (enimy->life <= 0) {
					deathEnimy->addObject(enimy);
					enimy->enimySprite->stopAllActions();
					removeChild(enimy->enimySprite, true);
				}
			}

		CCARRAY_FOREACH( deathEnimy, enimyO) {
				Enimy *enimy = dynamic_cast<Enimy*> (enimyO);
				mGameData->money = mGameData->money + enimy->cost;
				mGameData->removeEnimy(enimyO);

			}

		deathEnimy->release();
		deathEnimy = NULL;

		//update money
		CCLabelTTF* moneyLabel = dynamic_cast<CCLabelTTF*> (getChildByTag(10));
		char* moneyStr = new char[32];
		sprintf(moneyStr, "%d", mGameData->money);
		moneyLabel->setString(moneyStr);

		//update money
		CCLabelTTF* powerLabel = dynamic_cast<CCLabelTTF*> (getChildByTag(11));
		char* powerStr = new char[32];
		sprintf(powerStr, "%d", mGameData->power);
		powerLabel->setString(powerStr);

		//update game state
		if (mGameData->power <= 0) {
			mGameData->currentGameState = GameData::STATE_OVER;
		}

	} else {
		switch (gameState) {
		case GameData::STATE_OVER: {
			GameOverScene *gameOverScene = GameOverScene::create();
			gameOverScene->getLayer()->getLabel()->setString("You Lose");
			CCDirector::sharedDirector()->replaceScene(gameOverScene);
			break;
		}
		case GameData::STATE_PAUSE:
			break;
		case GameData::STATE_SUCCESS: {
			GameOverScene *gameOverScene = GameOverScene::create();
			gameOverScene->getLayer()->getLabel()->setString("You Success");
			CCDirector::sharedDirector()->replaceScene(gameOverScene);
			break;
		}
		default:
			break;
		}
	}
}

void HelloWorld::keyBackClicked() {
	CCLog("keyBackClicked");
	delete mGameData;
	CCDirector::sharedDirector()->end();
}
void HelloWorld::keyMenuClicked() {
	CCLog("keyMenuClicked");
}

void HelloWorld::createEnimyFrame(int rowNumber, CCSpriteFrame *pRun[]) {

	for (int j = 0; j < 4; j++) {
		pRun[j] = CCSpriteFrame::create("enemy1.png",
				CCRectMake(j*40,rowNumber*55,40,55));

	}
}

void HelloWorld::initResource() {

	CCAnimationCache *animCache = CCAnimationCache::sharedAnimationCache();

	CCTexture2D *pRunImage = CCTextureCache::sharedTextureCache()->addImage(
			"enemy1.png");
	CCSpriteFrame *topRun[4];
	CCSpriteFrame *downRun[4];
	CCSpriteFrame *leftRun[4];
	CCSpriteFrame *rightRun[4];

	createEnimyFrame(0, downRun);
	createEnimyFrame(1, leftRun);
	createEnimyFrame(2, rightRun);
	createEnimyFrame(3, topRun);

	CCArray *pArr = CCArray::createWithCapacity(4);

	for (int i = 0; i < 4; i++) {
		pArr->addObject(leftRun[i]);
	}

	CCAnimation *pLeftRunAnimation = CCAnimation::createWithSpriteFrames(pArr,
			0.2f);
	animCache->addAnimation(pLeftRunAnimation, "leftRun");

	pArr->removeAllObjects();
	for (int i = 0; i < 4; i++) {
		pArr->addObject(leftRun[i]);
	}
	CCAnimation *pRightRunAnimation = CCAnimation::createWithSpriteFrames(pArr,
			0.2f);
	animCache->addAnimation(pRightRunAnimation, "rightRun");

	pArr->removeAllObjects();
	for (int i = 0; i < 4; i++) {
		pArr->addObject(downRun[i]);
	}

	CCAnimation *pDownRunAnimation = CCAnimation::createWithSpriteFrames(pArr,
			0.2f);
	animCache->addAnimation(pDownRunAnimation, "downRun");

	pArr->removeAllObjects();
	for (int i = 0; i < 4; i++) {
		pArr->addObject(topRun[i]);
	}

	CCAnimation *pTopRunAnimation = CCAnimation::createWithSpriteFrames(pArr,
			0.2f);
	animCache->addAnimation(pTopRunAnimation, "topRun");
}

void HelloWorld::buildDenfenceTower(CCObject* pSender)
{
	CCMenuItemImage* item = dynamic_cast<CCMenuItemImage*>(pSender);

	CCLog("pSender type:%d",item->getTag());
	removeChild(item->getParent(),true);

}

//void HelloWorld::registerWithTouchDispatcher() {
//	CCLog("registerWithTouchDispatcher");
//	// CCTouchDispatcher::sharedDispatcher()->addTargetedDelegate(this,0,true);
//	CCDirector::sharedDirector()->getTouchDispatcher()->addStandardDelegate(
//			this, 0);
//}
