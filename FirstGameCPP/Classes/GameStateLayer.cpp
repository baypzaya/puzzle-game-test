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

	//init bg
	CCLayerColor* bgLayer = CCLayerColor::create(ccc4(0, 0, 0, 200));
	addChild(bgLayer);

	//init info
	CCLabelTTF* titleLabel = CCLabelTTF::create("YOUR SCORE", "fonts/Abduction.ttf", 70);
	titleLabel->setPosition(ccp(screenSize.width/2,screenSize.height*3/4));
	titleLabel->setColor(ccc3(255, 241, 0));
	addChild(titleLabel, 1);

	char* scoreStr = new char[10];
	sprintf(scoreStr, "Current Score: %d", GameData::getInstance()->getScore());
	CCLabelTTF* currentScoreLabel = CCLabelTTF::create(scoreStr, "", 30);
	currentScoreLabel->setAnchorPoint(CCPointZero);
	currentScoreLabel->setPosition(ccp(screenSize.width/8,screenSize.height/2+50));
	currentScoreLabel->setColor(ccc3(255, 241, 0));
	addChild(currentScoreLabel, 1);

	char* eggStr = new char[10];
	sprintf(eggStr, "Highest Score: %d", GameData::getInstance()->getScore());
	CCLabelTTF* hightestScoreLabel = CCLabelTTF::create(eggStr, "", 30);
	hightestScoreLabel->setAnchorPoint(CCPointZero);
	hightestScoreLabel->setPosition(ccp(screenSize.width/8,screenSize.height/2-50));
	hightestScoreLabel->setColor(ccc3(255, 241, 0));
	addChild(hightestScoreLabel, 1);

	//init menu
	if (STATE_PAUSE == GameData::getInstance()->getCurrentGameState()) {
		CCMenuItemFont* playItem = CCMenuItemFont::create("CONTINUE", this, menu_selector(GameStateLayer::playGame));
		CCMenu* stateMenu = CCMenu::create(playItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/4));
		addChild(stateMenu);
	} else if (STATE_OVER == GameData::getInstance()->getCurrentGameState()) {
		CCMenuItemFont* restartItem = CCMenuItemFont::create("RESTART", this,
				menu_selector(GameStateLayer::restartGame));
		CCMenuItemFont* exitItem = CCMenuItemFont::create("QUIT", this, menu_selector(GameStateLayer::exitGame));

		CCMenu* stateMenu = CCMenu::create(restartItem, exitItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/4));
		stateMenu->alignItemsHorizontallyWithPadding(100);
		addChild(stateMenu);
	}

	return true;
}
void GameStateLayer::showGameState() {
	setVisible(true);
}
void GameStateLayer::hideGameState() {
	setVisible(false);
}

void GameStateLayer::playGame() {
	GameData::getInstance()->setCurrentGameState(STATE_START);
	CCDirector::sharedDirector()->resume();
}

void GameStateLayer::restartGame() {
	GameData::getInstance()->setCurrentGameState(STATE_RESTART);
	CCDirector::sharedDirector()->resume();
}

void GameStateLayer::exitGame() {
	GameData::getInstance()->setCurrentGameState(STATE_END);
	CCDirector::sharedDirector()->resume();
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

