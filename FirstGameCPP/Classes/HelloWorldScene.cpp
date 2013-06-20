#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

#define PTM_RATIO 32.0f
#define FLOOR_HEIGHT    -100.0f

CCScene* HelloWorld::scene() {
	// 'scene' is an autorelease object
	CCScene *scene = CCScene::create();

	// 'layer' is an autorelease object
	HelloWorld *layer = HelloWorld::create();

	// add layer as a child to scene
	scene->addChild(layer);

	// return the scene
	return scene;
}

HelloWorld::~HelloWorld() {
	delete GameData::getInstance();
}

// on "init" you need to initialize your instance
bool HelloWorld::init() {
	if (!CCLayer::init()) {
		return false;
	}

	//init data
	setTouchEnabled(true);
	isJumpEggDown = false;
	jumpState = 1;

	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	//	CCDirector::sharedDirector()->pause();

	//init ui
	//add close menu item
	CCMenuItemImage *pCloseItem = CCMenuItemImage::create("CloseNormal.png", "CloseSelected.png", this,
			menu_selector(HelloWorld::menuCloseCallback));
	pCloseItem->setPosition(ccp(screenSize.width - 20, 20));
	CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	scoreLable = CCLabelTTF::create("1234", "fonts/Abduction.ttf", 35);
	scoreLable->setAnchorPoint(ccp(0,1));
	scoreLable->setPosition(ccp(20,screenSize.height - 20));
	addChild(scoreLable, 1);

	eggLable = CCLabelTTF::create("E:  ", "fonts/Abduction.ttf", 35);
	eggLable->setAnchorPoint(ccp(1,1));
	eggLable->setPosition(ccp(screenSize.width-20,screenSize.height - 20));
	addChild(eggLable, 1);

	CCLog("NestLayer::create()");
	m_nestLayer = NestLayer::create();
	m_nestLayer->setPosition(CCPointZero);
	m_nestLayer->setAnchorPoint(ccp(0,0));
	addChild(m_nestLayer);

	//init stateLayer
	//	m_stateLayer = GameStateLayer::create();
	//	m_stateLayer->setAnchorPoint(ccp(0,0));
	//	m_stateLayer->setPosition(CCPointZero);
	//	addChild(m_stateLayer,100);

	//add jump egg
	jumpEgg = CCSprite::create("jump_egg.png");
	jumpEgg->setAnchorPoint(ccp(0.5,0));
	jumpEgg->setPosition(ccp(screenSize.width/2,20.0f));
	addChild(jumpEgg);

	followNest = m_nestLayer->catchEgg(jumpEgg);
	//	createNest();

	scheduleUpdate();

	return true;
}

CCPoint preLocation = CCPointZero;

void HelloWorld::update(float dt) {
	GameData* gameData = GameData::getInstance();
	isJumpEggDown = preLocation.y > jumpEgg->getPosition().y;
	preLocation = jumpEgg->getPosition();
	if (isJumpEggDown && jumpState != 1) {
		CCSprite* nest = m_nestLayer->catchEgg(jumpEgg);
		CCLog("catchEgg end");
		if (nest != NULL) {
			float cy = nest->getParent()->convertToWorldSpace(nest->getPosition()).y;
			float fy = followNest->getParent()->convertToWorldSpace(followNest->getPosition()).y;
			if (cy > fy) {
				followNest = nest;
				gameData->addScore(10);
				jumpState = 1;
				CCPoint worldPoint = followNest->getParent()->convertToWorldSpace(followNest->getPosition());
				m_nestLayer->updateNestPositon(worldPoint);
			}
		}
	} else if (jumpState == 1 && followNest != NULL) {
		jumpEgg->stopAllActions();
		CCPoint worldPoint = followNest->getParent()->convertToWorldSpace(followNest->getPosition());
		jumpEgg->setPosition(jumpEgg->getParent()->convertToNodeSpace(worldPoint));
		jumpEgg->setRotation(0.0f);
	}

	if (jumpEgg->getPositionY() <= -10) {

		gameData->subEggCount();
		if (gameData->getEggCount() <= 0) {
			CCDirector::sharedDirector()->replaceScene(HelloWorld::scene());
		} else {
			jumpEgg->stopAllActions();
			CCPoint worldPoint = followNest->getParent()->convertToWorldSpaceAR(followNest->getPosition());
			jumpEgg->setPosition(worldPoint);
			jumpEgg->setRotation(0.0f);
			jumpState = 1;
		}
	}

	char* scoreStr = new char[10];
	sprintf(scoreStr, "s:%d", gameData->getScore());
	scoreLable->setString(scoreStr);

	char* eggStr = new char[10];
	sprintf(eggStr, "E:%2d", gameData->getEggCount());
	eggLable->setString(eggStr);

}

void HelloWorld::ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void HelloWorld::ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void HelloWorld::ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
	if (jumpState == 0 || m_nestLayer->getMoving()) {
		return;
	}

	jumpEgg->stopAllActions();
	CCJumpBy* jumpBy = CCJumpBy::create(1.0f, ccp(0,-40), 390, 1);
	jumpEgg->runAction(jumpBy);

	isJumpEggDown = false;
	CCPoint preLocation = CCPointZero;
	jumpState = 0;
}

void HelloWorld::menuCloseCallback(CCObject* pSender) {
	CCLog("menuCloseCallback");
	CCDirector::sharedDirector()->pause();
	m_stateLayer->setVisible(true);
	//	CCDirector::sharedDirector()->end();
	//
	//#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	//	exit(0);
	//#endif
}
