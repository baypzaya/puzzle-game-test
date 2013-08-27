/*
 * GameStateLayer.cpp
 *
 *  Created on: 2013-6-13
 *      Author: yujsh
 */

#include <GameStateLayer.h>
#include <limits>

GameStateLayer::~GameStateLayer() {
}

bool GameStateLayer::init() {
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	setTouchEnabled(true);

	CCSprite* background = CCSprite::create("jump_background.png");
	background->setAnchorPoint(CCPointZero);
	background->setPosition(CCPointZero);
	addChild(background);

	//init bg
//	CCLayerColor* bgLayer = CCLayerColor::create(ccc4(0, 0, 0, 200));
//	addChild(bgLayer);

	//init info
	CCLabelTTF* titleLabel = CCLabelTTF::create("YOUR SCORE", "fonts/Abduction.ttf", 70);
	titleLabel->setPosition(ccp(screenSize.width/2,screenSize.height*3/4));
	titleLabel->setColor(ccc3(255, 241, 0));
	addChild(titleLabel, 1);

	char* scoreStr = new char[20];
	sprintf(scoreStr, "Current Score: %d", GameData::getInstance()->getScore());
	CCLabelTTF* currentScoreLabel = CCLabelTTF::create(scoreStr, "", 30);
	currentScoreLabel->setAnchorPoint(CCPointZero);
	currentScoreLabel->setPosition(ccp(screenSize.width/8,screenSize.height/2+50));
	currentScoreLabel->setColor(ccc3(255, 241, 0));
	addChild(currentScoreLabel, 1);

	char* eggStr = new char[20];
	sprintf(eggStr, "Highest Score: %d", GameData::getInstance()->getHighestScore());
	CCLabelTTF* hightestScoreLabel = CCLabelTTF::create(eggStr, "", 30);
	hightestScoreLabel->setAnchorPoint(CCPointZero);
	hightestScoreLabel->setPosition(ccp(screenSize.width/8,screenSize.height/2-50));
	hightestScoreLabel->setColor(ccc3(255, 241, 0));
	addChild(hightestScoreLabel, 1);

	CCMenuItemFont* continueItem = CCMenuItemFont::create("RESUME", this, menu_selector(GameStateLayer::playGame));
	CCMenuItemFont* restartItem = CCMenuItemFont::create("RESTART", this, menu_selector(GameStateLayer::restartGame));
	CCMenuItemFont* exitItem = CCMenuItemFont::create("QUIT", this, menu_selector(GameStateLayer::exitGame));

	//init menu
	if (STATE_PAUSE == GameData::getInstance()->getCurrentGameState()) {
		CCMenu* stateMenu = CCMenu::create(continueItem, restartItem, exitItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/4));
		stateMenu->alignItemsHorizontallyWithPadding(50);
		addChild(stateMenu);
	} else if (STATE_OVER == GameData::getInstance()->getCurrentGameState()) {
		GameData::getInstance()->saveHighestScore();
		CCMenu* stateMenu = CCMenu::create(restartItem, exitItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/4));
		stateMenu->alignItemsHorizontallyWithPadding(100);
		addChild(stateMenu);
	}

	//	CCSprite* star = CCSprite::create("jump_egg.png");
	//	star->setPosition(
	//			ccp(titleLabel->getPositionX()+titleLabel->getContentSize().width/2,titleLabel->getPositionY()+titleLabel->getContentSize().height/2));
	//	addChild(star, 0);
	//	CCRotateBy* rotateBy = CCRotateBy::create(2.0f, 360);
	//	star->runAction(CCRepeatForever::create(rotateBy));

	return true;
}

void GameStateLayer::playGame() {
	GameData::getInstance()->setCurrentGameState(STATE_START);
	CCDirector::sharedDirector()->popScene();
}

void GameStateLayer::restartGame() {
	GameData::getInstance()->setCurrentGameState(STATE_RESTART);
	CCDirector::sharedDirector()->popScene();
}

void GameStateLayer::exitGame() {
	CCDirector::sharedDirector()->end();
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	exit(0);
#endif
}

bool GameStateLayer::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent) {
	CCLog("GameStateLayer::ccTouchBegan");
	return false;
}
void GameStateLayer::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent) {
	;
}
void GameStateLayer::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent) {
	;
}

void GameStateLayer::registerWithTouchDispatcher() {
	CCDirector::sharedDirector()->getTouchDispatcher()->addTargetedDelegate(this, std::numeric_limits<int>::min(), true);
	CCLayer::registerWithTouchDispatcher();
}

