#include "HelloWorldScene.h"
#include "GameOverScene.h"
#include "SimpleAudioEngine.h"
#include "GameData.h"
#include "Enimy.h"
#include "DefenceTower.h"

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
	delete mGameData;
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

void HelloWorld::gameLogic(float dt) {
	this->addTarget();
}

bool HelloWorld::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent) {
	CCLog("my touch began test");
	return true;
}

// cpp with cocos2d-x
void HelloWorld::ccTouchesEnded(CCSet* touches, CCEvent* event) {
	CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(
			"pew-pew-lei.wav");

	CCTouch* touch = (CCTouch*) (touches->anyObject());
	CCPoint location = touch->getLocation();
	DefenceTower* tower = new DefenceTower(location.x,location.y);
	addChild(tower->spriteTower);
	mGameData->_towers->addObject(tower);
}

void HelloWorld::updateGame(float dt) {
	int gameState = mGameData->currentGameState;
	switch (gameState) {
	case GameData::STATE_OVER:
		break;
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

//void HelloWorld::registerWithTouchDispatcher() {
//	CCLog("registerWithTouchDispatcher");
//	// CCTouchDispatcher::sharedDispatcher()->addTargetedDelegate(this,0,true);
//	CCDirector::sharedDirector()->getTouchDispatcher()->addStandardDelegate(
//			this, 0);
//}
