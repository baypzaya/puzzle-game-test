#include "GameLayer.h"
#include "SimpleAudioEngine.h"

#define PTM_RATIO 32.0f
#define FLOOR_HEIGHT    -100.0f
#define NEST_ZORDER 1
#define PRE_NEST_ZORDER 2
#define BACK_NEST_ZORDER 0

GameLayer::~GameLayer() {
}

// on "init" you need to initialize your instance
bool GameLayer::init() {
	if (!CCLayer::init()) {
		return false;
	}

	//init data
	setTouchEnabled(true);
	isJumpEggDown = false;
	jumpState = 1;

	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();



	//init ui
	CCSprite* background = CCSprite::create("jump_background.png");
	background->setAnchorPoint(CCPointZero);
	background->setPosition(CCPointZero);
	addChild(background);

	//add close menu item
	CCMenuItemImage *pCloseItem = CCMenuItemImage::create("CloseNormal.png", "CloseSelected.png", this,
			menu_selector(GameLayer::menuCloseCallback));
	pCloseItem->setPosition(ccp(screenSize.width - 20, 20));
	CCMenu* pMenu = CCMenu::create(pCloseItem, NULL);
	pMenu->setPosition(CCPointZero);
	this->addChild(pMenu, 1);

	scoreLable = CCLabelTTF::create("1234", "fonts/Abduction.ttf", 35);
	scoreLable->setAnchorPoint(ccp(0,1));
	scoreLable->setPosition(ccp(20,screenSize.height - 20));
	scoreLable->setColor(ccc3(255,0,0));
	addChild(scoreLable, 1);

	eggLable = CCLabelTTF::create("E:  ", "fonts/Abduction.ttf", 35);
	eggLable->setAnchorPoint(ccp(1,1));
	eggLable->setPosition(ccp(screenSize.width-20,screenSize.height - 20));
	addChild(eggLable, 1);

	CCLog("NestLayer::create()");
	m_nestLayer = NestLayer::create();
	m_nestLayer->setPosition(CCPointZero);
	m_nestLayer->setAnchorPoint(ccp(0,0));
	addChild(m_nestLayer,NEST_ZORDER);

	//init stateLayer
	//	m_stateLayer = GameStateLayer::create();
	//	m_stateLayer->setAnchorPoint(ccp(0,0));
	//	m_stateLayer->setPosition(CCPointZero);
	//	addChild(m_stateLayer,100);

	//add jump egg
	jumpEgg = CCSprite::create("jump_egg.png");
	jumpEgg->setAnchorPoint(ccp(0.5,0));
	jumpEgg->setPosition(ccp(screenSize.width/2,20.0f));
	addChild(jumpEgg,BACK_NEST_ZORDER);

	followNest = m_nestLayer->catchEgg(jumpEgg);
	//	createNest();

	scheduleUpdate();

	return true;
}

CCPoint preLocation = CCPointZero;

void GameLayer::update(float dt) {
	GameData* gameData = GameData::getInstance();

	if(STATE_START != gameData->getCurrentGameState()){
		return;
	}

	isJumpEggDown = preLocation.y > jumpEgg->getPosition().y;
	preLocation = jumpEgg->getPosition();
	if (isJumpEggDown && jumpState != 1) {
		jumpEgg->setZOrder(BACK_NEST_ZORDER);
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
		jumpEgg->setAnchorPoint(ccp(0.5f,0));
		jumpEgg->setPosition(jumpEgg->getParent()->convertToNodeSpace(worldPoint));
		jumpEgg->setRotation(0.0f);
	}

	if (jumpEgg->getPositionY() <= -10) {

		gameData->subEggCount();
		if (gameData->getEggCount() <= 0) {
			//			gameData->resetData();
			//			CCDirector::sharedDirector()->replaceScene(GameLayer::scene());
			gameData->setCurrentGameState(STATE_OVER);
		} else {
			jumpEgg->stopAllActions();
			CCPoint worldPoint = followNest->getParent()->convertToWorldSpaceAR(followNest->getPosition());
			jumpEgg->setAnchorPoint(ccp(0.5f,0));
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

void GameLayer::ccTouchesBegan(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void GameLayer::ccTouchesMoved(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
}

void GameLayer::ccTouchesEnded(cocos2d::CCSet* touches, cocos2d::CCEvent* event) {
	GameData* gameData = GameData::getInstance();
	if (STATE_START != gameData->getCurrentGameState() || jumpState == 0 || m_nestLayer->getMoving()) {
		return;
	}
	jumpEgg->setAnchorPoint(ccp(0.5f,0.5f));
	jumpEgg->setZOrder(PRE_NEST_ZORDER);
	jumpEgg->stopAllActions();
	CCJumpBy* jumpBy = CCJumpBy::create(1.5f, ccp(0,-40), 390, 1);
	jumpEgg->runAction(jumpBy);

	CCRotateBy* rotateBy = CCRotateBy::create(2.0f, -360);
	jumpEgg->runAction(CCRepeatForever::create(rotateBy));

	isJumpEggDown = false;
	CCPoint preLocation = CCPointZero;
	jumpState = 0;
}

void GameLayer::menuCloseCallback(CCObject* pSender) {
	GameData::getInstance()->setCurrentGameState(STATE_PAUSE);
}
