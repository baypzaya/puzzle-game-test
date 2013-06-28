/*
 * MainGameScene.cpp
 *
 *  Created on: 2013-6-27
 *      Author: yujsh
 */

#include "MainGameScene.h"

bool MainGameScene::init() {

	//init data
	GameData::getInstance()->setCurrentGameState(STATE_PAUSE);
	m_gameCurrentSate = STATE_PAUSE;

	//init ui
	m_gameLayer = HelloWorld::create();
	addChild(m_gameLayer);

	showStateLayer();
	scheduleUpdate();
	return true;

}

MainGameScene::~MainGameScene() {

}

void MainGameScene::update(float dt) {
	CCLog("maingamescene update");
	processGameState();
}

void MainGameScene::processGameState() {
	CCDirector* director = CCDirector::sharedDirector();
	GameData* gameData = GameData::getInstance();
	if (m_gameCurrentSate != gameData->getCurrentGameState()) {
		m_gameCurrentSate = gameData->getCurrentGameState();
		switch (m_gameCurrentSate) {
		case STATE_START:
			hideSateLayer();
			break;
		case STATE_PAUSE:
			showStateLayer();
			break;
		case STATE_OVER:
			gameData->resetData();
			removeChild(m_gameLayer, true);
			m_gameLayer = HelloWorld::create();
			addChild(m_gameLayer);
			showStateLayer();
			break;
		case STATE_SUCCESS:
			break;
		}
	}
}

void MainGameScene::showStateLayer() {
	if (m_stateLayer) {
		removeChild(m_stateLayer, true);
	}

	m_stateLayer = GameStateLayer::create();
	addChild(m_stateLayer, 100);

	CCDirector::sharedDirector()->pause();
}
void MainGameScene::hideSateLayer() {
	if (m_stateLayer) {
		removeChild(m_stateLayer, true);
	}
	CCDirector::sharedDirector()->resume();
}
