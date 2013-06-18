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

	CCLayerColor* bgLayer = CCLayerColor::create(ccc4(255, 0, 0, 255));
	addChild(bgLayer);

	CCMenuItemFont* playItem = CCMenuItemFont::create("PLAY", this, menu_selector(GameStateLayer::playGame));
	CCMenu* stateMenu = CCMenu::create(playItem, NULL);
	stateMenu->setPosition(ccp(screenSize.width/2,screenSize.height/2));
	addChild(stateMenu);

	return true;
}
void GameStateLayer::showGameState() {
	setVisible(true);
}
void GameStateLayer::hideGameState() {
	setVisible(false);
}

void GameStateLayer::playGame() {
	CCDirector::sharedDirector()->resume();
	setVisible(false);
}

bool GameStateLayer::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent) {
	CCLog("GameStateLayer::ccTouchBegan");
	return true;
}
void GameStateLayer::ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent){
	;
}
void GameStateLayer::ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent){
	;
}

void GameStateLayer::registerWithTouchDispatcher(){
	CCDirector::sharedDirector()->getTouchDispatcher()->addTargetedDelegate(this,std::numeric_limits<int>::min(),true);
	CCLayer::registerWithTouchDispatcher();
}
