/*
 * GameData.cpp
 *
 *  Created on: 2013-5-6
 *      Author: yujsh
 */

#include "GameData.h"
#include "ccMacros.h"

static GameData* sGameData = NULL;

GameData::GameData() {
	initData();
}

GameData::~GameData() {
	sGameData = NULL;
}

GameData* GameData::getInstance() {

	if (sGameData == NULL) {
		sGameData = new GameData();
	}

	return sGameData;
}

void GameData::initData() {
	m_score = 0;
	m_eggCount = 5;
	m_currentGameState = STATE_PAUSE;

	m_lastNestHeight = nest_base_height;

}

void GameData::resetData(){
	initData();
}

void GameData::addScore(int score) {
	m_score += score;
}
void GameData::subEggCount() {
	m_eggCount--;

	if (m_eggCount < 0) {
		m_eggCount = 0;
	}
}

Nest GameData::createNest() {
	m_lastNestHeight = m_lastNestHeight + nest_step_height;
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	Nest nest;

	nest.direction = (CCRANDOM_0_1() > 0.5) ? right : left;
	nest.width = screenSize.width / 4;
	nest.speed = nest_min_move_speed + CCRANDOM_0_1() * nest_move_speed_scope;
	if (nest.direction == left) {
		nest.location = ccp(screenSize.width,m_lastNestHeight);
	} else {
		nest.location = ccp(0,m_lastNestHeight);
	}
	return nest;
}

CCPoint GameData::getInitPosition(){
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	return ccp(screenSize.width/2,nest_base_height);
}


