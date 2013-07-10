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

	CCLayerColor* bgLayer = CCLayerColor::create(ccc4(0, 0, 0, 100));
	addChild(bgLayer);

	if (STATE_PAUSE == GameData::getInstance()->getCurrentGameState()) {
		CCMenuItemFont* playItem = CCMenuItemFont::create("PLAY", this, menu_selector(GameStateLayer::playGame));
		CCMenu* stateMenu = CCMenu::create(playItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/2));
		addChild(stateMenu);
	} else if (STATE_OVER == GameData::getInstance()->getCurrentGameState()) {
		CCMenuItemFont* restartItem = CCMenuItemFont::create("restart", this,
				menu_selector(GameStateLayer::restartGame));
		CCMenuItemFont* exitItem = CCMenuItemFont::create("exit", this, menu_selector(GameStateLayer::exitGame));

		CCMenu* stateMenu = CCMenu::create(restartItem, exitItem, NULL);
		stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/2));
		stateMenu->alignItemsHorizontallyWithPadding(20);
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

