/*
 * MainGameScene.cpp
 *
 *  Created on: 2013-6-27
 *      Author: yujsh
 */

#include "MainGameScene.h"

bool MainGameScene::init() {

	//init data
	GameData::getInstance()->setCurrentGameState(STATE_START);
	m_gameCurrentSate = STATE_START;

	//init ui
	m_gameLayer = HelloWorld::create();
	addChild(m_gameLayer);

//	showStateLayer();
	scheduleUpdate();
	return true;

}

MainGameScene::~MainGameScene() {

}

void MainGameScene::update(float dt) {
	processGameState();
}

void MainGameScene::processGameState() {
	CCDirector* director = CCDirector::sharedDirector();
	GameData* gameData = GameData::getInstance();
	if (m_gameCurrentSate != gameData->getCurrentGameState()) {
		m_gameCurrentSate = gameData->getCurrentGameState();
		CCLog("processGameState:%d",m_gameCurrentSate);
		switch (m_gameCurrentSate) {
		case STATE_START:
			hideSateLayer();
			break;
		case STATE_PAUSE:
			showStateLayer();
			break;
		case STATE_OVER:
			showStateLayer();
			gameData->saveHighestScore();
			break;
		case STATE_SUCCESS:
			break;
		case STATE_RESTART:
			gameData->resetData();
			removeChild(m_gameLayer, true);
			m_gameLayer = HelloWorld::create();
			addChild(m_gameLayer);
			gameData->setCurrentGameState(STATE_START);
			break;
		case STATE_END:
			CCDirector::sharedDirector()->end();
			#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
						exit(0);
			#endif

			break;

		default:
			CCLog("unknown game state: %d",m_gameCurrentSate);
			break;

		}
	}
}

void MainGameScene::showStateLayer() {
//	if (m_stateLayer) {
//		removeChild(m_stateLayer, true);
//	}
//
//	m_stateLayer = GameStateLayer::create();
//	addChild(m_stateLayer, 100);
//
//	CCDirector::sharedDirector()->pause();
	CCScene* scene=CCScene::create();
	scene->addChild(GameStateLayer::create());
	CCDirector::sharedDirector()->pushScene(scene);
}

void MainGameScene::hideSateLayer() {
//	if (m_stateLayer) {
//		removeChild(m_stateLayer, true);
//	}
//
//	CCDirector::sharedDirector()->resume();
}
