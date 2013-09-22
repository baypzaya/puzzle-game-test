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
	resetData();
}

void GameData::resetData() {
	m_score = 0;
	m_eggCount = 5;
	currentPageIndex = 0;
	m_currentGameState = STATE_PAUSE;
	m_lastNestHeight = nest_base_height;
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
	if (currentPageIndex == 0) {
		currentPageIndex++;
		Nest nest;
		nest.height = 16;
		nest.width = nest_l1_width * 2;
		nest.location = getInitPosition();
		nest.speed = 0;
		nest.type = normal;
		return nest;
	}

	int random = (int) arc4random() % 10;

	random = random < 0 ? -random : random;

	m_lastNestHeight = m_lastNestHeight + nest_step_height;
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();

	int level = currentPageIndex / 5;
	Nest nest;
	nest.height = 16;
	nest.direction = random < 6 ? left : right;
	CCLog("createNest level: %d ", level);
	switch (level) {
	case 0:
		nest.width = nest_l1_width;
		nest.speed = nest_l1_speed;
		break;
	case 1:
		nest.width = nest_l2_width;
		nest.speed = nest_l1_speed;
		break;
	case 2:
		nest.width = random < 5 ? nest_l1_width : nest_l2_width;
		nest.speed = random < 3 ? nest_l1_speed : random < 6 ? nest_l2_speed : nest_l3_speed;
		break;
	case 3:
		nest.width = random < 5 ? nest_l2_width : nest_l3_width;
		nest.speed = nest_l2_speed;
		break;
	default:
		nest.width = random < 5 ? nest_l3_width : nest_l4_widht;
		nest.speed = random < 5 ? nest_l3_speed : nest_l4_speed;
		break;
	}

	nest.type = random < 5 ? normal : random < 8 ? strong : slow;

	if (nest.direction == right) {
		nest.location = ccp(0,m_lastNestHeight);
	} else if (nest.direction == left) {
		nest.location = ccp(screenSize.width,m_lastNestHeight);
	} else {
		nest.location = ccp(screenSize.width/2,m_lastNestHeight);
	}

	CCLog("createNest width: %d, speed : %d ", nest.width, nest.speed);
	currentPageIndex++;
	return nest;
}

CCPoint GameData::getInitPosition() {
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	return ccp(screenSize.width/2,nest_base_height);
}

void GameData::saveHighestScore() {
	int highestScore = CCUserDefault::sharedUserDefault()->getIntegerForKey("key_highest_score", 0);
	if (m_score > highestScore) {
		CCUserDefault::sharedUserDefault()->setIntegerForKey("key_highest_score", m_score);
	}
}
int GameData::getHighestScore() {
	int highestScore = CCUserDefault::sharedUserDefault()->getIntegerForKey("key_highest_score", 0);
	return highestScore;
}

